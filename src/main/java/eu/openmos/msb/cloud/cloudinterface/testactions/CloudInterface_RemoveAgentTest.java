/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        
        /*if (args.length == 0)
        {
            System.out.println("Please specify the agent name to be removed");
            return;
        }*/
        
        String[] agentsToRemove = {
            "Resource_1501492847237",
            "Transport_1501493152921"   /*,
            "pd1300uniqueid_20170728152555",
            "pd1200uniqueid_20170728152555",
            "pd1100uniqueid_20170728152555" */ /* ,
            "productionDescription_1100_20170727115610",
            "productionDescription_1200_20170727115610",
            "productionDescription_1300_20170727115610",
            "productionDescription_1400_20170727115610"   */ /*
            "Resource_1501145282865"  
            "Transport_1501139692850",
            "Transport_1501139756354",
            "productionDescription_1000",
            "productionDescription_1100",
            "productionDescription_1200",
            "productionDescription_1300",
            "productionDescription_1400" */
        };
        
        for (int i = 0; i < agentsToRemove.length; i++)
        {
            // String agentToBeRemoved = "Resource_1500380953466"; //args[0];
            String agentToBeRemoved = agentsToRemove[i];
            
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
        ServiceCallStatus agentStatus = systemConfigurator.removeAgent(agentToBeRemoved);
        
                // emulation!
                // remove the created agent from the agents list
                // so that the msb emulator stops sending messages
        String agentsListFile = ConfigurationLoader.getMandatoryProperty("openmos.msb.agents.list.file");
        logger.debug("agentsListFile = [" + agentsListFile + "]");                    
        String agentsList = new String();
                try {
                    agentsList = new String(Files.readAllBytes(Paths.get(agentsListFile)));
                } catch (IOException ex) {
                    logger.error("cant read file " + agentsListFile + ": " + ex.getMessage());                    
                }
                if (agentsList.indexOf(agentToBeRemoved) != -1) // for sure
                {
                    agentsList = agentsList.replaceAll(agentToBeRemoved.concat("___"), "");
            try {                    
                Files.write(Paths.get(agentsListFile), agentsList.getBytes());
            } catch (IOException ex) {
                    logger.error("cant write file " + agentsListFile + ": " + ex.getMessage());                    
            }
                }        
                else
                {
                    logger.warn("agent " + agentToBeRemoved + " not found into file " + agentsListFile + ": WHY????");                    
                }
        
        
        logger.info(agentStatus.getCode());
        logger.info(agentStatus.getDescription());
        }

        logger.info("New Agent remove Test main end");
    }
            
    
}
