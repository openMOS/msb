/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.recipesmanagement;

import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.msb.starter.MSB_MiloClientSubscription;
import eu.openmos.msb.starter.MyHashMaps;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.apache.log4j.Logger;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@WebService(endpointInterface = "eu.openmos.msb.recipesmanagement.RecipesDeployer", serviceName = "RecipesDeployer")
public class RecipesDeployerImpl implements RecipesDeployer {
    public static int SUGGESTION = 1;
    public static int ACTUAL = 2;
    public static int ACTIVATION = 3;    
    private static final Logger logger = Logger.getLogger(RecipesDeployerImpl.class.getName());

    @Override
    public boolean sendRecipes(String deviceName, int mode, List<Recipe> recipes) {
        
        logger.debug("device name = [" + deviceName + "]");
        
        logger.debug("mode = [" + mode + "]");
        if (mode == RecipesDeployerImpl.SUGGESTION){
            logger.debug("suggested recipes mode");
            System.out.println("suggested recipes mode");
        }else if (mode == RecipesDeployerImpl.ACTUAL){
            logger.debug("actual recipes mode");
            System.out.println("actual recipes mode");
        }else if (RecipesDeployerImpl.ACTIVATION == mode){
            logger.debug("activation of already sent recipes mode");
            System.out.println("activation of already sent recipes mode");
        }else{
            logger.debug("unknown mode");
            System.out.println("unknown mode");
        }
        
        logger.debug("recipes list = [" + recipes + "]");
        System.out.println("recipes list = [" + recipes + "]");
        
        
         //send it to the device
        Map<String, MSB_MiloClientSubscription> OPCclientMapper;
        MyHashMaps mytest = MyHashMaps.getInstance();
        OPCclientMapper = mytest.getOPCclientIDMaps();
        
        MSB_MiloClientSubscription msbClientSub= null;
        msbClientSub = OPCclientMapper.get(deviceName);
        System.out.println("device name: "+deviceName);
        
        System.out.println(" TAMANHO ->hashmap WEB: "+OPCclientMapper.size());
        for (String key : OPCclientMapper.keySet()) {
            System.out.println(key + " ->hashmap WEB<- - " + OPCclientMapper.get(key));
        }
        
        String Recipes="dummyRecipe and mode"; //TODO parse the received .aml
        msbClientSub.SendRecipetoDevice(msbClientSub.getClientObject(), Recipes); //get the return code and check it
        
        
        return true;
    }
    
}
