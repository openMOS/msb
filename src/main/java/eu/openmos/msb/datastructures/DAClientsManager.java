/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.messages.ExecuteData;
import eu.openmos.msb.messages.ServerStatus;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 * @author fabio.miranda
 *
 */
public class DAClientsManager
{

  // Singleton specific objects
  private static final Object lock = new Object();
  private static volatile DAClientsManager instance = null;

  // [af-silva] TODO valdiate
  private final Map<Integer, DeviceAdapter> deviceAdapters;


  /**
   * @brief constructor
   */
  protected DAClientsManager()
  {
    deviceAdapters = new HashMap<Integer, DeviceAdapter>();
  }


  /**
   * @brief obtain the Device Adapter Clients Manager unique instance
   * @return
   */
  public static DAClientsManager getInstance()
  {
    DAClientsManager i = instance;
    if (i == null)
    {
      synchronized (lock)
      {
        // While we were waiting for the lock, another 
        i = instance; // thread may have instantiated the object.
        if (i == null)
        {
          i = new DAClientsManager();
          instance = i;
        }
      }
    }
    return i;
  }


  /**
   * @param deviceAdapter
   * @param device_protocol
   * @param short_info
   * @param long_info
   */
  public void addDeviceAdapter(String deviceAdapter, Protocol device_protocol, String short_info, String long_info)
  {
    String protocol = "";
    try
    {
      DeviceAdapter client = null;
      if (null != device_protocol)
      {
        switch (device_protocol)
        {
          case DDS:
          {
            client = new DeviceAdapterDDS();
            protocol = "DDS";
            break;
          }
          case OPC:
          {
            client = new DeviceAdapterOPC();
            protocol = "OPC";
            break;
          }
          case MQTT:
            throw new UnsupportedOperationException("Not supported yet.");
          default:
            break;

        }
      }
      int id = DatabaseInteraction.getInstance().createDevice(deviceAdapter, protocol, short_info, long_info);
      if (id != -1 && client != null) // the last condition should never happen
      {
        deviceAdapters.put(id, client);
      }
    }
    catch (UnsupportedOperationException ex)
    {
      System.out.println("[ERRO] at addDeviceAdapter" + ex.getMessage());
    }
  }


  /**
   * @brief @param deviceAdapter
   * @return
   */
  public boolean deleteDeviceAdapter(String deviceAdapter)
  {
    boolean ok = false;
    try
    {
      int id = DatabaseInteraction.getInstance().removeDeviceById(deviceAdapter);
      if (id != -1 && deviceAdapters.containsKey(id))
      {
        deviceAdapters.remove(id);
        ok = true;
      }
    }
    catch (Exception ex)
    {
      System.out.println("[Error] at deleteDeviceAdapter" + ex);
    }
    return ok;
  }


  /**
   * @brief @param deviceAdapter
   * @param cpad
   */
  public void setAgentDeviceIDMaps(String deviceAdapter, CyberPhysicalAgentDescription cpad)
  {
    int id = DatabaseInteraction.getInstance().getDeviceIdByName(deviceAdapter);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      deviceAdapters.get(id).setCyberPhysicalAgentDescription(cpad);
    }
  }


  /**
   * @param deviceAdapter
   * @brief WORKSTATIONName vs DEVICE data MAPS
   * @return
   */
  public List<ServerStatus> getDevicesNameDataMaps(String deviceAdapter)
  {

    int id = DatabaseInteraction.getInstance().getDeviceIdByName(deviceAdapter);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id).getServerStatusMaps();
    }
    return null;
  }


  /**
   *
   * @param deviceAdapter
   * @param devices
   */
  public void setDevicesDataMaps(String deviceAdapter, List<ServerStatus> devices)
  {
    int id = DatabaseInteraction.getInstance().getDeviceIdByName(deviceAdapter);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      deviceAdapters.get(id).setServerStatusMaps(devices);
    }
  }


  /**
   *
   * @param deviceAdapter
   * @param device
   */
  public void addDeviceToDevicesDataMaps(String deviceAdapter, ServerStatus device)
  {
    int id = DatabaseInteraction.getInstance().getDeviceIdByName(deviceAdapter);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      deviceAdapters.get(id).getServerStatusMaps().add(device);
    }
  }


  /**
   *
   * @param deviceAdapter
   * @param deviceName
   * @return 
   */
  public boolean deleteDevicesNameDataMaps(String deviceAdapter, String deviceName)
  {
    int id = DatabaseInteraction.getInstance().getDeviceIdByName(deviceAdapter);
    if (id != -1 && deviceAdapters.containsKey(id))
    {
      return deviceAdapters.get(id).removeServerStatusFromMaps(deviceName);
    }
    return false;
  }


}
