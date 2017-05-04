/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dummyclasses;

import eu.openmos.agentcloud.data.recipe.Recipe;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author renato.martins
 */
@XmlRootElement(name = "Device")
@XmlAccessorType(XmlAccessType.FIELD)

public class RegFile
{

    public RegFile() {
    }
    @XmlElement(name = "Name")
    public String Name = "";
    @XmlElement(name = "Type")
    public String Type = "";
    @XmlElement(name = "PhysicalLocation")
    public String PhysicalLocation = "";
    @XmlElement(name = "LogicalLocation")
    public String LogicalLocation = "";
    
    @XmlElement(name = "ExecuteTable")
    public HashMap<String, ExecuteData> ExecuteTable = new HashMap<>();
    @XmlElement(name = "ServerTable")
    public HashMap<String, ServerStatus> ServerTable = new HashMap<>();
    @XmlElement(name = "Recipes")
    public HashMap<String, Recipe> Recipes = new HashMap<>();
}
