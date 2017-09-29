package eu.openmos.msb.opcua.milo.server;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;
import eu.openmos.msb.cloud.cloudinterface.testactions.WebSocketsReceiver;
import eu.openmos.msb.cloud.cloudinterface.testactions.WebSocketsSender;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.EProtocol;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Admin
 */
public class OPCServersDiscoverySnippet extends Thread
{

  private final int discovery_period = 10; // 35 seconds
  private final String LDS_uri;
  private final IOPCNotifyGUI notify_channel;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final String MSB_OPCUA_SERVER_ADDRESS;

  /**
   * @param _msb_address
   * @brief Class constructor
   * @param _LDS_uri
   * @param _notify_channel
   */
  public OPCServersDiscoverySnippet(String _LDS_uri, String _msb_address, IOPCNotifyGUI _notify_channel)
  {
    LDS_uri = _LDS_uri;
    notify_channel = _notify_channel;
    MSB_OPCUA_SERVER_ADDRESS = _msb_address;

  }

  /**
   * @brief TODO fabio
   */
  @Override
  public void run()
  {
    while (true)
    {
      try
      {
        discoverServer(LDS_uri);
        //checkDBServersStatus();
        sleep(discovery_period * 1000);
      } catch (InterruptedException ex)
      {
        this.logger.error(ex.getMessage());
      }
    }
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @param uri
   * @return
   */
  private boolean discoverServer(String uri)
  {
    // notify gui of the pooling mechanism
    notify_channel.on_polling_cycle();
    try
    {
      // Discover a new server list from a discovery server at URI
      ApplicationDescription[] serverList = UaTcpStackClient.findServers(uri).get();
      if (serverList.length == 0)
      {
        System.out.println("No servers found");
        return false;
      }

      for (int i = 0; i < serverList.length; i++)
      {
        ApplicationDescription server = serverList[i];

        System.out.println("getApplicationUri output " + server.getApplicationUri());
        System.out.println("getDiscoveryUrls output " + server.getDiscoveryUrls()[0]);

        try
        {
          //discover endpoints only from servers. not from discovery
          if (server.getApplicationType() != DiscoveryServer)
          {
            discoverEndpoints(server);
          }
        } catch (Exception ex)
        {
          this.logger.error(ex.getMessage());
        }
      }
      return true;
    } catch (InterruptedException | ExecutionException ex)
    {
      this.logger.error(ex.getMessage());
    }
    return false;
  }

  /**
   * @brief TODO fabio
   * @param serverApp
   * @param applicationUri
   * @return
   */
  private EndpointDescription discoverEndpoints(ApplicationDescription serverApp)
  {
    // singleton of the class Device Adatper Manager, it holds all the Device Adapters registered in this MSB instance
    DACManager dacManager = DACManager.getInstance();
    String[] discoveryUrls = serverApp.getDiscoveryUrls();

    if (discoveryUrls != null)
    {
      List<EndpointDescription> edList = new ArrayList<>();
      for (String url : discoveryUrls)
      {
        String da_url = "";
        
        
        try
        {
          ApplicationDescription[] i = UaTcpStackClient.findServers(url).get();
          //EndpointDescription[] ed2 = UaTcpStackClient.getEndpoints(url).get();
          //UaTcpStackClient.findServers(url).get();
        } catch (Exception ex)
        {
          try
          {
            System.out.println("shit bad");
            ApplicationDescription[] i = UaTcpStackClient.findServers(url).get();
          } catch (InterruptedException | ExecutionException ex1)
          {
            java.util.logging.Logger.getLogger(OPCServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex1);
          }
        }
        
        try
        {
          for (EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get())
          {
            edList.add(ed);
            System.out.println("EndPoints from: " + url + " = " + ed);

            String daName = serverApp.getApplicationName().getText();
            // Creates a new Device Adapter
            DeviceAdapter da = dacManager.getDeviceAdapter(daName);

            if (da == null)
            {
              System.out.println("\n\n Registered the " + daName + "\n\n");
              
              da = dacManager.addDeviceAdapter(daName, EProtocol.OPC, "", "");
              if (da == null)
              {
                this.logger.error("Unable to create Device Adatper {} at {}", daName, url);
                System.out.println("Unable to create Device Adatper " + daName + " at " + url);
                continue;
              }
              // if Device Adatper created ok, let's do some magic
              da.getSubSystem().setName(daName);
              da_url = ed.getEndpointUrl();
 
              if(da_url.contains("4840"))
                continue;
              ApplicationDescription[] serverList = UaTcpStackClient.findServers(da_url).get();
              if (serverList[0].getApplicationType() != ApplicationType.DiscoveryServer)
              {

                DeviceAdapterOPC opc = (DeviceAdapterOPC) dacManager.getDeviceAdapter(daName);
                MSBClientSubscription instance = opc.getClient();
                
                instance.startConnection(da_url);
                OpcUaClient client = instance.getClientObject();
                
                da.getClient();
                
                // add subscribet for the ServerStatus of the device adatper
                
                // Iterate over all values, using the keySet method.
                // call SendServerURL() method from device
                if (daName != null && !daName.contains("MSB") && !daName.contains("discovery"))
                {

                  // oompa loompas at work here
                  System.out.println("\n");
                  System.out.println("***** Starting namespace browsing ***** \n");
                  
                  Element node = new Element("DeviceAdapter");
                  Set<String> ignore = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                  ignore.addAll(Arrays.asList(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.ignore").split(",")));
                  
                  node.addContent(instance.browseNode(client,
                          new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.dainstance")),
                          Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level")),
                          ignore));
                  
                  Element nSkills = new Element("Skills");
                  nSkills.addContent(instance.browseNode(client,
                          new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.skills")),
                          Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level.skills")),
                          ignore));
                  // add the skills node to the main doc, not necessary 
//                  node.addContent(nSkills);
                  
//                  // Print to console

//                  System.out.println("\n");
//                  new XMLOutputter(Format.getPrettyFormat()).output(node, System.out); // this is only for debug
//                  System.out.println("\n");
                  
                  // print to file the XML structure extracted from the browsing process             
                  XMLOutputter xmlOutput = new XMLOutputter();
                  xmlOutput.setFormat(Format.getPrettyFormat());
                  xmlOutput.output(node, new FileWriter("C:\\Users\\Introsys\\Desktop\\OpenMosWorksapce\\msb\\xml/file.xml", false));
                  xmlOutput.output(nSkills, new FileWriter("C:\\Users\\Introsys\\Desktop\\OpenMosWorksapce\\msb\\xml/file2.xml", false));
                  
                  System.out.println("Starting DA Parser **********************");
                  // TODO - WIP - parse the XML to java classes to be send up to the HMI and Agent Cloud
                  boolean ok = da.parseDNToObjects(node, nSkills);
                  
                  System.out.println("***** End namespace browsing ***** \n\n");                  
                  
                  //FILL TABLES
                  if (ok)
                  {
                    //notify_channel.on_namespace_read(da.getListOfRecipes(), daName);
                    workStationRegistration(da);
                  }
                  
                  // TODO next step validate this
                  // TODO fabio check if this will be valid after changes from fortiss
                  instance.SendServerURL(client, MSB_OPCUA_SERVER_ADDRESS).exceptionally(ex ->
                  {
                    System.out.println("error invoking SendServerURL() for server: " + daName + "\n" + ex);
                    //logger.error("error invoking SendServerURL()", ex);
                    return "-1.0";
                  }).thenAccept(v ->
                  {
                    System.out.println("SendServerURL(uri)={}\n" + v);
                  });

                }
                if (client == null)
                {
                  System.out.println("Client = null?");
                }

              }
              // Update the GUI with the new devices discovered 
              notify_channel.on_new_endpoint_discovered(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
            }

          } // end of second "for" for Discovery Endpoints
        } catch (InterruptedException | ExecutionException e)
        {
          this.logger.error("Cannot discover Endpoints from URL {} : {}", da_url, e.getMessage());
          System.out.println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + da_url + ": " + e.getMessage());

          if (e.getCause().getMessage().contains("Connection refused"))
          {
            String daName = serverApp.getApplicationName().getText();
            if (dacManager.getDeviceAdapter(daName) != null)
            {
              removeDownServer(daName);
            }
          }
        } catch (Exception ex)
        {
          java.util.logging.Logger.getLogger(OPCServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
        }
      } // end of first "for" for Discovery Urls
    } else
    {
      System.out.println("No suitable discoveryUrl available: using the current Url");
    }
    return null;
  }

  /**
   *
   * @param serverName
   * @return
   */
  private int removeDownServer(String serverName)
  {
    if (DACManager.getInstance().deleteDeviceAdapter(serverName))
    {
      System.out.println("DownServer successfully deleted from DB!");
      notify_channel.on_endpoint_dissapeared(serverName);
      return 1;
    } else
    {
      String error = "Unable to remove  " + serverName + " from DB!";
      notify_channel.on_notify_error(error);
      System.out.println(error);
      return -1;
    }
  }
  
  private String workStationRegistration(DeviceAdapter da)
  {
    try
    {
      DACManager dacManager = DACManager.getInstance();
      if (da.getListOfEquipments().size() > 0)
      {
        for (Module auxModule : da.getListOfEquipments())
        {
          auxModule.setAddress(UUID.randomUUID().toString()); //FOR TESTING, DELETE AFTER!******************************************************************
          dacManager.registerModule(da.getSubSystem().getName(), auxModule.getName(), auxModule.getStatus(), auxModule.getAddress());
        }
        MSB_gui.fillDevicesTable();
      }

      if (da.getListOfSkills().size() > 0)
      {
        for (Skill auxSkill : da.getListOfSkills())
        {
          dacManager.registerSkill(da.getSubSystem().getName(), auxSkill.getUniqueId(), auxSkill.getName(), auxSkill.getDescription());
        }
      }

      if (da.getListOfRecipes().size() > 0)
      {
        for (Recipe auxRecipe : da.getListOfRecipes())
        {
          dacManager.registerRecipe(da.getSubSystem().getName(), auxRecipe.getUniqueId(), auxRecipe.getSkill().getName(), "true", auxRecipe.getName());
        }
        MSB_gui.fillRecipesTable();
      }

      // --------------------------------------------------------------------------------------------------------------
      // create new agent
      // TODO falar com o velerio
      boolean withAGENTCloud = false;
      if (withAGENTCloud)
      {
        // THIS CODE IS WORKING!! 
        SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
        SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
        String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
        BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
        bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);
        // SubSystem cpad = dummySubSystemGeneration(parsedClass);
        //SubSystem cpad = SubSystemTest.getTestObject();

        ServiceCallStatus agentStatus = systemConfigurator.createNewResourceAgent(da.getSubSystem());
        
        System.out.println("\n\n Creating Resource or Transport Agent... \n\n");
        String msgToSend = Constants.MSB_MESSAGE_TYPE_EXTRACTEDDATA + "anything";
        //Vertx.vertx().deployVerticle(new WebSocketsSender(cpad.getUniqueName())); // TODO - DELETE THIS
        //da.getVertx().deployVerticle(new WebSocketsSender(da.getSubSystem().getUniqueName()));

        //add the sender client object to the respective agentID
        //da.setSubSystem(cpad);
        
        return agentStatus.getCode(); //OK? ou KO?
      } else
      {
        //call mainwindow filltables
        MSB_gui.fillRecipesTable();
        da.getVertx().deployVerticle(new WebSocketsReceiver("R1"));
        da.getVertx().deployVerticle(new WebSocketsReceiver("R2"));

        Thread.sleep(3000);

        da.getVertx().deployVerticle(new WebSocketsSender("R3"));

        return "OK - No AgentPlatform";

      }
    }catch (InterruptedException ex)
    {
      //Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("Problems parsing the RegFile - " + ex.getMessage());
    }
    //Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.SEVERE, null, ex);
     finally
    {
      long endTime = System.nanoTime();
      //long elapsedTime = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
      //Logger.getLogger(OPCDeviceHelper.class.getName()).log(Level.INFO, null, "ELAPSED TIME: " + elapsedTime + "ms");
      //System.out.println("\n\n ELAPSED TIME: " + elapsedTime + "ms");
    }
    return null;
  }
}
