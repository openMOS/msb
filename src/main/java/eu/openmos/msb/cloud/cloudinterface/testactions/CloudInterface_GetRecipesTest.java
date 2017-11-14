package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillTest;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;
import eu.openmos.model.Skill;
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
public class CloudInterface_GetRecipesTest {
    private static final Logger logger = Logger.getLogger(CloudInterface_GetRecipesTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New getRecipes Test main start");
        
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
	SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");           
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

        List<Recipe> recipes = systemConfigurator.getRecipes();
        logger.debug("recipes list = " + recipes);
        
        //////////////////////////////////////
        
        logger.debug("going to save recipes list = " + recipes);
        ServiceCallStatus putRecipes = systemConfigurator.putRecipes(recipes);
        logger.debug("just saved recipes list with result = " + putRecipes);        
          
        logger.info("New Skill Test main end");
    }
}
