package eu.openmos.msb.opcua.utils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import eu.openmos.msb.starter.MSB_gui;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
public class OpcUaServersDiscoverySnippet extends Thread
{

  private final int discovery_period = 10;
  /*
   * 35 seconds
   */
  private final String LDS_uri;
  private final IOPCDeviceDiscovery servers_dynamic;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @brief Class constructor
   * @param _LDS_uri
   * @param _servers_dynamic
   */
  public OpcUaServersDiscoverySnippet(String _LDS_uri, IOPCDeviceDiscovery _servers_dynamic)
  {
    LDS_uri = _LDS_uri;
    servers_dynamic = _servers_dynamic;
  }

  /**
   * @brief TODO fabio
   */
  @Override
  public void run()
  {
    while (true)
    {
      servers_dynamic.reset_tables();
      try
      {
        discoverServer(LDS_uri);
        checkDBServersStatus();
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
        final ApplicationDescription s = serverList[i];

        servers_dynamic.on_new_server(s.getApplicationName().getText(), s.getApplicationUri());

        System.out.println("getApplicationUri output " + s.getApplicationUri());
        System.out.println("getDiscoveryUrls output " + s.getDiscoveryUrls()[0]);

        try
        {
          //discover endpoints only from servers. not from discovery
          if (s.getApplicationType() != DiscoveryServer) 
          {
            discoverEndpoints(s, s.getApplicationUri());
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
   *
   * @return
   */
  private int checkDBServersStatus()
  {
    int retMsg = 0;
    
    ArrayList<String> devices = DatabaseInteraction.getInstance().listAllDeviceAdapters();
    String address = null;
    EndpointDescription[] endpointsFromServer = null;
    String DevicetoRemove = null;
    
    
    for (String device : devices)
    {
      try
      {
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(device);
        address = ((MSBClientSubscription) da.getClient()).getClientObject().getStackClient().getEndpointUrl();
        endpointsFromServer = UaTcpStackClient.getEndpoints(address).get();

        // OPCUA Client TODO - Add DDS and MQTT af-silva
      } catch (InterruptedException | ExecutionException ex)
      {
        this.logger.error(ex.getMessage());
        if (ex.getCause().getMessage().contains("Connection refused"))
        {
          //DELETE SERVER FROM DATABASE, HASHMAP AND TABLE
          servers_dynamic.on_server_dissapeared(device, address);
          retMsg = MSB_gui.RemoveDownServer(device);
          if (DevicetoRemove != null)
          {
            DevicetoRemove = device;
          }
        }
      }

      if (endpointsFromServer == null)
      {
        System.out.println();
        this.logger.warn("This server doens't have Endpoints available:  {}", device);
        //delete it from db and hashmap
        retMsg = MSB_gui.RemoveDownServer(device);
        if (DevicetoRemove != null)
        {
          DevicetoRemove = device;
        }

      } else if (endpointsFromServer.length == 0)
      {
        this.logger.warn("This server doens't have Endpoints available:  {}", device);
        //delete it from db and hashmap
        retMsg = MSB_gui.RemoveDownServer(device);
        if (DevicetoRemove != null)
        {
          // TODO
        }

      }
    }

    if (DevicetoRemove != null && address != null)
    {
      servers_dynamic.on_server_dissapeared(DevicetoRemove, address);
    }

    return retMsg;
  }

  /**
   * @brief TODO fabio
   * @param serverApp
   * @param applicationUri
   * @return
   */
  private EndpointDescription discoverEndpoints(ApplicationDescription serverApp, String applicationUri)
  {
    final String[] discoveryUrls = serverApp.getDiscoveryUrls();
    if (discoveryUrls != null)
    {
      List<EndpointDescription> edList = new ArrayList<>();
      for (String url : discoveryUrls)
      {
        try
        {
          for (EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get())
          {
            edList.add(ed);
            System.out.println("EndPoints from: " + url + " = " + ed);
            servers_dynamic.on_new_endpoint(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
          }
        } catch (InterruptedException | ExecutionException e)
        {
          this.logger.error("Cannot discover Endpoints from URL {} : {}", url, e.getMessage());
          System.out.println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + url + ": " + e.getMessage());

          if (e.getCause().getMessage().contains("Connection refused"))
          {
            //DELETE SERVER FROM DATABASE AND HASHMAP
            servers_dynamic.on_server_dissapeared(serverApp.getApplicationName().getText(), url);
          }
        }
      }

    } else
    {
      System.out.println("No suitable discoveryUrl available: using the current Url");
    }
    return null;
  }
}
