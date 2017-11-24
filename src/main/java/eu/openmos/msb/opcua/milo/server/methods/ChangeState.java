package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.FinishedProductInfo;
import eu.openmos.model.KPISetting;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.utilities.Functions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.sdk.server.annotations.UaInputArgument;
import org.eclipse.milo.opcua.sdk.server.annotations.UaMethod;
import org.eclipse.milo.opcua.sdk.server.annotations.UaOutputArgument;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
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
                  description = "Product instance ID") String productInstance_id,
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
    System.out.println("[CHANGE_STATE]Change State invoked with parameters-> DaID:" + da_id + " productID: " + productInstance_id + " recipeID:" + recipe_id);

    String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
    DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
    //add adapter states strings to properties
    NodeId statePath = Functions.convertStringToNodeId(da.getSubSystem().getStatePath());
    DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
    String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
    da.getSubSystem().setState(state);

    if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
    {
      System.out.println("[ChangeState] ADAPTER ERROR: " + da.getSubSystem().getName());
    }

    String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
    MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, da_name1);

    //read recipe KPIs
    Thread threadKPI = new Thread()
    {
      public synchronized void run()
      {
        readKPIs(da_id, recipe_id, productInstance_id); //MASMEC comment
      }
    };
    threadKPI.start();

    Thread threadCheck = new Thread()
    {
      public synchronized void run()
      {
        ChangeStateChecker(recipe_id, productInstance_id, da_id);
      }
    };
    threadCheck.start();

    //TODO Check current recipeState (running, idle, ready, etc?)
    result.set(1); //ok or nok
  }

  private void ChangeStateChecker(String recipe_id, String productInst_id, String da_id)
  {
    String nextRecipeID = checkNextRecipe(recipe_id); //returns the next recipe to execute
    System.out.println("[ChangeStateChecker]Next Recipe to execute will be: " + nextRecipeID + "\n");
    //String nextRecipeID=recipe_id; //MASMEC

    if (!nextRecipeID.isEmpty() && !nextRecipeID.equals("last"))
    {
      if (checkAdapterState(nextRecipeID)) //check if adapter is ready
      { //if is ready
        System.out.println("[ChangeStateChecker] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State\n");
        String method = DatabaseInteraction.getInstance().getRecipeMethodByID(nextRecipeID); //CRASHED HERE MASMEC! CHECK THIS
        NodeId methodNode = new NodeId(Integer.parseInt(method.split(":")[0]), method.substring(method.indexOf(":") + 1));
        String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(nextRecipeID);
        NodeId objNode = new NodeId(Integer.parseInt(obj.split(":")[0]), obj.substring(obj.indexOf(":") + 1));
        String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);

        String DA_name_next = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid_next);
        System.out.println("DA_name from DB: " + DA_name_next);

        DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyName(DA_name_next);
        System.out.println("[ChangeStateChecker] Trying to Invoke the nextRecipe" + "(" + nextRecipeID + ")" + " in DA: " + DA_name_next);
        MSBClientSubscription client = (MSBClientSubscription) da_next.getClient();

        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();

        boolean res = client.InvokeDeviceSkill(client.getClientObject(), objNode, methodNode, productInst_id);
        System.out.println("[EXECUTE] recipeID: " + nextRecipeID);
        if (res)
        {
          //release previous adapter
          String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
          System.out.println("[SEMAPHORE] RELEASED1 " + da_name1);
          PECManager.getInstance().getExecutionMap().get(da_id).release();

          MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, DA_name_next);

        } else
        {
          MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, "ERROR");
        }
      } else //if adapter is not ready
      {
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();
        System.out.println("NEXT ADAPTER IS AT ERROR STATE");
      }
    } else if (nextRecipeID.equals("last"))
    {
      ProductInstance prodInst = PECManager.getInstance().getProductsDoing().remove(productInst_id);

      if (prodInst != null)
      {
        MSB_gui.addToTableExecutedOrders(prodInst.getOrderId(), prodInst.getProductId(), prodInst.getUniqueId());
        MSB_gui.removeFromTableCurrentOrder(prodInst.getUniqueId());

        System.out.println("[ChangeState] This Recipe is the last one for product instance ID: " + productInst_id);
        String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
        System.out.println("[SEMAPHORE" + da_name1 + "] RELEASED1");
        PECManager.getInstance().getExecutionMap().get(da_id).release();

        Long prodTime= new Date().getTime() - prodInst.getStartedProductionTime().getTime();
        PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);
        
        String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
        boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
        if (withAGENTCloud)
        {
          // THIS CODE IS WORKING!! 
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
          FinishedProductInfo fpi = new FinishedProductInfo();
          fpi.setProductInstanceId(productInst_id);
          fpi.setFinishedTime(new Date());
          fpi.setRegistered(prodInst.getStartedProductionTime());
          systemConfigurator.finishedProduct(fpi);
        }
      }
      else{
        System.out.println("[ChangeStateChecker] ERROR prodInst not found: " + productInst_id);
      }
    } else if (nextRecipeID.isEmpty())
    {
      System.out.println("It was not possible to execute the next Recipe. The path is not available!/n"
              + " Last executed Recipe: " + recipe_id + " ProdInst_ID: " + productInst_id);
    }
  }

  private boolean checkAdapterState(String nextRecipeID)
  {
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      //add adapter states strings to properties
      do
      {
        NodeId statePath = Functions.convertStringToNodeId(da.getSubSystem().getStatePath());
        DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
        String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
        da.getSubSystem().setState(state);
      } while (!da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY) && !da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR));
      //if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())

      if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
      {
        DeviceAdapter da_next_next = getDAofNextRecipe(da, nextRecipeID);
        if (da_next_next != null)
        {
          try
          {
            System.out.println("[SEMAPHORE] Acquiring for " + da_next_next.getSubSystem().getName());
            PECManager.getInstance().getExecutionMap().get(da_next_next.getSubSystem().getUniqueId()).acquire();
            System.out.println("[SEMAPHORE] ACQUIRED for " + da_next_next.getSubSystem().getName());
          } catch (InterruptedException ex)
          {
            java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
          }
        } else
        {
          System.out.println("Next Recipe is the last ");
        }

        System.out.println("the next recipe Adapter (" + DA_name + ") is ready for execution!");
        return true;
      } else
      {
        System.out.println("ERROR in DA " + DA_name);
        return false;
      }
    }
    return false;
  }

  private Boolean checkNextValidation(String nextRecipeID)
  {
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      for (int i = 0; i < da.getExecutionTable().getRows().size(); i++)
      {
        if (da.getExecutionTable().getRows().get(i).getRecipeId() == null ? nextRecipeID == null : da.getExecutionTable().getRows().get(i).getRecipeId().equals(nextRecipeID))
        {
          String auxNextLKT1 = da.getExecutionTable().getRows().get(i).getNextRecipeId();
          boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(auxNextLKT1);
          if (valid && !da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
            return true;
          else
            return false;
        }
      }
    }
    return false;
  }

  private String checkNextRecipe(String recipeID)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    System.out.println("DA id from checkNExtRecipe: " + Daid);

    if (Daid != null)
    {
      //get DA name
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      System.out.println("[checkNextRecipe]DA name of finished recipe : " + DA_name);
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      if (da == null)
      {
        System.out.println("The DA is null!");
      } else
      {
        for (int i = 0; i < da.getExecutionTable().getRows().size(); i++)
        {
          if (da.getExecutionTable().getRows().get(i).getRecipeId() == null ? recipeID == null : da.getExecutionTable().getRows().get(i).getRecipeId().equals(recipeID))
          {
            //get the nextRecipe on its executionTables
            String auxNextRecipeNode = da.getExecutionTable().getRows().get(i).getNextRecipeIdPath();

            if (auxNextRecipeNode == null)
            {
              //is last recipe
              return "last";
            }

            NodeId nextRecipeNode = Functions.convertStringToNodeId(auxNextRecipeNode);
            DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;

            String nextRecipeID = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), nextRecipeNode);

            if (nextRecipeID == null || nextRecipeID.equals("done") || nextRecipeID.equals("last"))
            { //ATENÇÃO: DONE??
              return "last";
            } else if (nextRecipeID.isEmpty())
            {
              return "";
            }

            String SR_ID = DatabaseInteraction.getInstance().getSkillReqIDbyRecipeID(nextRecipeID);
            List<String> recipeID_for_SR = DatabaseInteraction.getInstance().getRecipesIDbySkillReqID(SR_ID);

            Boolean testing = true;
            if (testing)
            {
              if (recipeID_for_SR.size() > 1)
              {
                /*
                try
                {
                  Thread.sleep(5000);
                } catch (InterruptedException ex)
                {
                  java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
                }
                */
                Recipe firstRecipe = null;
                //check if the precedences are the same
                List<Recipe> recipes = new ArrayList<>();
                for (String auxRecipeID : recipeID_for_SR)
                {
                  String da_db_ID = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(auxRecipeID);
                  if (da_db_ID ==null)
                    continue;
                  String da_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(da_db_ID);
                  DeviceAdapter auxDA = DACManager.getInstance().getDeviceAdapterbyName(da_name);
                  for (Recipe auxRecipe : auxDA.getListOfRecipes())
                  {
                    if (auxRecipe.getUniqueId().equals(auxRecipeID))
                    {
                      recipes.add(auxRecipe);
                      if (auxRecipeID.equals(nextRecipeID))
                          firstRecipe = auxRecipe;
                      break;
                    }
                  }

                }
                //first validate if the main path, if not available check the others
                if (firstRecipe != null)
                    recipes.remove(firstRecipe);

                if (checkNextValidation(nextRecipeID))
                {
                  return nextRecipeID;
                } else
                {
                  for (Recipe auxRecipe : recipes)
                  {
                    if (checkNextValidation(auxRecipe.getUniqueId()))
                    {
                      return auxRecipe.getUniqueId();
                    }
                  }
                  System.out.println("There are no other Recipe choices for the current recipe " + recipeID);
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
  public static DeviceAdapter getDAofNextRecipe(DeviceAdapter da, String recipeID)
  {
    String nextRecipeID = "";
    for (ExecutionTableRow auxRow : da.getExecutionTable().getRows())
    {
      if (auxRow.getRecipeId().equals(recipeID))
      {
        nextRecipeID = auxRow.getNextRecipeId();
        String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
        if (Daid_next != null)
        {
          String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid_next);
          DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
          return da_next;
        }
        break;
      }
    }
    return null;
  }

  /**
   * Read KPIs of recipe_id from OPCServer
   *
   * @param da_id
   * @param recipe_id
   */
  private void readKPIs(String da_id, String recipe_id, String productInst_ID)
  {
    DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterbyName(DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id));
    if (CurrentDA != null)
    {
      List<Recipe> auxRepList = CurrentDA.getListOfRecipes();

      for (Recipe recipe : auxRepList)
      {
        if (recipe.getUniqueId().equals(recipe_id))
        {
          for (KPISetting kpi : recipe.getKpiSettings())
          {
            NodeId kpiPath = Functions.convertStringToNodeId(kpi.getPath());
            DeviceAdapterOPC client = (DeviceAdapterOPC) CurrentDA;
            String kpiValue = Functions.readOPCNodeToString(client.getClient().getClientObject(), kpiPath);
            kpi.setValue(kpiValue);
            System.out.println("[KPI] DA: "+ CurrentDA.getSubSystem().getName() + " | KPI_Name: " + kpi.getName() + " | KPI_Value: " + kpi.getValue());
          }

          //IF THE AC is activated, send the KPIs upwards
          String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
          boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
          if (withAGENTCloud)
          {
            RecipeExecutionData red = new RecipeExecutionData();
            red.setKpiSettings(recipe.getKpiSettings());
            red.setParameterSettings(recipe.getParameterSettings());
            red.setProductInstanceId(productInst_ID);
            red.setRecipeId(recipe_id);
            red.setRegistered(new Date());
            //add header to vertX message?
            System.out.println("...sending KPI's over websockets...");
            DeliveryOptions options = new DeliveryOptions();            
            // options.addHeader(eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, "MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA"); //use this??
            options.addHeader("messageType", eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA); //use this??
            JsonObject objectToSend = JsonObject.mapFrom(red);

            CurrentDA.getVertx().eventBus().send(productInst_ID, objectToSend, options); //serialize the entire class??
            
            SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
            SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
            String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
            BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
            
            System.out.println("Trying to send RED by WS");
            ServiceCallStatus scs = systemConfigurator.newRecipeExecutionData(red);
            System.out.println("WS returned: " + scs.getCode() + " | DESC: " + scs.getDescription());
          } else
          {

          }
          break;
        }
      }
    }
  }

}
