/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.soap;

import eu.openmos.agentcloud.data.recipe.Recipe;
import java.util.List;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService(endpointInterface = "eu.openmos.msb.services.RecipesDeployment", serviceName = "RecipesDeployment")
public class RecipesDeploymentImpl implements RecipesDeployment {
    public static int SUGGESTION = 1;
    public static int ACTUAL = 2;
    public static int ACTIVATION = 3;    
    private static final Logger logger = Logger.getLogger(RecipesDeploymentImpl.class.getName());

    @Override
    public boolean sendRecipes(String deviceName, int mode, List<Recipe> recipes) {
        logger.debug("device name = [" + deviceName + "]");
        
        logger.debug("mode = [" + mode + "]");
        if (mode == RecipesDeploymentImpl.SUGGESTION)
            logger.debug("suggested recipes mode");
        else if (mode == RecipesDeploymentImpl.ACTUAL)
            logger.debug("actual recipes mode");
        else if (RecipesDeploymentImpl.ACTIVATION == mode)
            logger.debug("activation of already sent recipes mode");
        else
            logger.debug("unknown mode");
        
        logger.debug("recipes list = [" + recipes + "]");
        
        return true;
    }
    
}
