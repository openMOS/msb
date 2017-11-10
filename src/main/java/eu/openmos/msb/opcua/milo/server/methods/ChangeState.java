package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Recipe;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.PendingProdInstance;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.sdk.server.annotations.UaInputArgument;
import org.eclipse.milo.opcua.sdk.server.annotations.UaMethod;
import org.eclipse.milo.opcua.sdk.server.annotations.UaOutputArgument;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fabio.miranda
 */
public class ChangeState
{

  private final Logger logger = LoggerFactory.getLogger(getClass());
  StopWatch changeStateAndNextRecipeTimer = new StopWatch();

  @UaMethod
  public void invoke(
          AnnotationBasedInvocationHandler.InvocationContext context,
          @UaInputArgument(
                  name = "DA_ID",
                  description = "ID of the device adapter") String da_id,
          @UaInputArgument(
                  name = "Product_ID",
                  description = "Product instance ID") String product_id,
          @UaInputArgument(
                  name = "recipe_id",
                  description = "current Recipe ID") String recipe_id,
          @UaOutputArgument(
                  name = "Ackowledge",
                  description = "Acknowledge 1-OK 0-NOK") AnnotationBasedInvocationHandler.Out<Integer> result)
  {
    changeStateAndNextRecipeTimer.reset();
    changeStateAndNextRecipeTimer.start();
      
    logger.debug("Change State invoked! '{}'", context.getObjectNode().getBrowseName().getName());
    System.out.println("Change State invoked with parameters-> DaID:" + da_id + " productID: " + product_id + " recipeID:" + recipe_id);

      //read recipe KPIs
      Thread threadKPI = new Thread() {
          public synchronized void run() {
              readKPIs(da_id, recipe_id); //MASMEC comment
          }
      };
      threadKPI.start();

      Thread threadCheck = new Thread() {
          public synchronized void run() {
              ChangeStateChecker(recipe_id, product_id, da_id);
          }
      };
      threadCheck.start();

    //TODO Check current recipeState (running, idle, ready, etc?)
    result.set(1); //ok or nok
  }

  private Boolean checkNextValidation(String nextRecipeID)
  {
    String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(nextRecipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
      for (int i = 0; i < da.getExecutionTable().getRows().size(); i++)
      {
        if (da.getExecutionTable().getRows().get(i).getRecipeId() == null ? nextRecipeID == null : da.getExecutionTable().getRows().get(i).getRecipeId().equals(nextRecipeID))
        {
          String auxNextLKT1 = da.getExecutionTable().getRows().get(i).getNextRecipeId();
          boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(auxNextLKT1);
          return valid;
        }
      }
    }
    return false;
  }

  private String checkNextRecipe(String recipeID)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(recipeID); //there can be only one?
    System.out.println("DA id from checkNExtRecipe: "+Daid);

