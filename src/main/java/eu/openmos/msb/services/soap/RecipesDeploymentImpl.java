package eu.openmos.msb.services.soap;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import eu.openmos.model.Recipe_DA;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.services.rest.SystemStatusController;
import eu.openmos.msb.utilities.Functions;
import static eu.openmos.msb.utilities.Functions.XMLtoString;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import javax.jws.WebService;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.persistence.jaxb.JAXBContext;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService(endpointInterface = "eu.openmos.msb.services.soap.RecipesDeployment", serviceName = "RecipesDeployment")
public class RecipesDeploymentImpl implements RecipesDeployment
{

  public static int SUGGESTION = 1;
  public static int ACTUAL = 2;
  public static int ACTIVATION = 3;
  private static final Logger logger = Logger.getLogger(RecipesDeploymentImpl.class.getName());

  @Override
  public boolean updateRecipes(String DAid, int mode, List<Recipe> recipes)
  {
    
    Boolean ret = false;
    logger.debug("updateRecipes MSB method");
    logger.debug("device ID = [" + DAid + "]");
    
    String deviceName=DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(DAid);

    logger.info("[RecipesDeploymentImpl] Received an update Recipe call from Cloud to DA: " + deviceName);

    logger.debug("mode = [" + mode + "]");
    if (mode == RecipesDeploymentImpl.SUGGESTION)
    {
      logger.debug("suggested recipes mode");
      
            DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(deviceName);
      if (da != null)
      {
        
        //updated:*******
        for (int j = 0; j < recipes.size(); j++)
        {
          boolean found = false;
          for (int i = 0; i < da.getSubSystem().getRecipes().size(); i++)
          {
            if (da.getSubSystem().getRecipes().get(i).getUniqueId().equals(recipes.get(j).getUniqueId()))
            {
              found = true;
              da.getSubSystem().getRecipes().get(i).setDescription(recipes.get(j).getDescription());
              da.getSubSystem().getRecipes().get(i).setEquipmentIds(recipes.get(j).getEquipmentIds());
              da.getSubSystem().getRecipes().get(i).setExecutedBySkillControlPort(recipes.get(j).getExecutedBySkillControlPort());
              da.getSubSystem().getRecipes().get(i).setInvokeMethodID(recipes.get(j).getInvokeMethodID());
              da.getSubSystem().getRecipes().get(i).setInvokeObjectID(recipes.get(j).getInvokeObjectID());
              da.getSubSystem().getRecipes().get(i).setKpiSettings(recipes.get(j).getKpiSettings());
              da.getSubSystem().getRecipes().get(i).setLastOptimizationTime(recipes.get(j).getLastOptimizationTime());
              da.getSubSystem().getRecipes().get(i).setMsbProtocolEndpoint(recipes.get(j).getMsbProtocolEndpoint());
              da.getSubSystem().getRecipes().get(i).setName(recipes.get(j).getName());
              da.getSubSystem().getRecipes().get(i).setOptimized(recipes.get(j).isOptimized());
              da.getSubSystem().getRecipes().get(i).setParameterSettings(recipes.get(j).getParameterSettings());
              da.getSubSystem().getRecipes().get(i).setRegistered(recipes.get(j).getRegistered());
              da.getSubSystem().getRecipes().get(i).setSkill(recipes.get(j).getSkill());
              da.getSubSystem().getRecipes().get(i).setSkillRequirements(recipes.get(j).getSkillRequirements());
              da.getSubSystem().getRecipes().get(i).setState(recipes.get(j).getState());
              da.getSubSystem().getRecipes().get(i).setStatePath(recipes.get(j).getStatePath());
              da.getSubSystem().getRecipes().get(i).setUniqueAgentName(recipes.get(j).getUniqueAgentName());
              da.getSubSystem().getRecipes().get(i).setUniqueId(recipes.get(j).getUniqueId());
              da.getSubSystem().getRecipes().get(i).setValid(recipes.get(j).isValid());

            }
          }
          if (!found)
          {
            da.getSubSystem().getRecipes().add(recipes.get(j));
          }
        }

      
        SystemStatusController ssc = null;
        if (ssc != null && !ssc.getSystemStatus().equals("PRODUCTION"))
        {
          int count = 0;
          for (int i = 0; i < recipes.size(); i++)
          {
            Recipe_DA recipe_DA = Recipe_DA.createRecipe_DA(recipes.get(i));
            MSBClientSubscription client = (MSBClientSubscription) da.getClient();
            String RecipeSerialized = Functions.ClassToString(recipe_DA);
            NodeId objectID = Functions.convertStringToNodeId(recipe_DA.getChangeRecipeObjectID());
            NodeId methodID = Functions.convertStringToNodeId(recipe_DA.getChangeRecipeMethodID());
            boolean updateRecipe = client.updateRecipe(client.getClientObject(), objectID, methodID, RecipeSerialized);
            if (updateRecipe)
            {
              count++;
            }
          }
          if (count == recipes.size())
          {
            return true;
          } else
          {
            return false;
          }
        }
        return true;
      } else
      {
        logger.error("DA for recipe update not found in the database!");
        return false;
      }
      
    } else if (mode == RecipesDeploymentImpl.ACTUAL)
    {
      logger.debug("actual recipes mode");

    } else if (RecipesDeploymentImpl.ACTIVATION == mode)
    {
      logger.debug("activation of already sent recipes mode");
      //msb should activate the selected Recipe (OPC namespace write?  method? )
    } else
    {
      logger.debug("unknown mode");
    }

    logger.debug("recipes list = [" + recipes + "]");

    return true;
  }

  @Override
  public boolean updateExecutionTable(ExecutionTable executionTable)
  {
    logger.debug("updateExecutionTable MSB method");
    logger.debug("executionTable: " + executionTable);

    String execTableUniqueId = executionTable.getUniqueId();
    List<String> deviceAdaptersNames = DACManager.getInstance().getDeviceAdaptersNames();
    Boolean ret = false;

    for (int i = 0; i < deviceAdaptersNames.size(); i++)
    {
      DeviceAdapter deviceAdapter = DACManager.getInstance().getDeviceAdapterbyName(deviceAdaptersNames.get(i));
      if(deviceAdaptersNames.get(i).contains("MSB"))
        continue;
      
      if (deviceAdapter != null)
      {

        if (deviceAdapter.getSubSystem().getExecutionTable().getUniqueId().equals(execTableUniqueId))
        {
          deviceAdapter.getSubSystem().setExecutionTable(executionTable);
          //MSBClientSubscription client = (MSBClientSubscription) deviceAdapter.getClient();

          /*try
          {
            File file = new File("updateExecTables.xml");
            javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(ExecutionTable.class);
            Marshaller jaxbMArshaller = jc.createMarshaller();
            jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMArshaller.marshal(executionTable, System.out); //print in the console
            jaxbMArshaller.marshal(executionTable, file); //print in the file
            StringWriter sw = new StringWriter();
            jaxbMArshaller.marshal(executionTable, sw); //print to String
            //String excTablesString = XMLtoString("updateExecTables.xml"); //TODO: use outputStream instead of file!
            String excTablesString = sw.toString();
            
          } catch (JAXBException ex)
          {
            java.util.logging.Logger.getLogger(RecipesDeploymentImpl.class.getName()).log(Level.SEVERE, null, ex);
          }*/
          //client.InvokeExecTableUpdate(client, NodeId.NULL_GUID, NodeId.NULL_GUID, excTablesString); //TO be done by DA
          ret = true;
          logger.info("Sending new execution table to DA: " + deviceAdaptersNames.get(i));
        } else
        {

        }
      }
    }

    return ret;
  }

}
