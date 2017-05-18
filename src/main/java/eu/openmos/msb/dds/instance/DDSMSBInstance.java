/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DomainParticipant;
import java.util.HashMap;


/**
 *
 * @author andre
 */
public class DDSMSBInstance
{

  private static DDSMSBInstance instance = null;
  private DDSDomainsManager domainsManger;
  private HashMap<String, DDSDeviceManager> devicesManager;


  /**
   *
   */
  protected DDSMSBInstance()
  {
    this.domainsManger = new DDSDomainsManager();
    this.devicesManager = new HashMap<String, DDSDeviceManager>();
  }


  /**
   * Crates a global instance for this object
   *
   * @return A DDSMSBInstance that is unique to the project
   */
  public static DDSMSBInstance getInstance()
  {
    if (instance == null)
    {
      synchronized (DDSMSBInstance.class)
      {
        if (instance == null) // doublce check
        {
          instance = new DDSMSBInstance();
        }
      }
    }
    return instance;
  }


  /**
   *
   * @param name
   * @param id
   */
  public void newDomain(String name, int id)
  {
    try
    {
      DomainParticipant dp = this.domainsManger.createDomainParticipant(name, id);
      this.devicesManager.put(name, new DDSDeviceManager(dp));
    }
    catch (Exception ex)
    {
      System.out.println("[ERROR] at newDomain (general message)" + ex.getMessage());
    }
  }


  /**
   *
   * @param name
   */
  public void deleteDomain(String name)
  {
    this.domainsManger.deleteDomainParticipant(name);
    this.devicesManager.remove(name);
  }


  /**
   * Convenience method to list all available domains in the DDS instance
   *
   * @return
   */
  public String[] getAllAvailableDomains()
  {
    return this.domainsManger.getDomainNames();
  }


  /**
   *
   * @param name
   * @return
   */
  public DDSDeviceManager getDeviceManager(String name)
  {
    return this.devicesManager.get(name);
  }
}
// EOF
