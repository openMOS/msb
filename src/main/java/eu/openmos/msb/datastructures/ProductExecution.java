/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.Module;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.utilities.Functions;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Introsys
 */
public class ProductExecution implements Runnable
{

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
  List<String> recipesExecuted = new ArrayList<>();
  static int HighOrderIndex = -1;
  StopWatch firstRecipeCallTime = new StopWatch();
  boolean notAgain = false;

  @Override
  public void run()
  {
    CheckExecutorState();
  }

  public void CheckExecutorState()
  {
    PECManager pecm = PECManager.getInstance();
    if (pecm.getState())
    {
      logger.info("Executor already Running!");
      checkPriority();
    }
    else
    {
      if (hasPendingOrders())
      {
        logger.info("\n\n************** Starting Executor! ********************\n\n\n");
        pecm.setState(true);
        ExecuteOrder();
      }
      else
      {
        logger.info("\n\n************** All orders are being executed! ********************\n\n\n");
      }
    }
  }

  public void ExecuteOrder()
  {
    firstRecipeCallTime.reset();
    firstRecipeCallTime.start();

    PECManager ProdManager = PECManager.getInstance();

    //Get priority and execute the higher value order
    int HighPriority = -1;
    HighOrderIndex = -1;

    List<OrderInstance> orderInstanceList = ProdManager.getOrderInstanceList();
    if (orderInstanceList.size() > 0)
    {
      for (int i = 0; i < orderInstanceList.size(); i++)
      {
        int priority = orderInstanceList.get(i).getPriority();
        if (priority > HighPriority)
        {
          HighPriority = priority;
          HighOrderIndex = i;
        }
      }
      if (HighOrderIndex != -1)
      {
        //get highest order instance 
        OrderInstance orderInstanceToExecute = ProdManager.getOrderInstanceList().get(HighOrderIndex);
        //put all the product instances from the orderinstance in the todo Queue
        ProdManager.getProductsToDo().addAll(orderInstanceToExecute.getProductInstances());

        logger.info("...Creating Order Instances...");
        //add to pec manager doing
        ExecuteProdsInstance();
      }
    }
    else
    {
      PECManager.getInstance().setState(false);
    }
  }

  private boolean hasPendingOrders()
  {
    PECManager ProdManager = PECManager.getInstance();
    List<OrderInstance> orderInstanceList = ProdManager.getOrderInstanceList();

    return orderInstanceList.size() > 0;
  }

