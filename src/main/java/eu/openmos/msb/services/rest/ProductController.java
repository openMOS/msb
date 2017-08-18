/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.Product;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.cloud.cloudinterface.testdata.ProductTest;
import eu.openmos.msb.cloud.cloudinterface.testdata.SkillRequirementTest;
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
public class ProductController
{

  private final Logger logger = Logger.getLogger(ProductController.class.getName());

  /**
   * Returns the list of products, i.e. product models. Useful for execution table management and for new order creation
   * view.
   *
   * @return list of product objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Product> getList()
  {
    logger.debug("products getList");
    List<Product> ls = new LinkedList<>();

    for (int i = 0; i < 5; i++)
    {
      Product p = ProductTest.getTestObject();
      String currentP = "PRODUCT " + Character.getNumericValue("A".charAt(0)) + i;
      p.setModelId(currentP);
      p.setName("name_" + currentP);
      p.setDescription("description_" + currentP);
      ls.add(p);
    }

    return ls;
  }

  /**
   * Returns the product object given its unique identifier. Useful for execution table management and for new order
   * creation view.
   *
   * @return detail of product
   *
   * @param modelId the unique id of the product model
   * @return product object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{modelId}")
  public Product getDetail(@PathParam("modelId") String modelId)
  {
    logger.debug("product getDetail - modelId = " + modelId);
    return ProductTest.getTestObject();
  }

  /**
   * Returns the list of skill requirements associated to a product. This method is exposed via a
   * "/products/{modelId}/skillRequirements" service call.
   *
   * @param modelId the product model unique identifier.
   * @return list of skill requirement objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{modelId}/skillRequirements")
  public List<SkillRequirement> getSkillRequirementsList(@PathParam("modelId") String modelId)
  {
    logger.debug("product - getSkillRequirementsList - modelId = " + modelId);
    logger.debug("product getSkillRequirementsList - of the product = " + modelId);
    return SkillRequirementTest.getTestList();
  }
}
