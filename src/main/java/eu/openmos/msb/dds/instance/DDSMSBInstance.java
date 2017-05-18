/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DOMAIN_ID_DEFAULT;
import DDS.DomainParticipant;
import DDS.DomainParticipantFactory;
import DDS.PARTICIPANT_QOS_DEFAULT;
import DDS.STATUS_MASK_NONE;
import java.util.HashMap;


/**
 *
 * @author andre
 */
public class DDSMSBInstance
{

  private static DDSMSBInstance instance = null;
  private final HashMap<String, DomainParticipant> domainsMap;
  private final HashMap<String, DDSDeviceManager> devicesManager;


  /**
   *
   */
  protected DDSMSBInstance()
  {
    this.domainsMap = new HashMap<String, DomainParticipant>();
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
   * Adds a new domain participant to the application that it's store in the domainsMap HashMap and enables the
   * application to handle communication between domains Refer to the documentation: OpenSplice DDS Java Reference Guide
   * chapter 3.2.2.1 (pag. 188)
   *
   * @param domainName Name of the domain to add
   * @param id Id of the domain
   * @return
   */
  public void createDomain(String domainName, int id)
  {
    if (id < 0)
    {
      id = DOMAIN_ID_DEFAULT.value;
    }

    DomainParticipant participant;
    participant = DomainParticipantFactory.get_instance().create_participant(
      id,
      PARTICIPANT_QOS_DEFAULT.value,
      null,
      STATUS_MASK_NONE.value);
    DDSErrorHandler.checkHandle(participant, "DomainParticipantFactory.create_participant");
    
    
    this.domainsMap.put(domainName, participant);
    this.devicesManager.put(domainName, new DDSDeviceManager(participant)); 
    
  }


  /**
   * Deletes the Domain participant object Refer to the documentation: OpenSplice DDS Java Reference Guide chapter
   * 3.2.2.2 (pag. 191)
   *
   * @param domainName Name of the domain to removed
   */
  public void deleteDomain(String domainName)
  {
    int status = DomainParticipantFactory.get_instance().delete_participant(this.domainsMap.remove(domainName));
    DDSErrorHandler.checkStatus(status, "DomainParticipantFactory.delete_participant");
    this.devicesManager.remove(domainName);
  }


  /**
   * Returns a array with all the domain names available
   *
   * @return array of strings
   */
  public String[] getDomainNames()
  {
    return (String[]) this.domainsMap.keySet().toArray();
  }


  /**
   * Get's the domain by the given name
   *
   * @param domainName The domain name to obtain
   * @return DomainParticipant
   */
  public DomainParticipant getDomain(String domainName)
  {
    return this.domainsMap.get(domainName);
  }
  
  /**
   * Method to obtain the Device Manager instance for a given domain
   * @param domainName
   * @return 
   */
  public DDSDeviceManager getDomainDeviceManager(String domainName)
  {
    return this.devicesManager.get(domainName);
  }
  
}
// EOF
