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
public class ServerStatus
{

  public ServerStatus()
  {
  }
  @XmlElement(name = "Name")
  private String Name = "";
  @XmlElement(name = "URL")
  private String URL = "";
  @XmlElement(name = "Connected")
  private Boolean Connected = false;


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
  public String getURL()
  {
    return URL;
  }


  /**
   * @param URL the URL to set
   */
  public void setURL(String URL)
  {
    this.URL = URL;
  }


  /**
   * @return the Connected
   */
  public Boolean getConnected()
  {
    return Connected;
  }


  /**
   * @param Connected the Connected to set
   */
  public void setConnected(Boolean Connected)
  {
    this.Connected = Connected;
  }
}