    if (Daid != null)
    {
      //get DA name
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
      System.out.println("DA name from checkNExtRecipe: "+DA_name);
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
      if(da!=null){
        System.out.println("The DA is null!");
      }
      for (int i = 0; i < da.getExecutionTable().getRows().size(); i++)
      {
        if (da.getExecutionTable().getRows().get(i).getRecipeId() == null ? recipeID == null : da.getExecutionTable().getRows().get(i).getRecipeId().equals(recipeID))
        {
          //get the nextRecipe on its executionTables
          //String NextRecipe = da.getExecutionTable().getRows().get(i).getNextRecipeId(); //NO PUEDE
          
          int nsindex=Integer.parseInt(da.getExecutionTable().getRows().get(i).getNextRecipeIdPath().split(":")[0]);
          String identifier = da.getExecutionTable().getRows().get(i).getNextRecipeIdPath().split(":")[1]+":"+ da.getExecutionTable().getRows().get(i).getNextRecipeIdPath().split(":")[2];
          
          NodeId nextRecipeNode = new NodeId(nsindex, identifier+"ID"); //CHECK THIS
           
          //NodeId nextRecipeNode = convertStringToNodeId(da.getExecutionTable().getRows().get(i).getNextRecipeIdPath());
          //DeviceAdapterOPC client = (DeviceAdapterOPC) da.getClient();
          MSBClientSubscription client = (MSBClientSubscription) da.getClient();
          
          String nextRecipeID = "";
          try
          {
            nextRecipeID = client.getClientObject().readValue(0, TimestampsToReturn.Neither, nextRecipeNode).get().getValue().getValue().toString();
          } catch (InterruptedException | ExecutionException ex)
          {
            java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
          }
          if (nextRecipeID==null || nextRecipeID.equals("done"))//CHECK THIS WiTH KIRILL
          { //ATENÇÃO: DONE??
            return "last";
          }

          List<String> PossibleRecipeChoices = da.getExecutionTable().getRows().get(i).getPossibleRecipeChoices();
          //check if there are more possible choices
          if (!PossibleRecipeChoices.isEmpty())
          {
            boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(nextRecipeID);
            if (valid)
            {
              //yes
              if (checkNextValidation(nextRecipeID))
              {
                return nextRecipeID;
              }
            } else
            {
              for (int j = 0; j < PossibleRecipeChoices.size(); j++)
              {
                String choice = PossibleRecipeChoices.get(j);
                String Daid1 = DatabaseInteraction.getInstance().getDAIDbyRecipeID(choice);
                if (Daid1 != null)
                {
                  String DA_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid1);
                  System.out.println("DA_name1: " + DA_name1);
                  DeviceAdapter da1 = DACManager.getInstance().getDeviceAdapter(DA_name1);
                  
                  if(da1==null){
                    System.out.println("DA class not found!! for daname: "+DA_name1);
                  }
                  
                  for (int l = 0; l < da1.getExecutionTable().getRows().size(); l++)
                  {
                    if (da1.getExecutionTable().getRows().get(l).getRecipeId() == null ? choice == null : da1.getExecutionTable().getRows().get(l).getRecipeId().equals(choice))
                    {
                      String NextRecipe1 = da1.getExecutionTable().getRows().get(l).getNextRecipeId();
                      System.out.println("NextRecipe from execution table: "+NextRecipe1);
                      boolean valid1 = DatabaseInteraction.getInstance().getRecipeIdIsValid(NextRecipe1);
                      if (valid1)
                      {
                        //yes
                        System.out.println("Recipe is valid!");
                        if (checkNextValidation(NextRecipe1))
                        {
                          return NextRecipe1;
                        } else
                        {
                          System.out.println("the next of the next of the choice isn't valid");
                          //return "";
                        }
                      } else
                      {
                        System.out.println("the next of the choice isn't valid");
                        //return "";
                      }
                    }
                  }
                } else
                {
                  System.out.println("There are no Adapters that can perform the required choice recipe: " + recipeID);
                }
              }
            }
          } else
          {
            boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(nextRecipeID);
            if (valid)
            {
              //yes
              return nextRecipeID;
            } else
            {
              System.out.println("There are no other Recipe choices for the current recipe " + recipeID);
            }
          }
        }
      }
    } else
    {
      //String recipeName=DatabaseInteraction.getInstance().getRecipeName(recipeID); //VER IDs ->de int para String
      System.out.println("There are no Adapters that can perform the required recipe: " + recipeID);
    }

    return "";
  }
