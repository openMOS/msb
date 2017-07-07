/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import com.prosysopc.ua.client.ServerListException;
import com.prosysopc.ua.client.UaClient;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.EndpointDescription;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.PropertyConfigurator;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePathTarget;
import org.opcfoundation.ua.core.DataChangeFilter;
import org.opcfoundation.ua.core.DataChangeTrigger;
import org.opcfoundation.ua.core.DeadbandType;
import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.core.ElementOperand;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.FilterOperator;
import org.opcfoundation.ua.core.HistoryEventFieldList;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.LiteralOperand;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.Range;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.database.stateless.DeviceRegistryBean;
import eu.openmos.msb.opcua.milo.client.X509IdentityProvider;
import eu.openmos.msb.starter.MSB_Struct;
import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import eu.openmos.msb.datastructures.HashMaps;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import org.eclipse.milo.opcua.stack.server.tcp.UaTcpStackServer;
import org.eclipse.milo.opcua.stack.core.application.services.DiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.server.config.UaTcpStackServerConfig;

/**
 *
 * @author Admin
 */
public class OpcUaServersDiscoverySnippet extends Thread {

    private final int discovery_period = 10;
    /*
   * 35 seconds
     */
    private final String LDS_uri;
    private final OPCDeviceDiscoveryItf servers_dynamic;
    private final List<String> list_servers = new ArrayList<String>();
    private final List<String> list_endpoints = new ArrayList<String>();

    public OpcUaServersDiscoverySnippet(String _LDS_uri, OPCDeviceDiscoveryItf _servers_dynamic) {
        LDS_uri = _LDS_uri;
        servers_dynamic = _servers_dynamic;
    }

    @Override
    public void run() {

        while (true) {
            servers_dynamic.reset_tables();

            try {
                discoverServer(LDS_uri);
                CheckDBServersStatus();
            } catch (ServerListException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                sleep(discovery_period * 1000);
                System.out.println("Sleeping...");
            } catch (InterruptedException ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected EndpointDescription discoverEndpoints(org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription serverApp, String applicationUri) throws URISyntaxException, InterruptedException, ExecutionException, Exception {
        final String[] discoveryUrls = serverApp.getDiscoveryUrls(); //OLDMSB

        if (discoveryUrls != null) {

            int i = 0;
            List<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription> edList = new ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription>();

            for (String url : discoveryUrls) {

                try {

                    for (org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get()) {

                        edList.add(ed);
                        System.out.println("EndPoints from: " + url + " = " + ed);

                        servers_dynamic.on_new_endpoint(serverApp.getApplicationName().getText(), ed.getEndpointUrl()); //before 13-12-17 args-> app_uri , ed.getEndpointUrl()

                    }
                } catch (Exception e) {
                    println("Cannot discover Endpoints from URL " + url + ": " + e.getMessage());
                    println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + url + ": " + e.getMessage());

                    if (e.getCause().getMessage().contains("Connection refused")) {
                        //DELETE SERVER FROM DATABASE AND HASHMAP

                        servers_dynamic.on_server_dissapeared(serverApp.getApplicationName().getText(), url);
                    }
                }
            }

        } else {
            println("No suitable discoveryUrl available: using the current Url");
        }
        return null;
    }

    boolean discoverServer(String uri) throws ServerListException, URISyntaxException, InterruptedException, ExecutionException {
        // Discover a new server list from a discovery server at URI

        org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription[] serverList = UaTcpStackClient.findServers(uri).get(); //new MSB

        if (serverList.length == 0) {
            println("No servers found");
            return false;
        }

        for (int i = 0; i < serverList.length; i++) {
            final org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription s = serverList[i];

            servers_dynamic.on_new_server(s.getApplicationName().getText(), s.getApplicationUri());
            /*
       * name, application
             */

            System.out.println("getApplicationUri output " + s.getApplicationUri());
            System.out.println("getDiscoveryUrls output " + s.getDiscoveryUrls()[0]);

            try {
                if (s.getApplicationType() != org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer) //discover endpoints only from servers. not from discovery
                {
                    discoverEndpoints(s, s.getApplicationUri());
                }
            } catch (Exception ex) {
                Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return true;
    }

    private boolean check_if_object_exists(List<String> list, String element) {
        return list.contains(element);
    }

    void println(String in) {
        System.out.println(in);
    }

    boolean on_server_dissapeared(String name, String url) {
        System.out.println("url disapeared: " + url);
        System.out.println("name disapeared: " + name);
        return true;
    }

    int CheckDBServersStatus() {
        DeviceRegistryBean dbMSB = new DeviceRegistryBean();
        ArrayList<String> devices = dbMSB.list_all_devices();
        System.out.println("Verifying DB servers status...");
        String address;
        org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription[] endpointsFromServer = null;
        int retMsg = 0;
        String DevicetoRemove = null;
        String Protocol = null;
        //UaTcpStackClient opcChecker = null;

        for (String device : devices) {
            ArrayList<String> addressProtocol = dbMSB.get_device_address_protocol(device);
            if (addressProtocol.size() > 0) {
                address = addressProtocol.get(0); //0->address 1->protocol
                try {
                    // OPCUA Client TODO - Add DDS and MQTT
                    endpointsFromServer = UaTcpStackClient.getEndpoints(address).get();
                } catch (InterruptedException ex) {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                    if (ex.getCause().getMessage().contains("Connection refused")) {
                        //DELETE SERVER FROM DATABASE, HASHMAP AND TABLE
                        servers_dynamic.on_server_dissapeared(device, address);
                        retMsg = MSB_Struct.RemoveDownServer(device);
                        if (DevicetoRemove != null && Protocol != null) {
                            DevicetoRemove = device;
                            Protocol = addressProtocol.get(1); //0->address 1->protocol
                        }

                    }
                }
            }

            if (endpointsFromServer == null) {
                System.out.println("This server doens't have Endpoints available:  " + device);
                //delete it from db and hashmap
                retMsg = MSB_Struct.RemoveDownServer(device);
                if (DevicetoRemove != null && Protocol != null) {
                    DevicetoRemove = device;
                    Protocol = addressProtocol.get(1); //0->address 1->protocol
                }

            } else if (endpointsFromServer.length == 0) {
                System.out.println("This server doens't have Endpoints available: " + device);
                //delete it from db and hashmap
                retMsg = MSB_Struct.RemoveDownServer(device);
                if (DevicetoRemove != null && Protocol != null) {
                    DevicetoRemove = device;
                    Protocol = addressProtocol.get(1); //0->address 1->protocol
                }

            }
        }

        if (DevicetoRemove != null) {
            servers_dynamic.on_server_dissapeared(DevicetoRemove, Protocol);
        }

        return retMsg;
    }

}
