/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.productionoptimizer;

import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer;
import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizerResponseBean;
import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer_Service;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class ProductionOptimizer_IsOptimizableTest {
    
    private static final Logger logger = Logger.getLogger(ProductionOptimizer_IsOptimizableTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New is optimizable Test main start");
        
        ProductionOptimizer_Service productionOptimizerService = new ProductionOptimizer_Service();
	ProductionOptimizer productionOptimizer = productionOptimizerService.getProductionOptimizerImplPort();
        
        ProductionOptimizerResponseBean productionOptimizerResponseBean = productionOptimizer.isOptimizable();
        
        logger.info(productionOptimizerResponseBean.getCode());
        logger.info(productionOptimizerResponseBean.getDescription());

        logger.info("New is optimizable Test main end");
    }               
}