/**
 * 
 * @param nextRecipeID
 * @return true if the adapter is at ready state
 */
  private boolean checkAdapterState(String nextRecipeID)
  {
    String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(nextRecipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
      //add adapter states strings to properties
      if (da.getSubSystem().getState() == null ? MSBConstants.ADAPTER_STATE_READY == null : da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
      {
        System.out.println("the next recipe Adapter (" + DA_name + ") is ready for execution!");
        return true;
      } else
      {
        System.out.println("Adapter cannot start another recipe! - " + da.getSubSystem().getState());
      }
    }
    return false;
  }

  private void ChangeStateChecker(String recipe_id, String product_id, String da_id){
    String nextRecipeID = checkNextRecipe(recipe_id); //returns the next recipe to execute
    System.out.println("Next Recipe to execute will be: "+nextRecipeID);
    //String nextRecipeID=recipe_id; //MASMEC
    
    if (!nextRecipeID.isEmpty() && !nextRecipeID.equals("last"))
    {
      if (checkAdapterState(nextRecipeID)) //check if adapter is ready
      { //if is ready
        System.out.println("[Change State] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State");
        String method = DatabaseInteraction.getInstance().getRecipeMethodByID(nextRecipeID); //CRASHED HERE MASMEC! CHECK THIS
        System.out.println("method returned from DB: "+method);
        NodeId methodNode = new NodeId(Integer.parseInt(method.split(":")[0]), method.substring(method.indexOf(":")+1));
        String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(nextRecipeID);
        System.out.println("objectID returned from DB: "+obj);

        NodeId objNode = new NodeId(Integer.parseInt(obj.split(":")[0]), obj.substring(obj.indexOf(":")+1));
        String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(nextRecipeID);
        System.out.println("Daid from DB: "+Daid);
        
        String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
        System.out.println("DA_name from DB: "+DA_name);
        
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
        System.out.println("Trying to Invoke the nextRecipe"+ "(" + nextRecipeID + ")" +" in DA: " + DA_name);
        MSBClientSubscription client = (MSBClientSubscription) da.getClient();
        
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        changeStateAndNextRecipeTimer.stop();
        
        boolean res = client.InvokeDeviceSkill(client.getClientObject(), objNode, methodNode, product_id);
        System.out.println("Invoke output: " + res);
        if (res)
        {
          //do happy flow stuff
        } else
        {
          //something wrong happened :c
        }
      } else //if adapter is not ready
      {
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        changeStateAndNextRecipeTimer.stop();
        
        //save daIDnext, nextRecipe and prodID to execute later
        String Daid_Next = DatabaseInteraction.getInstance().getDAIDbyRecipeID(nextRecipeID);
        List<PendingProdInstance> auxProdId = PECManager.getInstance().getPendejos().get(Daid_Next);
        if (auxProdId == null)
        {
          List<PendingProdInstance> ppi = new ArrayList<>();
          PendingProdInstance ppinst = new PendingProdInstance(nextRecipeID, product_id, Daid_Next);
          ppi.add(ppinst);
          PECManager.getInstance().getPendejos().put(Daid_Next, ppi);
        }
        else{
          auxProdId.add(new PendingProdInstance(nextRecipeID, product_id, Daid_Next));
          PECManager.getInstance().getPendejos().put(Daid_Next, auxProdId);
        }
        
        System.out.println("The adapter state is not ready! The recipe: " + nextRecipeID + "could not be called.\nIt was added to the pendent product list for DA ID: "+Daid_Next);

      }
    } else if(nextRecipeID.equals("last"))
    {
      PECManager.getInstance().getProductsDoing().remove(product_id);
      System.out.println("[ChangeState] Next Recipe is the last one for product instance ID: "+product_id);
      
    }else if(nextRecipeID.isEmpty()){
      
      System.out.println("NextRecipeId is empty. What now? just sent the KPIs?");
    }
    
    try
    {
      PECManager.getInstance().PendejoCheckerThread(da_id); //Run the pendent product instances checker thread. for the current adapter 
    } catch (InterruptedException | ExecutionException ex)
    {
      java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
    }
    
  }
  
  private static NodeId convertStringToNodeId(String toConvert)
  {    
    int ns = Integer.parseInt(toConvert.split(":")[0]);
    String aux = toConvert.substring(toConvert.indexOf(":")+1);
    return new NodeId(ns, aux);
  }
  
  /**
   * Read KPIs of recipe_id from OPCServer
   * @param da_id
   * @param recipe_id 
   */
  private void readKPIs(String da_id, String recipe_id)
  {
    DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapter(DatabaseInteraction.getInstance().getDeviceAdapterNameByID(da_id));
    if (CurrentDA != null)
    {
      List<Recipe> auxRepList = CurrentDA.getListOfRecipes();

      for (Recipe recipe : auxRepList)
      {
        if (recipe.getUniqueId().equals(recipe_id))
        {
          for (KPISetting kpi : recipe.getKpiSettings())
          {
            try
            {
              NodeId kpiPath = convertStringToNodeId(kpi.getPath());
              DeviceAdapterOPC client = (DeviceAdapterOPC) CurrentDA.getClient();
              String kpiValue = client.getClient().getClientObject().readValue(0, TimestampsToReturn.Neither, kpiPath).get().toString();
              kpi.setValue(kpiValue);

            } catch (InterruptedException | ExecutionException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
          break;
        }
        //IF THE AC is activated, send the KPIs upwards
        String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
        boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();

        if (withAGENTCloud)
        {
          //add header to vertX message?
          DeliveryOptions options = new DeliveryOptions();
          options.addHeader(eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, "some-value"); //use this??
          CurrentDA.getVertx().eventBus().publish(da_id, recipe.toString(), options); //serialize the entire class??
        } else
        {

        }
      }
    }
  }
}
