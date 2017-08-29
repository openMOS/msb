package eu.openmos.msb.opcua.milo.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.EProtocol;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import java.util.logging.Level;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    DACManager manager = DACManager.getInstance();
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
            System.out.println("EndPoints from: " + url + " = " + ed);

            String daName = serverApp.getApplicationName().getText();
            DeviceAdapter da = manager.getDeviceAdapter(daName);

            if (da == null)
            {
              manager.addDeviceAdapter(daName, EProtocol.OPC, "", "");
              da_url = ed.getEndpointUrl();

              //new MSB            
              ApplicationDescription[] serverList = UaTcpStackClient.findServers(da_url).get(); 
              if (serverList[0].getApplicationType() != ApplicationType.DiscoveryServer)
              {

                DeviceAdapterOPC opc = (DeviceAdapterOPC) manager.getDeviceAdapter(daName);
                MSBClientSubscription instance = opc.getClient();                
                instance.startConnection(da_url);
                OpcUaClient client = instance.getClientObject();
                
               
                // Iterate over all values, using the keySet method.
                // call SendServerURL() method from device
                
                if (daName != null && !daName.contains("MSB") && !daName.contains("discovery"))
                {

                  
                  
                  // oompa loompas at work here
                  System.out.println("\n");
                  System.out.println("***** Starting namespace browsing *****");
                  
                  System.out.println("Identifiers id " + Identifiers.References);
                  System.out.println("Identifiers id " + Identifiers.HasTypeDefinition);
                  
                  
                  System.out.println("\n");
                  instance.browseNode("", client, new NodeId(2, "Masmec_InstanceHierarchy/Transport1"), 4);
                  System.out.println("\n");
                  System.out.println("***** End namespace browsing *****");
                  System.out.println("\n");
                  
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
              notify_channel.on_new_endpoint_discovered(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
            }

          }
        } catch (InterruptedException | ExecutionException e)
        {
          this.logger.error("Cannot discover Endpoints from URL {} : {}", da_url, e.getMessage());
          System.out.println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + da_url + ": " + e.getMessage());

          if (e.getCause().getMessage().contains("Connection refused"))
          {
            String daName = serverApp.getApplicationName().getText();
            if (manager.getDeviceAdapter(daName) != null)
            {
              removeDownServer(daName);
            }
          }
        } catch (Exception ex)
        {
          java.util.logging.Logger.getLogger(OPCServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
        }
      }

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
}
