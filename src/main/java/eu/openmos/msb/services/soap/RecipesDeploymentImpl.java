package eu.openmos.msb.services.soap;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import static eu.openmos.msb.utilities.Functions.XMLtoString;
import java.io.File;
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
  public boolean updateRecipes(String deviceName, int mode, List<Recipe> recipes)
  {
    logger.debug("updateRecipes MSB method");
    logger.debug("device name = [" + deviceName + "]");

    logger.info("[RecipesDeploymentImpl] Received an update Recipe call from Cloud to DA: " + deviceName);

    logger.debug("mode = [" + mode + "]");
    if (mode == RecipesDeploymentImpl.SUGGESTION)
    {
      logger.debug("suggested recipes mode");
    } else if (mode == RecipesDeploymentImpl.ACTUAL)
    {
      logger.debug("actual recipes mode");
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(deviceName);
      if (da != null)
      {
        MSBClientSubscription client = (MSBClientSubscription) da.getClient();

         try
          {
            File file = new File("updateRecipes.xml");
            javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(ExecutionTable.class);
            Marshaller jaxbMArshaller = jc.createMarshaller();
            jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMArshaller.marshal(recipes, System.out); //print in the console
            jaxbMArshaller.marshal(recipes, file); //print in the file
            String recipesString = XMLtoString("updateRecipes.xml"); //TODO: use outputStream instead of file!
            
          } catch (JAXBException ex)
          {
            java.util.logging.Logger.getLogger(RecipesDeploymentImpl.class.getName()).log(Level.SEVERE, null, ex);
          }
         
        //boolean res = client.InvokeUpdateRecipe(client.getClientObject(), objNode, methodNode, mode, recipesString); //TO be done by DA
      } else
      {
        logger.error("DA for recipe update not found in the database!");
      }
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

    for (int i = 0; i < deviceAdaptersNames.size(); i++)
    {
      DeviceAdapter deviceAdapter = DACManager.getInstance().getDeviceAdapterbyName(deviceAdaptersNames.get(i));
      if (deviceAdapter != null)
      {
        if (deviceAdapter.getSubSystem().getExecutionTable().getUniqueId().equals(execTableUniqueId))
        {
          MSBClientSubscription client = (MSBClientSubscription) deviceAdapter.getClient();
          
          try
          {
            File file = new File("updateExecTables.xml");
            javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(ExecutionTable.class);
            Marshaller jaxbMArshaller = jc.createMarshaller();
            jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMArshaller.marshal(executionTable, System.out); //print in the console
            jaxbMArshaller.marshal(executionTable, file); //print in the file
            String excTablesString = XMLtoString("updateExecTables.xml"); //TODO: use outputStream instead of file!
            
          } catch (JAXBException ex)
          {
            java.util.logging.Logger.getLogger(RecipesDeploymentImpl.class.getName()).log(Level.SEVERE, null, ex);
          }
          
          //client.InvokeExecTableUpdate(client, NodeId.NULL_GUID, NodeId.NULL_GUID, excTablesString); //TO be done by DA
          logger.info("Sending new execution table to DA: " + deviceAdaptersNames.get(i));
        }
      }
    }

    return true;
  }

}
