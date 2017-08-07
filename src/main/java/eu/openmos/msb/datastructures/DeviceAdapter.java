/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.msb.messages.HelperDevicesInfo;
import eu.openmos.msb.messages.ServerStatus;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author andre
 */
public abstract class DeviceAdapter
{

  protected int bd_id;

  /*
   * device name || devices in the workstation and its data
   */
  protected List<ServerStatus> serverStatusMaps;
  protected CyberPhysicalAgentDescription cpad;


  public DeviceAdapter()
  {

  }


  /**
   * @return the id
   */
  public int getId()
  {
    return bd_id;
  }


  /**
   * @param id the id to set
   */
  public void setId(int id)
  {
    this.bd_id = id;
  }


  /**
   * @param name
   * @return the ServerTableMaps
   */
  public ServerStatus getServerStatusByDevice(String name)
  {
    Iterator<ServerStatus> it = this.serverStatusMaps.iterator();
    while (it.hasNext())
    {
      ServerStatus temp = it.next();
      if (temp.getName().toUpperCase().equals(name.toUpperCase()))
      {
        return temp;
      }
    }
    return null;
  }


  /**
   *
   * @return
   */
  public List<ServerStatus> serverStatusMaps()
  {
    return serverStatusMaps;
  }


  /**
   *
   * @param server
   */
  public void addToServerStatusMaps(ServerStatus server)
  {
    this.serverStatusMaps.add(server);
  }


  /**
   *
   * @return List of HelperDevicesInfo
   */
  public List<ServerStatus> getServerStatusMaps()
  {
    return this.serverStatusMaps;
  }


  /**
   *
   * @param devices
   */
  public void setServerStatusMaps(List<ServerStatus> devices)
  {
    this.serverStatusMaps = devices;
  }


  /**
   *
   * @param ss
   */
  public void addServerStatusToMaps(ServerStatus ss)
  {
    this.serverStatusMaps.add(ss);
  }


  /**
   * 
   * @param deviceName
   * @return 
   */
  public boolean removeServerStatusFromMaps(String deviceName)
  {
    Iterator<ServerStatus> it = this.serverStatusMaps.iterator();
    while (it.hasNext())
    {
      ServerStatus ss = it.next();
      if (ss.getName().equals(deviceName))
      {
        return this.serverStatusMaps.remove(ss);
      }
    }
    return false;
  }


  /**
   * 
   * @param agent 
   */
  public void setCyberPhysicalAgentDescription(CyberPhysicalAgentDescription agent)
  {
    this.cpad = agent;
  }

  /**
   * 
   * @return  CyberPhysicalAgentDescription 
   */
  public CyberPhysicalAgentDescription getCyberPhysicalAgentDescription()
  {
    return this.cpad;
  }

  /**
   *
   * @return
   */
  public abstract Object getClient();

}