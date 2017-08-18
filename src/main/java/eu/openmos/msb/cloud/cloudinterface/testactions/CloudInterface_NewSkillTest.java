package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.cloudinterface.ServiceCallStatus;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
// import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.msb.cloud.cloudinterface.testdata.RecipeTest;
import eu.openmos.msb.cloud.cloudinterface.testdata.SkillTest;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
import eu.openmos.model.Skill;

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
public class CloudInterface_NewSkillTest
{

  private static final Logger logger = Logger.getLogger(CloudInterface_NewResourceAgentTest.class.getName());

  public static void main(String[] args)
  {
    logger.info("New Skill Test main start");

    SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
    SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
    String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
    logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");
    BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

    Skill s = SkillTest.getTestObject();
    ServiceCallStatus skillStatus = systemConfigurator.createNewSkill(s);

    logger.info("New Skill Test main end");
  }
}
