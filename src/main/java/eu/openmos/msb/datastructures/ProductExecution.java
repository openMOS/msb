/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

  @Override
  public void run()
  {
    CheckState();
  }

  public void CheckState()
  {
    PECManager aux = PECManager.getInstance();
    if (aux.getState())
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
    firstRecipeCallTime.start();
    
    PECManager ProdManager = PECManager.getInstance();

    //Get priority and execute the higher value order
    int HighPriority = -1;

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
    
    System.out.println("number of product instances to be done: "+ProdManager.getProductsToDo().size());
    
    while (ProdManager.getProductsToDo().size() > 0)
    {
      /*try
      {
        Thread.sleep(5000);
      } catch (InterruptedException ex)
      {
        Logger.getLogger(ProductExecution.class.getName()).log(Level.SEVERE, null, ex);
      }*/
      ProductInstance auxProdInstance = ProdManager.getProductsToDo().peek();
      System.out.println(auxProdInstance.getUniqueId()); //da instancia
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
                  if (checkRecipeAvailable(recipeID))
                  {
                    if (executeRecipe(recipeID,auxProdInstance.getUniqueId()))
                    {
                      System.out.println("The execution of Recipe: " + recipeID + " Returned true");
                      ProdManager.getProductsDoing().put(auxProdInstance.getProductId(), ProdManager.getProductsToDo().poll()); //the first recipe of the product is done, put it into "doing"
                      getOut = true;
                      break;
                    }else{
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
      //break;//MARTELO -> só fazer uma vez a primeira instancia do produto MASMEC 25-10-17
    }
    //acabou a order, começar a proxima
    ProdManager.getOrderInstanceList().remove(HighOrderIndex); //remove the orderInstance that finished
    ProdManager.setState(false); //true=running false=ready
    CheckState(); //do the next orderistance
  }

  private boolean checkRecipeAvailable(String recipeID)
  {

    boolean recipeIdIsValid = DatabaseInteraction.getInstance().getRecipeIdIsValid(recipeID);
    if (recipeIdIsValid)
    {
      String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(recipeID);
      if (Daid != null)
      {
        String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
        if (da.getSubSystem().getState().equals(MSBConstants.ADAPTER_STATE_READY))
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean executeRecipe(String recipeID, String prodInstID)
  {
    String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(recipeID);
    if (Daid != null)
    {
      String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
      for (Recipe auxRep : da.getListOfRecipes())
      {
        if (auxRep.getUniqueId() == null ? recipeID == null : auxRep.getUniqueId().equals(recipeID))
        {
          String invokeObjectID = auxRep.getInvokeObjectID();
          String invokeMethodID = auxRep.getInvokeMethodID();
          DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
          boolean result=false;
          /*String result = invokeMethod(daOPC.getClient().getClientObject(),
          convertStringToNodeId(invokeObjectID),
          convertStringToNodeId(invokeMethodID)).get();*/
          System.out.println("\nTrying to execute recipe: " + recipeID + " from prodInstance: " + prodInstID);
          
          PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
          perfMeasurement.getOrderTillRecipeCallTimers().add(firstRecipeCallTime.getTime());
          firstRecipeCallTime.stop();
          
          result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), convertStringToNodeId(invokeObjectID), convertStringToNodeId(invokeMethodID), prodInstID);
          System.out.println("Execute invokeSkill Successfull");
          da.getSubSystem().setState(MSBConstants.ADAPTER_STATE_RUNNING);
          System.out.println("Executing Recipe: " + recipeID + " of product instance: "+ prodInstID + " from the adapter:"+DA_name+" returned: " + result);
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

  private static NodeId convertStringToNodeId(String toConvert)
  {

    int ns = Integer.parseInt(toConvert.split(":")[0]);
    //int ns = Integer.parseInt(toConvert);
    String aux = toConvert.substring(toConvert.indexOf(":")+1);
    return new NodeId(ns, aux);
  }
}