  public void ExecuteProdsInstance()
  {
    PECManager ProdManager = PECManager.getInstance();
    logger.info("number of product instances to be done: " + ProdManager.getProductsToDo().size());

    while (ProdManager.getProductsToDo().size() > 0)
    {
      while (true)
      {
        if (ProdManager.getNewInstanceSemaphore().tryAcquire())
        {
          ProductInstance auxProdInstance = ProdManager.getProductsToDo().get(0);
          logger.info("ProdInst to start: " + auxProdInstance.getUniqueId()); //da instancia
          String productId = auxProdInstance.getProductId(); //prod type
          //ir as tabelas de execução

          List<Product> availableProducts = ProdManager.getAvailableProducts();
          for (Product auxProduct : availableProducts)
          {
            if (auxProduct.getUniqueId() == null ? productId == null : auxProduct.getUniqueId().equals(productId)) //check if the resquested product is available
            {
              //analisar SkillRequirements
              List<SkillRequirement> skillRequirements = auxProduct.getSkillRequirements();
              for (SkillRequirement auxSR : skillRequirements)
              {
                //will execute all non precedent requirements  i the found order, only goes to the next after executing successfully
                if (auxSR.getPrecedents() == null) //check which recipe has the precedents =null , which means it is the first one 
                {
                    logger.debug("[PS] SR to execute: " + auxSR.getName() + " --- " + auxSR.getUniqueId());
                  //check for 1 available recipe for the SR
                  while (true)
                  {
                    boolean getOut = false;

                    for (String recipeID : auxSR.getRecipeIDs())
                    {
                      String recipe_to_check = recipeID;
                      if (MSBConstants.MSB_OPTIMIZER)
                      {
                        recipe_to_check = PECManager.getInstance().getRecipeIDbyTrackPI(auxSR, auxProdInstance.getUniqueId(), recipeID);
                      }
                      if (checkRecipeAvailable(recipe_to_check, auxProdInstance)) //check if the recipe is valid and if the DA and nextDA are at ready state
                      {
                        if (executeRecipe(recipe_to_check, auxProdInstance, auxSR)) //if returns false, check another alternative recipe for the same SR
                        {
                          logger.info("The execution of Recipe: " + recipe_to_check + " Returned true");
                          HashMap<String, String> temp_sr_map = PECManager.getInstance().getProduct_sr_tracking().get(auxProdInstance.getUniqueId());
                          if (temp_sr_map == null)
                          {
                            temp_sr_map = new HashMap<>();
                          }
                          String da_aml_id = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(recipe_to_check);
                          temp_sr_map.put(auxSR.getUniqueId(), da_aml_id);

                          PECManager.getInstance().getProduct_sr_tracking().put(auxProdInstance.getUniqueId(), temp_sr_map);
                          logger.debug("[] added " + auxSR.getUniqueId() + da_aml_id + " to product_sr_tracking for pi_ID: " + auxProdInstance.getUniqueId());
                          /*
                          if (temp_sr_map.get(auxSR.getUniqueId()) == null)
                          {
                            
                          }
                           */

                          if (ProdManager.getProductsDoing().get(auxProdInstance.getUniqueId()) == null)
                          {
                            //the first recipe of the product is done, put it into "doing"
                            ProdManager.getProductsDoing().put(auxProdInstance.getUniqueId(), ProdManager.getProductsToDo().remove(0));
                            MSB_gui.addToTableCurrentOrders(auxProdInstance.getOrderId(), auxProdInstance.getProductId(), auxProdInstance.getUniqueId());
                            MSB_gui.removeFromTableSubmitedOrder(auxProdInstance.getUniqueId());
                          }
                          String da_db_id = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
                          if (da_db_id != null)
                          {
                            String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(da_db_id);
                            MSB_gui.updateDATableCurrentOrderNextDA(auxProdInstance.getUniqueId(), DA_name);
                          }
                          getOut = true;
                          break;
                        }
                        else
                        {
                          logger.warn("[ExecuteProdsInstance] The execution of Recipe: " + recipeID + " Returned false! checking alternatives...");
                        }
                      }
                    }
                    if (getOut)
                    {
                      break;
                    }

                    //trying recipes in 5sec cycles
                    try
                    {
                      Thread.sleep(5000);
                    }
                    catch (InterruptedException ex)
                    {
                      Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  }
                }
              }
              break;
            }
          }
          ProdManager.getNewInstanceSemaphore().release();
          try
          {
            Thread.sleep(1000);
          }
          catch (InterruptedException ex)
          {
            Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
          }
          break;
        }
      }
    }
    //acabou a order, começar a proxima
    ProdManager.getOrderInstanceList().remove(HighOrderIndex); //remove the orderInstance that finished
    try
    {
      Thread.sleep(1000);
    }
    catch (InterruptedException ex)
    {
      Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
    }
    ProdManager.setState(false); //true=running false=ready
    CheckExecutorState(); //do the next orderistance
  }

  /**
   *
   * @param recipeID
   * @param prodInst
   * @return check if recipeID is ready to be executed
   */
  private boolean checkRecipeAvailable(String recipeID, ProductInstance prodInst)
  {
    boolean recipeIdIsValid = DatabaseInteraction.getInstance().getRecipeIdIsValid(recipeID);
    if (recipeIdIsValid)
    {
      String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
      if (Daid != null)
      {
        String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);

        NodeId statePath = Functions.convertStringToNodeId(da.getSubSystem().getStatePath());
        DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
        if (statePath.isNotNull())
        {
          String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath); //read the DA state, capable of executing the required recipeID
          da.getSubSystem().setState(state);

          if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
          {
            if (checkNextRecipe(da, recipeID, prodInst))
            {
              return true;
            }
          }
        }
        else
        {
          return false;
        }
      }
    }
    return false;
  }

  /**
   *
   * @param da
   * @param recipeID
   * @param prodInst
   * @return true if the nextRecipe is available or if it was the last recipe
   */
  private boolean checkNextRecipe(DeviceAdapter da, String recipeID, ProductInstance prodInst)
  {
    String nextRecipeID = "";
    String prodID = prodInst.getUniqueId();
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
      {
        if (execRow.getRecipeId() != null && execRow.getProductId() != null && execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
        {
          nextRecipeID = execRow.getNextRecipeId();
          if (nextRecipeID == null)
          {
            return true;
          }

          if (MSBConstants.MSB_OPTIMIZER)
          {
            Product prod = PECManager.getInstance().getProductByID(prodInst.getProductId());
            SkillRequirement sr = PECManager.getInstance().getSRbyRecipeID(prod, nextRecipeID);
            nextRecipeID = PECManager.getInstance().getRecipeIDbyTrackPI(sr, prodInst.getUniqueId(), nextRecipeID);
          }

          boolean recipeIdIsValid = DatabaseInteraction.getInstance().getRecipeIdIsValid(nextRecipeID);

          if (recipeIdIsValid)
          {
            String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
            if (Daid_next != null)
            {
              String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid_next);
              DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyName(DA_name);

              NodeId statePath = Functions.convertStringToNodeId(da_next.getSubSystem().getStatePath());
              DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da_next;
              if (statePath.isNotNull())
              {
                String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
                da_next.getSubSystem().setState(state);
                System.out.println("daState for NEXT: " + state);

                //if (da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
                {
                  return true;
                }
              }
            }
            else
            {
              return false;
            }
          }
          return false;
        }
      }
      //no prodInst found in execTable, search for productType now
      prodID = prodInst.getProductId();
    }
    return false;
  }

  /**
   *
   * @param da
   * @param recipeID
   * @param prodInst
   * @return 1 if next DA is found; 2 if this is the last recipe (no next DA); 0 if there a next recipe but no DA;
   */
  public static int nextDAExists(DeviceAdapter da, String recipeID, ProductInstance prodInst)
  {
    String nextRecipeID = "";
    String prodID = prodInst.getUniqueId();
    Boolean foundP = false;

    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
      {
        if (execRow.getRecipeId() != null && execRow.getProductId() != null
                && execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
        {
          foundP = true;
          nextRecipeID = execRow.getNextRecipeId();
          if (nextRecipeID == null || nextRecipeID.equals("done") || nextRecipeID.equals("last"))
          {
            //current recipe is the last recipe --- TO BE TESTED!!!
            return 2;
          }
          String Daid_next = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
          if (Daid_next != null)
          {
            return 1;
          }
          break;
        }
      }
      //no prodInst found in execTable, search for productType now
      prodID = prodInst.getProductId();
    }
    if (foundP)
    {
      return 0;
    }

    return 2;
  }

  /**
   *
   * @param recipeID
   * @param prodInst
   * @return true if the recipe was executed successfully; Checks if DAs are available (semaphores)
   */
  private boolean executeRecipe(String recipeID, ProductInstance prodInst, SkillRequirement sr)
  {
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      DeviceAdapter da_next = null;
      List<Recipe> recipes = new ArrayList<>(da.getListOfRecipes());
      for (Module module : da.getSubSystem().getInternalModules())
      {
        recipes.addAll(module.getRecipes());
      }

      for (Recipe recipe : recipes)
      {
        if (recipe.getUniqueId() == null ? recipeID == null : recipe.getUniqueId().equals(recipeID))
        {
          String invokeObjectID = recipe.getInvokeObjectID();
          String invokeMethodID = recipe.getInvokeMethodID();
          DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
          boolean result = false;

          if (!notAgain) //only get timer once
          {
            PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
            perfMeasurement.getOrderTillRecipeCallTimers().add(firstRecipeCallTime.getTime());
            firstRecipeCallTime.stop();
            notAgain = true;
          }
          //need the first 2 adapters available to execute

          if (MSBConstants.MSB_OPTIMIZER)
          {
            MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());

            //check da_state
            if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())
            {
              MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());

              logger.info("[SEMAPHORE][PS] ACQUIRED for " + da.getSubSystem().getName());
              int nextStep = nextDAExists(da, recipeID, prodInst);
              //DeviceAdapter da_next = getDAofNextRecipe(da, recipeID, prodInst);
              //ONLY EXECUTE IF NEXT DA IS AVAILABLE
              //if (da_next != null)
              switch (nextStep)
              {
                case 1:
                  logger.info("[executeRecipe]: 1 - next Recipe is available - checking DA");
                  //there is next DA
                  da_next = PECManager.getInstance().getDAofNextRecipe(da, recipeID, prodInst.getUniqueId(), prodInst.getProductId(), sr.getUniqueId());
                  MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());
                  if (da.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
                  {
                    logger.info("[executeRecipe] The first and second recipe are from the same adapter!");

                    //break;
                  }
                  else
                  {
                      String next_recipe_id = PECManager.getInstance().getNextRecipe(da, recipeID, prodInst.getUniqueId(), prodInst.getProductId(), sr.getUniqueId());
                    SkillRequirement sr_next = PECManager.getInstance().getNextSR(sr.getUniqueId(), prodInst.getProductId(), next_recipe_id);
                    if (PECManager.getInstance().need_to_get_da(da_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), prodInst.getUniqueId()))
                    {
                      if (PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).tryAcquire())
                      {
                        MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                        logger.info("[SEMAPHORE][PS] ACQUIRED for NEXT " + da_next.getSubSystem().getName());
                        //break;
                      }
                      else
                      {
                        PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                        MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                        MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                        logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
                        return false;
                      }
                    }
                  }
                  break;
                case 2:
                  logger.info("[executeRecipe]: 2 - current Recipe is the last recipe");
                  //there is no next recipe
                  MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                  break;
                default:
                  logger.info("[executeRecipe]: 0 - next recipe is not available");
                  //CHECK THIS AFTER FOR ONLY ONE RECIPE!!!!!!
                  PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                  MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                  logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
                  break;
              }
            }
            else
            {
              return false;
            }
          }
          //NO OPTIMIZER
          else
          {
            OUTER:
            while (true)
            {
              MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
              if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())
              {
                logger.info("[SEMAPHORE][PS] ACQUIRED for " + da.getSubSystem().getName());
                int nextStep = nextDAExists(da, recipeID, prodInst);
                //ONLY EXECUTE IF NEXT DA IS AVAILABLE
                //if (da_next != null)
                switch (nextStep)
                {
                  case 1:
                    //next DA exists
                    da_next = PECManager.getInstance().getDAofNextRecipe(da, recipeID, prodInst.getUniqueId(), prodInst.getProductId(), sr.getUniqueId());

                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());
                    if (da.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
                    {
                      logger.info("The first and second recipe are from the same adapter!");
                      break OUTER;
                    }
                    else
                    {
                      SkillRequirement sr_next = PECManager.getInstance().getNextSRbyRecipeID(da, recipeID, prodInst.getUniqueId(), prodInst.getProductId());
                      if (PECManager.getInstance().need_to_get_da(da_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), prodInst.getUniqueId()))
                      {
                        if (PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).tryAcquire())
                        {
                          MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());
                          logger.info("[SEMAPHORE] ACQUIRED for NEXT " + da_next.getSubSystem().getName());
                          break OUTER;
                        }
                        else
                        {
                          PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                          MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                          MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                          logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());

                          try
                          {
                            Thread.sleep(3000);
                          }
                          catch (InterruptedException ex)
                          {
                            Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
                          }
                        }
                      }
                      else
                      {
                        logger.info("[SEMAPHORE] ACQUIRED from SR " + da_next.getSubSystem().getName());
                        break OUTER;
                      }
                    }
                    break;
                  case 2:
                    //there is no next recipe
                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                    break;
                  default:
                    //next DA is not available
                    PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                    logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
                    break;
                }
              }
            }
          }

          prodInst.setStartedProductionTime(new Date());

          if (MSBConstants.USING_CLOUD)
          {
            try
            {
              SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
              SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
              BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);
              systemConfigurator.startedProduct(prodInst);
            }
            catch (Exception ex)
            {
              System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
            }
          }
          if (MSBConstants.MSB_OPTIMIZER)
          {
            PECManager.getInstance().lock_SR_to_WS(da.getSubSystem().getUniqueId(), sr.getUniqueId(), prodInst.getUniqueId());
            if (da_next != null)
            {
              String next_recipe_id = PECManager.getInstance().getNextRecipe(da, recipeID, prodInst.getUniqueId(), prodInst.getProductId(), sr.getUniqueId());
              SkillRequirement sr_next = PECManager.getInstance().getNextSR(sr.getUniqueId(), prodInst.getProductId(), next_recipe_id);
              PECManager.getInstance().lock_SR_to_WS(da_next.getSubSystem().getUniqueId(), sr_next.getUniqueId(), prodInst.getUniqueId());
            }
          }
          logger.info("[EXECUTE] recipeID: " + recipeID);
          NodeId objectID = Functions.convertStringToNodeId(invokeObjectID);
          NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);
          //MARTELO bool = false apenas para a recolha bruta de dados
          result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objectID, methodID, prodInst.getUniqueId(), prodInst.getProductId(), true, sr.getUniqueId());

          return result;
        }
      }
    }
    return false;
  }

  /**
   * check if the new order have higher priority than the one being executed
   */
  private void checkPriority()
  {
    try
    {
      PECManager.getInstance().getNewInstanceSemaphore().acquire();
      Thread.sleep(1000);
      List<OrderInstance> orderInstanceList = PECManager.getInstance().getOrderInstanceList();

      int highPriority = -1;
      int highIndex = -1;
      for (int i = 0; i < orderInstanceList.size(); i++)
      {
        if (highPriority < orderInstanceList.get(i).getPriority())
        {
          highPriority = orderInstanceList.get(i).getPriority();
        }
        highIndex = i;
      }
      if (highIndex != -1 && HighOrderIndex != -1 && highIndex > HighOrderIndex)
      {
        OrderInstance oi = PECManager.getInstance().getOrderInstanceList().get(HighOrderIndex);

        int until = oi.getProductInstances().size() - PECManager.getInstance().getProductsToDo().size();
        for (int i = 0; i < until; i++)
        {
          oi.getProductInstances().remove(0);
        }

        OrderInstance orderInstanceToExecute = PECManager.getInstance().getOrderInstanceList().get(highIndex);
        PECManager.getInstance().getProductsToDo().clear();
        PECManager.getInstance().getProductsToDo().addAll(orderInstanceToExecute.getProductInstances());
        HighOrderIndex = highIndex;
      }
    }
    catch (InterruptedException ex)
    {
      Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
    }
    PECManager.getInstance().getNewInstanceSemaphore().release();
  }

}
