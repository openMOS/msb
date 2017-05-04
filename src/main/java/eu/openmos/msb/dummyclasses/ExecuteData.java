/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dummyclasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author renato.martins
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecuteData
{

    public ExecuteData() {
    }
    @XmlElement(name = "productID")
    public String productID = "";
    @XmlElement(name = "recipeID")
    public String recipeID = "";
    @XmlElement(name = "status")
    public Boolean status = false;
    @XmlElement(name = "objectID")
    public String objectID = "";
    @XmlElement(name = "methodID")
    public String methodID = "";
    
}
