/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DomainParticipant;
import java.util.HashMap;


/**
 * Description TODO
 *
 * @author af-silva
 *
 */
public class DDSDeviceManager
{

  private final DomainParticipant domain;
  private final HashMap<String, DDSMSBDevice> devices;


  /**
   * TODO - Construct doc
   *
   * @param dp
   */
  public DDSDeviceManager(DomainParticipant dp)
  {
    this.domain = dp;
    this.devices = new HashMap<String, DDSMSBDevice>();
  }


  /**
   *
   * @param name
   * @return
   */
  public int addDevice(String name)
  {
    if (!this.devices.containsKey(name))
    {
      this.devices.put(name, new DDSMSBDevice(name, this.domain));
    }
    return -1;
  }


  /**
   *
   * @param name
   * @return
   */
  public int removeDevice(String name)
  {
    if (this.devices.containsKey(name))
    {
      this.devices.remove(name);
      return 1;
    }
    return -1;
  }

  /**
   * 
   * @param name
   * @return 
   */
  public DDSMSBDevice getDevice(String name){
    if (this.devices.containsKey(name))
    {
      return this.devices.get(name);
    }
    return null;
  }
  
  /**
   * Method to access the domain associated with this Device Manager instance
   * @return DomainParticipant
   */
  public DomainParticipant getDomain()
  {
    return this.domain;
  }


  /**
   * Get the devices list by names
   *
   * @return Array of devices names
   */
  public String[] getDevices()
  {
    return (String[]) this.devices.keySet().toArray();
  }

}
// EOF
