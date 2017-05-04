/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import eu.openmos.msb.opcua.utils.OPCDeviceDiscoveryItf;

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

/*import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.ContentFilterBuilder;
import com.prosysopc.ua.EventNotifierClass;
import com.prosysopc.ua.MethodCallStatusException;
import com.prosysopc.ua.MonitoredItemBase;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UaAddress;
import com.prosysopc.ua.UaApplication.Protocol;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredEventItem;
import com.prosysopc.ua.client.MonitoredEventItemListener;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.ServerConnectionException;
import com.prosysopc.ua.client.ServerList;
import com.prosysopc.ua.client.ServerListException;
import com.prosysopc.ua.client.ServerStatusListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionAliveListener;
import com.prosysopc.ua.client.SubscriptionNotificationListener;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.client.UaClientListener;
import com.prosysopc.ua.nodes.MethodArgumentException;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaInstance;
import com.prosysopc.ua.nodes.UaMethod;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaObject;
import com.prosysopc.ua.nodes.UaReferenceType;
import com.prosysopc.ua.nodes.UaType;
import com.prosysopc.ua.nodes.UaVariable;
import com.prosysopc.ua.types.opcua.AnalogItemType;*/
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.database.stateless.DeviceRegistryBean;
import eu.openmos.msb.opcua.milo.client.X509IdentityProvider;
import eu.openmos.msb.starter.FabioMSB_Struct;
import eu.openmos.msb.starter.MSB_MiloClientSubscription;
import eu.openmos.msb.starter.MyHashMaps;
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
public class OpcUaServersDiscoverySnippet extends Thread{

    private final int discovery_period = 10; /*35 seconds*/
    private final String LDS_uri;
    private final OPCDeviceDiscoveryItf servers_dynamic;
    private final List<String> list_servers = new ArrayList<String>();
    private final List<String> list_endpoints = new ArrayList<String>();

    public OpcUaServersDiscoverySnippet(String _LDS_uri, OPCDeviceDiscoveryItf _servers_dynamic){
        LDS_uri = _LDS_uri;
        servers_dynamic = _servers_dynamic;
    }
    
