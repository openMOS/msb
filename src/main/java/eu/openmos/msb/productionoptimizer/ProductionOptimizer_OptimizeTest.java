/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.productionoptimizer;

import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer;
import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer_Service;
import eu.openmos.agentcloud.data.recipe.Recipe;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class ProductionOptimizer_OptimizeTest {
    
    private static final Logger logger = Logger.getLogger(ProductionOptimizer_OptimizeTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New optimize Test main start");
        
        ProductionOptimizer_Service productionOptimizerService = new ProductionOptimizer_Service();
	ProductionOptimizer productionOptimizer = productionOptimizerService.getProductionOptimizerImplPort();
        
        List<Recipe> recipes = productionOptimizer.optimize();
        
        logger.info(recipes.toString());

        logger.info("New optimize Test main end");
    }               
}
