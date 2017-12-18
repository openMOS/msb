/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import com.sun.net.httpserver.HttpServer;
import eu.openmos.msb.services.soap.RecipesDeploymentImpl;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.apache.log4j.Logger;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
//import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author valerio.gentile
 */
public class StartRS {
    
    private final static int port = 9995;
    private final static String host="http://localhost/";

    private static final Logger logger = Logger.getLogger(StartRS.class.getName());

public static void main(String[] args)
    {
        logger.info("StartRS - starting...");
        URI baseUri = UriBuilder.fromUri(host).port(port).build();
        
//                .register(new Resource(new Core(), configuration)) // create instance of Resource and dynamically register        
        ResourceConfig resourceConfig = new ResourceConfig()                
                .register(ExecutionTableController.class)
                .register(FileUploadController.class)
                .register(ModuleController.class)
                .register(OrderController.class)
                .register(ProductController.class)
                .register(RecipeController.class)
                .register(SkillController.class)
                .register(SubSystemController.class)
                .register(MultiPartFeature.class)
                .register(SystemStatusController.class);
                
        
        resourceConfig.register(new CORSFilter());
        
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, resourceConfig);    
        logger.info("StartRS - started succesfully");        
    }

}
