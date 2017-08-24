/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
// import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.SubSystem;
import eu.openmos.model.testdata.SubSystemTest;
// import eu.openmos.model.testdata.CyberPhysicalAgentDescriptionTest;
import io.vertx.core.Vertx;
import java.util.Calendar;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class CloudInterface_NewTransportAgentTest {
    
    private static final Logger logger = Logger.getLogger(CloudInterface_NewTransportAgentTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New Transport Agent Test main start");
        
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
	SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        /////////////////////////////
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");   
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
        //////////////////////////////
        
        String myAgentId = "Transport_" + Calendar.getInstance().getTimeInMillis();
//        CyberPhysicalAgentDescription cpad = CyberPhysicalAgentDescriptionTest.getTestObject();
        SubSystem cpad = SubSystemTest.getTestObject();
        cpad.setType(Constants.DF_TRANSPORT);
//        cpad.setEquipmentId(myAgentId);
        cpad.setName(myAgentId);

        ServiceCallStatus agentStatus = systemConfigurator.createNewTransportAgent(cpad);
        
        logger.info(agentStatus.getCode());
        logger.info(agentStatus.getDescription());
        
        // Vertx.vertx().deployVerticle(new WebSocketsSender(myAgentId));
        
        logger.info("New Transport Agent Test main end");
    }
}
