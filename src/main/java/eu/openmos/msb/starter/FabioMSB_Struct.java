/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.opcua.utils.OpcUaServersDiscoverySnippet;
import eu.openmos.msb.opcua.utils.OPCDeviceDiscoveryItf;
import eu.openmos.msb.database.stateless.DeviceRegistryBean;
import com.prosysopc.ua.ServiceException;
import eu.openmos.msb.opcua.prosys.server.OPCUAServer;
import eu.openmos.msb.opcua.prosys.client.OPCUAClient;
import eu.openmos.msb.starter.MyHashMaps;


import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ServerConnectionException;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.msb.opcua.milo.server.opcuaServerMSB;
import eu.openmos.msb.recipesmanagement.RecipesDeployer;
import eu.openmos.msb.recipesmanagement.RecipesDeployerImpl;
import eu.openmos.msb.opcua.utils.CheckOPCServers;
import eu.openmos.msb.opcua.utils.OpcUaDiscoveryMilo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

import org.eclipse.milo.opcua.stack.core.util.ConversionUtil;

import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Endpoint;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author fabio.miranda
 */
public class FabioMSB_Struct {


    private static final Map<String, Object> OPCclientIDMap = new HashMap<String, Object>();
    private static String MSB_OPCUA_SERVER_ADDRESS=null;
    //private static fabio_opcua_badjoraz.prosysclient.OPCUAClient opcua_client_instance;
    private static eu.openmos.msb.opcua.prosys.server.OPCUAServer opcuaServerInstancePROSYS;
    private static eu.openmos.msb.opcua.milo.server.opcuaServerMSB opcuaServerInstanceMILO;
    
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /*
        opcuaServerInstancePROSYS = new OPCUAServer(); //old MSB prosys server

        //launch PROSYS MSB OPCUA Server endpoint
        if (opcuaServerInstancePROSYS.getServer() == null) {
            try {
                opcuaServerInstancePROSYS.create_server("MSB_OPCUA_SERVER");
            } catch (Exception ex) {
                Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("MSB Server already created!\n");
        }*/

