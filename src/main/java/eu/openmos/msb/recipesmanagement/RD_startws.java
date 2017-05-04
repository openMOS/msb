/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.recipesmanagement;

import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class RD_startws {
    private static final Logger logger = Logger.getLogger(RD_startws.class.getName());
    
    public static void main(String[] args)
    {
        logger.debug("RD_startws main start");
        // Endpoint.publish("http://localhost:9997/wsRecipesDeployer", new RecipesDeployerImpl());
        Endpoint.publish("http://0.0.0.0:9997/wsRecipesDeployer", new RecipesDeployerImpl());
        logger.debug("RD_startws main stop");
    }
    
}
