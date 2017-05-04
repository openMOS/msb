/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dummyclasses;


import eu.openmos.agentcloud.data.recipe.KPISetting;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author renato.martins
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecuteState
{
    public ExecuteState() {}
    
    @XmlElement(name = "recipeID")
    public String recipeID = "";
    @XmlElement(name = "KPISettings")
    public List<KPISetting> kpisSetting;

}
