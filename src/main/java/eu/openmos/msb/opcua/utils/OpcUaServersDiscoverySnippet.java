package eu.openmos.msb.opcua.utils;

import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import java.util.ArrayList;
import java.util.List;
import eu.openmos.msb.starter.MSB_gui;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

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
    private final OPCDeviceDiscoveryItf servers_dynamic;

    /**
     * @brief TODO fabio
     * @param _LDS_uri
     * @param _servers_dynamic
     */
    public OpcUaServersDiscoverySnippet(String _LDS_uri, OPCDeviceDiscoveryItf _servers_dynamic)
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
                CheckDBServersStatus();
                sleep(discovery_period * 1000);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @brief TODO fabio
     * @param serverApp
     * @param applicationUri
     * @return
     * @throws URISyntaxException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws Exception
     */
    protected EndpointDescription discoverEndpoints(org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription serverApp, String applicationUri) throws URISyntaxException, InterruptedException, ExecutionException, Exception
    {
        final String[] discoveryUrls = serverApp.getDiscoveryUrls(); //OLDMSB
        if (discoveryUrls != null)
        {
            int i = 0;
            List<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription> edList = new ArrayList<>();
            for (String url : discoveryUrls)
            {
                try
                {
                    for (org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get())
                    {
                        edList.add(ed);
                        System.out.println("EndPoints from: " + url + " = " + ed);
                        //before 13-12-17 args-> app_uri , ed.getEndpointUrl()
                        servers_dynamic.on_new_endpoint(serverApp.getApplicationName().getText(), ed.getEndpointUrl());
                    }
                } catch (Exception e)
                {
                    println("Cannot discover Endpoints from URL " + url + ": " + e.getMessage());
                    println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + url + ": " + e.getMessage());
                    if (e.getCause().getMessage().contains("Connection refused"))
                    {
                        //DELETE SERVER FROM DATABASE AND HASHMAP

                        servers_dynamic.on_server_dissapeared(serverApp.getApplicationName().getText(), url);
                    }
                }
            }

        } else
        {
            println("No suitable discoveryUrl available: using the current Url");
        }
        return null;
    }

    /**
     *
     * @param uri
     * @return
     */
    boolean discoverServer(String uri)
    {
        try
        {
            // Discover a new server list from a discovery server at URI

            org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription[] serverList = UaTcpStackClient.findServers(uri).get(); //new MSB

            if (serverList.length == 0)
            {
                println("No servers found");
                return false;
            }

            for (int i = 0; i < serverList.length; i++)
            {
                final org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription s = serverList[i];

                servers_dynamic.on_new_server(s.getApplicationName().getText(), s.getApplicationUri());
                /*
              * name, application
                 */

                System.out.println("getApplicationUri output " + s.getApplicationUri());
                System.out.println("getDiscoveryUrls output " + s.getDiscoveryUrls()[0]);

                try
                {
                    if (s.getApplicationType() != org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer) //discover endpoints only from servers. not from discovery
                    {
                        discoverEndpoints(s, s.getApplicationUri());
                    }
                } catch (Exception ex)
                {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return true;
        } catch (InterruptedException ex)
        {
            Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex)
        {
            Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param list
     * @param element
     * @return
     */
    private boolean check_if_object_exists(List<String> list, String element)
    {
        return list.contains(element);
    }

    /**
     *
     * @param in
     */
    void println(String in)
    {
        System.out.println(in);
    }

    /**
     *
     * @param name
     * @param url
     * @return
     */
    boolean on_server_dissapeared(String name, String url)
    {
        System.out.println("url disapeared: " + url);
        System.out.println("name disapeared: " + name);
        return true;
    }

    /**
     *
     * @return
     */
    int CheckDBServersStatus()
    {

        ArrayList<String> devices = DatabaseInteraction.getInstance().listAllDeviceAdapters();
        String address = null;
        org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription[] endpointsFromServer = null;
        int retMsg = 0;
        String DevicetoRemove = null;

        for (String device : devices)
        {
            try
            {
                DeviceAdapter da = DACManager.getInstance().getDeviceAdapter(device);
                address = ((MSB_MiloClientSubscription) da.getClient()).getClientObject().getStackClient().getEndpointUrl();
                endpointsFromServer = UaTcpStackClient.getEndpoints(address).get();
                   
                // OPCUA Client TODO - Add DDS and MQTT af-silva

            } catch (InterruptedException | ExecutionException ex)
            {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println("This server doens't have Endpoints available:  " + device);
                //delete it from db and hashmap
                retMsg = MSB_gui.RemoveDownServer(device);
                if (DevicetoRemove != null)
                {
                    DevicetoRemove = device;
                }

            } else if (endpointsFromServer.length == 0)
            {
                System.out.println("This server doens't have Endpoints available: " + device);
                //delete it from db and hashmap
                retMsg = MSB_gui.RemoveDownServer(device);
                if (DevicetoRemove != null)
                {
                }

            }
        }

        
        if (DevicetoRemove != null && address != null)
        {
            servers_dynamic.on_server_dissapeared(DevicetoRemove, address);
        }

        return retMsg;
    }
}
