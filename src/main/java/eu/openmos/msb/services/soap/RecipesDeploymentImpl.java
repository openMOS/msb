package eu.openmos.msb.services.soap;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import java.util.List;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService(endpointInterface = "eu.openmos.msb.services.soap.RecipesDeployment", serviceName = "RecipesDeployment")
public class RecipesDeploymentImpl implements RecipesDeployment {
    public static int SUGGESTION = 1;
    public static int ACTUAL = 2;
    public static int ACTIVATION = 3;    
    private static final Logger logger = Logger.getLogger(RecipesDeploymentImpl.class.getName());

    @Override
    public boolean updateRecipes(String deviceName, int mode, List<Recipe> recipes) {
        logger.debug("updateRecipes MSB method");
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

    @Override
    public boolean updateExecutionTable(ExecutionTable executionTable) {
        logger.debug("updateExecutionTable MSB method");
        logger.debug("executionTable: " + executionTable);
        return true;
    }
    
}
