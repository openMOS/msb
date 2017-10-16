/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.Order;
import eu.openmos.model.OrderLine;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

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
    throw new UnsupportedOperationException("Not supported yet. ProductExecution Thread"); //To change body of generated methods, choose Tools | Templates.
    
  }
  
    public void CheckState() {
        PECManager aux = PECManager.getInstance();
        if (aux.getState()) {
            System.out.println("Executor already Running");
            
        } else {
            System.out.println("Executor started");
            ExecuteOrder();
        }
    }
    
    public void ExecuteOrder() {
        int HighPriority = -1;
        String HighOrderId = "";
        PECManager aux = PECManager.getInstance();
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
        
    }
    
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

}
