/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloudinterface;

import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.cloudinterface.AgentStatus;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_RemoveAgentTest {
    
    private static final Logger logger = Logger.getLogger(CloudInterface_RemoveAgentTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New Agent remove Test main start");
        
        if (args.length == 0)
        {
            System.out.println("Please specify the agent name to be removed");
            return;
        }
        String agentToBeRemoved = args[0];
        logger.info("Agent to be removed = [" + agentToBeRemoved + "]");
        
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
        
        // String agentToBeRemoved = "Resource_1482329568568";
        AgentStatus agentStatus = systemConfigurator.removeAgent(agentToBeRemoved);
        
        logger.info(agentStatus.getCode());
        logger.info(agentStatus.getDescription());

        logger.info("New Agent remove Test main end");
    }
            
    
}
