/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.Product;
// import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.model.*;
import eu.openmos.msb.datastructures.PECManager;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/products")
public class ProductController {
    private final Logger logger = Logger.getLogger(ProductController.class.getName());
    
    /**
     * Returns the list of products, i.e. product models.
     * Useful for execution table management and for new order creation view.
     * 
     * @return list of product objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getList()
    {     
        logger.debug("products getList");       
        List<Product> lp = new LinkedList<>();

        PECManager aux = PECManager.getInstance();
        lp = aux.getProductList();
    
        return lp;
    }

    /**
     * Returns the product object given its unique identifier.
     * Useful for execution table management and for new order creation view.
     * 
     * @return detail of product
     * 
     * @param modelId the unique id of the product model
     * @return product object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{modelId}")
    public Product getDetail(@PathParam("modelId") String modelId) {
        logger.debug("product getDetail - modelId = " + modelId);
//        return ProductTest.getTestObject();

        PECManager aux = PECManager.getInstance();

        for (Product product : getList()) {
//            if (product.getName().equals(modelId)) {
            if (product.getUniqueId().equals(modelId)) {
                logger.debug("product - found " + modelId + " - returning " + product.toString());
                return product;
            }
        }
        logger.debug("product - not found " + modelId + " - returning null");
        return null; // TBV
   }

    /**
     * Returns the list of skill requirements associated to a product.
     * This method is exposed via a "/products/{modelId}/skillRequirements" service call.
     * 
     * @param modelId   the product model unique identifier.
     * @return list of skill requirement objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{modelId}/skillRequirements")    
    public List<SkillRequirement> getSkillRequirementsList(@PathParam("modelId") String modelId)
    {
        logger.debug("product - getSkillRequirementsList - modelId = " + modelId);
        logger.debug("product getSkillRequirementsList - of the product = " + modelId);
//        return SkillRequirementTest.getTestList();
        Product p = getDetail(modelId);
        return p.getSkillRequirements();
    }
}