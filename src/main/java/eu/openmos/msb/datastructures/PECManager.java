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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

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
  private final HashMap<String, Semaphore> executionMap;

  private static boolean state;
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
    executionMap = new HashMap<>();
  }

  public HashMap<String, ProductInstance> getProductsDoing()
  {
    return productsDoing;
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
  
}
