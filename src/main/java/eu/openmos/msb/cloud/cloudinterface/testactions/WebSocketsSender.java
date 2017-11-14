package eu.openmos.msb.cloud.cloudinterface.testactions;

import eu.openmos.model.testdata.RawEquipmentDataTest;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.model.PhysicalLocation;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.RawEquipmentData;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.UnexpectedProductData;
import eu.openmos.model.testdata.RecipeExecutionDataTest;
import eu.openmos.model.testdata.UnexpectedProductDataTest;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
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
  }

  EventBus eventBus = null;

  @Override
  public void start() throws Exception
  {

    // String myIP = "localhost";   //"192.168.15.1";
    // String myIP = "172.20.11.115";   //"192.168.15.1";
    String myIP = ConfigurationLoader.getMandatoryProperty("openmos.msb.ipaddress");
    logger.debug("MSBEmulator runs on this machine = [" + myIP + "]");

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

          JsonObject objectToSend = null;

          String messageType = null;

          DeliveryOptions deliveryOptions = new DeliveryOptions();

          Long now = Calendar.getInstance().getTimeInMillis();

          boolean messageOk = false;
          if (now % 2 == 0)       // recipe execution data 
          {
            RecipeExecutionData red = RecipeExecutionDataTest.getTestObject();
            logger.debug("SENDING - " + red.toString());
            messageType = Constants.MSB_MESSAGE_TYPE_RECIPE_EXECUTION_DATA;
            objectToSend = JsonObject.mapFrom(red); // method available from 3.4 on
            messageOk = true;
          }
          if (now % 7 == 0)
          {
            if (topic.startsWith("Resource"))
            {
              RawEquipmentData red = RawEquipmentDataTest.getTestObject(topic);
              messageType = Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES;
              objectToSend = JsonObject.mapFrom(red); // method available from 3.4 on
              messageOk = true;
            }
          }
          if (now % 5 == 0)
          {
            if (topic.startsWith("Transport"))
            {
              PhysicalLocation pl = new PhysicalLocation("physicallocation for agent " + topic, 1, 2, 3, 4, 5, 6, "productId", new Date());
              messageType = Constants.MSB_MESSAGE_TYPE_NEWLOCATION;
              objectToSend = JsonObject.mapFrom(pl); // method available from 3.4 on
              messageOk = true;
            }
          }
          if (now % 4 == 0)       // unexpected product data
          {
            UnexpectedProductData upd = UnexpectedProductDataTest.getTestObject();
            logger.debug("SENDING - " + upd.toString());
            messageType = Constants.MSB_MESSAGE_TYPE_UNEXPECTED_PRODUCT_DATA;
            objectToSend = JsonObject.mapFrom(upd); // method available from 3.4 on
            messageOk = true;
          }
          if (now % 3 == 0)
          {
            messageType = Constants.MSB_MESSAGE_TYPE_LIFEBEAT;
            messageOk = false;
          }
          if (now % 6 == 0)
          {
            RawEquipmentData red = RawEquipmentDataTest.getTestObject(topic);
            logger.debug("SENDING - " + red.toString());
            messageType = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA;
            objectToSend = JsonObject.mapFrom(red); // method available from 3.4 on
            messageOk = true;
          }
//                if (!messageOk)
//                    msgToSend = "ping! current topic is [" + topic + "]";

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
            /*
// Create a JsonObject from the fields of a Java object.
// Faster than calling `new JsonObject(Json.encode(obj))`.
public static JsonObject mapFrom(Object obj)

// Instantiate a Java object from a JsonObject.
// Faster than calling `Json.decodeValue(Json.encode(jsonObject), type)`.
public <T> T mapTo(Class<T> type)
             */
            if (messageOk)
            {
              deliveryOptions.addHeader("messageType", messageType);
//                        eventBus.send(topic, red, reply -> {
              eventBus.send(topic, objectToSend, deliveryOptions, reply ->
              {
                if (reply.succeeded())
                {
                  logger.debug("Received reply: " + reply.result().body());
                } else
                {
                  logger.debug("No reply");
                }
              });
            }
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
