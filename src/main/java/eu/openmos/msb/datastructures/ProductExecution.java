/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.Order;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.OrderLine;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import java.util.ArrayList;
import java.util.HashMap;
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

  @Override
  public void run()
  {
    CheckState();
    
  }
  
    public void CheckState() {
        PECManager aux = PECManager.getInstance();
        if (aux.getState()) {
            System.out.println("Executor already Running!");
            
        } else {
            System.out.println("Starting Executor!");
            ExecuteOrder();
        }
    }
    
    public void ExecuteOrder() {
        
        PECManager aux = PECManager.getInstance();

        //OLD
        /*
        int HighPriority = -1;
        String HighOrderId = "";
      List<Order> orderList = aux.getOrderList();
      //List<OrderLine> orderLines;
      for (int i = 0; i < orderList.size(); i++) {
      int priority = orderList.get(i).getPriority();
      if (priority > HighPriority) {
      HighPriority = priority;
      HighOrderId = orderList.get(i).getUniqueId();
      }
      }
      if (!HighOrderId.isEmpty()) {
      //orderLines = orderList.get(HighOrderIdx).getOrderLines();
      HashMap<String,Queue> queueList = aux.getOrderMap();
      Queue orderLineQueue = queueList.get(HighOrderId);
      HashMap<String, ProductInstance> polled= (HashMap<String, ProductInstance>) orderLineQueue.poll();
      //add to pec manager doing
      ExecuteOrderLine(polled);
      }
         */
        
        int HighPriority = -1;
        int HighOrderIndex = -1;
        List<OrderInstance> orderInstanceList = aux.getOrderInstanceList();
        for (int i = 0; i < orderInstanceList.size(); i++) {
            int priority = orderInstanceList.get(i).getPriority();
            if (priority > HighPriority) {
                HighPriority = priority;
                HighOrderIndex = i;
            }
        }
        if (HighOrderIndex != -1) {
            //orderLines = orderList.get(HighOrderIdx).getOrderLines();
            OrderInstance orderInstanceToExecute = aux.getOrderInstanceList().get(HighOrderIndex);
            aux.getProductsToDo().addAll(orderInstanceToExecute.getProductInstances());
            
            //add to pec manager doing
            ExecuteProdsInstance();
        }
    }

    /*
    public void ExecuteOrderLine(HashMap<String, ProductInstance> orderLine) {
        boolean finished=false;
        
        for (String key : orderLine.keySet()) {
            System.out.println(key);
            String productId = orderLine.get(key).getProductId();
            //ir as tabelas de execução
            PECManager aux = PECManager.getInstance();
            List<Product> availableProducts = aux.getAvailableProducts();
            while(!finished){
                
            }
        }
    }

    */
    public void ExecuteProdsInstance() {
        boolean finished=false;
        
        PECManager aux = PECManager.getInstance();
        Queue<ProductInstance> prodList = aux.getProductsToDo();
        while(prodList.size() > 0){
            ProductInstance auxProdInstance = prodList.peek();
            System.out.println(auxProdInstance.getUniqueId());
            String productId = auxProdInstance.getProductId();
            //ir as tabelas de execução
            
            List<Product> availableProducts = aux.getAvailableProducts();
            for (Product auxProduct : availableProducts){
                if (auxProduct.getUniqueId() == productId){
                    //analisar SkillRequirements
                    List<SkillRequirement> skillRequirements = auxProduct.getSkillRequirements();
                    for (SkillRequirement auxSR : skillRequirements){
                        if (auxSR.getPrecedents() == null){
                            //check for 1 available recipe for the SR
                            for (String recipeID : auxSR.getRecipeIDs()){
                                if (checkRecipeAvailable(recipeID)){
                                    executeRecipe(recipeID);
                                }
                            }
                        }
                    }
                }
            }
            while(!finished){
                
            }
        
        }
    }
    
    private boolean checkRecipeAvailable(String recipeID) {

        boolean recipeIdIsValid = DatabaseInteraction.getInstance().getRecipeIdIsValid(recipeID);
        if (recipeIdIsValid) {
            String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(recipeID);
            if (Daid != null) {
                String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
                DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
                if (da.getSubSystem().getState() == MSBConstants.ADAPTER_STATE_READY) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean executeRecipe(String recipeID) {
        try {
            String Daid = DatabaseInteraction.getInstance().getDAIDbyRecipeID(recipeID);
            if (Daid != null) {
                String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByID(Daid);
                DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(DA_name);
                for (Recipe auxRep : da.getListOfRecipes()) {
                    if (auxRep.getUniqueId() == recipeID) {
                        String invokeObjectID = auxRep.getInvokeObjectID();
                        String invokeMethodID = auxRep.getInvokeMethodID();
                        DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
                        String result = invokeMethod(daOPC.getClient().getClientObject(),
                                                    convertStringToNodeId(invokeObjectID),
                                                    convertStringToNodeId(invokeMethodID)).get();
                    }
                }
            }
        } catch (InterruptedException | ExecutionException ex) {

        }
        return false;
    }

    public static CompletableFuture<String> invokeMethod(OpcUaClient client, NodeId objectID, NodeId methodID)
    {
        System.out.println("\nTrying to call " + methodID + "...");
        CallMethodRequest request = new CallMethodRequest(objectID, methodID, null);

        return client.call(request).thenCompose(result
                -> {
            StatusCode statusCode = result.getStatusCode();
            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue();
                System.out.println("\nReturning result...\n");
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                System.out.println("\nMethod error - " + methodID + "\n");
                return f;
            }
        });
    }

    private static NodeId convertStringToNodeId(String toConvert) {

        int ns = Integer.parseInt(toConvert);
        String aux = toConvert.substring(toConvert.indexOf(":"));
        return new NodeId(ns, aux);
    }
}
