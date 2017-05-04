/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import eu.openmos.msb.opcua.utils.DevicesDynamic;
//import Connections.Manager;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.UaClient;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.opcua.prosys.client.OPCUAClient;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opcfoundation.ua.core.MonitoringMode;
import eu.openmos.msb.opcua.utils.OPCDeviceDiscoveryItf;

/**
 *
 * @author Admin
 */
public class OPCUAInteraction {
    
    public String discovery_url;
    //private final Manager manager;
    
    public OPCUAInteraction(/*Manager _mngr*/){
        //manager = _mngr;
        
    }
    
    public void listen_to_servers_and_endpoints(){
    
        OPCDeviceDiscoveryItf opc_ua_servs_dynamic = new OPCDeviceDiscoveryItf() {
            @Override
            public void on_new_server(String name, String app_uri) {
                registerServer(name, app_uri);
                System.out.println("onNewServer Registered Server: " + name + " " + app_uri);
            }

            @Override
            public void on_new_endpoint(String parent_app_uri, String app_uri) {
                registerEndpoint(app_uri, parent_app_uri);
                System.out.println("onNewEndpointRegistered Server: " + app_uri + " " + parent_app_uri);
                String deviceid = app_uri.split("OPCUA/")[1];
                registerDevice(deviceid, "No description added yet.", "opcua_protocol");
                System.out.println("criar cliente para o device endpoint detetado! se não tiver sido já criado");
                try {
                    OPCUAClient uaclnt = new OPCUAClient();
                    //@TODO verify if is a readonly or writeonly...
                    //manager.setSender(deviceid, uaclnt);
                    //manager.setRequester(deviceid, uaclnt);

                } catch (ServiceException ex) {
                    Logger.getLogger(OPCUAInteraction.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(OPCUAInteraction.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                DevicesDynamic devdyn = new DevicesDynamic(){
                    @Override
                    public void on_participant_disappear(String id, String reason) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void on_participant_appear(String id, String description) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void on_new_readonly_topic(String device_id, String topic_name) {
                        registerTopic(topic_name, "ReadOnly", device_id);
                        //@TODO
                    }

                    @Override
                    public void on_new_writeonly_topic(String device_id, String topic_name) {
                        registerTopic(topic_name, "WriteOnly", device_id);

                        
                        
                    }    
                };
                
                /*try {
                    EndpointObjectsDiscovery eod = new EndpointObjectsDiscovery(app_uri, devdyn);
                } catch (SecureIdentityException | IOException | ServiceException ex) {
                    Logger.getLogger(OPCUAInteraction.class.getName()).log(Level.SEVERE, null, ex);
                }*/
            }


            @Override
            public void on_endpoint_dissapeared() {
                
            }

            @Override
            public void reset_tables() {
                dropServersAndEndpoints();
                System.out.println("reset endpoint and servers tables...InteractionClass");
            }

            @Override
            public void on_server_dissapeared(String name, String app_uri) {
               System.out.println("Server disapeared:" + name);
            }
            
        };
        
        OpcUaServersDiscoverySnippet opcua_discovery_snppt = new OpcUaServersDiscoverySnippet(discovery_url, opc_ua_servs_dynamic);
        opcua_discovery_snppt.start();
    
    }
    
    
    public static void main(String[] args){
        
        
        OPCUAInteraction opcua_inter = new OPCUAInteraction(/*new Manager()*/);
        // opcua_inter.discovery_url = "opc.tcp://localhost:4840";
        opcua_inter.discovery_url = ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service");
        
        opcua_inter.listen_to_servers_and_endpoints();

        System.out.println("listening to endpoints on " + opcua_inter.discovery_url);
        

    }
    
    

    private static String registerEndpoint(java.lang.String endpointUri, java.lang.String parentServerUri) {
        /*msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
        msbweb.DBInteraction port = service.getDBInteractionPort();
        return port.registerEndpoint(endpointUri, parentServerUri);*/
        return "registerEndpoint";
    }

    private static String registerServer(java.lang.String serverName, java.lang.String serverUri) {
       /* msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
        msbweb.DBInteraction port = service.getDBInteractionPort();
        return port.registerServer(serverName, serverUri);*/
       return "registerServer";
    }

    private static boolean dropServersAndEndpoints() {
        //msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
       //msbweb.DBInteraction port = service.getDBInteractionPort();
        //return port.dropServersAndEndpoints();
        return true;
    }

    private static String registerDevice(java.lang.String deviceId, java.lang.String deviceInfo, java.lang.String deviceProtocol) {
        //msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
        //msbweb.DBInteraction port = service.getDBInteractionPort();
        //return port.registerDevice(deviceId, deviceInfo, deviceProtocol);
        
        return "registerDevice";
    }

    private static String registerTopic(java.lang.String topicId, java.lang.String type, java.lang.String deviceId) {
        //msbweb.RegisterDevice_Service service = new msbweb.RegisterDevice_Service();
        //msbweb.DBInteraction port = service.getDBInteractionPort();
        //return port.registerTopic(topicId, type, deviceId);
        return "registerTopic";
    }
    
    
    
    
    
}
