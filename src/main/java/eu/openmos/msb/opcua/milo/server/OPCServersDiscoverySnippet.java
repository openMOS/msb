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
  private final IOPCNotifyGUI servers_dynamic;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final String MSB_OPCUA_SERVER_ADDRESS;

  /**
   * @param _msb_address
   * @brief Class constructor
   * @param _LDS_uri
   * @param _servers_dynamic
   */
  public OPCServersDiscoverySnippet(String _LDS_uri, String _msb_address, IOPCNotifyGUI _servers_dynamic)
  {
    LDS_uri = _LDS_uri;
    servers_dynamic = _servers_dynamic;
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
    // notify gui of the pooling mechanism
    servers_dynamic.on_polling_cycle();
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
   *
   * @return
   */
  private int checkDBServersStatus()
  {
    int retMsg = 0;

    ArrayList<String> devices = DatabaseInteraction.getInstance().listAllDeviceAdapters();
    String address = "";

    EndpointDescription[] endpointsFromServer = null;
    String DeviceToRemove = null;
    for (String device : devices)
    {
      try
      {
        DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(device);
        address = ((MSBClientSubscription) da.getClient()).getClientObject().getStackClient().getEndpointUrl();
        endpointsFromServer = UaTcpStackClient.getEndpoints(address).get();
      } catch (InterruptedException | ExecutionException ex)
      {
        this.logger.error(ex.getMessage());
        if (ex.getCause().getMessage().contains("Connection refused"))
        {
          //DELETE SERVER FROM DATABASE, HASHMAP AND TABLE
          servers_dynamic.on_endpoint_dissapeared(device);
          retMsg = removeDownServer(device);
          if (DeviceToRemove != null)
          {
            DeviceToRemove = device;
          }
        }
      }

      if (endpointsFromServer == null)
      {
        System.out.println();
        this.logger.warn("This server doens't have Endpoints available:  {}", device);
        //delete it from db and hashmap
        retMsg = removeDownServer(device);
        if (DeviceToRemove != null)
        {
          DeviceToRemove = device;
        }

      } else if (endpointsFromServer.length == 0)
      {
        this.logger.warn("This server doens't have Endpoints available:  {}", device);
        //delete it from db and hashmap
        retMsg = removeDownServer(device);
        if (DeviceToRemove != null)
        {
          // TODO
        }

      }
    }

    if (DeviceToRemove != null && address != null)
    {
      servers_dynamic.on_endpoint_dissapeared(DeviceToRemove);
    }

    return retMsg;
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
              // TODO af-silva validate this
              ApplicationDescription[] serverList = UaTcpStackClient.findServers(da_url).get(); //new MSB            
              if (serverList[0].getApplicationType() != ApplicationType.DiscoveryServer)
              {

                DeviceAdapterOPC opc = (DeviceAdapterOPC) manager.getDeviceAdapter(daName);
                MSBClientSubscription instance = opc.getClient();

                //start connection after inserting on the hashmap!
                instance.startConnection(da_url);

                // Iterate over all values, using the keySet method.
                // call SendServerURL() method from device
                OpcUaClient client = instance.getClientObject();
                if (daName != null && !daName.contains("MSB"))
                {

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
              servers_dynamic.on_new_endpoint_discovered(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
            }

          }
        } catch (InterruptedException | ExecutionException e)
        {
          this.logger.error("Cannot discover Endpoints from URL {} : {}", da_url, e.getMessage());
          System.out.println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + da_url + ": " + e.getMessage());

          if (e.getCause().getMessage().contains("Connection refused"))
          {
            //DELETE SERVER FROM DATABASE AND HASHMAP
            servers_dynamic.on_endpoint_dissapeared(serverApp.getApplicationName().getText());
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
   * @param ServerName
   * @return
   */
  private int removeDownServer(String ServerName)
  {
    if (DACManager.getInstance().deleteDeviceAdapter(ServerName))
    {
      System.out.println("DownServer successfully deleted from DB!");
      servers_dynamic.on_endpoint_dissapeared(ServerName);
      return 1;
    } else
    {
      System.out.println("ERROR deleting DownServer from DB!");
      return -1;
    }
  }
}