    @Override
    public void run() {
        
        while(true){
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
            //org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription[] discoveryUrls2 = UaTcpStackClient.getEndpoints(serverApp.getDiscoveryUrls()[0]).get();
            //final String[] discoveryUrls2 = serverApp.getDiscoveryUrls();
            
            
            if (discoveryUrls != null) {
                    //UaClient discoveryClient = new UaClient(); //prosys oldMSB
           
                    int i = 0;
                    List<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription> edList = new ArrayList<org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription>();

                    /*println("Available endpoints: ");
                    println(String.format("%s - %-50s - %-20s - %-20s - %s", "#", "URI", "Security Mode", "Security Policy",
                                    "Transport Profile"));*/ //descomentar 14/2/17
                    for (String url : discoveryUrls) {
                        
//********workshop modification 
            //OpcUaClientConfig config = OpcUaClientConfig.builder().setApplicationName(LocalizedText.english("opc-ua client")).setApplicationUri("urn:opcua client").setCertificate(cert).setKeyPair(keyPair).setEndpoint(endpoint).setIdentityProvider(x509IdentityProvider)./*setIdentityProvider(clientExample.getIdentityProvider()).*/setRequestTimeout(uint(5000)).build();
            //OpcUaClient client = new OpcUaClient(config);
            //client.connect();
            //OpcUaClient discoveryClient2 = new OpcUaClient(config); //TODO workshop problem!

            /*SecurityPolicy securityPolicy = SecurityPolicy.Basic256; // For example : SecurityPolicy.Basic256
            String securityMode = "SignAndEncrypt"; // For example : "SignAndEncrypt"
                        org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription endpoint = null;
                        try {
                            org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(url).get();
                            endpoint = Arrays.stream(endpoints)
                                    .filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getSecurityPolicyUri()))//
                                    .filter(e -> e.getSecurityMode().toString().compareTo(securityMode) == 0)//
                                    .findFirst()//
                                    .orElseThrow(() -> new Exception("no desired endpoints returned"));
                        } catch (Exception e) {
                            println("error getting endpoints from URL " + url + ": " + e.getMessage());
                        }
                        try {
                            println("Getting servers from URL " + url + ": " + UaTcpStackClient.findServers(url).toString());
                        } catch (Exception e) {
                            println("error getting endpoints from URL " + url + ": " + e.getMessage());
                        }
      // EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints("opc.tcp://fmoranda-pc.introsys.lan:4840").get();

      
              X509IdentityProvider x509IdentityProvider = new X509IdentityProvider("MSB_OPCUA_SERVER@FMIRANDA-PC.introsys.lan.der",
                    "MSB_OPCUA_SERVER@FMIRANDA-PC.introsys.lan.pem");
            X509Certificate cert = x509IdentityProvider.getCertificate();
            java.security.KeyPair keyPair = new java.security.KeyPair(cert.getPublicKey(), x509IdentityProvider.getPrivateKey());
            OpcUaClientConfig clientConfig = OpcUaClientConfig.builder().setApplicationName(org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText.english("opc-ua milo client"))//
                    .setApplicationUri("urn:opcua client")//
                    .setCertificate(cert)//
                    .setKeyPair(keyPair)//
                    .setEndpoint(endpoint)//
                    .setIdentityProvider(x509IdentityProvider)//
                    // .setIdentityProvider(clientExample.getIdentityProvider())//
                    .setRequestTimeout(uint(10000))//
                    .build();
            OpcUaClient client = new OpcUaClient(clientConfig);
            
            System.out.println("Client adress space: "+client.getAddressSpace().toString());
            
            
                        //ANOTHER WAY for creating milo client
                        MSB_MiloClientSubscription instance = new MSB_MiloClientSubscription();
                        instance.startConnection(url);
                        OpcUaClient TestMiloClient = instance.getClientObject();
                        
                        TestMiloClient.getAddressSpace().toString();
                         System.out.println("TestMiloClient adress space: "+TestMiloClient.getAddressSpace().toString());
                         System.out.println("TestMiloClient getEndpointUrl: "+TestMiloClient.getStackClient().getEndpointUrl());
            */
//****************end workshop modification

                            //discoveryClient.setUri(url);//prosys oldMSB
                            try {
                                    //for (EndpointDescription ed : discoveryClient.discoverEndpoints()) {
                                    for (org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription ed : UaTcpStackClient.getEndpoints(url).get()) {
                                            /*println(String.format("%s - %-50s - %-20s - %-20s - %s", i++, ed.getEndpointUrl(),
                                                            ed.getSecurityMode(),
                                                            ed.getSecurityPolicyUri().replaceFirst("http://opcfoundation.org/UA/SecurityPolicy#",
                                                                            ""),
                                                            ed.getTransportProfileUri()
                                                                            .replaceFirst("http://opcfoundation.org/UA-Profile/Transport/", "")));*/ //descomentar 14/2/17
                                            edList.add(ed);
                                            System.out.println("EndPoints from: "+url+" = "+ ed);
                                            //if (check_if_object_exists(list_endpoints, app_uri) == true){
                                                //do nothing
                                            //} else {
                                                //list_endpoints.add(ed.getEndpointUrl());
                                                servers_dynamic.on_new_endpoint(serverApp.getApplicationName().getText(), ed.getEndpointUrl()); //before 13-12-17 args-> app_uri , ed.getEndpointUrl()
                                            //}
                                                
                            
                                            //@TODO create a hashmap with the present endpoints and check if endpointsare gone
                                            //@TODO if endpoints are gone, remove endpoint from database
                                    }
                            } catch (Exception e) {
                                    println("Cannot discover Endpoints from URL " + url + ": " + e.getMessage());
                                    println("DELETE THIS SERVER FROM DB IF CONNECTION LOST? " + url + ": " + e.getMessage());
                                  
                                    if(e.getCause().getMessage().contains("Connection refused")){
                                        //DELETE SERVER FROM DATABASE AND HASHMAP
                                        //on_server_dissapeared(serverApp.getApplicationName().getText(),url);
                                        servers_dynamic.on_server_dissapeared(serverApp.getApplicationName().getText(), url);
                                    }
                            }
                    }


            } else
                    println("No suitable discoveryUrl available: using the current Url");
            return null;
    }

    boolean discoverServer(String uri) throws ServerListException, URISyntaxException, InterruptedException, ExecutionException {
            // Discover a new server list from a discovery server at URI
            //ServerList serverList = new ServerList(uri); //old MSB 09-03-17
            
            org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription[] serverList = UaTcpStackClient.findServers(uri).get(); //new MSB
            
            /*
            Changes to Milo 08-03-17
            */
            /*URI LDSURI=new URI(ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service"));
            
            CertificateManager certificateManager=new DirectoryCertificateManager(keyPair,certificate,securityDir);
            UaTcpStackServerConfig cfg=UaTcpStackServerConfig.builder().setServerName("example").setApplicationName(LocalizedText.english("Stack Example Server")).setApplicationUri(String.format("urn:example-server:%s",UUID.randomUUID())).setCertificateManager(certificateManager).build();
            server=new UaTcpStackServer(config);
            UaTcpStackServer ServerEndpoint = new UaTcpStackServer(cfg);
            ServerEndpoint.addDiscoveryUrl(LDSURI);
            DiscoveryServiceSet serviceSet;
            ServerEndpoint.addServiceSet(serviceSet);*/
            
             /*
            Changes to Milo 08-03-17
            */
             
            if (serverList.length == 0) {
                    println("No servers found");
                    return false;
            }
 
            //println(String.format("%s - %-25s - %-15s - %-30s - %s", "#", "Name", "Type", "Product", "Application")); //descomentar 14/2/17
            for (int i = 0; i < serverList.length; i++) {
                    final org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription s = serverList[i];
                    /*println(String.format("%d - %-25s - %-15s - %-30s - %s", i, s.getApplicationName().getText(),
                                    s.getApplicationType(), s.getProductUri(), s.getApplicationUri()));*/ //descomentar 14/2/17

                    servers_dynamic.on_new_server(s.getApplicationName().getText(), s.getApplicationUri()); /*name, application*/
                    //@TODO create a hashmap with the present servers and check if servers are gone
                    //@TODO if servers are gone, remove server from database
                    System.out.println("getApplicationUri output "+ s.getApplicationUri());
                    System.out.println("getDiscoveryUrls output "+ s.getDiscoveryUrls()[0]);
                    
                try {
                    if(s.getApplicationType()!=org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType.DiscoveryServer) //discover endpoints only from servers. not from discovery
                    discoverEndpoints(s, s.getApplicationUri());
                } catch (Exception ex) {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            return true;
    }


    private boolean check_if_object_exists(List<String> list, String element){
        return list.contains(element);
    }


    void println (String in){
        System.out.println(in);
    }

    boolean on_server_dissapeared(String name, String url) {
        System.out.println("url disapeared: "+ url);
        System.out.println("name disapeared: "+ name);
        return true;
    }
    
    
 int CheckDBServersStatus() {
        DeviceRegistryBean dbMSB = new DeviceRegistryBean();
        ArrayList<String> devices = dbMSB.list_all_devices();
        System.out.println("Verifying DB servers status...");
        String address;
        org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription[] endpointsFromServer = null;
        int retMsg = 0;
        String DevicetoRemove=null;
        String Protocol=null;
        //UaTcpStackClient opcChecker = null;

        for (String device : devices) {
            ArrayList<String> addressProtocol = dbMSB.get_device_address_protocol(device);
            if (addressProtocol.size() > 0) {
                address = addressProtocol.get(0); //0->address 1->protocol
                try {
                    endpointsFromServer = UaTcpStackClient.getEndpoints(address).get();
                } catch (InterruptedException ex) {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(OpcUaServersDiscoverySnippet.class.getName()).log(Level.SEVERE, null, ex);
                    if (ex.getCause().getMessage().contains("Connection refused")) {
                        //DELETE SERVER FROM DATABASE, HASHMAP AND TABLE
                        servers_dynamic.on_server_dissapeared(device, address);
                        retMsg = FabioMSB_Struct.RemoveDownServer(device);
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
                retMsg = FabioMSB_Struct.RemoveDownServer(device);
                if (DevicetoRemove != null && Protocol!=null) {
                            DevicetoRemove = device;
                            Protocol = addressProtocol.get(1); //0->address 1->protocol
                        }
                /*int res = dbMSB.deregister_device(device);
                if (res != -999) {
                    System.out.println("DownServer successfully deleted from DB!");
                    retMsg = 1;
                } else {
                    System.out.println("ERROR deleting DownServer from DB!");
                    retMsg = -1;
                }*/
            } else if (endpointsFromServer.length == 0) {
                System.out.println("This server doens't have Endpoints available: " + device);
                //delete it from db and hashmap
                retMsg = FabioMSB_Struct.RemoveDownServer(device);
                if (DevicetoRemove != null && Protocol!=null) {
                            DevicetoRemove = device;
                            Protocol = addressProtocol.get(1); //0->address 1->protocol
                        }
                /*int res = dbMSB.deregister_device(device);
                if (res != -999) {
                    System.out.println("DownServer successfully deleted from DB!");
                    retMsg = 1;
                } else {
                    System.out.println("ERROR deleting DownServer from DB!");
                    retMsg = -1;
                }*/
            }
        }
        
     if (DevicetoRemove != null) {
         servers_dynamic.on_server_dissapeared(DevicetoRemove, Protocol);
     }
        
        return retMsg;
    }
         
}
