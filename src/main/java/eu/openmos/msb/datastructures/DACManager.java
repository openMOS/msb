package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTable_DA;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.utilities.Functions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.BindingProvider;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author fabio.miranda
 *
 */
public class DACManager
{

  private final Set<String> trueSet = new HashSet<>(Arrays.asList("1", "true", "True"));
  // Singleton specific objects
  private static final Object lock = new Object();
  private static volatile DACManager instance = null;

  // [af-silva] TODO valdiate
  // para aceder as classes especificas (DeviceAdapterOPC e DeviceAdapterDDS) fazer cast
  private final Map<Integer, DeviceAdapter> deviceAdapters;
  public Map<String, Thread> threadMap = new HashMap<>();
  
  /**
   * @brief constructor
   */
  protected DACManager()
  {
    deviceAdapters = new HashMap<>();
  }

  /**
   * @brief obtain the Device Adapter Clients Manager unique instance
   * @return
   */
  public static DACManager getInstance()
  {
    DACManager i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new DACManager();
          instance = i;
        }
      }
    }
    return i;
  }

  /**
   * @param deviceAdapterName
   * @param device_protocol
   * @param short_info
   * @param long_info
   * @return
   */
  public DeviceAdapter addDeviceAdapter(String deviceAdapterName, EProtocol device_protocol, String short_info, String long_info)
  {
    String protocol = "";
    try
    {
      DeviceAdapter client = null;
      if (null != device_protocol)
      {
        switch (device_protocol)
        {
          case DDS:
          {
            client = new DeviceAdapterDDS();
            protocol = "DDS";
            break;
          }
          case OPC:
          {
            client = new DeviceAdapterOPC();
            protocol = "OPC";
            break;
          }
          case MQTT:
            throw new UnsupportedOperationException("Not supported yet.");
          default:
            break;

        }
      }
      int id = DatabaseInteraction.getInstance().createDeviceAdapter(deviceAdapterName, protocol, short_info, long_info);
      if (id != -1 && client != null) // the last condition should never happen
      {
        client.setId(id);
        deviceAdapters.put(id, client);

        return client;
      }
    } catch (UnsupportedOperationException ex)
    {
      System.out.println("[ERROR] at addDeviceAdapter" + ex.getMessage());
    }
    return null;
  }

  public DeviceAdapter getDeviceAdapterbyName(String deviceAdapterName)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(deviceAdapterName);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id);
    }
    return null;
  }

  public DeviceAdapter getDeviceAdapterbyAML_ID(String da_aml_id)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByAML_ID(da_aml_id);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id);
    }
    return null;
  }

  /**
   * @brief @param deviceAdapterName
   * @return
   */
  public boolean deleteDeviceAdapter(String deviceAdapterName)
  {

    try
    {
      int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(deviceAdapterName);

      if (id != -1 && DatabaseInteraction.getInstance().removeDeviceAdapterByName(deviceAdapterName) && deviceAdapters.containsKey(id))
      {

        deviceAdapters.remove(id);
        return true;
      }
    } catch (Exception ex)
    {
      System.out.println("[Error] at deleteDeviceAdapter" + ex);
    }
    return false;
  }

  public boolean delete_DA_Stuff(String da_id)
  {
    DatabaseInteraction.getInstance().removeRecipeByDaId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByAML_ID(da_id));
    DatabaseInteraction.getInstance().removeModuleByDAId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByAML_ID(da_id));

    //remove skills for a DA. from DAS table
    DatabaseInteraction.getInstance().removeSkillByDaId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByAML_ID(da_id));
    //remove skills that don't have a DA associated
    List<String> availableSkillIDList = DatabaseInteraction.getInstance().getAvailableSkillIDList();
    List<String> DAassociatedSkillIDList = DatabaseInteraction.getInstance().getDAassociatedSkillIDList();

    //check if all the available skills are associated with a da
    for (String availableSkillID : availableSkillIDList)
    {
      boolean exists = false;
      for (String DAassociatedSkillID : DAassociatedSkillIDList)
      {
        if (availableSkillID == null ? DAassociatedSkillID == null : availableSkillID.equals(DAassociatedSkillID))
        {
          exists = true;
          System.out.println(availableSkillID + " Exists");
        }
      }
      if (!exists)
      {
        int ret = DatabaseInteraction.getInstance().removeSkillByID(availableSkillID);
      }
    }

    DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
    List<Recipe> recipes = new ArrayList<>(da.getSubSystem().getRecipes());

    for (Module module : da.getSubSystem().getModules())
    {
      recipes.addAll(module.getRecipes());
    }

    for (Recipe recipe : recipes)
    {
      DatabaseInteraction.getInstance().remove_recipe_from_SR(recipe.getUniqueId());
    }

    return true;
  }

  public boolean delete_DA_Stuff_by_name(String da_name)
  {
    DatabaseInteraction.getInstance().removeRecipeByDaId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(da_name));
    DatabaseInteraction.getInstance().removeModuleByDAId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(da_name));

    //remove skills for a DA. from DAS table
    DatabaseInteraction.getInstance().removeSkillByDaId(DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(da_name));
    //remove skills that don't have a DA associated
    List<String> availableSkillIDList = DatabaseInteraction.getInstance().getAvailableSkillIDList();
    List<String> DAassociatedSkillIDList = DatabaseInteraction.getInstance().getDAassociatedSkillIDList();

    //check if all the available skills are associated with a da
    for (String availableSkillID : availableSkillIDList)
    {
      boolean exists = false;
      for (String DAassociatedSkillID : DAassociatedSkillIDList)
      {
        if (availableSkillID == null ? DAassociatedSkillID == null : availableSkillID.equals(DAassociatedSkillID))
        {
          exists = true;
          System.out.println(availableSkillID + " Exists");
        }
      }
      if (!exists)
      {
        int ret = DatabaseInteraction.getInstance().removeSkillByID(availableSkillID);
      }
    }

    DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(da_name);
    List<Recipe> recipes = new ArrayList<>(da.getSubSystem().getRecipes());

    for (Module module : da.getSubSystem().getModules())
    {
      recipes.addAll(module.getRecipes());
    }

    for (Recipe recipe : recipes)
    {
      DatabaseInteraction.getInstance().remove_recipe_from_SR(recipe.getUniqueId());
    }

    return true;
  }

  /**
   * @param deviceAdapterName
   * @brief WORKSTATIONName vs DEVICE data MAPS
   * @return
   */
  public List<Module> getModulesFromDeviceAdapter(String deviceAdapterName)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(deviceAdapterName);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id).getListOfModules();
    }
    return null;
  }

  public DeviceAdapter getDeviceAdapterFromModuleID(String module_id)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapter_DB_ID_byModuleID(module_id);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id);
    }
    return null;
  }

  /**
   *
   * @param deviceAdapterName
   * @param device
   */
  public void addEquipmentModuleToList(String deviceAdapterName, Module device)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(deviceAdapterName);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      deviceAdapters.get(id).getSubSystem().getModules().add(device);
    }
  }

  /**
   *
   * @param deviceAdapterName
   * @param deviceName
   * @return
   */
  public boolean deleteEquipmentModuleFromList(String deviceAdapterName, String deviceName)
  {
    int id = DatabaseInteraction.getInstance().getDeviceAdapterDB_ID_ByName(deviceAdapterName);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id).removeEquipmentModule(deviceName);
    }
    return false;
  }

  public ArrayList<String> listDevicesByProtocol(EProtocol p)
  {
    switch (p)
    {
      case DDS:
        return DatabaseInteraction.getInstance().listDevicesByProtocol("DDS");
      case OPC:
        return DatabaseInteraction.getInstance().listDevicesByProtocol("OPC");
      default:
        return null;
    }
  }

  /**
   * @brief Wrapper
   * @param deviceAdapterName
   * @return
   */
  public ArrayList<String> getRecipesFromDeviceAdapter(String deviceAdapterName)
  {
    return DatabaseInteraction.getInstance().getRecipesIDByDAName(deviceAdapterName);
  }

  /**
   *
   * @return
   */
  /*
  public List<String> getDeviceAdaptersNames()
  {
    return DatabaseInteraction.getInstance().getDeviceAdapters_name();
  }
   */
  public List<String> getDeviceAdapters_AML_IDs()
  {
    return DatabaseInteraction.getInstance().getDeviceAdapters_AML_ID();
  }

  /**
   *
   * @param deviceAdapterName
   * @param aml_id
   * @param skillName
   * @param recipeValid
   * @param recipeName
   * @param object_id
   * @param method_id
   * @return
   */
  public boolean registerRecipe(String deviceAdapterName, String aml_id, String skillName, String recipeValid, String recipeName, String object_id, String method_id)
  {
    DatabaseInteraction db = DatabaseInteraction.getInstance();
    int da_id = db.getDeviceAdapterDB_ID_ByName(deviceAdapterName);
    int sk_id = db.getSkillIdByName(skillName);
    boolean valid = trueSet.contains(recipeValid);
    if (da_id != -1 && sk_id != -1)
    {
      return db.registerRecipe(aml_id, da_id, sk_id, valid, recipeName, object_id, method_id);
    } else
    {
      return false;
    }
  }

  /**
   *
   * @param deviceAdapterName
   * @param aml_id
   * @param name
   * @param description
   * @return
   */
  public boolean registerSkill(String deviceAdapterName, String aml_id, String name, String description)
  {
    DatabaseInteraction db = DatabaseInteraction.getInstance();
    return db.registerSkill(deviceAdapterName, aml_id, name, description);
  }

  public boolean registerModule(String da_Name, String module_name, String aml_id, String status, String address)
  {
    DatabaseInteraction db = DatabaseInteraction.getInstance();
    return db.registerModule(da_Name, module_name, aml_id, status, address);
  }

  public boolean skillExists(String aml_id)
  {
    DatabaseInteraction db = DatabaseInteraction.getInstance();
    return db.skillExists(aml_id);
  }

  public static String daAgentCreation(DeviceAdapter da)
  {
    if (MSBConstants.USING_CLOUD)
    {
      try
      {
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
        SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

        SubSystem ss = da.getSubSystem();

        if (ss.getDescription() == null || ss.getDescription().length() == 0)
        {
          ss.setDescription(ss.getName());
        }
        if (ss.getRegistered() == null)
        {
          ss.setRegistered(new Date());
        }
        ServiceCallStatus agentStatus;
        if (ss.getSsType().equals("TransportSystem"))
        {
          agentStatus = systemConfigurator.createNewTransportAgent(ss);
          PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
          perfMeasure.getAgentCreationTimers().put(ss.getUniqueId(), new Date().getTime());
          System.out.println("\n\n Creating Transport Agent... \n\n");
        } else
        {
          agentStatus = systemConfigurator.createNewResourceAgent(ss);
          PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
          perfMeasure.getAgentCreationTimers().put(ss.getUniqueId(), new Date().getTime());
          System.out.println("\n\n Creating Resource Agent... \n\n");
        }

        if (agentStatus != null)
        {
          return agentStatus.getCode(); //OK? ou KO?
        } else
        {
          return "KO";
        }
      } catch (Exception ex)
      {
        System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        return "KO";
      }
    } else //no AC
    {
      return "OK - No AgentPlatform";
    }
  }

  public static void updateExecutionTable(String da_id, ExecutionTable execTable)
  {
    DACManager dac = getInstance();
    DeviceAdapter da = dac.getDeviceAdapterbyAML_ID(da_id);
    DeviceAdapterOPC da_opc = (DeviceAdapterOPC) da.getClient();
    MSBClientSubscription client = (MSBClientSubscription) da_opc.getClient();

    ExecutionTable_DA execTable_da = ExecutionTable_DA.createExecutionTable_DA(execTable);
    String execTableSerialized = Functions.ClassToString(execTable_da);
    NodeId objectID = Functions.convertStringToNodeId(execTable_da.getChangeExecutionTableObjectID());
    NodeId methodID = Functions.convertStringToNodeId(execTable_da.getChangeExecutionTableMethodID());

    boolean updateExecTable = client.updateRecipe(client.getClientObject(), objectID, methodID, execTableSerialized);

  }

  public void AssociateRecipeToSR(Skill skill)
  {
    for (SkillRequirement sr : skill.getSkillRequirements())
    {
      if (sr.getRecipeIDs() != null)
      {
        DatabaseInteraction.getInstance().associateRecipeToSR(sr.getUniqueId(), sr.getRecipeIDs());
      }
    }
  }

}
