package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillTest;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
* WP4 Cloud Platform Re-work related code.
* 
* @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
* @author Valerio Gentile <valerio.gentile@we-plus.eu>
*/
public class CloudInterface_GetExecutionTableBySubSystemIdTest {
    private static final Logger logger = Logger.getLogger(CloudInterface_GetExecutionTableBySubSystemIdTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("CloudInterface_GetExecutionTableBySubSystemIdTest Test main start");
        
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
	SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");           
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

        ExecutionTable executionTable = systemConfigurator.getExecutionTableBySubSystemId("2b30d0eb-5cc7-4e26-864e-8748a9ce487f");
        logger.debug("executionTable detail = " + executionTable);
                  
        logger.info("CloudInterface_GetExecutionTableBySubSystemIdTest Test main end");
    }
}
