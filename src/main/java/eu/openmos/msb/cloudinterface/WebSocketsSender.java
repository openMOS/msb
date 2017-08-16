package eu.openmos.msb.cloudinterface;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.PhysicalLocation;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.data.RawEquipmentData;
import eu.openmos.agentcloud.data.RecipeExecutionData;
import eu.openmos.agentcloud.data.UnexpectedProductData;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class WebSocketsSender extends AbstractVerticle
{

  private static final Logger logger = Logger.getLogger(WebSocketsSender.class.getName());
  String topic;
  // String ip;
  int retryCount = 0;
  private static final int MAX_RETRY = 5;

  public WebSocketsSender(String _topic)
  {
    logger.debug("got topic [" + _topic + "]");
    topic = _topic;
    // ip = _ip;
  }

  EventBus eventBus = null;

  @Override
  public void start() throws Exception
  {

    // String myIP = "localhost";   //"192.168.15.1";
    // String myIP = "172.20.11.115";   //"192.168.15.1";
    String myIP = ConfigurationLoader.getMandatoryProperty("openmos.msb.ipaddress");
    logger.debug("MSBEmulator runs on this machine = [" + myIP + "]");
/////////////////////////////
//        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
//        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");            

    VertxOptions options = new VertxOptions();
    options.setClustered(true).setClusterHost(myIP);
    Vertx.clusteredVertx(options, res ->
    {
      if (res.succeeded())
      {
        vertx = res.result();
        eventBus = vertx.eventBus();
        vertx.setPeriodic(21000, v ->
        {
          Long now = Calendar.getInstance().getTimeInMillis();
          String msgToSend = "";
          /*
                if (now % 7 == 0)
                    msgToSend = "_SUICIDE_";
                else
           */
          boolean messageOk = false;
          if (now % 2 == 0)       // recipe execution data 
          {
            RecipeExecutionData red = RecipeExecutionDataTest.getTestObject();
            logger.debug("SENDING - " + red.toString());
            msgToSend = Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA + red.toString();
            messageOk = true;
          }
          if (now % 7 == 0)
          {
            if (topic.startsWith("Resource"))
            {
              RawEquipmentData ad = RawEquipmentDataTest.getTestObject(topic);
//                        Recipe r = ad.getRecipe();
//                        List<Recipe> cpadRecipes = new LinkedList<>(Arrays.asList(r));
//                        msgToSend = Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES + cpadRecipes.toString();
// TO CHECK -> which recipes?!?!?!?!??
              msgToSend = Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES + ad.toString();
              messageOk = true;
            }
          }
          if (now % 5 == 0)
          {
            if (topic.startsWith("Transport"))
            {
              // PhysicalLocation pl = new PhysicalLocation("physicallocation for agent " + topic, 1, 2, 3, 4, 5, 6);
              PhysicalLocation pl = new PhysicalLocation("physicallocation for agent " + topic, 1, 2, 3, 4, 5, 6, "productId", new Date());
              msgToSend = Constants.MSB_MESSAGE_TYPE_NEWLOCATION + pl.toString();
              messageOk = true;
            }
          }
          if (now % 4 == 0)       // unexpected product data
          {
            UnexpectedProductData upd = UnexpectedProductDataTest.getTestObject();
            logger.debug("SENDING - " + upd.toString());
            msgToSend = Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA + upd.toString();
            messageOk = true;
          }
          if (now % 3 == 0)
          {
            msgToSend = Constants.MSB_MESSAGE_TYPE_LIFEBEAT + " current topic is [" + topic + "] and the message is of type A";
            messageOk = true;
          }
          if (now % 6 == 0)
          {
//                    AgentData ad = getTestObject(topic);
            RawEquipmentData ad = RawEquipmentDataTest.getTestObject(topic);
            logger.debug("SENDING - " + ad.toString());
            msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + ad.toString();
            messageOk = true;
          }
          if (!messageOk)
          {
            msgToSend = "ping! current topic is [" + topic + "]";
          }

          // emulation!
          // check if the agent is into the agents list
          // if yes, the message is sent
          // if not, someone has killed the agent
          String agentsListFile = ConfigurationLoader.getMandatoryProperty("openmos.msb.agents.list.file");
          logger.debug("agentsListFile = [" + agentsListFile + "]");
          String agentsList = new String();
          try
          {
            agentsList = new String(Files.readAllBytes(Paths.get(agentsListFile)));
          } catch (IOException ex)
          {
            logger.error("cant read file " + agentsListFile + ": " + ex.getMessage());
          }
          if (agentsList.indexOf(topic) != -1) // if agent still alive
          {
            eventBus.send(topic, msgToSend, reply ->
            {
              if (reply.succeeded())
              {
                logger.debug("Received reply: " + reply.result().body());

                /*if (reply.result().body().toString().equalsIgnoreCase("STOP_SENDING"))
                        {
                            // TODO stop sending
                            // next instruction is the one!!!
                            this.getVertx().cancelTimer(v);
                                                        
                            this.getVertx().undeploy(this.deploymentID(), res1 -> 
                            {
                                if (res1.succeeded()) {
                                    System.out.println("WebSocket undeployment succedeed! "); // Deployment id for agent " + this.getLocalName() + " is [" + websocketsDeploymentId + "]");
                                  // System.out.println("Deployment id is: " + res.result());
                                } else {
                                    System.out.println("WebSocket undeployment failed!"); // for agent " + this.getLocalName() + " FAILED");
                                  // System.out.println("Deployment failed!");
                                }
                            }
                            );

                        }*/
              } else
              {
                System.out.println("No reply");
              }
            });
          } // if agent into agents list file    
          else
          {
            logger.warn("agent " + topic + " no more into agents list");
            retryCount++;

            if (retryCount > MAX_RETRY)
            {
              logger.warn("agent " + topic + " no more into agents list - time to remove the socket sender stuff");
              this.getVertx().cancelTimer(v);

              this.getVertx().undeploy(this.deploymentID(), res1 ->
              {
                if (res1.succeeded())
                {
                  System.out.println("WebSocket undeployment succedeed! "); // Deployment id for agent " + this.getLocalName() + " is [" + websocketsDeploymentId + "]");
                  // System.out.println("Deployment id is: " + res.result());
                } else
                {
                  System.out.println("WebSocket undeployment failed!"); // for agent " + this.getLocalName() + " FAILED");
                  // System.out.println("Deployment failed!");
                }
              }
              );
            }
          }
        });
        logger.debug("We now have a clustered event bus: " + eventBus);
      } else
      {
        logger.debug("Failed: " + res.cause());
      }
    });
  }
}
