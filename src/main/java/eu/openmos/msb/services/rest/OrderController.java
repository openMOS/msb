package eu.openmos.msb.services.rest;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.Order;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.OrderLine;
import eu.openmos.model.Part;
import eu.openmos.model.PartInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.testdata.OrderTest;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.datastructures.ProductExecution;
import eu.openmos.msb.starter.MSB_gui;
import java.util.ArrayList;
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
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;


/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/orders")
public class OrderController {
    private final Logger logger = Logger.getLogger(OrderController.class.getName());
    private final StopWatch OrderWatch = new StopWatch();
    
    /**
     * Returns the list of orders.
     * There's no slide requesting this method, it's only for testing purpose.
     * 
     * @return list of order objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
//    public List<OrderInstance> getList()
    public List<Order> getList()
    {
        logger.debug("orders getList");
/*        
        List<OrderInstance> ls = new LinkedList<>();
        
        for (int i = 0; i < 5; i++)
        {
            OrderInstance o = OrderTest.getTestObject();
            ls.add(o);
        }
        
        return ls;
*/
        PECManager pecManager = PECManager.getInstance();
        
        return pecManager.getOrderList();
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
    public Order newOrder(Order newOrder)
    {
      OrderWatch.start();
      logger.debug("orders newOrder - order to insert = " + newOrder.toString());
   
      PECManager pec = PECManager.getInstance();
        
        //CREATE ORDERINSTANCE AND PRODUCTINSTANCE
        OrderInstance oi= new OrderInstance();
        List<ProductInstance> piList =  new ArrayList<>();
        
        for (int z = 0; z < newOrder.getOrderLines().size(); z++) { //iterate between all orderlines
            int quantity = newOrder.getOrderLines().get(z).getQuantity();
            
            MSB_gui.addToTableSubmittedOrder(newOrder.getUniqueId(), newOrder.getOrderLines().get(z).getProductId(), newOrder.getOrderLines().get(z).getUniqueId(), quantity); //add to GUI table of submitted orders

            for (int prodIDX = 0; prodIDX < quantity; prodIDX++) { //create product instances for each quantity of the orderline
                ProductInstance pi = new ProductInstance();
                pi.setDescription("my Pinstance description");
                pi.setName(pec.getProductNameByID(newOrder.getOrderLines().get(z).getProductId())); //GET THE PRODUCT NAME from the ID
                pi.setOrderId(newOrder.getUniqueId());

                //??
                List<PartInstance> comps = new LinkedList();
                Part p1 = new Part("uniqueCpID", "CpName", "CpDescription", new Date());
                PartInstance c1 = new PartInstance("uniqueCpinstanceID", "CpinstanceName", "CpinstanceDescription", p1, new Date());
                comps.add(c1);

                pi.setParts(comps);
                pi.setProductId(newOrder.getOrderLines().get(z).getProductId());
                pi.setUniqueId(UUID.randomUUID().toString()); //generate unique IDs for each product instance
                pi.setRegistered(new Date());
                
                piList.add(pi);
            } 
        }
        
        oi.setDescription(newOrder.getDescription());
        oi.setName(newOrder.getName());
        oi.setPriority(newOrder.getPriority());
        oi.setProductInstances(piList);
        oi.setRegistered(new Date());
        oi.setUniqueId(newOrder.getUniqueId());

        logger.debug("Order instance created and added to ProductManagerClass");

        pec.getOrderList().add(newOrder);
        pec.getOrderInstanceList().add(oi);
        
        //get first product instance and start doing stuff
        new Thread(new ProductExecution()).start();    
        
        /*pecManager.getOrderList().add(newOrder);
        
        List<OrderLine> orderLines = newOrder.getOrderLines();
        
        //List<ProductInstance> prods;
        
        Queue<HashMap> orderLinesQueue = new LinkedList<>();

        for (int i = 0; i < orderLines.size(); i++) {

            HashMap<String, ProductInstance> prods = new HashMap();
            String productToDO = orderLines.get(i).getProductId();
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
        new Thread(new ProductExecution()).start();*/
               
      //Forward the order from HMI to AC
      String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
      boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();

      if (withAGENTCloud)
      { //check if the agentcloud is active
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
        SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
/////////////////////////////
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");

        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
//////////////////////////////              
        ServiceCallStatus orderStatus = systemConfigurator.acceptNewOrderInstance(oi);
        logger.info("Order Instance sent to the Agent Cloud with code: " + orderStatus.getCode());
        logger.info("Order Instance status: " + orderStatus.getDescription());
      } else
      {
        logger.info("Order Instance not sent to the Agent Cloud because the cloud is deactivated");
      }

      //PERFORMANCE MEASUREMENT
      PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
      Long time=OrderWatch.getTime();
      perfMeasure.getOrderTillOrderInstanceCreationTimers().add(time);
      logger.info("Order Instance took "+time.toString()+"ms to be created from the received order");
      OrderWatch.stop();
                
        return newOrder;
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
//    public OrderInstance getDetail(@PathParam("orderId") String orderId) {
    public Order getDetail(@PathParam("orderId") String orderId) {
        logger.debug("order getDetail - orderId = " + orderId);
 //       return OrderTest.getTestObject();
        PECManager pecManager = PECManager.getInstance();       
        List<Order> lo = pecManager.getOrderList();
        for (Order o : lo)
            if (o.getUniqueId().equalsIgnoreCase(orderId))
                return o;
        return null;
   }
}