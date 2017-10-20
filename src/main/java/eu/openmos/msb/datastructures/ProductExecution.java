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
      System.out.println("Starting Executor!");
      ExecuteOrder();
    }
  }

  public void ExecuteOrder()
  {
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

      //add to pec manager doing
      ExecuteProdsInstance();
    }
  }

  public void ExecuteProdsInstance()
  {
    PECManager ProdManager = PECManager.getInstance();
    
    while (ProdManager.getProductsToDo().size() > 0)
    {
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
                  System.out.println("\n Trying to check if recipe is VALID ***** " + recipeID + " *****\n");
                  if (checkRecipeAvailable(recipeID))
                  {
                    if (executeRecipe(recipeID,auxProdInstance.getUniqueId()))
                    {
                      ProdManager.getProductsDoing().put(auxProdInstance.getProductId(), ProdManager.getProductsToDo().poll()); //the first recipe of the product is done, put it into "doing"
                      getOut = true;
                      break;
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
          /*String result = invokeMethod(daOPC.getClient().getClientObject(),
          convertStringToNodeId(invokeObjectID),
          convertStringToNodeId(invokeMethodID)).get();*/
          daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), convertStringToNodeId(invokeObjectID), convertStringToNodeId(invokeMethodID), prodInstID);
          da.getSubSystem().setState(MSBConstants.ADAPTER_STATE_RUNNING);
          return true;
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

    int ns = Integer.parseInt(toConvert);
    String aux = toConvert.substring(toConvert.indexOf(":"));
    return new NodeId(ns, aux);
  }
}
