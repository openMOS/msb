package eu.openmos.msb.services.soap;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
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
        /*List<Recipe> daRecipesList = da.getSubSystem().getRecipes();
        for (int j = 0; j < daRecipesList.size(); j++)
        {
          boolean exists=false;
          for (int i = 0; i < recipes.size(); i++)
          {
            if (daRecipesList.get(j).getUniqueId().equals(recipes.get(i).getUniqueId()))
            {
              recipes.get(i).setDescription(recipes.get(i).getDescription());
              recipes.get(i).setEquipmentIds(recipes.get(i).getEquipmentIds());
              recipes.get(i).setExecutedBySkillControlPort(recipes.get(i).getExecutedBySkillControlPort());
              recipes.get(i).setInvokeMethodID(recipes.get(i).getInvokeMethodID());
              recipes.get(i).setInvokeObjectID(recipes.get(i).getInvokeObjectID());
              recipes.get(i).setKpiSettings(recipes.get(i).getKpiSettings());
              recipes.get(i).setLastOptimizationTime(recipes.get(i).getLastOptimizationTime());
              recipes.get(i).setMsbProtocolEndpoint(recipes.get(i).getMsbProtocolEndpoint());
              recipes.get(i).setName(recipes.get(i).getName());
              recipes.get(i).setOptimized(recipes.get(i).isOptimized());
              recipes.get(i).setParameterSettings(recipes.get(i).getParameterSettings());
              recipes.get(i).setRegistered(recipes.get(i).getRegistered());
              recipes.get(i).setSkill(recipes.get(i).getSkill());
              recipes.get(i).setSkillRequirements(recipes.get(i).getSkillRequirements());
              recipes.get(i).setState(recipes.get(i).getState());
              recipes.get(i).setStatePath(recipes.get(i).getStatePath());
              recipes.get(i).setUniqueAgentName(recipes.get(i).getUniqueAgentName());
              recipes.get(i).setUniqueId(recipes.get(i).getUniqueId());
              recipes.get(i).setValid(recipes.get(i).isValid());
              exists=true;
            }
          }
          if(!exists){
            
          }

        }*/

        //updated:*******
        for (int j = 0; j < recipes.size(); j++)
        {
          for (int i = 0; i < da.getSubSystem().getRecipes().size(); i++)
          {
            if (da.getSubSystem().getRecipes().get(i).getUniqueId().equals(recipes.get(j).getUniqueId()))
            {
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

            } else if (!da.getSubSystem().getRecipes().contains(recipes.get(j)))
            { //if it doesn't exist, add it to DA's recipes list - DONT WORK
              da.getSubSystem().getRecipes().add(recipes.get(j));
            }
          }

        }

        //*********
        //MSBClientSubscription client = (MSBClientSubscription) da.getClient();

        /*try
        {
          File file = new File("updateRecipes.xml");
          javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(ExecutionTable.class);
          Marshaller jaxbMArshaller = jc.createMarshaller();
          jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
          jaxbMArshaller.marshal(da.getSubSystem().getRecipes(), System.out); //print in the console
          jaxbMArshaller.marshal(da.getSubSystem().getRecipes(), file); //print in the file

          StringWriter sw = new StringWriter();
          jaxbMArshaller.marshal(da.getSubSystem().getRecipes(), sw); //print to String
          //String recipesString = XMLtoString("updateRecipes.xml"); //TODO: use outputStream instead of file!
          String recipesString = sw.toString();

        } catch (JAXBException ex)
        {
          java.util.logging.Logger.getLogger(RecipesDeploymentImpl.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //boolean res = client.InvokeUpdateRecipe(client.getClientObject(), objNode, methodNode, mode, recipesString); //TO be done by DA
        ret = true;
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
