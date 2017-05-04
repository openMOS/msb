/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.stateless;

import eu.openmos.msb.database.interaction.DatabaseInteraction;
import java.util.ArrayList;
//import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;


//@WebService(serviceName = "DeviceRegistry")
public class DeviceRegistryBean implements DeviceRegistryLocal{

/**
     * Web service operation
     */
    //@WebMethod(operationName = "register_device")
    public String register_device(@WebParam(name = "device_name") String device_name, 
            @WebParam(name = "short_info") String short_info,  
            @WebParam(name = "long_info") String long_info,
            @WebParam(name = "ip_address") String ip_addr,
            @WebParam(name = "device_protocol") String device_protocol) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        boolean res = dbm.registerDevices(device_name, device_protocol, short_info, long_info, ip_addr);
        
        if(res)
            return "Server Registered Successfully";
        else return "Server Registered Failed";
    }

    /**
     * Web service operation
     */
  //  @WebMethod(operationName = "read_device_info")
    public ArrayList<String> read_device_info(@WebParam(name = "device_id") String device_name) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        
        return dbm.readDeviceInfo(device_name);
        
    }
    
    /** 
     * Web service operation
     */
    //@WebMethod(operationName = "register_topic")
    public String register_topic(@WebParam(name = "topic_name") String topic_name, 
            @WebParam(name = "access_type") String access_type,
            @WebParam(name = "data_type") String data_type,  
            @WebParam(name = "device_name") String device_name) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        
        int device_id = Integer.parseInt(dbm.readDeviceInfo(device_name).get(0));
        
        boolean res = dbm.registerTopics(device_id, topic_name, access_type, data_type);
        
        if(res)
            return "ok";
        else return "nok";
    }
    
    /** 
     * Web service operation
     */
    //int parent_topic_id, String target, String timestamp, String header, String content)
    //@WebMethod(operationName = "register_message")
    public String register_message(@WebParam(name = "parent_topic_name") String parent_topic_name, 
            @WebParam(name = "target") String target,
            @WebParam(name = "timestamp") String timestamp,  
            @WebParam(name = "header") String header,
            @WebParam(name = "content") String content) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        
        int parent_topic_id = Integer.parseInt(dbm.readTopicInfo(parent_topic_name).get(0));
        
        boolean res = dbm.registerMessage(parent_topic_id, target, timestamp, header, content);
        
        if(res)
            return "ok";
        else return "nok";
    }
    
    /**
     * Web service operation
     * @param device_id
     * @return 
     */
    //@WebMethod(operationName = "get_topics_from_device")
    public ArrayList<String> get_topics_from_device(@WebParam(name = "device_id") String device_name) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        
        int device_id = Integer.parseInt(dbm.readDeviceInfo(device_name).get(0));
        
        return dbm.readTopicsFromDevice(device_id);
        
    }
    
    /**
     * Web service operation
     */
    //@WebMethod(operationName = "list_all_devices")
    public ArrayList<String> list_all_devices() {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.listAllDevices();
    }
    
      /**
     * Web service operation
     */
    //@WebMethod(operationName = "get_device_address")
    public ArrayList<String> get_device_address_protocol(@WebParam(name = "device_id") String device_id) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.getDeviceAddressProtocol(device_id);
    }
    
    //get name from device address
      public String get_device_name(@WebParam(name = "ip_address") String ip_address) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        //return dbm.getDeviceAddressProtocol(device_id);
        return dbm.getDeviceName(ip_address);
    }
    
    
    /**
     * Web service operation
     */
    //@WebMethod(operationName = "reset_db")
    public boolean reset_DB() {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.resetDB();
    }
    
    /**
     * Web service operation
     */
    //@WebMethod(operationName = "deregister_device")
    public int deregister_device(@WebParam(name = "device_id") String device_id) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.deregisterDevice(device_id);
        
    }
    
    /**
     * Web service operation
     */
    //@WebMethod(operationName = "deregister_topic")
    public int deregister_topic(@WebParam(name = "topic_name") String topic_name) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.DeregisterTopic(topic_name);
        
    }

    @Override
   // @WebMethod
    public String edit_device(String device_name, String short_info, String long_info, String ip_addr, String device_protocol) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        boolean res = dbm.editDevice(device_name, device_protocol, short_info, long_info, ip_addr);
        if(res)
            return "ok";
        else return "nok";
    }
    
      //@WebMethod(operationName = "list_all_execution_info")
    public ArrayList<String> list_all_execution_info() {
        DatabaseInteraction dbm = new DatabaseInteraction();
        return dbm.listAllExecutionInfo();
    }
    
    //@WebMethod(operationName = "register_execution_info")
    public String register_execution_info(@WebParam(name = "workstation_name") String workstation_name,
            @WebParam(name = "recipe_IDs") String recipe_IDs,
            @WebParam(name = "AgentUniqueIDs") String AgentUniqueIDs,
            @WebParam(name = "ProductIDs") String ProductIDs,
            @WebParam(name = "MethodID") String MethodID,
            @WebParam(name = "ObjectID") String ObjectID) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        boolean res = dbm.registerExecutionInfo(workstation_name, recipe_IDs, AgentUniqueIDs, ProductIDs, MethodID, ObjectID);

        if (res) {
            return "Server Registered Successfully";
        } else {
            return "Server Registered Failed";
        }
    }
    
    // @WebMethod
    public String edit_execute_info(String Workstation, String RecipeIDs, String AgentUniqueID, String ProductIDs, String MethodID, String ObjectID) {
        DatabaseInteraction dbm = new DatabaseInteraction();
        boolean res = dbm.editExecutionInfo(Workstation, RecipeIDs, AgentUniqueID, ProductIDs, MethodID, ObjectID);
        if (res) {
            return "ok";
        } else {
            return "nok";
        }
    }
    
}
