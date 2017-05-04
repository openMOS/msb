/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dummyclasses;

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

    public ServerStatus() {
    }
    @XmlElement(name = "Name")
    public String Name = "";
    @XmlElement(name = "URL")
    public String URL = "";
    @XmlElement(name = "Connected")
    public Boolean Connected = false;
}
