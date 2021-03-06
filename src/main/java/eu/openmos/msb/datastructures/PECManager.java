/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.Order;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Introsys
 */
public class PECManager
{
  // Singleton specific objects
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static final Object lock = new Object();
  private static volatile PECManager instance = null;
  private final List<Product> availableProducts;
  private final List<Order> orders;
  private final List<OrderInstance> orderInstances;
  private final HashMap<String, Queue> orderMap;
  private final List<ProductInstance> productsToDo;
  private final HashMap<String, ProductInstance> productsDoing;
  private final HashMap<String, Semaphore> executionMap;
  private final HashMap<String, Semaphore> interruptMap; //catch execution problems and avoid inop states e.g. rebrowse DA during execution
  private final HashMap<String, HashMap<String, String>> product_sr_tracking;   //HashMap<Prod_Inst_ID, HashMap<SR_ID, WS_ID>> 
  private final Semaphore addNewInstance;

  private static boolean state;
  //private final HashMap<>

  public PECManager()
  {
    state = false;
    availableProducts = new ArrayList<>();
    orders = new ArrayList<>();
    orderMap = new HashMap<>();
    orderInstances = new ArrayList<>();
    productsToDo = new ArrayList<>();
    productsDoing = new HashMap<>();
    executionMap = new HashMap<>();
    interruptMap = new HashMap<>();
    addNewInstance = new Semaphore(1);
    product_sr_tracking = new HashMap<>();
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

  public List<ProductInstance> getProductsToDo()
  {
    PECManager aux = PECManager.getInstance();
    return aux.productsToDo;
  }

  public Semaphore getNewInstanceSemaphore()
  {
    return addNewInstance;
  }

  /**
   * @return the product_sr_tracking
   */
  public HashMap<String, HashMap<String, String>> getProduct_sr_tracking()
  {
    return product_sr_tracking;
  }

  public Product getProductByID(String prod_type_id)
  {
    List<Product> availableProducts = PECManager.getInstance().getAvailableProducts();
    for (Product auxProduct : availableProducts)
    {
      if (auxProduct.getUniqueId().equals(prod_type_id))
      {
        return auxProduct;
      }
    }
    return null;
  }

  public String getRecipeIDbyTrackPI(SkillRequirement sr, String prod_inst_id, String recipe_id)
  {
    if (sr != null)
    {
      //System.out.println("[getRecipeIDbyTrackPI] " + sr + " -- " + prod_inst_id + " -- " + recipe_id);
      HashMap<String, String> temp = PECManager.getInstance().getProduct_sr_tracking().get(prod_inst_id);
      if (temp != null)
      {
        String temp_da_id = temp.get(sr.getUniqueId());
        if (temp_da_id != null)
        {
          for (String temp_recipe_id : sr.getRecipeIDs())
          {
            String aml_da_id = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(temp_recipe_id);
            if (aml_da_id != null && aml_da_id.equals(temp_recipe_id))
            {
              //lock_SR_to_WS(aml_da_id, sr.getUniqueId(), prod_inst_id);
              return temp_recipe_id;
            }
          }
        }
      }
      //String aml_da_id = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(recipe_id);
      //lock_SR_to_WS(aml_da_id, sr.getUniqueId(), prod_inst_id);
    }
    return recipe_id;
  }

  public void lock_SR_to_WS(String da_id, String sr_id, String prod_inst_id)
  {
    HashMap<String, String> temp = PECManager.getInstance().getProduct_sr_tracking().get(prod_inst_id);
    if (temp == null)
    {
      temp = new HashMap<>();
    }
    temp.put(sr_id, da_id);
    PECManager.getInstance().getProduct_sr_tracking().put(prod_inst_id, temp);
    logger.debug("[lock_SR_to_WS] prod_inst_id: " + prod_inst_id + " --- sr_id: " + sr_id + " --- da_id: " + da_id);

  }

  public boolean need_to_get_da(String da_id, String sr_id, String prod_inst_id)
  {
    HashMap<String, String> temp = PECManager.getInstance().getProduct_sr_tracking().get(prod_inst_id);
    if (temp == null)
    {
      return true;
    }
    else
    {
      String da = temp.get(sr_id);
      if (da == null)
      {
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @param da
   * @param last_recipeID
   * @param productInst_id
   * @param productType_id
   * @param last_sr_id
   * @return true if the adapter is at ready state
   */
  public DeviceAdapter getDAofNextRecipe(DeviceAdapter da, String last_recipeID, String productInst_id, String productType_id, String last_sr_id)
  {
    String nextRecipeID;
    String prodID = productInst_id;
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow auxRow : da.getExecutionTable().getRows())
      {
        if (auxRow.getRecipeId() != null && auxRow.getProductId() != null
                && auxRow.getRecipeId().equals(last_recipeID) && auxRow.getProductId().equals(prodID))
        {
          nextRecipeID = auxRow.getNextRecipeId();
          if (MSBConstants.MSB_OPTIMIZER)
          {
            System.out.println("[getDAofNextRecipe] -- last_sr = " + last_sr_id + " -- nextRecipeID = " + nextRecipeID);
            if (nextRecipeID == null)
            {
              return null;
            }
            SkillRequirement sr_next = getNextSR(last_sr_id, productType_id, nextRecipeID);
            if (sr_next != null)
            {
              System.out.println("[getDAofNextRecipe] -- next_sr = " + sr_next.getUniqueId());
            }
            nextRecipeID = PECManager.getInstance().getRecipeIDbyTrackPI(sr_next, productInst_id, nextRecipeID);
          }
          String Daid_next = DatabaseInteraction.getInstance().getDA_AML_IDbyRecipeID(nextRecipeID);
          if (Daid_next != null)
          {
            DeviceAdapter da_next = DACManager.getInstance().getDeviceAdapterbyAML_ID(Daid_next);
            return da_next;
          }
          break;
        }
      }
      //no prodInst found in execTable, search for productType now
      prodID = productType_id;
    }
    return null;
  }

  public String getNextRecipe(DeviceAdapter da, String recipeID, String productInst_id, String productType_id, String sr_id)
  {
    String nextRecipeID;
    String prodID = productInst_id;
    for (int i = 0; i < 2; i++)
    {
      for (ExecutionTableRow auxRow : da.getExecutionTable().getRows())
      {
        if (auxRow.getRecipeId() != null && auxRow.getProductId() != null
                && auxRow.getRecipeId().equals(recipeID) && auxRow.getProductId().equals(prodID))
        {
          nextRecipeID = auxRow.getNextRecipeId();
          if (MSBConstants.MSB_OPTIMIZER)
          {
            SkillRequirement sr_next = getNextSR(sr_id, productType_id, nextRecipeID);
            return PECManager.getInstance().getRecipeIDbyTrackPI(sr_next, productInst_id, nextRecipeID);
          }
          else
          {
            return nextRecipeID;
          }
        }
      }
      //no prodInst found in execTable, search for productType now
      prodID = productType_id;
    }
    return null;
  }

  /**
   *
   * @param last_sr_id
   * @param prod_id
   * @return list of SRs next to last_sr_id
   */
  public List<SkillRequirement> getNextSR_list(String last_sr_id, String prod_id)
  {
    List<SkillRequirement> sr_list = new ArrayList<>();
    Product prod = PECManager.getInstance().getProductByID(prod_id);
    for (SkillRequirement sr : prod.getSkillRequirements())
    {
      if (sr.getPrecedents() != null)
      {
        for (SkillReqPrecedent srp : sr.getPrecedents())
        {
          if (srp.getUniqueId().equals(last_sr_id))
          {
            sr_list.add(sr);
          }
        }
      }
    }
    return sr_list;
  }

  public SkillRequirement getNextSR(String last_sr_id, String prod_id, String next_recipe_id)
  {
    for (SkillRequirement temp_sr : getNextSR_list(last_sr_id, prod_id))
    {
      if (temp_sr.getRecipeIDs().contains(next_recipe_id))
      {
        return temp_sr;
      }
    }
    return null;
  }

}