        opcuaServerInstanceMILO = new opcuaServerMSB(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.server")); //new MSB Milo server
        //launch MILO MSB OPCUA Server endpoint
        if (opcuaServerInstanceMILO.control == false) {
            try {
                opcuaServerInstanceMILO.startup().get();
                opcuaServerInstanceMILO.register(ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service"));
                /*final CompletableFuture<Void> future = new CompletableFuture<>();
                Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));
                future.get();*/
            } catch (Exception ex) {
                Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("MSB Server already created!\n");
        }

        try{
            //OpcUaDiscoveryMilo discMilo=new OpcUaDiscoveryMilo("opc.tcp://172.18.2.89:12636/test-device-server"); 
            //discMilo.start();
            //discMilo.register("opc.tcp://172.18.2.89:4840");
       
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OpcUaDiscoveryMilo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {//test to see if the server is running at the first discovery
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
       
        //launch Discovery Service to search for other devices (OPCUA servers from devices)
        OPCDeviceDiscoveryItf OPCdevDiscItf;
        OPCdevDiscItf = new OPCDeviceDiscoveryItf() {
            @Override
            public void on_new_server(String name, String app_uri) {
                    System.out.println("server found: "+app_uri);
            }

            @Override
            public void on_new_endpoint(String name, String app_uri) {
                //every time there is a "polling" cycle in discovery, check OPC client/server connections
                System.out.println("POLLING CYCLE********************************");
                if (OPCclientIDMap.size() > 0) { //verify client connection status in the end of the polling cycle
                    
                    /*CheckOPCServers checkServers = new CheckOPCServers(OPCclientIDMap);
                    Map<String, String> DownServers = checkServers.run();
                    
                    for(String key: DownServers.keySet()){
                    
                    System.out.println("o qq aparece?? :" + DownServers.keySet() + DownServers.get(key));
                    }
                    
                    if (DownServers.size() > 0) {   //if exists down servers, remove them from DB
                    System.out.println("Remove server from DB!********************************");
                    RemoveDownServers(DownServers);
                    }*/
                    
                    /*for (String key : OPCclientIDMap.keySet()) {
                    MSB_MiloClientSubscription TempSubscription = (MSB_MiloClientSubscription) OPCclientIDMap.get(key);
                    
                    System.out.println("a tentar sacar o cliente do clientsubscription: " + TempSubscription);
                    //OpcUaClient client=TempSubscription.milo_client_instanceMSB;
                    if (TempSubscription.getClientObject() != null) {
                    OpcUaClient client = TempSubscription.getClientObject();
                    }else{
                    System.out.println("calhou cócó********************************");
                    }
                    }*/
                }
                //DeviceRegistryBean dbMSB = new DeviceRegistryBean();
                //dbMSB.edit_device(parent_app_uri, "null", "null", app_uri , "opcua");
                System.out.println("NAME: " + name + " URL: " + app_uri);

                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                //System.out.println("Endpoint : " + parent_app_uri + " " + app_uri );
                DeviceRegistryBean dbMSB = new DeviceRegistryBean();
                ArrayList ArrayData = dbMSB.read_device_info(name);
                //OPCUAClient OPCClient;
                
                if (ArrayData.isEmpty()) { //if the device name doesn't exist in the database, register 
                    registerServer(name, app_uri);
                    System.out.println("onNewServer Registered Server: " + name + " " + app_uri);
                    if(name.contains("MSB")){
                        MSB_OPCUA_SERVER_ADDRESS=app_uri;
                    }
                    
                    if (app_uri.contains("4840")) {
                        System.out.println("Don't create an opc client for LDS!");
                    } else {

                        try {
                            //opcua_client_instance = new OPCUAClient(); //prosys?
                            //opcua_client_instance.initialize(app_uri); //prosys?
                            MSB_MiloClientSubscription instance = new MSB_MiloClientSubscription();
                            instance.startConnection(app_uri);

                            OPCclientIDMap.put(name, instance); //save the client objectID and the name of the device as hashmap
                            //MyHashMaps hash = null;
                            
                            MyHashMaps myOpcUaClientsMap = MyHashMaps.getInstance(); //singleton to access client objects in other classes
                            myOpcUaClientsMap.setOPCclientIDMaps(name, instance);
                            
                            // Iterate over all values, using the keySet method.
                                                       
                            //call SendServerURL() method from device
                            OpcUaClient client = instance.getClientObject();
                            if (!name.contains("MSB")) {
                                
                                instance.SendServerURL(client, MSB_OPCUA_SERVER_ADDRESS).exceptionally(ex -> {
                                    System.out.println("error invoking SendServerURL() for server: " + name + "\n" +ex); //logger.error("error invoking SendServerURL()", ex);
                                    return "-1.0";
                                }).thenAccept(v -> {
                                    //logger.info("SendServerURL(cenas)={}", v);
                                    System.out.println("SendServerURL(uri)={}\n" + v);
                                    //future.complete(client);
                                });
                            }
                            
                            
                            // call the DeviceXMLmethod function ->call a method
                            /*DeviceXMLmethod(client, "jasus").exceptionally(ex -> {
                            logger.error("error invoking DeviceXMLmethod()", ex);
                            return "-1.0";
                            }).thenAccept(v -> {
                            logger.info("DeviceXMLmethod(cenas)={}", v);
                            
                            future.complete(client);
                            });*/
                            
                            
                            if (client == null) {
                                System.out.println("Client = null?");
                            }

                            for (String key : OPCclientIDMap.keySet()) {
                                System.out.println(key + " ->hashmap<- - " + OPCclientIDMap.get(key));
                            }
                            MyHashMaps myOpcUaClientsMap2 = MyHashMaps.getInstance(); //singleton to access client objects in other classes
                            for (String key : myOpcUaClientsMap.getOPCclientIDMaps().keySet()) {
                                System.out.println(key + " ->singleton hashmap<- - " + myOpcUaClientsMap.getOPCclientIDMaps().get(key));
                            }
                            
                        } catch (ServiceException ex) {
                            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    
                } else {  //if the device name exist in the database, check if it is still available 
                    System.out.println("Server : " + name + " " + app_uri + " Already registered in the Database!");
                    //check if it's still online ->ping?
                    /*if(pingUrl(app_uri)){//check currentTimeNode
                    System.out.println("Server : " + parent_app_uri + " " + app_uri + " is still online!");
                    }else{
                    System.out.println("Server : " + parent_app_uri + " " + app_uri + " is offline!"); //if the device is not available, deregister
                    //dbMSB.deregister_device(parent_app_uri);
                    }*/
                }
            }

            @Override
            public void on_endpoint_dissapeared() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void reset_tables() {
                // dropServersAndEndpoints(); //ver
                System.out.println("reset endpoint and servers tables...");
            }

            @Override
            public void on_server_dissapeared(String name, String app_uri) {
                System.out.println("This server has disapeared: " + name);
                RemoveDownServer(name);
                
                for (String key : OPCclientIDMap.keySet()) {
                    System.out.println(key + " ->new local hashmap<- - " + OPCclientIDMap.get(key));
                }
                MyHashMaps myOpcUaClientsMap = MyHashMaps.getInstance(); //singleton to access client objects in other classes
                for (String key : myOpcUaClientsMap.getOPCclientIDMaps().keySet()) {
                    System.out.println(key + " ->new singleton hashmap<- - " + myOpcUaClientsMap.getOPCclientIDMaps().get(key));
                }
            }

        };
        // String LDS_uri="opc.tcp://localhost:4840";
        String LDS_uri = ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service");

        
        OpcUaServersDiscoverySnippet OPCDiscoverySnippet=new OpcUaServersDiscoverySnippet(LDS_uri, OPCdevDiscItf); //descomentar 13-2-17
        OPCDiscoverySnippet.start();//descomentar 13-2-17
        
        //TODO
       
        //create a new OPCDevice with structure methods - common to all OPCcommunication?
        OPCDeviceItf Device = new OPCDeviceItf(); //inputs? endpoints, MAP<ID, OPCclientObject> ?
        
         String address = "http://0.0.0.0:9997/wsRecipesDeployer";
        //Endpoint.publish(address, new NewWebService());
         Endpoint.publish(address, new RecipesDeployerImpl());
         System.out.println("Listening: " + address);
         
        
            //Device.AllCases("deviceregistration", "dummyregistry"); //simulate a device registration
            //regist device in the DB
            
            //retrieve device address and protocol
            /*DeviceRegistryBean dbMSB = new DeviceRegistryBean();
            ArrayList Arrays= dbMSB.get_device_address_protocol(7);
            System.out.println(Arrays.toString());*/
            
            //List Fargs = new ArrayList();
            //Fargs.add("112");
            //Device.EquipmentCommunication("requestproduct", Fargs);
            
            
            /*try {
            MSB_MiloClientSubscription.startConnection("opc.tcp://FMIRANDA-PC.introsys.lan:52520/OPCUA/MSB_OPCUA_SERVER");
            //opcua_client_instance=new OPCUAClient();
            //opcua_client_instance.initialize("opc.tcp://FMIRANDA-PC:53531/OPCUA/SimuServerXPTO");//opc.tcp://FMIRANDA-PC.introsys.lan:52520/OPCUA/MSB_OPCUA_SERVER
            } catch (ServiceException ex) {
            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }*/

    }
    
    private static String registerServer(java.lang.String serverName, java.lang.String serverUri) {
        //msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
        //msbweb.DBInteraction port = service.getDBInteractionPort();
        //return port.registerServer(serverName, serverUri);*/
        DeviceRegistryBean dbMSB = new DeviceRegistryBean();
        String res=dbMSB.register_device(serverName, "null", "null", serverUri, "opcua");
        System.out.println(res);
                
       return res;
    }
    
    /*private static String RemoveDownServers(Map<String, String> DownServers) {
            
        for (String key : DownServers.keySet()) {
            System.out.println("Extracting errorMessage from the hashMAP...");
            String errorMessage =  DownServers.get(key);
            if(errorMessage.contains("Connection refused")){
                //delete server from DB
                DeviceRegistryBean dbMSB = new DeviceRegistryBean();
                String ServerName=dbMSB.get_device_name(key);
                int res=dbMSB.deregister_device(ServerName);
                 
                if(res!=-999){
                    System.out.println("DownServer successfully deleted from DB!");
                    return "OK";
                }else{
                    System.out.println("ERROR deleting DownServer from DB!");
                    return "ERROR";
                }
            } 
        }
        return null;
    }*/
    
    public static int RemoveDownServer(String ServerName) {

        MyHashMaps myOpcUaClientsMap = MyHashMaps.getInstance(); //singleton to access client objects in other classes
        myOpcUaClientsMap.deleteOPCclientIDMaps(ServerName); //remove server from singleton Hashmap
        OPCclientIDMap.remove(ServerName); //remove server from HashMap
        //delete server from DB
        DeviceRegistryBean dbMSB = new DeviceRegistryBean();
        int res = dbMSB.deregister_device(ServerName);

        if (res != -999) {
            System.out.println("DownServer successfully deleted from DB!");
            return 1;
        } else {
            System.out.println("ERROR deleting DownServer from DB!");
            return -1;
        }

    }
    
    public static boolean pingUrl(final String address) {
        
        String[] temp=address.split(":");
        String addressFiltered=temp[1];
        System.out.println("Trying to Ping: "+addressFiltered);
        
        
        try {
            //boolean reachable = InetAddress.getByName(addressFiltered).isReachable(200);
            if(InetAddress.getByName(addressFiltered).isReachable(200))
                return true;
            
        } catch (IOException ex) {
            Logger.getLogger(FabioMSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        /*try {
            final URL url = new URL("http://" + addressFiltered);
            final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
            final long startTime = System.currentTimeMillis();
            urlConn.connect();
            final long endTime = System.currentTimeMillis();
                if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                 System.out.println("Time (ms) : " + (endTime - startTime));
                 System.out.println("Ping to "+addressFiltered +" was success");
                 return true;
                }
        } catch (final MalformedURLException e1) {
         e1.printStackTrace();
        } catch (final IOException e) {
         e.printStackTrace();
        }*/
        return false;
    }
   
            
}

