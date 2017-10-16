package eu.openmos.msb.services.rest;

import eu.openmos.model.Order;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.OrderLine;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.testdata.OrderTest;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.ProductExecution;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;


/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/orders")
public class OrderController {
    private final Logger logger = Logger.getLogger(OrderController.class.getName());
    
    /**
     * Returns the list of orders.
     * There's no slide requesting this method, it's only for testing purpose.
     * 
     * @return list of order objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderInstance> getList()
    {
        logger.debug("orders getList");
        List<OrderInstance> ls = new LinkedList<>();
        
        for (int i = 0; i < 5; i++)
        {
            OrderInstance o = OrderTest.getTestObject();
            ls.add(o);
        }
        
        return ls;
    }

     /**
     * Allows to insert a new order into the system.
     * Returns the order object.
     * 
     * There will be a view into the HMI application for order creation.
     * This method is exposed via a POST to "/orders" service call.
     * 
     * @param newOrder   the order to be inserted.
     * @return the order object. 
     * List can be empty, cannot be null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderInstance newOrder(Order newOrder)
    {
        logger.debug("orders newOrder - order to insert = " + newOrder.toString());
        
        PECManager pecManager = PECManager.getInstance();
        
        //set order on the pecManager?
        //set executedOrders
        //set executedRecipesFromProduct
        //setOrdersToExecute
        //setRecipesFromProducttoExecute
        
        pecManager.getOrderList().add(newOrder);
        
        List<OrderLine> orderLines = newOrder.getOrderLines();
        
        //List<ProductInstance> prods;
        
        Queue<HashMap> orderLinesQueue = new LinkedList<>();

        for (int i = 0; i < orderLines.size(); i++) {

            HashMap<String, ProductInstance> prods = new HashMap();
            String productToDO = orderLines.get(i).getModelId();
            int quantity = orderLines.get(i).getQuantity();

            for (int j = 0; j < quantity; j++) {

                String instanceID = UUID.randomUUID().toString();
                ProductInstance instance = new ProductInstance(instanceID, productToDO, "ProdDummy", "ProdDescDummy",
                        newOrder.getUniqueId(), orderLines.get(i).getUniqueId(), null, new Date());

                prods.put(instanceID, instance); //create an hashmap
            }

            orderLinesQueue.add(prods); //add queue of all productinstances for each orderline 

        }

        pecManager.getOrderMap().put(newOrder.getUniqueId(),orderLinesQueue); //add a queue for each order on pecmanager singleton
        
        //get first product instance and start doing stuff
        new Thread(new ProductExecution()).start();
        

        return OrderTest.getTestObject();
    }

   /**
     * Returns the order object given its unique identifier.
     * There's no slide requesting this method, it's only for testing purpose.
     * 
     * @return detail of order
     * 
     * @param orderId the unique id of the order
     * @return order object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{orderId}")
    public OrderInstance getDetail(@PathParam("orderId") String orderId) {
        logger.debug("order getDetail - orderId = " + orderId);
        return OrderTest.getTestObject();
   }
}