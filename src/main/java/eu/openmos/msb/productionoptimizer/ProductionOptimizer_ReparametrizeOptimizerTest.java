/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.productionoptimizer;

import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer;
import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizerResponseBean;
import eu.openmos.agentcloud.ws.productionoptimizer.wsimport.ProductionOptimizer_Service;
import eu.openmos.agentcloud.optimizer.data.OptimizationParameter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class ProductionOptimizer_ReparametrizeOptimizerTest {
    
    private static final Logger logger = Logger.getLogger(ProductionOptimizer_ReparametrizeOptimizerTest.class.getName());
    
    public static void main(String[] args) {
        logger.info("New reparametrize optimizer Test main start");
        
        ProductionOptimizer_Service productionOptimizerService = new ProductionOptimizer_Service();
	ProductionOptimizer productionOptimizer = productionOptimizerService.getProductionOptimizerImplPort();

        List<OptimizationParameter> listParameters = new ArrayList<>();
        ProductionOptimizerResponseBean productionOptimizerResponseBean = productionOptimizer.reparametrizeOptimizer(listParameters);
        
        logger.info(productionOptimizerResponseBean.getCode());
        logger.info(productionOptimizerResponseBean.getDescription());

        logger.info("New reparametrize optimizer Test main end");
    }               
}
