/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Introsys
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class DaRecipe
{

    public DaRecipe()
    {
    }

    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "amlId")
    private String amlId;
    @XmlElement(name = "skill")
    private String skill;
    @XmlElement(name = "valid")
    private String valid;
    @XmlElement(name = "skillRequirements")
    private List<DaSkillRequirement> skillRequirements = new ArrayList<>();

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAmlId()
    {
        return amlId;
    }

    public void setAmlId(String amlId)
    {
        this.amlId = amlId;
    }

    public String getSkill()
    {
        return skill;
    }

    public void setSkill(String skill)
    {
        this.skill = skill;
    }

    public String getValid()
    {
        return valid;
    }

    public void setValid(String valid)
    {
        this.valid = valid;
    }

    public List<DaSkillRequirement> getSkillRequirements()
    {
        return skillRequirements;
    }

    public void setSkillRequirements(List<DaSkillRequirement> skillRequirements)
    {
        this.skillRequirements = skillRequirements;
    }



}
