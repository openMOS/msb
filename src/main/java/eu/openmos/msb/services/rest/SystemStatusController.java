package eu.openmos.msb.services.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("/api/v1/systemstatus")
public class SystemStatusController
{

  private final Logger logger = Logger.getLogger(SystemStatusController.class.getName());

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getSystemStatus()
  {
    logger.debug("get system status");
    
    // TODO

    return "PRODUCTION";
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String setStatus(String newStatus)
  {
    logger.debug("set system status - new status = " + newStatus);
    
    // TODO
    
    return newStatus;
  }
}
