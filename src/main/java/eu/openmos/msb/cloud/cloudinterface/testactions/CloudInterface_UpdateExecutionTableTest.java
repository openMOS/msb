package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
//import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.SubSystem;
import eu.openmos.model.testdata.ExecutionTableTest;
import eu.openmos.model.testdata.SubSystemTest;
//import eu.openmos.model.testdata.CyberPhysicalAgentDescriptionTest;
import io.vertx.core.Vertx;
import java.util.Calendar;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_UpdateExecutionTableTest {
    
    private static final Logger logger = Logger.getLogger(CloudInterface_UpdateExecutionTableTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New Resource Agent Test main start");
        
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
	SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        /////////////////////////////
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
        //////////////////////////////

        logger.debug("before calling update execution table");
        
        ExecutionTable et = ExecutionTableTest.getTestObject();
        et.setDescription("updated exec table");
        ServiceCallStatus agentStatus = systemConfigurator.updateExecutionTable("Resource_1", et);
        
        logger.info("return code on MSB side: " + agentStatus.getCode());
        logger.info("description on MSB side: " + agentStatus.getDescription());

        // Vertx.vertx().deployVerticle(new WebSocketsSender(myAgentId));
        
        logger.info("update execution table Test main end");
    }
}
