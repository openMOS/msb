/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import eu.openmos.agentcloud.data.recipe.KPISetting;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author renato.martins
 */
@XmlRootElement(name = "ChangedState")
@XmlAccessorType(XmlAccessType.FIELD)

public class ChangedState {

    public ChangedState() {
    }

    @XmlElement(name = "productID")
    public String productID = "";
    @XmlElement(name = "recipeID")
    public String recipeID = "";
    @XmlElement(name = "KPISettings")
    public List<KPISetting> kpisSetting;
    @XmlElement(name = "ExecuteState")
    public ExecuteState executeState;

}
