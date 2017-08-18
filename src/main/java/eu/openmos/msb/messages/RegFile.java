/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import eu.openmos.model.Equipment;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author renato.martins
 */
@XmlRootElement(name = "deviceAdapter")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegFile
{

  @XmlElement(name = "name")
  private String name = "";
  @XmlElement(name = "type")
  private String type = "";
  @XmlElement(name = "physicalLocation")
  private String physicalLocation = "";
  @XmlElement(name = "logicalLocation")
  private String logicalLocation = "";

  @XmlElement(name = "Equipment")
  private HashMap<String, Equipment> devices = new HashMap<>();

  @XmlElement(name = "skills")
  private HashMap<String, DaSkill> skills = new HashMap<>();

  @XmlElement(name = "recipes")
  private HashMap<String, DaRecipe> recipes = new HashMap<>();

  public RegFile()
  {
    // Empty
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getPhysicalLocation()
  {
    return physicalLocation;
  }

  public void setPhysicalLocation(String physicalLocation)
  {
    this.physicalLocation = physicalLocation;
  }

  public String getLogicalLocation()
  {
    return logicalLocation;
  }

  public void setLogicalLocation(String logicalLocation)
  {
    this.logicalLocation = logicalLocation;
  }

  public HashMap<String, Equipment> getDevicesTable()
  {
    return devices;
  }

  public void setDevicesTable(HashMap<String, Equipment> DevicesTable)
  {
    this.devices = DevicesTable;
  }

  public HashMap<String, DaSkill> getSkillsTable()
  {
    return skills;
  }

  public void setSkillsTable(HashMap<String, DaSkill> SkillsTable)
  {
    this.skills = SkillsTable;
  }

  public HashMap<String, DaRecipe> getRecipesTable()
  {
    return recipes;
  }

  public void setRecipesTable(HashMap<String, DaRecipe> RecipesTable)
  {
    this.recipes = RecipesTable;
  }

}
