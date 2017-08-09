/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.msb.messages.DaDevice;
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
  protected List<DaDevice> listOfDevices;
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
   * @param name
   * @return the ServerTableMaps
   */
  public DaDevice getServerStatusByDevice(String name)
  {
    Iterator<DaDevice> it = this.listOfDevices.iterator();
    while (it.hasNext())
    {
      DaDevice temp = it.next();
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
  public List<DaDevice> getListOfDevices()
  {
    return listOfDevices;
  }


  /**
   *
   * @param server
   */
  public void addDevice(DaDevice server)
  {
    this.listOfDevices.add(server);
  }




  /**
   *
   * @param devices
   */
  public void setListOfDevices(List<DaDevice> devices)
  {
    this.listOfDevices = devices;
  }


  /**
   * 
   * @param deviceName
   * @return 
   */
  public boolean removeServerStatusFromMaps(String deviceName)
  {
    Iterator<DaDevice> it = this.listOfDevices.iterator();
    while (it.hasNext())
    {
      DaDevice ss = it.next();
      if (ss.getName().equals(deviceName))
      {
        return this.listOfDevices.remove(ss);
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