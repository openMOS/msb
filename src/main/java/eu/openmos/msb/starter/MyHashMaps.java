/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.msb.dummyclasses.ExecuteData;
import eu.openmos.msb.dummyclasses.ServerStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fabio.miranda
 */
public class MyHashMaps {

    public static Map<String, MSB_MiloClientSubscription> OPCclientIDMaps = new HashMap<String, MSB_MiloClientSubscription>();
    public static Map<MSB_MiloClientSubscription, CyberPhysicalAgentDescription> AgentDeviceMaps = new HashMap<MSB_MiloClientSubscription, CyberPhysicalAgentDescription>(); //OPCClient vs AgentID
    public static Map<String, MSB_MiloClientSubscription> ProductIDAdapterMaps=new HashMap<String, MSB_MiloClientSubscription>(); //ProductID vs Adapterclient
    
    public static Map<String, List<ExecuteData>> ExecutiontTableMaps=new HashMap<>(); //workstation name and executetabledata
    public static Map<String, ServerStatus> ServerTableMaps=new HashMap<String, ServerStatus>(); //devices in the workstation and its data
    
    public static MyHashMaps instance = null;

    
    public static MyHashMaps getInstance() {

        if (instance == null) {
            instance = new MyHashMaps();
        }
        return instance;
    }

//OPC DEVICE name vs client MAPS*************************************************
    
    public static Map<String, MSB_MiloClientSubscription> getOPCclientIDMaps() {
        return OPCclientIDMaps;
    }

    public static void setOPCclientIDMaps(String key, MSB_MiloClientSubscription keyset) {
        MyHashMaps.OPCclientIDMaps.put(key, keyset);
    }
    
    public static void deleteOPCclientIDMaps(String key) {
        MyHashMaps.OPCclientIDMaps.remove(key);
    }
    
//AGENT vs DEVICE MAPS*************************************************
    public static Map<MSB_MiloClientSubscription, CyberPhysicalAgentDescription> getAgentDeviceIDMaps() {
        return AgentDeviceMaps;
    }

    public static void setAgentDeviceIDMaps(MSB_MiloClientSubscription key, CyberPhysicalAgentDescription keyset) {
        MyHashMaps.AgentDeviceMaps.put(key, keyset);
    }

    public static void deleteOAgentDeviceIDMaps(MSB_MiloClientSubscription key) {
        MyHashMaps.AgentDeviceMaps.remove(key);
    }

//PRODUCT vs DEVICE MAPS*************************************************
    public static Map<String, MSB_MiloClientSubscription> getProductIDAdapterMaps() {
        return ProductIDAdapterMaps;
    }

    public static void setProductIDAdapterMaps(String key, MSB_MiloClientSubscription keyset) {
        MyHashMaps.ProductIDAdapterMaps.put(key, keyset);
    }

    public static void deleteProductIDAdapterMaps(String key) {
        MyHashMaps.ProductIDAdapterMaps.remove(key);
    }

//WORKSTATIONName vs DEVICE data MAPS*************************************************
    public static Map<String, ServerStatus> getDevicesNameDataMaps() {
        return ServerTableMaps;
    }

    public static void setDevicesNameDataMaps(String key, ServerStatus keyset) {
        MyHashMaps.ServerTableMaps.put(key, keyset);
    }

    public static void deleteDevicesNameDataMaps(String key) {
        MyHashMaps.ServerTableMaps.remove(key);
    }
    
    
//ExecutionInfoMaps
     public static Map<String, List<ExecuteData>> getExecutionInfoMaps() {
        return ExecutiontTableMaps;
    }

    public static void setExecutionInfoMaps(String key, List<ExecuteData> keyset) {
        MyHashMaps.ExecutiontTableMaps.put(key, keyset);
    }

    public static void deleteExecutionInfoMaps(String key) {
        MyHashMaps.ExecutiontTableMaps.remove(key);
    }
    
    
    protected MyHashMaps() {

    }
}
