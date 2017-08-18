/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author renato.martins
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class DaDevice
{

  //
  @XmlElement(name = "amlId")
  private String amlId = "";
  @XmlElement(name = "name")
  private String name = "";
  @XmlElement(name = "address")
  private String address = "";
  @XmlElement(name = "status")
  private String status = "";

  public DaDevice()
  {
  }

  public String getAmlId()
  {
    return amlId;
  }

  public void setAmlId(String amlId)
  {
    this.amlId = amlId;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getAddress()
  {
    return address;
  }

  public void setAddress(String address)
  {
    this.address = address;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

}
