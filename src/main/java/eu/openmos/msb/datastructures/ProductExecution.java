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
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;

/**
 *
 * @author Introsys
 */
public class ProductExecution implements Runnable
{

  List<String> recipesExecuted = new ArrayList<>();
  int HighOrderIndex = -1;
  StopWatch firstRecipeCallTime = new StopWatch();
  boolean notAgain = false;

  @Override
  public void run()
  {
    CheckState();
  }

  public void CheckState()
  {
    PECManager pecm = PECManager.getInstance();
    if (pecm.getState())
    {
      System.out.println("Executor already Running!");
      
    } else
    {
      System.out.println("\n\n\n\n**************Starting Executor!********************\n\n\n\n\n");
      ExecuteOrder();
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

      System.out.println("...Creating Order Instances...");
      //add to pec manager doing
      ExecuteProdsInstance();
    }
  }

  public void ExecuteProdsInstance()
  {
    PECManager ProdManager = PECManager.getInstance();

    System.out.println("number of product instances to be done: " + ProdManager.getProductsToDo().size());

    while (ProdManager.getProductsToDo().size() > 0)
    {
      ProductInstance auxProdInstance = ProdManager.getProductsToDo().peek();
      System.out.println("ProdInst to start: " + auxProdInstance.getUniqueId()); //da instancia
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
                  //System.out.println("\n Trying to check if recipe is VALID ***** " + recipeID + " *****\n");
                  if (checkRecipeAvailable(recipeID) /*&& checkProductAgentComms(auxProdInstance.getUniqueId())*/)
                  {
                    if (executeRecipe(recipeID, auxProdInstance))
                    {
                      System.out.println("The execution of Recipe: " + recipeID + " Returned true");
                      ProdManager.getProductsDoing().put(auxProdInstance.getUniqueId(), ProdManager.getProductsToDo().poll()); //the first recipe of the product is done, put it into "doing"
                      MSB_gui.addToTableCurrentOrders(auxProdInstance.getOrderId(), auxProdInstance.getProductId(), auxProdInstance.getUniqueId());
                      
                      String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
                        if (Daid != null) {
                            String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
                            MSB_gui.updateDATableCurrentOrderNextDA(auxProdInstance.getUniqueId(), DA_name);
                        }
                      MSB_gui.removeFromTableSubmitedOrder(auxProdInstance.getUniqueId());
                      getOut = true;
                      break;
                    } else
                    {
                      System.out.println("[ERROR] The execution of Recipe: " + recipeID + " Returned false!");
                    }
                  }
                }
                if (getOut)
                {
                  break;
                }
              }
            }
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
    CheckState(); //do the next orderistance
  }

  private boolean checkRecipeAvailable(String recipeID)
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
          String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
          da.getSubSystem().setState(state);

        if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
        {
          if (checkNextRecipe(da, recipeID))
            return true;
        }
      }
    }
    return false;
  }

  private boolean checkNextRecipe(DeviceAdapter da, String recipeID)
  {
    String nextRecipeID = "";
    for (ExecutionTableRow auxRow : da.getExecutionTable().getRows())
    {
      if (auxRow.getRecipeId().equals(recipeID))
      {
        nextRecipeID = auxRow.getNextRecipeId();
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
            
            String state = Functions.readOPCNodeToString(daOPC.getClient().getClientObject(), statePath);
            da_next.getSubSystem().setState(state);
            System.out.println("daState for NEXT: " + state);  
            
            if (da_next.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
            {
              return true;
            }
          }
        }
        return false;
      }
    }
    return false;
  }

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
  
  private boolean checkProductAgentComms(String productInstID)
  {

    for (ProductInstance prodInst : PECManager.getInstance().getProductsToDo())
    {
      if (productInstID.equals(prodInst.getUniqueId()))
      {
        System.out.println("Checking if the current product instance has agent representation...");
        Boolean hasAgent = prodInst.getHasAgent();

        if (hasAgent)
        {
          System.out.println("It has!");
        }

        String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
        boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();

        if (withAGENTCloud && hasAgent)
        {
          return true;
        } else if (!withAGENTCloud && !hasAgent)
        {
          return true;
        } else if (!withAGENTCloud && hasAgent)
        {
          return true;
        } else
        {
          return false;
        }
      }
    }

    return false;

  }
  
  private boolean executeRecipe(String recipeID, ProductInstance prodInst)
  {
    String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
      for (Recipe auxRep : da.getListOfRecipes())
      {
        if (auxRep.getUniqueId() == null ? recipeID == null : auxRep.getUniqueId().equals(recipeID))
        {
          String invokeObjectID = auxRep.getInvokeObjectID();
          String invokeMethodID = auxRep.getInvokeMethodID();
          DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
          boolean result = false;
          //System.out.println("\n[1FIRST RECIPE]Trying to execute recipe: " + recipeID + " from prodInstance: " + prodInstID);

          if (!notAgain)
          {
            PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
            perfMeasurement.getOrderTillRecipeCallTimers().add(firstRecipeCallTime.getTime());
            firstRecipeCallTime.stop();
            notAgain = true;
          }
          //da.getSubSystem().setState(MSBConstants.ADAPTER_STATE_RUNNING);
          while (true)
          {
            if (PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).tryAcquire())
            {
              System.out.println("[SEMAPHORE][PS] ACQUIRED for " + da.getSubSystem().getName());
              DeviceAdapter da_next = getDAofNextRecipe(da, recipeID);
              //ONLY EXECUTE IF NEXT DA IS AVAILABLE
              if (PECManager.getInstance().getExecutionMap().get(da_next.getSubSystem().getUniqueId()).tryAcquire())
              {
                System.out.println("[SEMAPHORE] ACQUIRED for NEXT " + da_next.getSubSystem().getName());
                break;
              } else
              {
                PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).release();
                System.out.println("[SEMAPHORE][PS] RELEASED for " + da.getSubSystem().getName());
                try
                {
                  Thread.sleep(3000);
                } catch (InterruptedException ex)
                {
                  Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
                }
              }
            }
          }
          //System.out.println("Executing Recipe: " + recipeID + " of product instance: " + prodInst.getUniqueId() + " from the adapter:" + DA_name + " returned: " + result + "\n");
          prodInst.setStartedProductionTime(new Date());

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
            systemConfigurator.startedProduct(prodInst);
          }
          System.out.println("[EXECUTE] recipeID: " + recipeID);
          NodeId objID = Functions.convertStringToNodeId(invokeObjectID);
          NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);
          result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objID, methodID, prodInst.getUniqueId());
          //System.out.println("[FIRST RECIPE]Execute invokeSkill Successfull\n");        
          
          return result;
        }
      }
    }
    return false;
  }

  public static CompletableFuture<String> invokeMethod(OpcUaClient client, NodeId objectID, NodeId methodID)
  {
    System.out.println("\nTrying to call " + methodID + "...");
    CallMethodRequest request = new CallMethodRequest(objectID, methodID, null);

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();
      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue();
        System.out.println("\nReturning result...\n");
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        System.out.println("\nMethod error - " + methodID + "\n");
        return f;
      }
    });
  }
  
  private void checkPriority()
  {
    
  }
  
}
