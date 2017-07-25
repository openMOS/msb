/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;


import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.msb.messages.ExecuteData;
import eu.openmos.msb.messages.ServerStatus;
import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author fabio.miranda
 */
public class MSBClients
{

  private final Map<String, DeviceAdapter> deviceAdapters = new HashMap<String, DeviceAdapter>();
  
  // workstation name || OPC UA Client
  public static Map<String, MSB_MiloClientSubscription> OPCclientIDMaps = new HashMap<String, MSB_MiloClientSubscription>();
  //workstation name || executetabledata
  public static Map<String, List<ExecuteData>> ExecutiontTableMaps = new HashMap<>();
  // device name || devices in the workstation and its data
  public static Map<String, ServerStatus> ServerTableMaps = new HashMap<String, ServerStatus>();

  //OPCClient vs AgentID
  public static Map<MSB_MiloClientSubscription, CyberPhysicalAgentDescription> AgentDeviceMaps = new HashMap<MSB_MiloClientSubscription, CyberPhysicalAgentDescription>();

  private static final Object lock = new Object();
  private static volatile MSBClients instance = null;

  

  protected MSBClients()
  {
    // EMPTY 
  }


  public static MSBClients getInstance()
  {
    MSBClients i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new MSBClients();
          instance = i;
        }
      }
    }
    return i;
  }


  /**
   * @brief OPC DEVICE name vs client MAPS 
   * @return
   */
  public static Map<String, MSB_MiloClientSubscription> getOPCclientIDMaps()
  {
    return OPCclientIDMaps;
  }


  /**
   * @brief @param key
   * @param keyset
   */
  public static void setOPCclientIDMaps(String key, MSB_MiloClientSubscription keyset)
  {
    MSBClients.OPCclientIDMaps.put(key, keyset);
  }


  /**
   * @brief @param key
   */
  public static void deleteOPCclientIDMaps(String key)
  {
    MSBClients.OPCclientIDMaps.remove(key);
  }


  /**
   * @brief AGENT vs DEVICE MAPS ************************************************ 
   * @return
   */
  public static Map<MSB_MiloClientSubscription, CyberPhysicalAgentDescription> getAgentDeviceIDMaps()
  {
    return AgentDeviceMaps;
  }


  /**
   *
   * @param key
   * @param keyset
   */
  public static void setAgentDeviceIDMaps(MSB_MiloClientSubscription key, CyberPhysicalAgentDescription keyset)
  {
    MSBClients.AgentDeviceMaps.put(key, keyset);
  }


  /**
   *
   * @param key
   */
  public static void deleteOAgentDeviceIDMaps(MSB_MiloClientSubscription key)
  {
    MSBClients.AgentDeviceMaps.remove(key);
  }


  /**
   * @brief WORKSTATIONName vs DEVICE data MAPS ************************************************ 
   * @return
   */
  public static Map<String, ServerStatus> getDevicesNameDataMaps()
  {
    return ServerTableMaps;
  }


  /**
   *
   * @param key
   * @param keyset
   */
  public static void setDevicesNameDataMaps(String key, ServerStatus keyset)
  {
    MSBClients.ServerTableMaps.put(key, keyset);
  }


  /**
   *
   * @param key
   */
  public static void deleteDevicesNameDataMaps(String key)
  {
    MSBClients.ServerTableMaps.remove(key);
  }


  /**
   * @brief ExecutionInfoMaps
   * @return
   */
  public static Map<String, List<ExecuteData>> getExecutionInfoMaps()
  {
    return ExecutiontTableMaps;
  }


  /**
   * @brief @param key
   * @param keyset
   */
  public static void setExecutionInfoMaps(String key, List<ExecuteData> keyset)
  {
    MSBClients.ExecutiontTableMaps.put(key, keyset);
  }


  /**
   * @brief @param key
   */
  public static void deleteExecutionInfoMaps(String key)
  {
    MSBClients.ExecutiontTableMaps.remove(key);
  }

}
