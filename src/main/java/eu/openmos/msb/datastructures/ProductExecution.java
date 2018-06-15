/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.utilities.Functions;
import java.util.ArrayList;
import java.util.Date;
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
    } else
    {
      if (hasPendingOrders())
      {
        logger.info("\n\n************** Starting Executor! ********************\n\n\n");
        pecm.setState(true);
        ExecuteOrder();
      }
      else
        logger.info("\n\n************** All orders are being executed! ********************\n\n\n");
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
    } else
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
                if (auxSR.getPrecedents() == null) //check which recipe has the precedents =null , which means it is the first one 
                {
                  //check for 1 available recipe for the SR
                  while (true)
                  {
                    boolean getOut = false;
                    for (String recipeID : auxSR.getRecipeIDs())
                    {
                      if (checkRecipeAvailable(recipeID, auxProdInstance)) //check if the recipe is valid and if the DA and nextDA are at ready state
                      {
                        if (executeRecipe(recipeID, auxProdInstance)) //if returns false, check another alternative recipe for the same SR
                        {
                          logger.info("The execution of Recipe: " + recipeID + " Returned true");

                          ProdManager.getProductsDoing().put(auxProdInstance.getUniqueId(), ProdManager.getProductsToDo().remove(0)); //the first recipe of the product is done, put it into "doing"
                          MSB_gui.addToTableCurrentOrders(auxProdInstance.getOrderId(), auxProdInstance.getProductId(), auxProdInstance.getUniqueId());

                          String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
                          if (Daid != null)
                          {
                            String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
                            MSB_gui.updateDATableCurrentOrderNextDA(auxProdInstance.getUniqueId(), DA_name);
                          }
                          MSB_gui.removeFromTableSubmitedOrder(auxProdInstance.getUniqueId());
                          getOut = true;
                          break;
                        } else
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
                    } catch (InterruptedException ex)
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
          } catch (InterruptedException ex)
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
    } catch (InterruptedException ex)
    {
      Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
    }
    ProdManager.setState(false); //true=running false=ready
    CheckExecutorState(); //do the next orderistance
  }

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
          
          //MARTELO
          //if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
          {
            if (checkNextRecipe(da, recipeID, prodInst))
            {
              return true;
            }
          }
        } else
        {
          return false;
        }
      }
    }
    return false;
  }

  private boolean checkNextRecipe(DeviceAdapter da, String recipeID, ProductInstance prodInst)
  {
    String nextRecipeID = "";
    String prodID = prodInst.getUniqueId();
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
      {
        if (execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
        {
          nextRecipeID = execRow.getNextRecipeId();
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

                if (da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
                {
                  return true;
                }
              }
            } else
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

  public static DeviceAdapter getDAofNextRecipe(DeviceAdapter da, String recipeID, ProductInstance prodInst)
  {
    String nextRecipeID = "";
    String prodID = prodInst.getUniqueId();
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow execRow : da.getExecutionTable().getRows())
      {
        if (execRow.getRecipeId().equals(recipeID) && execRow.getProductId().equals(prodID))
        {
          nextRecipeID = execRow.getNextRecipeId();
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
      prodID = prodInst.getProductId();
    }
    return null;
  }

  private boolean executeRecipe(String recipeID, ProductInstance prodInst)
  {
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      for (Recipe recipe : da.getListOfRecipes())
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

            if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())
            {
              logger.info("[SEMAPHORE][PS] ACQUIRED for " + da.getSubSystem().getName());
              DeviceAdapter da_next = getDAofNextRecipe(da, recipeID, prodInst);
              MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

              //ONLY EXECUTE IF NEXT DA IS AVAILABLE
              if (da_next != null)
              {
                if (da.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
                {
                  logger.info("The first and second recipe are from the same adapter!");
                  //break;
                } else
                {
                  if (PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).tryAcquire())
                  {
                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                    logger.info("[SEMAPHORE] ACQUIRED for NEXT " + da_next.getSubSystem().getName());
                    //break;
                  } else
                  {
                    PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                    MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                    logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());

                    return false;
                  }
                }
              } else
              {
                PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());

                logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
              }
            } else
            {
              return false;
            }
          }
          //NO OPTIMIZER
          else
          {
            while (true)
            {
              MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());

              if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())
              {
                logger.info("[SEMAPHORE][PS] ACQUIRED for " + da.getSubSystem().getName());
                DeviceAdapter da_next = getDAofNextRecipe(da, recipeID, prodInst);

                MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                //ONLY EXECUTE IF NEXT DA IS AVAILABLE
                if (da_next != null)
                {
                  if (da.getSubSystem().getUniqueId().equals(da_next.getSubSystem().getUniqueId()))
                  {
                    logger.info("The first and second recipe are from the same adapter!");
                    break;
                  } else
                  {
                    if (PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).tryAcquire())
                    {
                      MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                      logger.info("[SEMAPHORE] ACQUIRED for NEXT " + da_next.getSubSystem().getName());
                      break;
                    } else
                    {
                      PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                      MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                      MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).availablePermits()), da_next.getSubSystem().getName());

                      logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());

                      try
                      {
                        Thread.sleep(3000);
                      } catch (InterruptedException ex)
                      {
                        Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
                      }
                    }
                  }
                } else
                {
                  PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                  MSB_gui.updateTableAdaptersSemaphore(String.valueOf(PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()), da.getSubSystem().getName());
                  logger.info("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
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
            } catch (Exception ex)
            {
              System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
            }
          }
          logger.info("[EXECUTE] recipeID: " + recipeID);
          NodeId objectID = Functions.convertStringToNodeId(invokeObjectID);
          NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);
          //MARTELO bool = false apenas para a recolha bruta de dados
          result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objectID, methodID, prodInst.getUniqueId(), prodInst.getProductId(), true);

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
    } catch (InterruptedException ex)
    {
      Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
    }
    PECManager.getInstance().getNewInstanceSemaphore().release();
  }

}
