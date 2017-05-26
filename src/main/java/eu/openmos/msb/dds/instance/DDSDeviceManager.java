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
 * DDS Device Manager Class
 *
 * @author af-silva
 *
 * TODO - implement runnable
 */
public class DDSDeviceManager implements Runnable
{

  private static DDSDeviceManager dmInstance = null; // Singleton Object
  private final HashMap<String, DDSMSBDevice> devices;
  private final DomainParticipantFactory dpf;
  // future implementation could enable multiple participants!
  private final DomainParticipant participant;


  /**
   * Crates a global instance for this object
   *
   * @return A DDSMSBInstance that is unique to the project
   */
  public static DDSDeviceManager getInstance()
  {
    synchronized (DDSDeviceManager.class)
    {
      if (dmInstance == null) // doublce check
      {
        dmInstance = new DDSDeviceManager(DOMAIN_ID_DEFAULT.value);
      }
    }
    return dmInstance;
  }


  /**
   * Singleton protected constructor for creating the instance
   * This constructor is responsible to obtained the 
   * DomainParticipantFactory and create a new participant (DDS Domain)
   *
   * @param domainId
   */
  protected DDSDeviceManager(int domainId)
  {
    // Devices list associated with this DDS Domain 
    this.devices = new HashMap<String, DDSMSBDevice>();
    dpf = DomainParticipantFactory.get_instance();
    DDSErrorHandler.checkHandle(dpf, "DomainParticipantFactory.get_instance");
    participant = dpf.create_participant(
      domainId,
      PARTICIPANT_QOS_DEFAULT.value,
      null,
      STATUS_MASK_NONE.value
    );
    DDSErrorHandler.checkHandle(dpf, "DomainParticipantFactory.create_participant");
  }

  /**
   * This operation deletes all the Entity objects that were created on the Domain Participant
   */
  public void deleteContainerEntities()
  {
    int status = participant.delete_contained_entities();
    DDSErrorHandler.checkStatus(status, "delete_contained_entities");
  }


  /**
   * Delete the DDS Domain Participant associated with this instance
   */
  public void deleteParticipant()
  {
    int status = dpf.delete_participant(participant);
    DDSErrorHandler.checkStatus(status, "delete_participant");
  }


  /**
   * Function to register a device in the Device Manater
   * @param name Device name (id)
   * @return 1 if ok -1 if object already exists
   */
  public int addDevice(String name)
  {
    if (!this.devices.containsKey(name))
    {
      this.devices.put(name, new DDSMSBDevice(name, participant));
    }
    return -1;
  }


  /**
   * Function to remove a specific device registered in the Device Manager
   * @param name Device name (id)
   * @return 1 if ok -1 if not exists
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
   * Function to retrieve a specific device registered in the Device Manager
   * @param name Device name (id)
   * @return the specified devcie
   */
  public DDSMSBDevice getDevice(String name)
  {
    if (this.devices.containsKey(name))
    {
      return this.devices.get(name);
    }
    return null;
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


  /**
   * TODO
   */
  @Override
  public void run()
  {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet."); 
  }

}
// EOF
