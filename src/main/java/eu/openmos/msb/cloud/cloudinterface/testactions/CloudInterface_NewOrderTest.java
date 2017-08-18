/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.cloudinterface.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.Order;
import eu.openmos.msb.cloud.cloudinterface.testdata.OrderTest;
import io.vertx.core.Vertx;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_NewOrderTest
{

  private static final Logger logger = Logger.getLogger(CloudInterface_NewOrderTest.class.getName());

  public static void main(String[] args)
  {
    logger.info("New Order Test main start");

    SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
    SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();

/////////////////////////////
    String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
    logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");

    BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
    bindingProvider.getRequestContext().put(
            // BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://192.168.15.5:9999/wsSystemConfigurator");
            BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
//////////////////////////////

//        Order newOrder = getTestObject();
    Order newOrder = OrderTest.getTestObject();

    ServiceCallStatus orderStatus = systemConfigurator.acceptNewOrder(newOrder);
    logger.info(orderStatus.getCode());
    logger.info(orderStatus.getDescription());

    // since july 2017, aka wp4 several semantic model alignment meetings,
    // also product agents have socket communication
//        for (ProductDescription productDescription : newOrder.getProductDescriptions())
//                Vertx.vertx().deployVerticle(new WebSocketsSender(productDescription.getUniqueId()));
    logger.info("New Order Test main end");
  }

//    public static Order getTestObject()
//    {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String now = sdf.format(new Date());
//
//            Order o = new Order();
//        o.setUniqueId("order_unique_id_" + now);
//        o.setDescription("my description_" + now);
//        o.setName("order_name_" + now);
//        o.setPriority(1);
//        List<ProductDescription> lpd = new LinkedList();
//        for (int y = 1000; y < 1500; y+=100)
//        {
//            ProductDescription pd1 = new ProductDescription();
//
//            String pdName = "productionDescription_" + y + "_" + now;
//
//            List<SkillRequirement> lsr = new LinkedList();
//            for (int i = 0; i < 10; i++)
//            {
//                SkillRequirement sr = new SkillRequirement();
//                sr.setDescription(pdName + "_skillRequirementDescription_" + i);
//                sr.setUniqueId(pdName + "_skillRequirementUniqueId_" + i);
//                sr.setName(pdName + "_skillRequirementName_" + i);
//                sr.setType("weld");
//                sr.setPrecedenceIds(new LinkedList<String>());
//                sr.setRegisteredTimestamp(new Date());
//
//                // pd1.getSkillRequirements().add(sr);
//                lsr.add(sr);
//            }
//            List<Component> comps = new LinkedList();
//            Component c1 = new Component("uniqueCpID", "CpName", "CpDescription", new Date());
//            comps.add(c1);
//            pd1.setComponents(comps);
//            pd1.setDescription("product description");
//            pd1.setModelId("model_xpto");
//            pd1.setName(pdName);
//            pd1.setOrderId(o.getUniqueId());
//            pd1.setRegisteredTimestamp(new Date());
//            pd1.setSkillRequirements(lsr);
//            
//            pd1.setUniqueId("pd" + y + "uniqueid_" + now.toString());
//                
//            // o.getProductDescriptions().add(pd1);
//            lpd.add(pd1);
//        }        
//        o.setProductDescriptions(lpd);
//        o.setRegisteredTimestamp(new Date());
//        return o;
//    }
}
