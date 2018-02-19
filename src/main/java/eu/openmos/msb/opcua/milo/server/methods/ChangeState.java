package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.FinishedProductInfo;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
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
                  name = "ProductInstance_ID",
                  description = "Product instance ID") String productInstance_id,
          @UaInputArgument(
                  name = "recipe_id",
                  description = "current Recipe ID") String recipe_id,
          @UaInputArgument(
                  name = "productType_id",
                  description = "Product type ID") String productType_id,
          @UaOutputArgument(
                  name = "Ackowledge",
                  description = "Acknowledge 1-OK 0-NOK") AnnotationBasedInvocationHandler.Out<Integer> result)
  {
    changeStateAndNextRecipeTimer.reset();
    changeStateAndNextRecipeTimer.start();

    logger.debug("Change State invoked! '{}'", context.getObjectNode().getBrowseName().getName());
    logger.info("[CHANGE_STATE]Change State invoked with parameters-> DaID:" + da_id + " productID: " + productInstance_id + " recipeID:" + recipe_id);

    String da_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
    
    if(da_name.equals(""))
    {
      //da does not exists, check modules?
      //da_id can be module_id
      //read recipe KPIs
      Thread threadKPI = new Thread()
      {
        public synchronized void run()
        {
          readKPIs_Module(da_id, recipe_id, productInstance_id);
        }
      };
      threadKPI.start();
      
      result.set(1);
      logger.info("returned 1 changeState - " + da_name + " *** " + da_id);
      return;
    }
    else
    {
      //da exists
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(da_name);
      //add adapter states strings to properties
      NodeId statePath = Functions.convertStringToNodeId(da.getSubSystem().getStatePath());
      DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
      
      if (statePath.isNotNull())
      {
        String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
        da.getSubSystem().setState(state);

        if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
        {
          //System.out.println("[ChangeState] ADAPTER ERROR: " + da.getSubSystem().getName());
          logger.info("[ChangeState] ADAPTER ERROR: " + da.getSubSystem().getName());
        }
      } else
      {
        //System.out.println("Error reading ADAPTER STATE!");
        logger.error("Error reading ADAPTER STATE!");
      }
      MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, da_name);

      //read recipe KPIs
      Thread threadKPI = new Thread()
      {
        public synchronized void run()
        {
          readKPIs_DA(da_id, recipe_id, productInstance_id);
        }
      };
      threadKPI.start();

      //start checker depending on the adapterStage
      if (!da.getSubSystem().getStage().equals(MSBConstants.SYSTEM_STAGE_RAMP_UP))
      {
        Thread threadCheck = new Thread()
        {
          public synchronized void run()
          {
            logger.info("[ChangeState] Starting ChangeStateChecker!");
            ChangeStateChecker(recipe_id, productInstance_id, da_id, productType_id);
          }
        };
        threadCheck.start();
      }
      //********************************************************************************************
      result.set(1);
      logger.info("returned 1 changeState - " + da_name + " *** " + da_id);
      return;
    }
  }

  private void ChangeStateChecker(String recipe_id, String productInst_id, String da_id, String productType_id)
  {
    String nextRecipeID = checkNextRecipe(recipe_id, productInst_id, productType_id); //returns the next recipe to execute
    //System.out.println("[ChangeStateChecker]Next Recipe to execute will be: " + nextRecipeID + "\n");
    logger.info("[ChangeStateChecker]Next Recipe to execute will be: " + nextRecipeID);
    
    if (!nextRecipeID.isEmpty() && !nextRecipeID.equals("last"))
    {
      int retSem = checkAdapterState(da_id, nextRecipeID, productInst_id, productType_id);
      if (retSem != 0) //check if adapter is ready
      { //if is ready
        //System.out.println("[ChangeStateChecker] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State\n");
        logger.info("[ChangeStateChecker] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State");
        
        String method = DatabaseInteraction.getInstance().getRecipeMethodByID(nextRecipeID);
        NodeId methodNode = Functions.convertStringToNodeId(method);
        String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(nextRecipeID);
        NodeId objNode = Functions.convertStringToNodeId(obj);
        
        String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
        String DA_name_next = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid_next);
        DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyName(DA_name_next);
        //System.out.println("[ChangeStateChecker] Trying to Invoke the nextRecipe" + "(" + nextRecipeID + ")" + " in DA: " + DA_name_next);
        logger.info("[ChangeStateChecker] Trying to Invoke the nextRecipe" + "(" + nextRecipeID + ")" + " in DA: " + DA_name_next);
        MSBClientSubscription client = (MSBClientSubscription) da_next.getClient();

        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();

        int tries = 0;
        //for (tries = 1; tries < 4; tries++)
        while(true)
        {
          tries++;
          boolean res = client.InvokeDeviceSkill(client.getClientObject(), objNode, methodNode, productInst_id, productType_id);
          logger.info("[ChangeStateChecker] executing recipeID: " + nextRecipeID);

          if (res)
          {
            if (retSem == 2)
            {
              logger.info("[ChangeStateChecker] DA is the same as the previous one, semaphore will not be released!");
            } else
            {
              //release previous adapter
              if (retSem == 1)
              {
                String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
                logger.info("[SEMAPHORE] RELEASED1 " + da_name1);
                PECManager.getInstance().getExecutionMap().get(da_id).release();
                
                DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyName(da_name1);
                MSB_gui.updateTableAdaptersSomaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()), da_test.getSubSystem().getName());
              }
            }
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, DA_name_next);
            break;
          } else
          {
            //ERROR EXECUTING RECIPE
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, "Try " + tries);
            logger.error("Error executing recipe: " + nextRecipeID + " -- probably because the recipe was already in execution.");
            logger.warn("** " + tries + " **Trying every 5s until success! - DA_NAME:" + da_next.getSubSystem().getName() + " * Recipe_ID: " + nextRecipeID);
            try
            {
              Thread.sleep(5000);
            } catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      } else //if adapter is not ready
      {
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();
        //System.out.println("NEXT ADAPTER IS AT ERROR STATE");
        logger.info("NEXT ADAPTER IS AT ERROR STATE");
      }
    } else if (nextRecipeID.equals("last"))
    {
      ProductInstance prodInst = PECManager.getInstance().getProductsDoing().remove(productInst_id);

      if (prodInst != null)
      {
        MSB_gui.addToTableExecutedOrders(prodInst.getOrderId(), prodInst.getProductId(), prodInst.getUniqueId());
        MSB_gui.removeFromTableCurrentOrder(prodInst.getUniqueId());

        //System.out.println("[ChangeState] This Recipe is the last one for product instance ID: " + productInst_id);
        logger.info("[ChangeState] This Recipe is the last one for product instance ID: " + productInst_id);
        String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
        logger.info("[SEMAPHORE" + da_name1 + "] RELEASED1");
        PECManager.getInstance().getExecutionMap().get(da_id).release();
        
        DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyName(da_name1);
        MSB_gui.updateTableAdaptersSomaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()), da_test.getSubSystem().getName());
            
        Long prodTime = new Date().getTime() - prodInst.getStartedProductionTime().getTime();
        PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);

        String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
        boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
        if (withAGENTCloud)
        {
          try
          {
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
          } catch (Exception ex)
          {
            System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
          }
        }
      } else
      {
        logger.error("[ChangeStateChecker] ERROR prodInst not found: " + productInst_id);
      }
    } else if (nextRecipeID.isEmpty())
    {
      logger.warn("It was not possible to execute the next Recipe. The path is not available!/n"
              + " Last executed Recipe: " + recipe_id + " ProdInst_ID: " + productInst_id);
    }
  }

  /**
   *
   * @param nextRecipeID
   * @return
   */
  private int checkAdapterState(String da_id, String nextRecipeID, String productInst_id, String productType_id)
  {
    logger.info("[checkAdapterState] current da - " + da_id);
    String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
    
    if (Daid_next != null)
    {
      String DA_name_next = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid_next);
      DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyName(DA_name_next);
      logger.info("[checkAdapterState] checking da_next state - " + da_next.getSubSystem().getUniqueId());
      //add adapter states strings to properties
      do
      {
        NodeId statePath = Functions.convertStringToNodeId(da_next.getSubSystem().getStatePath());
        DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da_next;
        if (statePath.isNotNull())
        {
          String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
          da_next.getSubSystem().setState(state);
        } else
        {
          logger.error("[checkAdapterState] Error reading adapter state!");
          return 0;
        }
        try
        {
          Thread.sleep(50);
        } catch (InterruptedException ex)
        {
          java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info("[checkAdapterState] waiting for da_next state READY - " + da_next.getSubSystem().getUniqueId());
        
        //MARTELO
        if (da_next.getSubSystem().getName().toLowerCase().equals("agv"))
          break;
        
      } while (!da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY) && !da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR));

      logger.info("[checkAdapterState] DA_next STATE: " + da_next.getSubSystem().getState());
      
      if (!da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
      {
        DeviceAdapter da_next_next = getDAofNextRecipe(da_next, nextRecipeID, productInst_id, productType_id);
        int ret = 0;
        if (da_next_next != null)
        {
          //check if is the same as previous one
          if (da_next_next.getSubSystem().getUniqueId().equals(da_id) || da_next_next.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
          {
            logger.info("No need to get semaphore! same as previous one.");
            ret = 2;
          } else
          {
            try
            {
              logger.info("[checkAdapterState][SEMAPHORE] Acquiring for " + da_next_next.getSubSystem().getName());
              PECManager.getInstance().getExecutionMap().get(da_next_next.getSubSystem().getUniqueId()).acquire();
              logger.info("[checkAdapterState][SEMAPHORE] ACQUIRED for " + da_next_next.getSubSystem().getName());
              MSB_gui.updateTableAdaptersSomaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next_next.getSubSystem().getUniqueId()).availablePermits()), da_next_next.getSubSystem().getName());
              //CHECK LATER
              if (da_id.equals(da_next.getSubSystem().getUniqueId()))
                ret = 2;
              else
                ret = 1;
              
            } catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        } else
        {
          logger.info("Next Recipe is the last ");
          ret = 1;
        }

        logger.info("the next recipe Adapter (" + DA_name_next + ") is ready for execution!");
        return ret;
      } else
      {
        logger.error("ERROR in DA " + DA_name_next);
        return 0;
      }
    }
    logger.error("DA NOT READY! - returning 0");
    return 0;
  }

  /**
   *
   * @param nextRecipeID
   * @return
   */
  private Boolean checkNextValidation(String nextRecipeID)
  {
    logger.info("[checkNextValidation] recipeID: " + nextRecipeID);
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
          //if its the last recipe the next is null
          if (auxNextLKT1 == null)
            return true;
          boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(auxNextLKT1);
          if (valid && !da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
          {
            return true;
          } else
          {
            return false;
          }
        }
      }
    }
    return false;
  }

  /**
   *
   * @param recipeID
   * @return
   */
  private String checkNextRecipe(String recipeID, String productInst_id, String productType_id)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    logger.info("[checkNextRecipe] DA id from checkNextRecipe: " + Daid);

    if (Daid != null)
    {
      //get DA name
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      logger.info("[checkNextRecipe]DA name of finished recipe : " + DA_name);
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      if (da == null)
      {
        logger.warn("The DA is null!");
      } else
      {
        String prodID = productInst_id;
        for (int i = 0; i < 2; i++)
        {
          for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
          {
            if (execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
            {
              //get the nextRecipe on its executionTables
              String auxNextRecipeNode = execRow.getNextRecipeIdPath();

              if (auxNextRecipeNode == null)
              {
                //is last recipe
                logger.info("[checkNextRecipe] returning - last");
                return "last";
              }

              NodeId nextRecipeNode = Functions.convertStringToNodeId(auxNextRecipeNode);
              DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
              String nextRecipeID = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), nextRecipeNode);

              if (nextRecipeID == null || nextRecipeID.equals("done") || nextRecipeID.equals("last"))
              {
                logger.info("[checkNextRecipe] returning - last");
                return "last";
              } else
              {
                if (nextRecipeID.isEmpty())
                {
                  logger.info("[checkNextRecipe] returning - 'empty'");
                  return "";
                }
              }

              String SR_ID = DatabaseInteraction.getInstance().getSkillReqIDbyRecipeID(nextRecipeID);
              List<String> recipeID_for_SR = DatabaseInteraction.getInstance().getRecipesIDbySkillReqID(SR_ID);

              if (recipeID_for_SR.size() > 1)
              {
                Recipe firstRecipe = null;
                //check if the precedences are the same
                List<Recipe> recipes = new ArrayList<>();
                for (String auxRecipeID : recipeID_for_SR)
                {
                  String da_db_ID = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(auxRecipeID);
                  if (da_db_ID == null)
                  {
                    continue;
                  }
                  String da_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(da_db_ID);
                  DeviceAdapter auxDA = DACManager.getInstance().getDeviceAdapterbyName(da_name);
                  for (Recipe auxRecipe : auxDA.getListOfRecipes())
                  {
                    if (auxRecipe.getUniqueId().equals(auxRecipeID))
                    {
                      recipes.add(auxRecipe);
                      if (auxRecipeID.equals(nextRecipeID))
                      {
                        firstRecipe = auxRecipe;
                      }
                      break;
                    }
                  }
                }
                //first validate if the main path is available, if not available check the others
                if (firstRecipe != null)
                {
                  recipes.remove(firstRecipe);
                }

                if (checkNextValidation(nextRecipeID))
                {
                  logger.info("[checkNextRecipe] returning - " + nextRecipeID);
                  return nextRecipeID;
                } else
                {
                  for (Recipe auxRecipe : recipes)
                  {
                    if (checkNextValidation(auxRecipe.getUniqueId()))
                    {
                      logger.info("[checkNextRecipe] returning - " + auxRecipe.getUniqueId());
                      return auxRecipe.getUniqueId();
                    }
                  }
                  logger.warn("There are no other Recipe choices for the current recipe " + recipeID);
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
                  logger.warn("There are no other Recipe choices for the current recipe " + recipeID);
                }
              }
              return "";
            }
          }
          //no prodInst found in execTable, search for productType now
          prodID = productType_id;
        }
      }
    } else
    {
      logger.error("There are no Adapters that can perform the required recipe: " + recipeID);
    }
    logger.info("[checkNextRecipe] returning - 'empty'");
    return "";
  }

  /**
   *
   * @param da
   * @param recipeID
   * @param productInst_id
   * @param productType_id
   * @return true if the adapter is at ready state
   */
  public static DeviceAdapter getDAofNextRecipe(DeviceAdapter da, String recipeID, String productInst_id, String productType_id)
  {
    String nextRecipeID = "";
    String prodID = productInst_id;
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow auxRow : da.getExecutionTable().getRows())
      {
        if (auxRow.getRecipeId().equals(recipeID) && auxRow.getProductId().equals(prodID))
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
      //no prodInst found in execTable, search for productType now
      prodID = productType_id;
    }
    return null;
  }

  /**
   * Read KPIs of recipe_id from OPCServer
   *
   * @param da_id
   * @param recipe_id
   */
  private void readKPIs_DA(String da_id, String recipe_id, String productInst_ID)
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
            logger.info("[KPI] DA: " + CurrentDA.getSubSystem().getName() + " | KPI_Name: " + kpi.getName() + " | KPI_Value: " + kpi.getValue());
          }

          //IF THE AC is activated, send the KPIs upwards
          String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
          boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
          if (withAGENTCloud)
          {
            try
            {
              RecipeExecutionData red = new RecipeExecutionData();
              red.setKpiSettings(recipe.getKpiSettings());
              red.setParameterSettings(recipe.getParameterSettings());
              red.setProductInstanceId(productInst_ID);
              red.setRecipeId(recipe_id);
              red.setRegistered(new Date());
              //add header to vertX message?
              logger.info("...sending KPI's over websockets...");
              DeliveryOptions options = new DeliveryOptions();
              // options.addHeader(eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, "MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA"); //use this??
              options.addHeader("messageType", eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA); //use this??
              JsonObject objectToSend = JsonObject.mapFrom(red);

              if (CurrentDA.getVertx() != null)
              {
                CurrentDA.getVertx().eventBus().send(productInst_ID, objectToSend, options); //serialize the entire class??
              } else
              {
                logger.warn("ChangeState -> vertx is null, so let's skip it! -> " + productInst_ID);
              }

              SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
              SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
              String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
              BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

              logger.info("Trying to send RED by WS");
              ServiceCallStatus scs = systemConfigurator.newRecipeExecutionData(red);
              logger.info("WS returned: " + scs.getCode() + " | DESC: " + scs.getDescription());
            } catch (Exception ex)
            {
              logger.error("Error trying to connect to cloud!: " + ex.getMessage());
            }
          } else
          {

          }
          break;
        }
      }
    }
  }
  
  private void readKPIs_Module(String module_id, String recipe_id, String productInst_ID)
  {
    DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterFromModuleID(module_id);

    if (CurrentDA != null)
    {
      for (Module module : CurrentDA.getListOfModules())
      {
        if (module.getUniqueId().equals(module_id))
        {
          List<Recipe> auxRepList = module.getRecipes();

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
                logger.info("[KPI] DA: " + CurrentDA.getSubSystem().getName() + " | KPI_Name: " + kpi.getName() + " | KPI_Value: " + kpi.getValue());
              }

              //IF THE AC is activated, send the KPIs upwards
              String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
              boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
              if (withAGENTCloud)
              {
                try
                {
                  RecipeExecutionData red = new RecipeExecutionData();
                  red.setKpiSettings(recipe.getKpiSettings());
                  red.setParameterSettings(recipe.getParameterSettings());
                  red.setProductInstanceId(productInst_ID);
                  red.setRecipeId(recipe_id);
                  red.setRegistered(new Date());
                  //add header to vertX message?
                  logger.info("...sending KPI's over websockets...");
                  DeliveryOptions options = new DeliveryOptions();
                  // options.addHeader(eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA, "MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA"); //use this??
                  options.addHeader("messageType", eu.openmos.agentcloud.utilities.Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA); //use this??
                  JsonObject objectToSend = JsonObject.mapFrom(red);

                  if (CurrentDA.getVertx() != null)
                  {
                    CurrentDA.getVertx().eventBus().send(productInst_ID, objectToSend, options); //serialize the entire class??
                  } else
                  {
                    logger.warn("ChangeState -> vertx is null, so let's skip it! -> " + productInst_ID);
                  }

                  SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
                  SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
                  String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
                  BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
                  bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

                  logger.info("Trying to send RED by WS");
                  ServiceCallStatus scs = systemConfigurator.newRecipeExecutionData(red);
                  logger.info("WS returned: " + scs.getCode() + " | DESC: " + scs.getDescription());
                } catch (Exception ex)
                {
                  logger.error("Error trying to connect to cloud!: " + ex.getMessage());
                }
              } else
              {

              }
              break;
            }
          }
        }
      }
    }
  }

}
