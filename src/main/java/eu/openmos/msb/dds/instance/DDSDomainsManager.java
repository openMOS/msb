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
import org.opensplice.dds.dcps.TypeSupportImpl;


/**
 *
 * @author andre
 */
public class DDSDomainsManager
{

  private HashMap<String, DomainParticipant> domainsMap;


  /**
   *
   */
  public DDSDomainsManager()
  {

  }


  /**
   * Adds a new domain participant to the application that it's store in the domainsMap HashMap and enables the
   * application to handle communication between domains Refer to the documentation: OpenSplice DDS Java Reference Guide
   * chapter 3.2.2.1 (pag. 188)
   *
   * @param domainName Name of the domain to add
   * @param id Id of the domain
   */
  public void createDomainParticipant(String domainName, int id)
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
  }


  /**
   * Deletes the Domain participant object Refer to the documentation: OpenSplice DDS Java Reference Guide chapter
   * 3.2.2.2 (pag. 191)
   *
   * @param domainName Name of the domain to removed
   */
  public void deleteDomainParticipant(String domainName)
  {
    int status = DomainParticipantFactory.get_instance().delete_participant(this.domainsMap.remove(domainName));
    DDSErrorHandler.checkStatus(status, "DomainParticipantFactory.delete_participant");
  }


  /**
   * Register the data type to the specified domain
   *
   * @param ts
   * @param domainName
   */
  public void registerType(TypeSupportImpl ts, String domainName)
  {
    String typeName = ts.get_type_name();
    int status = ts.register_type(this.domainsMap.get(domainName), typeName);
    DDSErrorHandler.checkStatus(status, "register_type");
  }


  /**
   *
   * @param domainName
   * @return
   */
  public DomainParticipant getDomain(String domainName)
  {
    return this.domainsMap.get(domainName);
  }

}
