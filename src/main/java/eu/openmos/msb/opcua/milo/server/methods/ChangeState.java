package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.FinishedProductInfo;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.ProductInstanceStatus;
import eu.openmos.model.Recipe;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.datastructures.ProductExecution;
import eu.openmos.msb.datastructures.QueuedAction;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.utilities.Functions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;
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
  static Semaphore semaphore_passive = new Semaphore(1);

  static int STATION_1_ID = 0;
  static int STATION_5_ID = 0;

  @UaMethod
  public void invoke(
          AnnotationBasedInvocationHandler.InvocationContext context,
          @UaInputArgument(
                  name = "DA_ID",
                  description = "ID of the device adapter") String da_id,
          @UaInputArgument(
                  name = "recipe_id",
                  description = "current Recipe ID") String recipe_id,
          @UaInputArgument(
                  name = "ProductInstance_ID",
                  description = "Product instance ID") String productInstance_id,
          @UaInputArgument(
                  name = "productType_id",
                  description = "Product type ID") String productType_id,
          @UaInputArgument(
                  name = "checkNextRecipe",
                  description = "Check next Recipe if true") boolean checkNextRecipe,
          @UaInputArgument(
                  name = "newProductState",
                  description = "New Product State") String newProductState,
          @UaInputArgument(
                  name = "newProductState",
                  description = "SkillRequirement completed") String sr_id,
          @UaOutputArgument(
                  name = "Ackowledge",
                  description = "Acknowledge 1-OK 0-NOK") AnnotationBasedInvocationHandler.Out<Integer> result)
  {
    changeStateAndNextRecipeTimer.reset();
    changeStateAndNextRecipeTimer.start();

    logger.debug("Change State invoked! '{}'", context.getObjectNode().getBrowseName().getName());
    logger.info("[CHANGE_STATE]Change State invoked with parameters-> DaID:" + da_id
            + " productInstID: " + productInstance_id + " recipeID:" + recipe_id
            + " productTypeID: " + productType_id + " checkNextRecipe: " + checkNextRecipe + " newProductState: " + newProductState);

    String da_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);

    if (MSBConstants.MSB_MODE_PASSIVE)
    {
      Thread threadPassive = new Thread()
      {
        public synchronized void run()
        {
          try
          {
            semaphore_passive.acquire();
            logger.debug("Doing passive stuff!");
            passiveMode(productInstance_id, productType_id, da_name, da_id, recipe_id);
            //passiveMode_VDMA_MARTELO(productInstance_id, productType_id, da_name, da_id, recipe_id);
            semaphore_passive.release();
          }
          catch (InterruptedException ex)
          {
            java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      };
      threadPassive.start();

      result.set(1);
      logger.info("returned 1 changeState - " + da_name + " *** " + da_id);
      return;
    }
    else //ACTIVE STATE
    {
      switch (newProductState)
      {
        case MSBConstants.STATE_PRODUCT_QUEUE:
          productIsOnQueue(productInstance_id);
          break;
        case MSBConstants.STATE_PRODUCT_PRODUCING:
          productIsExecuting(productInstance_id);
          break;
        case MSBConstants.STATE_PRODUCT_READY:
          if (da_name.equals(""))
          {
            //da does not exists, check modules?
            //da_id can be module_id
            //read recipe KPIs
            logger.info("Module changeState");
            Thread threadKPI = new Thread()
            {
              public synchronized void run()
              {
                readKPIs_Module(da_id, recipe_id, productInstance_id);

                if (!checkNextRecipe)
                {
                  remove_queued_action(recipe_id);
                }
              }
            };
            threadKPI.start();

            if (checkNextRecipe)
            {
              Thread threadCheck = new Thread()
              {
                public synchronized void run()
                {
                  logger.info("[ChangeState] Starting ChangeStateChecker!");
                  ChangeStateChecker_Modules(recipe_id, productInstance_id, da_id, productType_id, sr_id);
                  remove_queued_action(recipe_id);
                }
              };
              threadCheck.start();
            }

            result.set(1);
            logger.info("returned 1 changeState - " + da_name + " *** " + da_id);
            return;
          }
          else
          {
            //da exists
            DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
            //add adapter states strings to properties
            NodeId statePath = Functions.convertStringToNodeId(da.getSubSystem().getStatePath());
            DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;

            if (statePath.isNotNull())
            {
              String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
              da.getSubSystem().setState(state);

              if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
              {
                logger.info("[ChangeState] ADAPTER ERROR: " + da.getSubSystem().getName());
              }
            }
            else
            {
              logger.error("Error reading ADAPTER STATE!");
            }
            MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, da_name);

            //read recipe KPIs
            Thread threadKPI = new Thread()
            {
              public synchronized void run()
              {
                readKPIs_DA(da_id, recipe_id, productInstance_id);
                if (!checkNextRecipe)
                {
                  remove_queued_action(recipe_id);
                }
              }
            };
            threadKPI.start();

            //start checker depending on the adapterStage
            if (checkNextRecipe /* && !da.getSubSystem().getStage().equals(MSBConstants.STAGE_RAMP_UP) */)
            {
              Thread threadCheck = new Thread()
              {
                public synchronized void run()
                {
                  logger.info("[ChangeState] Starting ChangeStateChecker!");
                  ChangeStateChecker(recipe_id, productInstance_id, da_id, productType_id, sr_id);
                  remove_queued_action(recipe_id);
                }
              };
              threadCheck.start();
            }
            /*
            //MARTELO mass production
            else
            {
            finishProduct_MARTELO(da_id, productInstance_id);
            }
             */
            //********************************************************************************************
            result.set(1);
            logger.info("returned 1 changeState - " + da_name + " *** " + da_id);
            return;
          }
        default:
          break;
      }
    }
  }

  private void passiveMode(String productInstance_id, String productType_id, String da_name, String da_id, String recipe_id)
  {
    //check if prodInst_ID is on the list
    if (!PECManager.getInstance().getProductsDoing().keySet().contains(productInstance_id))
    {
      OrderInstance oi = new OrderInstance();
      List<ProductInstance> piList = new ArrayList<>();
      oi.setUniqueId(productInstance_id);
      //create instance and agent
      ProductInstance pi = new ProductInstance(productInstance_id, productType_id, productType_id, "no_description",
              oi.getUniqueId(), null, false, null, ProductInstanceStatus.PRODUCING,
              new Date(), new Date());

      piList.add(pi);

      oi.setName(productInstance_id + "_name");
      oi.setDescription(productInstance_id + "_description");
      oi.setPriority(1);
      oi.setProductInstances(piList);
      oi.setRegistered(new Date());

      PECManager.getInstance().getProductsDoing().put(productInstance_id, pi);

      MSB_gui.addToTableCurrentOrders(oi.getUniqueId(), productType_id, productInstance_id);

      if (MSBConstants.USING_CLOUD)
      {
        try
        {
          //send oi to cloud
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          logger.info("Agent Cloud Cloudinterface address = [" + MSBConstants.CLOUD_ENDPOINT + "]");
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

          ServiceCallStatus orderStatus = systemConfigurator.acceptNewOrderInstance(oi);
          logger.info("Order Instance sent to the Agent Cloud with code: " + orderStatus.getCode());
          logger.info("Order Instance status: " + orderStatus.getDescription());
          //***
          //check order status
          if (orderStatus.getCode().equals("success.openmos.agentcloud.cloudinterface.systemconfigurator"))
          {
            ServiceCallStatus piStartStatus = systemConfigurator.startedProduct(pi);

          }
        }
        catch (Exception ex)
        {
          System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
    }

    //modules
    if (da_name.equals(""))
    {
      //da_id is module_id
      //da does not exists, check modules?
      //da_id can be module_id
      //read recipe KPIs
      DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterFromModuleID(da_id);
      if (CurrentDA != null)
      {
        MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, CurrentDA.getSubSystem().getName() + "(A)");
      }

      logger.info("Module changeState");
      Thread threadKPI = new Thread()
      {
        public synchronized void run()
        {
          readKPIs_Module(da_id, recipe_id, productInstance_id);
        }
      };
      threadKPI.start();
    }
    else
    {
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
    }

    //if (!da_name.equals("")) //martelo
    {
      Thread threadLastRecipe = new Thread()
      {
        public synchronized void run()
        {
          logger.info("[ChangeState] Starting ChangeStateChecker!");

          //if (true) //martelo
          if (isLastRecipe_withoutProd(recipe_id)) //check if its last recipe if there is no productType
          //if (isLastRecipe(recipe_id, productInstance_id, productType_id))
          {
            finishProduct(productInstance_id);
          }
        }
      };
      threadLastRecipe.start();
    }

  }

  private void passiveMode_VDMA_MARTELO(String productInstance_id, String productType_id, String da_name, String da_id, String recipe_id)
  {
    productInstance_id = check_prod_ID_VDMA_MARTELO(da_name, da_id, recipe_id);
    //check if prodInst_ID is on the list
    if (!PECManager.getInstance().getProductsDoing().keySet().contains(productInstance_id))
    {
      OrderInstance oi = new OrderInstance();
      List<ProductInstance> piList = new ArrayList<>();
      oi.setUniqueId(productInstance_id);
      //create instance and agent
      ProductInstance pi = new ProductInstance(productInstance_id, productType_id, productType_id, "no_description",
              oi.getUniqueId(), null, false, null, ProductInstanceStatus.PRODUCING,
              new Date(), new Date());

      piList.add(pi);

      oi.setName(productInstance_id + "_name");
      oi.setDescription(productInstance_id + "_description");
      oi.setPriority(1);
      oi.setProductInstances(piList);
      oi.setRegistered(new Date());

      PECManager.getInstance().getProductsDoing().put(productInstance_id, pi);

      MSB_gui.addToTableCurrentOrders(oi.getUniqueId(), productType_id, productInstance_id);

      if (MSBConstants.USING_CLOUD)
      {
        try
        {
          //send oi to cloud
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          logger.info("Agent Cloud Cloudinterface address = [" + MSBConstants.CLOUD_ENDPOINT + "]");
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

          ServiceCallStatus orderStatus = systemConfigurator.acceptNewOrderInstance(oi);
          logger.info("Order Instance sent to the Agent Cloud with code: " + orderStatus.getCode());
          logger.info("Order Instance status: " + orderStatus.getDescription());
          //***
          //check order status
          if (orderStatus.getCode().equals("success.openmos.agentcloud.cloudinterface.systemconfigurator"))
          {
            ServiceCallStatus piStartStatus = systemConfigurator.startedProduct(pi);

          }
        }
        catch (Exception ex)
        {
          System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
    }

    //modules
    if (da_name.equals(""))
    {
      //da_id is module_id
      //da does not exists, check modules?
      //da_id can be module_id
      //read recipe KPIs
      DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterFromModuleID(da_id);
      if (CurrentDA != null)
      {
        MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, CurrentDA.getSubSystem().getName() + "(A)");
      }
      logger.info("Module changeState");
      readKPIs_Module(da_id, recipe_id, productInstance_id);

    }
    else
    {
      MSB_gui.updateDATableCurrentOrderLastDA(productInstance_id, da_name);
      //read recipe KPIs
      readKPIs_DA(da_id, recipe_id, productInstance_id);
    }

    //if (!da_name.equals("")) //martelo
    {
      //if (true) //martelo
      if (isLastRecipe_withoutProd(recipe_id)) //check if its last recipe if there is no productType
      //if (isLastRecipe(recipe_id, productInstance_id, productType_id))
      {
        finishProduct(productInstance_id);
      }
    }

  }

  private String check_prod_ID_VDMA_MARTELO(String da_name, String da_id, String recipe_id)
  {
    DeviceAdapter da = null;

    if (da_name.equals(""))
    {
      da = DACManager.getInstance().getDeviceAdapterFromModuleID(da_id);
    }
    else
    {
      da = DACManager.getInstance().getDeviceAdapterbyName(da_name);
    }

    if (da != null)
    {
      if (da.getSubSystem().getName().toUpperCase().equals("VDMA_STATION1"))
      {
        if (!recipe_id.equals("ac792724-c45d-4740-8929-f4003f975217"))
        {
          STATION_1_ID++;
        }
        return String.valueOf(STATION_1_ID);
      }
      else if (da.getSubSystem().getName().toUpperCase().equals("VDMA_STATION5"))
      {
        if (STATION_1_ID > 3)
        {
          STATION_5_ID++;
          return String.valueOf(STATION_5_ID);
        }
      }
    }

    return "-999";
  }

  private void finishProduct(String productInstance_id)
  {
    ProductInstance prodInst = PECManager.getInstance().getProductsDoing().remove(productInstance_id);

    if (prodInst != null)
    {
      MSB_gui.addToTableExecutedOrders(prodInst.getOrderId(), prodInst.getProductId(), prodInst.getUniqueId());
      MSB_gui.removeFromTableCurrentOrder(prodInst.getUniqueId());

      //System.out.println("[ChangeState] This Recipe is the last one for product instance ID: " + productInst_id);
      logger.info("[ChangeState] This Recipe is the last one for product instance ID: " + productInstance_id);

      Long prodTime = new Date().getTime() - prodInst.getStartedProductionTime().getTime();
      PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);

      if (MSBConstants.USING_CLOUD)
      {
        try
        {
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);
          FinishedProductInfo fpi = new FinishedProductInfo();
          fpi.setProductInstanceId(productInstance_id);
          fpi.setFinishedTime(new Date());
          fpi.setRegistered(prodInst.getStartedProductionTime());
          systemConfigurator.finishedProduct(fpi);
        }
        catch (Exception ex)
        {
          System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
    }
  }

  private void ChangeStateChecker_Modules(String recipe_id, String productInst_id, String module_id, String productType_id, String sr_id)
  {
    String nextRecipeID = getNextValidRecipe(recipe_id, productInst_id, productType_id, sr_id); //returns the next recipe to execute
    logger.info("[ChangeStateChecker]Next Recipe to execute will be: " + nextRecipeID);

    DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterFromModuleID(module_id);
    String da_id = CurrentDA.getSubSystem().getUniqueId();

    if (!nextRecipeID.isEmpty() && !nextRecipeID.equals("last"))
    {
      int retSem = checkAdapterState(da_id, nextRecipeID, productInst_id, productType_id, sr_id);
      if (retSem != 0) //check if adapter is ready
      { //if is ready
        logger.info("[ChangeStateChecker] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State");

        String method = DatabaseInteraction.getInstance().getRecipeMethodByID(nextRecipeID);
        NodeId methodNode = Functions.convertStringToNodeId(method);
        String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(nextRecipeID);
        NodeId objNode = Functions.convertStringToNodeId(obj);

        String Daid_next = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);
        DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid_next);
        logger.info("[ChangeStateChecker] Trying to Invoke the nextRecipe" + "(" + nextRecipeID + ")" + " in DA: " + da_next.getSubSystem().getName());
        MSBClientSubscription client = (MSBClientSubscription) da_next.getClient();

        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();

        int tries = 0;
        //for (tries = 1; tries < 4; tries++)
        while (true)
        {
          tries++;
          SkillRequirement sr_next = PECManager.getInstance().getNextSR(sr_id, productType_id, nextRecipeID);
          boolean res = client.InvokeDeviceSkill(client.getClientObject(), objNode, methodNode, productInst_id, productType_id, true, sr_next.getUniqueId());
          logger.info("[ChangeStateChecker] executing recipeID: " + nextRecipeID);

          if (res)
          {
            if (MSBConstants.MSB_OPTIMIZER)
            {
              PECManager.getInstance().lock_SR_to_WS(da_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), productInst_id);
            }
            if (retSem == 2)
            {
              logger.info("[ChangeStateChecker] DA is the same as the previous one, semaphore will not be released!");
            }
            else
            {
              //release previous adapter
              if (retSem == 1)
              {
                String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
                logger.info("[SEMAPHORE] RELEASED1 " + da_name1);
                PECManager.getInstance().getExecutionMap().get(da_id).release();

                DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
                MSB_gui.updateTableAdaptersSemaphore(
                        String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()),
                        da_test.getSubSystem().getName());
              }
            }
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, da_next.getSubSystem().getName());
            break;
          }
          else
          {
            //ERROR EXECUTING RECIPE
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, "Try " + tries);
            logger.error("Error executing recipe: " + nextRecipeID + " -- probably because the recipe was already in execution.");
            logger.warn("** " + tries + " **Trying every 5s until success! - DA_NAME:" + da_next.getSubSystem().getName() + " * Recipe_ID: " + nextRecipeID);
            try
            {
              Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
      else //if adapter is not ready
      {
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();
        logger.info("NEXT ADAPTER IS AT ERROR STATE");
      }
    }
    else if (nextRecipeID.equals("last"))
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

        DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
        MSB_gui.updateTableAdaptersSemaphore(
                String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()),
                da_test.getSubSystem().getName());

        Long prodTime = new Date().getTime() - prodInst.getStartedProductionTime().getTime();
        PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);

        if (MSBConstants.USING_CLOUD)
        {
          try
          {
            SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
            SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
            BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);
            FinishedProductInfo fpi = new FinishedProductInfo();
            fpi.setProductInstanceId(productInst_id);
            fpi.setFinishedTime(new Date());
            fpi.setRegistered(prodInst.getStartedProductionTime());
            systemConfigurator.finishedProduct(fpi);
          }
          catch (Exception ex)
          {
            System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
          }
        }
      }
      else
      {
        logger.error("[ChangeStateChecker] ERROR prodInst not found: " + productInst_id);
      }
    }
    else if (nextRecipeID.isEmpty())
    {
      logger.warn("It was not possible to execute the next Recipe. The path is not available!/n"
              + " Last executed Recipe: " + recipe_id + " ProdInst_ID: " + productInst_id);
    }
  }

  private void ChangeStateChecker(String recipe_id, String productInst_id, String da_id, String productType_id, String sr_id)
  {
      
    String nextRecipeID = getNextValidRecipe(recipe_id, productInst_id, productType_id, sr_id); //returns the next recipe to execute
    logger.info("[ChangeStateChecker]Next Recipe to execute will be: " + nextRecipeID);

    if (!nextRecipeID.isEmpty() && !nextRecipeID.equals("last"))
    {
      int retSem = checkAdapterState(da_id, nextRecipeID, productInst_id, productType_id, sr_id);
      if (retSem != 0) //check if adapter is ready
      { //if is ready
        logger.info("[ChangeStateChecker] The adapter for the nextRecipe: " + nextRecipeID + " is at READY State");

        String method = DatabaseInteraction.getInstance().getRecipeMethodByID(nextRecipeID);
        NodeId methodNode = Functions.convertStringToNodeId(method);
        String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(nextRecipeID);
        NodeId objNode = Functions.convertStringToNodeId(obj);

        String Daid_next = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);
        DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid_next);
        logger.info("[ChangeStateChecker] Trying to Invoke the nextRecipe" + "(" + nextRecipeID + ")" + " in DA: " + da_next.getSubSystem().getName());
        MSBClientSubscription client = (MSBClientSubscription) da_next.getClient();

        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();
        
        int tries = 0;
        //for (tries = 1; tries < 4; tries++)
        while (true)
        {
          tries++;
          SkillRequirement sr_next = PECManager.getInstance().getNextSR(sr_id, productType_id, nextRecipeID);
          boolean res = client.InvokeDeviceSkill(client.getClientObject(), objNode, methodNode, productInst_id, productType_id, true, sr_next.getUniqueId());
          logger.info("[ChangeStateChecker] executing recipeID: " + nextRecipeID);

          if (res)
          {
            if (MSBConstants.MSB_OPTIMIZER)
            {
              PECManager.getInstance().lock_SR_to_WS(da_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), productInst_id);
            }
            if (retSem == 2)
            {
              logger.info("[ChangeStateChecker] DA is the same as the previous one, semaphore will not be released!");
            }
            else
            {
              //release previous adapter
              if (retSem == 1)
              {
                String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
                logger.info("[SEMAPHORE] RELEASED1 " + da_name1);
                PECManager.getInstance().getExecutionMap().get(da_id).release();

                DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
                MSB_gui.updateTableAdaptersSemaphore(
                        String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()),
                        da_test.getSubSystem().getName());
              }
            }
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, da_next.getSubSystem().getName());
            break;
          }
          else
          {
            //ERROR EXECUTING RECIPE
            MSB_gui.updateDATableCurrentOrderNextDA(productInst_id, "Try " + tries);
            logger.error("Error executing recipe: " + nextRecipeID + " -- probably because the recipe was already in execution.");
            logger.warn("** " + tries + " **Trying every 5s until success! - DA_NAME:" + da_next.getSubSystem().getName() + " * Recipe_ID: " + nextRecipeID);
            try
            {
              Thread.sleep(5000);
            }
            catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      }
      else //if adapter is not ready
      {
        PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
        perfMeasurement.getChangeStateTillNextRecipeCallTimers().add(changeStateAndNextRecipeTimer.getTime());
        //changeStateAndNextRecipeTimer.stop();
        logger.info("NEXT ADAPTER IS AT ERROR STATE");
      }
    }
    else if (nextRecipeID.equals("last"))
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

        DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
        MSB_gui.updateTableAdaptersSemaphore(
                String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()),
                da_test.getSubSystem().getName());

        Long prodTime = new Date().getTime() - prodInst.getStartedProductionTime().getTime();
        PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);

        PECManager.getInstance().getProduct_sr_tracking().remove(productInst_id);
        
        if (MSBConstants.USING_CLOUD)
        {
          try
          {
            SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
            SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
            BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);
            FinishedProductInfo fpi = new FinishedProductInfo();
            fpi.setProductInstanceId(productInst_id);
            fpi.setFinishedTime(new Date());
            fpi.setRegistered(prodInst.getStartedProductionTime());
            systemConfigurator.finishedProduct(fpi);
          }
          catch (Exception ex)
          {
            System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
          }
        }
      }
      else
      {
        logger.error("[ChangeStateChecker] ERROR prodInst not found: " + productInst_id);
      }
    }
    else if (nextRecipeID.isEmpty())
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
  private int checkAdapterState(String da_id, String nextRecipeID, String productInst_id, String productType_id, String sr_id)
  {
    logger.info("[checkAdapterState] current da - " + da_id);
    String Daid_next = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);

    if (Daid_next != null)
    {
      DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid_next);

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
        }
        else
        {
          logger.error("[checkAdapterState] Error reading adapter state!");
          return 0;
        }
        try
        {
          Thread.sleep(50);
        }
        catch (InterruptedException ex)
        {
          java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.info("[checkAdapterState] waiting for da_next state READY - " + da_next.getSubSystem().getUniqueId());

        //MARTELO
        if (da_next.getSubSystem().getName().toLowerCase().equals("agv"))
        {
          break;
        }

      }
      while (!da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY)
              && !da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR));

      logger.info("[checkAdapterState] DA_next STATE: " + da_next.getSubSystem().getState());

      if (!da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR))
      {
          SkillRequirement sr_next = PECManager.getInstance().getNextSR(sr_id, productType_id, nextRecipeID);
        DeviceAdapter da_next_next = PECManager.getInstance().getDAofNextRecipe(da_next, nextRecipeID, productInst_id, productType_id, sr_next.getUniqueId());
        int ret = 0;
        if (da_next_next != null)
        {
          //check if is the same as previous one
          if (da_next_next.getSubSystem().getUniqueId().equals(da_id) || da_next_next.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
          {
            logger.info("[checkAdapterState] No need to get semaphore! same as previous one.");
            ret = 2;
          }
          else
          {
            try
            {
              //SkillRequirement sr_next = PECManager.getInstance().getNextSRbyRecipeID(da_next, nextRecipeID, productInst_id, productType_id);
              
              if (PECManager.getInstance().need_to_get_da(da_next_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), productInst_id))
              {
                logger.info("[checkAdapterState][SEMAPHORE] Acquiring for " + da_next_next.getSubSystem().getName());
                PECManager.getInstance().getExecutionMap().get(da_next_next.getSubSystem().getUniqueId()).acquire();
                logger.info("[checkAdapterState][SEMAPHORE] ACQUIRED for " + da_next_next.getSubSystem().getName());
                MSB_gui.updateTableAdaptersSemaphore(
                        String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next_next.getSubSystem().getUniqueId()).availablePermits()),
                        da_next_next.getSubSystem().getName());
              }
              //CHECK LATER
              if (da_id.equals(da_next.getSubSystem().getUniqueId()))
              {
                ret = 2;
              }
              else
              {
                ret = 1;
              }
            }
            catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(ChangeState.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
        else
        {
          logger.info("[checkAdapterState] Next Recipe is the last ");
          ret = 1;
        }

        logger.info("[checkAdapterState] the next recipe Adapter (" + da_next.getSubSystem().getName() + ") is ready for execution!");
        return ret;
      }
      else
      {
        logger.error("[checkAdapterState] ERROR in DA " + da_next.getSubSystem().getName());
        return 0;
      }
    }
    logger.error("[checkAdapterState] DA NOT READY! - returning 0");
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
    String Daid = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);
    if (Daid != null)
    {
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid);
      for (int i = 0; i < da.getExecutionTable().getRows().size(); i++)
      {
        if (da.getExecutionTable().getRows().get(i).getRecipeId() == null ? nextRecipeID == null : da.getExecutionTable().getRows().get(i).getRecipeId().equals(nextRecipeID))
        {
          String auxNextLKT1 = da.getExecutionTable().getRows().get(i).getNextRecipeId();
          //if its the last recipe the next is null
          if (auxNextLKT1 == null)
          {
            return true;
          }
          boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(auxNextLKT1);
          return valid && !da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_ERROR);
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
  private String getNextValidRecipe(String recipeID, String productInst_id, String productType_id, String sr_id)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(recipeID);
    logger.info("[checkNextRecipe] DA id from checkNextRecipe: " + Daid);

    if (Daid != null)
    {
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid);
      if (da == null)
      {
        logger.warn("The DA is null!");
      }
      else
      {
        logger.info("[checkNextRecipe]DA name of finished recipe : " + da.getSubSystem().getName());
        String prodID = productInst_id;
        for (int i = 0; i < 2; i++)
        {
          for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
          {
            if (execRow.getRecipeId() != null && execRow.getProductId() != null
                    && execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
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
              }
              else
              {
                if (nextRecipeID.isEmpty())
                {
                  logger.info("[checkNextRecipe] returning - 'empty'");
                  return "";
                }
              }

              List<String> recipeIDs_for_SR = new ArrayList<>();
              SkillRequirement sr_to_do = null;
              //String SR_ID = DatabaseInteraction.getInstance().getSkillReqIDbyRecipeID(nextRecipeID);
              //recipeIDs_for_SR = DatabaseInteraction.getInstance().getRecipesIDbySkillReqID(SR_ID);
              //RECIPE REQUIREMENTS MUST COME FROM PRODUCT DEF?? -- to be tested cpde below
                
                  for (SkillRequirement temp_sr : PECManager.getInstance().getNextSR_list(sr_id, prodID))
                  {
                    if (temp_sr.getRecipeIDs().contains(nextRecipeID))
                    {
                      sr_to_do = temp_sr;
                      recipeIDs_for_SR = temp_sr.getRecipeIDs();
                      break;
                    }
                  }
                  //break;
                
              
              //***************

              if (recipeIDs_for_SR.size() > 1 && sr_to_do != null)
              {
                String temp_used_da_id = null;
                HashMap<String, String> temp_sr_map = PECManager.getInstance().getProduct_sr_tracking().get(productInst_id);
                if (temp_sr_map != null)
                {
                  temp_used_da_id = temp_sr_map.get(sr_to_do.getUniqueId());
                }
                Recipe firstRecipe = null;
                //check if the precedences are the same
                List<Recipe> recipes = new ArrayList<>();
                for (String auxRecipeID : recipeIDs_for_SR)
                {
                  String da_aml_ID = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(auxRecipeID);
                  if (da_aml_ID == null)
                  {
                    continue;
                  }
                  if (temp_used_da_id != null && !da_aml_ID.equals(temp_used_da_id))
                  {
                    continue;
                  }
                  DeviceAdapter auxDA = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_aml_ID);

                  //MARTELO add modules recipe list to global list temporary
                  List<Recipe> tempRepList = new ArrayList<>(auxDA.getListOfRecipes());
                  for (Module auxMod : auxDA.getListOfModules())
                  {
                    tempRepList.addAll(auxMod.getRecipes());
                  }

                  for (Recipe auxRecipe : tempRepList)
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

                  if (temp_used_da_id != null)
                  {
                    logger.info("[checkNextRecipe][product_sr_tracking] will be locked until the recipe is valid " + firstRecipe.getUniqueId());
                    while (true)
                    {
                      if (checkNextValidation(firstRecipe.getUniqueId()))
                      {
                        logger.info("[checkNextRecipe] returning - " + firstRecipe.getUniqueId());
                        return firstRecipe.getUniqueId();
                      }
                      else
                      {
                        try
                        {
                          Thread.sleep(1000);
                        }
                        catch (InterruptedException ex)
                        {
                          java.util.logging.Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      }
                    }
                  }
                  else
                  {
                    if (checkNextValidation(firstRecipe.getUniqueId()))
                    {
                      logger.info("[checkNextRecipe] returning - " + firstRecipe.getUniqueId());
                      return firstRecipe.getUniqueId();
                    }
                    else
                    {
                      String auxDAFirst_id = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);
                      DeviceAdapter auxDAFirst = DACManager.getInstance().getDeviceAdapterbyAML_ID(auxDAFirst_id);

                      for (Recipe auxRecipe : recipes)
                      {
                        //check if DA is the same? only offer possibilities if next if a transport or same DA as first option
                        String auxDA_id = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(auxRecipe.getUniqueId());
                        DeviceAdapter auxDA = DACManager.getInstance().getDeviceAdapterbyAML_ID(auxDA_id);

                        if ((auxDA.getSubSystem().getSsType().equals(MSBConstants.DEVICE_ADAPTER_TYPE_TRANSPORT)
                                || auxDA.getSubSystem().getUniqueId().equals(auxDAFirst.getSubSystem().getUniqueId()))
                                && checkNextValidation(auxRecipe.getUniqueId()))
                        {
                          logger.info("[checkNextRecipe] returning - " + auxRecipe.getUniqueId());
                          return auxRecipe.getUniqueId();
                        }
                      }
                      logger.warn("There are no other Recipe choices for the current recipe " + recipeID);
                    }
                  }
                }
              }
              else
              {
                boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(nextRecipeID);
                if (valid)
                {
                  //yes
                  return nextRecipeID;
                }
                else
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
    }
    else
    {
      logger.error("There are no Adapters that can perform the required recipe: " + recipeID);
    }
    logger.info("[checkNextRecipe] returning - 'empty'");
    return "";
  }

  /**
   * Read KPIs of recipe_id from OPCServer
   *
   * @param da_id
   * @param recipe_id
   */
  private void readKPIs_DA(String da_id, String recipe_id, String productInst_ID)
  {
    DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
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
            logger.info("[KPI] DA: " + CurrentDA.getSubSystem().getName() + "| RECIPE.NAME - " + recipe.getName() + " | KPI_Name: " + kpi.getName() + " | KPI_Value: " + kpi.getValue());
          }

          //IF THE AC is activated, send the KPIs upwards
          if (MSBConstants.USING_CLOUD)
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
              }
              else
              {
                logger.warn("ChangeState -> vertx is null, so let's skip it! -> " + productInst_ID);
              }

              SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
              SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
              BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

              logger.info("Trying to send RED by WS");
              ServiceCallStatus scs = systemConfigurator.newRecipeExecutionData(red);
              logger.info("WS returned: " + scs.getCode() + " | DESC: " + scs.getDescription());
            }
            catch (Exception ex)
            {
              logger.error("Error trying to connect to cloud!: " + ex.getMessage());
            }
          }
          else
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
          for (Recipe recipe : module.getRecipes())
          {
            if (recipe.getUniqueId().equals(recipe_id))
            {
              for (KPISetting kpi : recipe.getKpiSettings())
              {
                NodeId kpiPath = Functions.convertStringToNodeId(kpi.getPath());
                DeviceAdapterOPC client = (DeviceAdapterOPC) CurrentDA;
                String kpiValue = Functions.readOPCNodeToString(client.getClient().getClientObject(), kpiPath);
                kpi.setValue(kpiValue);
                logger.info("[KPI] DA: " + CurrentDA.getSubSystem().getName() + "| RECIPE.NAME - " + recipe.getName() + " | KPI_Name: " + kpi.getName() + " | KPI_Value: " + kpi.getValue());
              }

              //IF THE AC is activated, send the KPIs upwards
              if (MSBConstants.USING_CLOUD)
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
                  }
                  else
                  {
                    logger.warn("ChangeState -> vertx is null, so let's skip it! -> " + productInst_ID);
                  }

                  SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
                  SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
                  BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
                  bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

                  logger.info("Trying to send RED by WS");
                  ServiceCallStatus scs = systemConfigurator.newRecipeExecutionData(red);
                  logger.info("WS returned: " + scs.getCode() + " | DESC: " + scs.getDescription());
                }
                catch (Exception ex)
                {
                  logger.error("Error trying to connect to cloud!: " + ex.getMessage());
                }
              }
              else
              {

              }
              break;
            }
          }
        }
      }
    }
  }

  private void finishProduct_MARTELO(String da_id, String productInst_id)
  {
    try
    {
      Thread.sleep(5000);
    }
    catch (Exception ex)
    {

    }
    ProductInstance prodInst = PECManager.getInstance().getProductsDoing().remove(productInst_id);

    MSB_gui.addToTableExecutedOrders(prodInst.getOrderId(), prodInst.getProductId(), prodInst.getUniqueId());
    MSB_gui.removeFromTableCurrentOrder(prodInst.getUniqueId());

    PECManager.getInstance().getExecutionMap().get(da_id).release();
    String da_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(da_id);
    DeviceAdapter da_test = DACManager.getInstance().getDeviceAdapterbyName(da_name1);
    MSB_gui.updateTableAdaptersSemaphore(
            String.valueOf(PECManager.getInstance().getExecutionMap().get(da_test.getSubSystem().getUniqueId()).availablePermits()),
            da_test.getSubSystem().getName());

    //****** SUPER MARTELO ************
    DeviceAdapter da_agv = DACManager.getInstance().getDeviceAdapterbyName("AGV");
    PECManager.getInstance().getExecutionMap().get(da_agv.getSubSystem().getUniqueId()).release();
    MSB_gui.updateTableAdaptersSemaphore(
            String.valueOf(PECManager.getInstance().getExecutionMap().get(da_agv.getSubSystem().getUniqueId()).availablePermits()),
            da_agv.getSubSystem().getName());
    //*******************

    Long prodTime = new Date().getTime() - prodInst.getStartedProductionTime().getTime();
    PerformanceMasurement.getInstance().getProdInstanceTime().add(prodTime);

    if (MSBConstants.USING_CLOUD)
    {
      try
      {
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
        SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);
        FinishedProductInfo fpi = new FinishedProductInfo();
        fpi.setProductInstanceId(productInst_id);
        fpi.setFinishedTime(new Date());
        fpi.setRegistered(prodInst.getStartedProductionTime());
        systemConfigurator.finishedProduct(fpi);
      }
      catch (Exception ex)
      {
        System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
      }
    }
  }

  private boolean isLastRecipe(String recipeID, String productInst_id, String productType_id)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    logger.info("[isLastRecipe] DA id from checkNextRecipe: " + Daid);

    if (Daid != null)
    {
      //get DA name
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      logger.info("[isLastRecipe]DA name of finished recipe : " + DA_name);
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      if (da == null)
      {
        logger.warn("The DA is null!");
      }
      else
      {
        String prodID = productInst_id;
        for (int i = 0; i < 2; i++)
        {
          for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
          {
            if (execRow.getRecipeId() != null && execRow.getProductId() != null
                    && execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
            {
              //get the nextRecipe on its executionTables
              String auxNextRecipeNode = execRow.getNextRecipeIdPath();

              if (auxNextRecipeNode == null)
              {
                //is last recipe
                logger.info("[isLastRecipe] returning - last");
                return true;
              }

              NodeId nextRecipeNode = Functions.convertStringToNodeId(auxNextRecipeNode);
              DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
              String nextRecipeID = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), nextRecipeNode);

              if (nextRecipeID == null || nextRecipeID.equals("done") || nextRecipeID.equals("last"))
              {
                logger.info("[isLastRecipe] returning - last");
                return true;
              }
              else
              {
                if (nextRecipeID.isEmpty())
                {
                  logger.info("[isLastRecipe] returning - 'empty'");
                  return true;
                }
              }
            }
          }
          //no prodInst found in execTable, search for productType now
          prodID = productType_id;
        }
      }
    }
    else
    {
      logger.error("There are no Adapters that can perform the required recipe: " + recipeID);
    }

    return false;
  }

  private boolean isLastRecipe_withoutProd(String recipeID)
  {
    //get deviceAdapter that does the required recipe
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    logger.info("[isLastRecipe_withoutProd] DA id from checkNextRecipe: " + Daid);

    if (Daid != null)
    {
      //get DA name
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      logger.info("[isLastRecipe_withoutProd]DA name of finished recipe : " + DA_name);
      //get DA object from it's name
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      if (da == null)
      {
        logger.warn("The DA is null!");
      }
      else
      {
        for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
        {
          if (execRow.getRecipeId() != null && execRow.getRecipeId().equals(recipeID))
          {
            //get the nextRecipe on its executionTables
            String auxNextRecipeNode = execRow.getNextRecipeIdPath();

            if (auxNextRecipeNode == null)
            {
              //is last recipe
              logger.info("[isLastRecipe_withoutProd] returning - last");
              return true;
            }

            NodeId nextRecipeNode = Functions.convertStringToNodeId(auxNextRecipeNode);
            DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
            String nextRecipeID = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), nextRecipeNode);

            if (nextRecipeID == null || nextRecipeID.equals("done") || nextRecipeID.equals("last"))
            {
              logger.info("[isLastRecipe_withoutProd] returning - last");
              return true;
            }
            else
            {
              if (nextRecipeID.isEmpty())
              {
                logger.info("[isLastRecipe_withoutProd] returning - 'empty'");
                return true;
              }
            }
          }
        }
      }
    }
    else
    {
      logger.error("There are no Adapters that can perform the required recipe: " + recipeID);
    }

    return false;
  }

  private void productIsOnQueue(String productInst_ID)
  {
    ProductInstance prodInst = PECManager.getInstance().getProductsDoing().get(productInst_ID);
    prodInst.setState(ProductInstanceStatus.QUEUED);
  }

  private void productIsExecuting(String productInst_ID)
  {
    ProductInstance prodInst = PECManager.getInstance().getProductsDoing().get(productInst_ID);
    prodInst.setState(ProductInstanceStatus.PRODUCING);
  }

  private void remove_queued_action(String recipe_id)
  {
    QueuedAction qa = DACManager.getInstance().QueuedActionMap.get(recipe_id);
    if (qa != null)
    {
      //TODO add recipe remove code
      DACManager.getInstance().QueuedActionMap.remove(recipe_id);
    }
  }

}
