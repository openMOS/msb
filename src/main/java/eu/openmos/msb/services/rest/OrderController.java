/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.SubSystem;
import eu.openmos.agentcloud.data.Order;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Parameter;
import eu.openmos.model.ParameterSetting;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillRequirement;
import eu.openmos.msb.cloud.cloudinterface.testdata.OrderTest;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/orders")
public class OrderController
{

  private final Logger logger = Logger.getLogger(OrderController.class.getName());

  /**
   * Returns the list of orders. There's no slide requesting this method, it's only for testing purpose.
   *
   * @return list of order objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Order> getList()
  {
    logger.debug("orders getList");
    List<Order> ls = new LinkedList<>();

    for (int i = 0; i < 5; i++)
    {
      Order o = OrderTest.getTestObject();
//            o.setEquipmentId("WORKSTATION_" + i);
      ls.add(o);
    }

    return ls;
  }

  /**
   * Allows to insert a new order into the system. Returns the order object.
   *
   * There will be a view into the HMI application for order creation. This method is exposed via a POST to "/orders"
   * service call.
   *
   * @param newOrder the order to be inserted.
   * @return the order object. List can be empty, cannot be null.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Order newOrder(Order newOrder)
  {
    logger.debug("orders newOrder - order to insert = " + newOrder.toString());
    return OrderTest.getTestObject();
  }

  /**
   * Returns the order object given its unique identifier. There's no slide requesting this method, it's only for
   * testing purpose.
   *
   * @return detail of order
   *
   * @param orderId the unique id of the order
   * @return order object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{orderId}")
  public Order getDetail(@PathParam("orderId") String orderId)
  {
    logger.debug("order getDetail - orderId = " + orderId);
    return OrderTest.getTestObject();
  }
}
