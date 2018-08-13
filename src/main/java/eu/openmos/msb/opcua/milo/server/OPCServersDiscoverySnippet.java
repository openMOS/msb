package eu.openmos.msb.opcua.milo.server;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
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
import eu.openmos.msb.datastructures.MSBConstants;
import static eu.openmos.msb.datastructures.MSBConstants.XML_PATH;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
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

      for (ApplicationDescription server : serverList)
      {
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

      //get da and check for server status
      for (String id : DACManager.getInstance().getDeviceAdapters_AML_IDs())
      {
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(id);
        String daName = da.getSubSystem().getName();
        boolean found = false;

        for (ApplicationDescription server : serverList)
        {
          if (daName.equals(server.getApplicationName().getText()))
          {
            found = true;
          }
        }
        if (!found)
        {
          DACManager.getInstance().delete_DA_Stuff(da.getSubSystem().getUniqueId()); //remove from DB?
          removeDownServer(daName);
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
          for (EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get())
          {
            edList.add(ed);

            String daName = serverApp.getApplicationName().getText();
            // Creates a new Device Adapter
            DeviceAdapter da = dacManager.getDeviceAdapterbyName(daName);

            if (da == null)
            {
              da_url = ed.getEndpointUrl();
              // VaG FABIO PATCH 20170927
              if (da_url.contains("4840") || da_url.contains("9995"))
              {
                continue;
              }

              logger.info("\n\n Registered the " + daName + "\n\n");

              da = dacManager.addDeviceAdapter(daName, EProtocol.OPC, "", ""); //add to DB
              if (da == null)
              {
                this.logger.error("Unable to create Device Adapter {} at {}", daName, url);
                System.out.println("Unable to create Device Adapter " + daName + " at " + url);
                continue;
              }
              // if Device Adatper created ok, let's do some magic
              da.getSubSystem().setName(daName);
              da.getSubSystem().setAddress(da_url);

              ApplicationDescription[] serverList = UaTcpStackClient.findServers(da_url).get();
              if (serverList[0].getApplicationType() != ApplicationType.DiscoveryServer)
              {
                // add subscribet for the ServerStatus of the device adatper
                // Iterate over all values, using the keySet method.
                if (daName != null && !daName.contains("MSB") && !daName.contains("discovery"))
                {
                  final DeviceAdapter aux_da = da;
                  final String aux_url = da_url;
                  Thread threadDiscoverEndpoints = new Thread()
                  {
                    @Override
                    public synchronized void run()
                    {
                      read_parse_opcua_namespace(aux_da, aux_url);
                    }
                  };
                  threadDiscoverEndpoints.start();   
                }
              }
              // Update the GUI with the new devices discovered 
              notify_channel.on_new_endpoint_discovered(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
            }
          } // end of second "for" for Discovery Endpoints
        } catch (InterruptedException | ExecutionException e)
        {
          logger.warn("Cannot discover Endpoints from URL {} : {}", da_url, e.getMessage());
          logger.warn("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + da_url + ": " + e.getMessage());

          if (e.getCause().getMessage().toLowerCase().contains("connection refused")
                  || e.getCause().getMessage().toLowerCase().contains("connection timed out"))
          {
            String daName = serverApp.getApplicationName().getText();
            if (dacManager.getDeviceAdapterbyName(daName) != null)
            {
              dacManager.delete_DA_Stuff_by_name(daName); //remove from DB?
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
      logger.warn("No suitable discoveryUrl available: using the current Url");
    }
    return null;
  }

  /**
   *
   * @param serverName
   * @return
   */
  private int removeDownServer(String da_name)
  {
    DeviceAdapter da = null;
    da = DACManager.getInstance().getDeviceAdapterbyName(da_name);

    if (da != null) //check if the da exists on the system
    {
      if (MSBConstants.USING_CLOUD) //check if the agentcloud is active
      {
        try
        {
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

          SubSystem ss = da.getSubSystem();

          if (ss.getDescription() == null || ss.getDescription().length() == 0)
          {
            ss.setDescription(ss.getName());
          }

          PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
          perfMeasure.getAgentRemovalTimers().put(ss.getUniqueId(), new Date().getTime());
          ServiceCallStatus agentStatus = systemConfigurator.removeAgent(ss.getUniqueId());
          // end

          logger.info("\n\n Removing Resource or Transport Agent: " + agentStatus + "\n\n");
          String resCode = agentStatus.getCode();

          logger.info("Successfully removed Agent with UID: " + ss.getUniqueId() + "with status: " + resCode);
        } catch (Exception ex)
        {
          logger.info("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
      if (DACManager.getInstance().deleteDeviceAdapter(da.getSubSystem().getName())) //if da removed from the system
      {
        logger.info("DownServer successfully deleted from DB!");
        notify_channel.on_endpoint_dissapeared(da.getSubSystem().getName());

        //end
        return 1;
      } else
      {
        String error = "Unable to remove  " + da.getSubSystem().getName() + " from DB!";
        notify_channel.on_notify_error(error);
        logger.error(error);
        return -1;
      }

    } else
    {
      logger.error("DA: " + da_name + " is null. Doesn't exist on the system!");
      return -1;
    }

  }

  private String workstationRegistration(DeviceAdapter da)
  {
    try
    {
      DACManager dacManager = DACManager.getInstance();
      DatabaseInteraction.getInstance().UpdateDAamlID(da.getSubSystem().getUniqueId(), da.getId()); //insert aml ID into the DB

      MSB_gui.addToTableAdapters(da.getSubSystem().getName(), "opcua", da.getSubSystem().getAddress());

      if (da.getListOfModules() != null && da.getListOfModules().size() > 0)
      {
        for (Module auxModule : da.getListOfModules())
        {
          boolean res = dacManager.registerModule(da.getSubSystem().getName(), auxModule.getName(), auxModule.getUniqueId(), auxModule.getStatus(), auxModule.getAddress());
          if (res)
          {
            logger.info("Module registed | Name: " + auxModule.getName() + " | ID: " + auxModule.getUniqueId());
          } else
          {
            logger.info("Module not registed | Name: " + auxModule.getName() + " | ID: " + auxModule.getUniqueId());
          }
        }
        MSB_gui.fillModulesTable();
      }

      if (da.getListOfSkills() != null && da.getListOfSkills().size() > 0)
      {
        for (Skill auxSkill : da.getListOfSkills())
        {
          //if skill does not exist yet
          //if (!dacManager.skillExists(auxSkill.getUniqueId()))
          {
            boolean res = dacManager.registerSkill(da.getSubSystem().getName(), auxSkill.getUniqueId(), auxSkill.getName(), auxSkill.getDescription());
            if (res)
            {
              logger.info("Skill registed | Name: " + auxSkill.getName() + " | ID: " + auxSkill.getUniqueId());
            } else
            {
              logger.info("Skill not registed | Name: " + auxSkill.getName() + " | ID: " + auxSkill.getUniqueId());
            }
          }
        }
      }

      if (da.getListOfRecipes() != null && da.getListOfRecipes().size() > 0)
      {
        for (Recipe auxRecipe : da.getListOfRecipes())
        {
          if (da.getSubSystem().getName() != null && auxRecipe.getSkill() != null && auxRecipe.getSkill().getName() != null)
          {
            boolean res = dacManager.registerRecipe(da.getSubSystem().getName(), auxRecipe.getUniqueId(), auxRecipe.getSkill().getName(),
                    "true", auxRecipe.getName(), auxRecipe.getInvokeObjectID(), auxRecipe.getInvokeMethodID());
            if (res)
            {
              dacManager.AssociateRecipeToSR(auxRecipe.getSkill());
              logger.info("Recipe registed | Name: " + auxRecipe.getName() + " | ID: " + auxRecipe.getUniqueId());
            } else
            {
              logger.info("Recipe not registed | Name: " + auxRecipe.getName() + " | ID: " + auxRecipe.getUniqueId());
            }
          } else
          {
            logger.error("\nCouldn't register recipe: " + auxRecipe.getName() + "\n");
          }
        }
        for (Module mod : da.getListOfModules())
        {
          for (Recipe auxRecipe : mod.getRecipes())
          {
            if (da.getSubSystem().getName() != null && auxRecipe.getSkill() != null && auxRecipe.getSkill().getName() != null)
            {
              boolean res = dacManager.registerRecipe(da.getSubSystem().getName(), auxRecipe.getUniqueId(), auxRecipe.getSkill().getName(),
                      "true", auxRecipe.getName(), auxRecipe.getInvokeObjectID(), auxRecipe.getInvokeMethodID());
              if (res)
              {
                dacManager.AssociateRecipeToSR(auxRecipe.getSkill());
                logger.info("Module recipe registed | Name: " + auxRecipe.getName() + " | ID: " + auxRecipe.getUniqueId());
              } else
              {
                logger.info("Module recipe not registed | Name: " + auxRecipe.getName() + " | ID: " + auxRecipe.getUniqueId());
              }
            } else
            {
              logger.error("\nCouldn't register module recipe: " + auxRecipe.getName() + "\n");
            }
          }
        }

        MSB_gui.fillRecipesTable();
      }

      // --------------------------------------------------------------------------------------------------------------
      // create new agent
      return DACManager.daAgentCreation(da);
    } catch (Exception ex)
    {
      logger.error("Errors in workStationRegistration - " + ex.getMessage());
    }
    return null;
  }

  private boolean isCloudRunning()
  {

    return true;
  }

  public static NodeId browseInstaceHierarchyNode(String indent, OpcUaClient client, NodeId nodeToBrowse)
  {
    try
    {
      List<Node> nodes = client.getAddressSpace().browse(nodeToBrowse).get();
      int max_lvl_search = 10;
      int count = 0;

      while (count < max_lvl_search)
      {
        NodeId inst_hierarchy_node = getInst(nodes);
        if (inst_hierarchy_node != null)
        {
          return inst_hierarchy_node;
        }

        List<Node> nodes1 = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++)
        {
          nodes1.addAll(client.getAddressSpace().browse(nodes.get(i).getNodeId().get()).get());
        }
        nodes = nodes1;
        count++;
      }
      return null;
    } catch (InterruptedException | ExecutionException ex)
    {
      return null;
    }
  }

  private static NodeId getInst(List<Node> nodes) throws InterruptedException, ExecutionException
  {
    for (int i = 0; i < nodes.size(); i++)
    {
      if (nodes.get(i).getBrowseName().get().getName().toLowerCase().contains("_instancehierarchy"))
      {
        return nodes.get(i).getNodeId().get();
      }
    }
    return null;
  }

  private void read_parse_opcua_namespace(DeviceAdapter da, String da_url)
  {
    try
    {
      StopWatch namespaceParsingTimer = new StopWatch();
      namespaceParsingTimer.reset();
      namespaceParsingTimer.start();

      DeviceAdapterOPC opc = (DeviceAdapterOPC) da;
      MSBClientSubscription msbClient = opc.getClient();
      msbClient.startConnection(da_url);
      OpcUaClient client = msbClient.getClientObject();

      logger.info("\n***** Starting namespace browsing ***** \n");

      Element node = new Element("DeviceAdapter");
      Set<String> ignore = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
      ignore.addAll(Arrays.asList(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.ignore").split(",")));

      logger.info("Browse instance Hierarchy started");

      NodeId InstaceHierarchyNode = browseInstaceHierarchyNode("", client, new NodeId(0, 84));
      if (InstaceHierarchyNode != null)
      {
        logger.info("Browse instance Hierarchy ended with: " + InstaceHierarchyNode.getIdentifier().toString());
        node.addContent(msbClient.browseNode(client,
                InstaceHierarchyNode,
                Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level")),
                ignore));
      }

      Element nSkills = new Element("Skills");
      nSkills.addContent(msbClient.browseNode(client,
              new NodeId(2, ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.namespace.skills")),
              Integer.valueOf(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.parser.level.skills")),
              ignore));

      // print to file the XML structure extracted from the browsing process
      
      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      
      xmlOutput.output(node, new FileWriter(XML_PATH + "\\main_" + da.getSubSystem().getName() + ".xml", false));
      xmlOutput.output(nSkills, new FileWriter(XML_PATH + "\\skills_" + da.getSubSystem().getName() + ".xml", false));
      
      logger.info("Starting DA Parser **********************");

      boolean ok = da.parseDNToObjects(client, node, nSkills, true);

      logger.info("***** End namespace browsing ***** \n\n");

      if (ok)
      {
        DatabaseInteraction.write_stuff.acquire();
        workstationRegistration(da);
        DatabaseInteraction.write_stuff.release();
        
        MSB_gui.updateTableAdaptersSemaphore(String.valueOf(
                PECManager.getInstance().getExecutionMap().get(da.getSubSystem().getUniqueId()).availablePermits()),
                da.getSubSystem().getName());
        
        
      } else
      {
        System.out.println("parseDNToObjects FAILED!");
      }

      PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
      Long time = namespaceParsingTimer.getTime();
      perfMeasure.getNameSpaceParsingTimers().add(time);
      logger.info("Namespace Parsing took " + time.toString() + "ms to be executed");
      namespaceParsingTimer.stop();

      if (client == null)
      {
        logger.info("Client = null?");
      }
    } catch (Exception ex)
    {
      java.util.logging.Logger.getLogger(OPCServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
