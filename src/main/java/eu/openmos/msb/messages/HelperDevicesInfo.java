/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author renato.martins
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HelperDevicesInfo
{

  public HelperDevicesInfo()
  {
  }
  @XmlElement(name = "Name")
  private String Name = "";
  @XmlElement(name = "Address")
  private String Address = "";
  @XmlElement(name = "Status")
  private int Status = 0;

  private String DeviceAdapter = "";

  /**
   * @return the Name
   */
  public String getName()
  {
    return Name;
  }

  /**
   * @param Name the Name to set
   */
  public void setName(String Name)
  {
    this.Name = Name;
  }

  /**
   * @return the URL
   */
  public String getAddress()
  {
    return Address;
  }

  /**
   * @param address
   */
  public void setAddress(String address)
  {
    this.Address = address;
  }

  public int getStatus()
  {
    return Status;
  }

  public void setStatus(int Status)
  {
    this.Status = Status;
  }

  public String getDeviceAdapter()
  {
    return DeviceAdapter;
  }

  public void setDeviceAdapter(String DeviceAdapter)
  {
    this.DeviceAdapter = DeviceAdapter;
  }

}
