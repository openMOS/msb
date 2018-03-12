package eu.openmos.msb.services.rest;

import eu.openmos.msb.datastructures.MSBVar;
import eu.openmos.msb.services.rest.data.SystemStage;
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
@Path("/api/v1/systemstage")
public class SystemStageController extends Base
{

  private final Logger logger = Logger.getLogger(SystemStageController.class.getName());

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public SystemStage getSystemStage()
  {
    logger.debug("get system stage");
    
    // TODO

    return new SystemStage(MSBVar.getSystemStage());
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SystemStage setStage(String newStage)
  {
    logger.debug("set system stage - new stage = " + newStage);
    
    // TODO
    MSBVar.setSystemStage(newStage);
    
    return new SystemStage(newStage);
  }
}
