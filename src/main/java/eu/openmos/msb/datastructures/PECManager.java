/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.Order;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.utilities.Functions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author Introsys
 */
public class PECManager
{

  // Singleton specific objects
  private static final Object lock = new Object();
  private static volatile PECManager instance = null;
  private final List<Product> availableProducts;
  private final List<Order> orders;
  private final List<OrderInstance> orderInstances;
  private final HashMap<String, Queue> orderMap;
  private final Queue<ProductInstance> productsToDo;
  private final HashMap<String, ProductInstance> productsDoing;
  private final HashMap<String, List<PendingProdInstance>> pendejos;
  private final HashMap<String, Semaphore> executionMap;

  private boolean state;
  //private final HashMap<>

  public PECManager()
  {
    this.state = false;
    availableProducts = new ArrayList<>();
    orders = new ArrayList<>();
    orderMap = new HashMap<>();
    orderInstances = new ArrayList<>();
    productsToDo = new LinkedList<>();
    productsDoing = new HashMap<>();
    pendejos = new HashMap<>();
    executionMap = new HashMap<>();
  }

  public HashMap<String, ProductInstance> getProductsDoing()
  {
    return productsDoing;
  }

  public HashMap<String, List<PendingProdInstance>> getPendejos()
  {
    return pendejos;
  }

  /**
   * @brief obtain the Product Execution Client Manager unique instance
   * @return
   */
  public static PECManager getInstance()
  {
    PECManager i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new PECManager();
          instance = i;
        }
      }
    }
    return i;
  }

  public void addNewProductToExecution()
  {

  }

  public List<Product> getProductList()
  {
    PECManager aux = PECManager.getInstance();
    return aux.availableProducts;
  }

  public List<Order> getOrderList()
  {
    PECManager aux = PECManager.getInstance();
    return aux.orders;
  }

  public List<OrderInstance> getOrderInstanceList()
  {
    PECManager aux = PECManager.getInstance();
    return aux.orderInstances;
  }

  public void setState(boolean state)
  {
    PECManager aux = PECManager.getInstance();
    aux.state = state;
  }

  public boolean getState()
  {
    PECManager aux = PECManager.getInstance();
    return aux.state;
  }

  public boolean removeOrder(String uID)
  {
    PECManager aux = PECManager.getInstance();
    for (int i = 0; i < aux.orders.size(); i++)
    {
      if (aux.orders.get(i).getUniqueId() == null ? uID == null : aux.orders.get(i).getUniqueId().equals(uID))
      {
        aux.orders.remove(i);
        return true;
      }
    }
    return false;
  }

  //get list of queue for all orders
  public HashMap<String, Queue> getOrderMap()
  {
    PECManager aux = PECManager.getInstance();
    return aux.orderMap;
  }
  
  public HashMap<String, Semaphore> getExecutionMap()
  {
    PECManager aux = PECManager.getInstance();
    return aux.executionMap;
  }
  
  public String getProductNameByID(String productUUID)
  {

    PECManager pec = PECManager.getInstance();
    List<Product> productList = pec.getProductList();
    for (int prodIDX = 0; prodIDX < productList.size(); prodIDX++)
    {
      if (productUUID.equals(productList.get(prodIDX).getUniqueId()))
      {
        return productList.get(prodIDX).getName();
      }
    }

    return "";

  }

  public List<Product> getAvailableProducts()
  {
    PECManager aux = PECManager.getInstance();
    return aux.availableProducts;
  }

  public Queue<ProductInstance> getProductsToDo()
  {
    PECManager aux = PECManager.getInstance();
    return aux.productsToDo;
  }

  public String PendejoCheckerThread(String daID) throws InterruptedException, ExecutionException
  {

    Thread th = new Thread()
    {
      public synchronized void run()
      {
        PendejoChecker(daID);
      }
    };
    th.start();
    return "OK";
  }
  
  public void PendejoChecker(String DaID){
    
    List<PendingProdInstance> prodInst = this.pendejos.get(DaID);
    if (prodInst != null && prodInst.size() > 0)
    {
      System.out.println("[PendejoChecker] Adapter: " + DaID + " is ready and with pending product instances todo");
      //EXECUTOR AGAIN
      PendingProdInstance prodInstToDo = prodInst.get(0);
      DeviceAdapter deviceAdapter = DACManager.getInstance().getDeviceAdapterbyName(DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(DaID));
      DeviceAdapterOPC client = (DeviceAdapterOPC) deviceAdapter;
      
      String method = DatabaseInteraction.getInstance().getRecipeMethodByID(prodInstToDo.getNextRecipeID());
      NodeId methodNode = Functions.convertStringToNodeId(method);
      String obj = DatabaseInteraction.getInstance().getRecipeObjectByID(prodInstToDo.getNextRecipeID());
      NodeId objNode = Functions.convertStringToNodeId(obj);
              
      boolean res = client.getClient().InvokeDeviceSkill(client.getClient().getClientObject(), objNode, methodNode, prodInstToDo.getProductInstanceID());
      if (res)
      {
        prodInst.remove(0);
        System.out.println("[EXECUTE] pendejo recipeID: " + prodInstToDo.getNextRecipeID() + "\n Remain: " + prodInst.size());
      }
      else
        System.out.println("[EXECUTE] Error on pendejo recipeID: " + prodInstToDo.getNextRecipeID() + "\n Remain: " + prodInst.size());
    } else
    {
      System.out.println("[PendejoChecker]Adapter " + DaID + " is ready! no pendejos found");
      DeviceAdapter deviceAdapter = DACManager.getInstance().getDeviceAdapterbyName(DatabaseInteraction.getInstance().getDeviceAdapterNameByAmlID(DaID));
      if (deviceAdapter != null)
      {
        //deviceAdapter.getSubSystem().setState(MSBConstants.ADAPTER_STATE_READY);
        PECManager.getInstance().getExecutionMap().get(DaID).release();
        System.out.println("[SEMAPHORE" + deviceAdapter.getSubSystem().getName() + "] RELEASED2");
        //System.out.println("[SEMAPHORE] RELEASED for " + deviceAdapter.getSubSystem().getName());
        System.out.println("[PendejoChecker]Adapter " + DaID + " state changed to ready!");
      }
      else
        System.out.println("[PendejoChecker]Adapter " + DaID + " not found in DB!");
    }
    
  }
}
