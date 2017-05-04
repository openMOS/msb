/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloudinterface;

import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.agentcloud.cloudinterface.OrderStatus;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.Order;
import eu.openmos.agentcloud.data.ProductDescription;
import java.util.LinkedList;
import java.util.List;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_NewOrderTest {
    
    private static final Logger logger = Logger.getLogger(CloudInterface_NewOrderTest.class.getName());
    
    public static void main(String[] args) {
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

        Order newOrder = getTestObject();
                
        OrderStatus orderStatus = systemConfigurator.acceptNewOrder(newOrder);
        logger.info(orderStatus.getCode());
        logger.info(orderStatus.getDescription());

        logger.info("New Order Test main end");
    }
            

    public static Order getTestObject()
    {
        Order o = new Order();
        
        List<ProductDescription> lpd = new LinkedList();
        for (int y = 1000; y < 2000; y+=100)
        {
            ProductDescription pd1 = new ProductDescription();
            String pdName = "productionDescription_" + y;
            pd1.setName(pdName);

            List<SkillRequirement> lsr = new LinkedList();
            for (int i = 0; i < 10; i++)
            {
                SkillRequirement sr = new SkillRequirement();
                sr.setDescription(pdName + "_skillRequirementDescription_" + i);
                sr.setUniqueId(pdName + "_skillRequirementUniqueId_" + i);
                sr.setName(pdName + "_skillRequirementName_" + i);
                sr.setType(y + i);

                // pd1.getSkillRequirements().add(sr);
                lsr.add(sr);
            }
                pd1.setSkillRequirements(lsr);
            
            // o.getProductDescriptions().add(pd1);
            lpd.add(pd1);
        }        
            o.setProductDescriptions(lpd);
        
        return o;
    }
}
