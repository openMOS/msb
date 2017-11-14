
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.SubSystem;


/**
 * <p>Java class for getSubSystemsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getSubSystemsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subSystems" type="{http://cloudinterface.agentcloud.openmos.eu/}subSystem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSubSystemsResponse", propOrder = {
    "subSystems"
})
public class GetSubSystemsResponse {

    protected List<SubSystem> subSystems;

    /**
     * Gets the value of the subSystems property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subSystems property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubSystems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubSystem }
     * 
     * 
     */
    public List<SubSystem> getSubSystems() {
        if (subSystems == null) {
            subSystems = new ArrayList<SubSystem>();
        }
        return this.subSystems;
    }

}
