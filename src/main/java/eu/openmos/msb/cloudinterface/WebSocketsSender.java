package eu.openmos.msb.cloudinterface;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.data.recipe.KPISetting;
import eu.openmos.agentcloud.data.recipe.ParameterSetting;
import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.agentcloud.data.LogicalLocation;
import eu.openmos.agentcloud.data.PhysicalLocation;
import eu.openmos.agentcloud.data.AgentData;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.data.DataType;
import eu.openmos.agentcloud.data.ValueType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WebSocketsSender extends AbstractVerticle {
 
    String topic;
    // String ip;
    
    public WebSocketsSender(String _topic){
        System.out.println("got topic [" + _topic + "]");
        topic = _topic;
        // ip = _ip;
    }
    
    EventBus eventBus = null;
    
    @Override
    public void start() throws Exception {
        
        String myIP = "172.18.2.117";   //"192.168.15.1"; 172.20.11.105;
/////////////////////////////
//        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
//        logger.info("Agent Cloud Cloudinterface address = [" + CLOUDINTERFACE_WS_VALUE + "]");            


        VertxOptions options = new VertxOptions();
        options.setClustered(true).setClusterHost(myIP);
        Vertx.clusteredVertx(options, res -> {
          if (res.succeeded()) {
            vertx = res.result();
            eventBus = vertx.eventBus();
            vertx.setPeriodic(7000, v -> {
                Long now = Calendar.getInstance().getTimeInMillis();
                String msgToSend = "";
/*
                if (now % 7 == 0)
                    msgToSend = "_SUICIDE_";
                else
*/
boolean messageOk = false;
               /* if (now % 3 == 0)
                {
                    msgToSend = Constants.MSB_MESSAGE_TYPE_LIFEBEAT + " current topic is [" + topic + "] and the message is of type A";
                    messageOk = true;
                }
                if (now % 2 == 0)
                {*/
                    AgentData ad = getTestObject(topic);
                    System.out.println("SENDING - " + ad.toString());
                    msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + ad.toString();
                    messageOk = true;
              /*  }
             /   if (now % 5 == 0)
                {
                    if (topic.startsWith("Transport"))
                    {
                        PhysicalLocation pl = new PhysicalLocation("physicallocation for agent " + topic, 1, 2, 3, 4, 5, 6);
                         msgToSend = Constants.MSB_MESSAGE_TYPE_NEWLOCATION + pl.toString();
                        messageOk = true;
                    }
                }
                if (now % 7 == 0)
                {
                    if (topic.startsWith("Resource"))
                    {
                        AgentData ad = getTestObject(topic);
                        Recipe r = ad.getRecipe();
                        List<Recipe> cpadRecipes = new LinkedList<>(Arrays.asList(r));
                        msgToSend = Constants.MSB_MESSAGE_TYPE_APPLIEDRECIPES + cpadRecipes.toString();
                        messageOk = true;
                    }
                }
                if (now % 17 == 0)
                {
                    msgToSend = Constants.MSB_MESSAGE_TYPE_SUICIDE + " current topic is [" + topic + "] and the message is of type B";
                    messageOk = true;
                }*/
                if (!messageOk)
                    msgToSend = "ping! current topic is [" + topic + "]";
                eventBus.send(topic, msgToSend, reply -> {
                    if (reply.succeeded()) {
                        System.out.println("Received reply: " + reply.result().body());
                        
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
                    } else {
                        System.out.println("No reply");
                    }
                    
               });
            });
            System.out.println("We now have a clustered event bus: " + eventBus);
          } else {
            System.out.println("Failed: " + res.cause());
          }
        });                
    }
    
    @Override
    public void stop() throws Exception {
        System.out.println("Verticle stopped");
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    private AgentData getTestObject(String agentName)
    {
        AgentData ad = new AgentData();
        ad.setAgentUniqueName(agentName);
        ad.setDeviceTime("deviceTime");
        ad.setMsbTime("msbTime");
        ad.setValue("current value");

        ad.setDataType(DataType.ORDER_UPDATE);
        ad.setValueType(ValueType.FLOAT);
        
        LogicalLocation ll = new LogicalLocation("lcgicallocation");
        ad.setLogicalLocation(ll);
        
        PhysicalLocation pl = new PhysicalLocation("physicallocation", 1, 2, 3, 4, 5, 6);
        ad.setPhysicalLocation(pl);
        
            Recipe r;
                String recipeDescription = "asdsad";
                String recipeUniqueId = "asda";
                    KPISetting recipeKpiSetting;
                    String kpiSettingDescription = "asdsad";
                    String kpiSettingId = "asda";
                    String kpiSettingName = "asdsa";
                    String kpiSettingValue = "asds";
                    // recipeKpiSetting = new KPISetting(kpiSettingDescription, 
                    // kpiSettingId, kpiSettingName, kpiSettingValue);
                    recipeKpiSetting = new KPISetting();
                    recipeKpiSetting.setDescription(kpiSettingDescription);
                    recipeKpiSetting.setId(kpiSettingId);
                    recipeKpiSetting.setName(kpiSettingName);
                    recipeKpiSetting.setValue(kpiSettingValue);
                    
                List<KPISetting> recipeKpiSettings = new LinkedList<>(Arrays.asList(recipeKpiSetting));
                String recipeName = "asda";
                    ParameterSetting recipePS;
                    String parameterSettingDescription = "asdsad";
                    String parameterSettingId = "asda";
                    String parameterSettingName = "asdsa";
                    String parameterSettingValue = "asds";
                    // recipePS = new ParameterSetting(parameterSettingDescription, parameterSettingId, parameterSettingName, parameterSettingValue);
                    recipePS = new ParameterSetting();
                    recipePS.setDescription(parameterSettingDescription);
                    recipePS.setId(parameterSettingId);
                    recipePS.setName(parameterSettingName);
                    recipePS.setValue(parameterSettingValue);
                    
                List<ParameterSetting> recipeParameterSettings = new LinkedList<>(Arrays.asList(recipePS));
                String recipeUniqueAgentName = "asdsad";
                    SkillRequirement recipeSR;
                    String skillRequirementDescription = "asdsad";
                    String skillRequirementUniqueId = "asda";
                    String skillRequirementName = "asdsa";
                    int skillRequirementType = 0;
                    // recipeSR = new SkillRequirement(skillRequirementDescription, skillRequirementUniqueId, skillRequirementName, skillRequirementType);
                    recipeSR = new SkillRequirement();
                    recipeSR.setDescription(skillRequirementDescription);
                    recipeSR.setUniqueId(skillRequirementUniqueId);
                    recipeSR.setName(skillRequirementName);
                    recipeSR.setType(skillRequirementType);
                    
                List<SkillRequirement> recipeSkillRequirements = new LinkedList<>(Arrays.asList(recipeSR));
            // r = new Recipe(recipeDescription, recipeUniqueId, recipeKpiSettings, recipeName, recipeParameterSettings, recipeUniqueAgentName, recipeSkillRequirements);
            r = new Recipe();
            r.setDescription(recipeDescription);
            r.setName(recipeName);
            r.setUniqueAgentName(recipeUniqueAgentName);
            r.setUniqueId(recipeUniqueId);
            // r.getKpisSetting().addAll(recipeKpiSettings);
            r.setKpisSetting(recipeKpiSettings);
            // r.getParametersSetting().addAll(recipeParameterSettings);
            r.setParametersSetting(recipeParameterSettings);
            // r.getSkillRequirements().addAll(recipeSkillRequirements);
            r.setSkillRequirements(recipeSkillRequirements);
            

            ad.setRecipe(r);
            
        
        

        
        
        return ad;        
    }
    
}