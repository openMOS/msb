
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getPhysicalAdjustmentsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getPhysicalAdjustmentsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="physicalAdjustments" type="{http://cloudinterface.agentcloud.openmos.eu/}physicalAdjustment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getPhysicalAdjustmentsResponse", propOrder = {
    "physicalAdjustments"
})
public class GetPhysicalAdjustmentsResponse {

    protected List<PhysicalAdjustment> physicalAdjustments;

    /**
     * Gets the value of the physicalAdjustments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the physicalAdjustments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhysicalAdjustments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PhysicalAdjustment }
     * 
     * 
     */
    public List<PhysicalAdjustment> getPhysicalAdjustments() {
        if (physicalAdjustments == null) {
            physicalAdjustments = new ArrayList<PhysicalAdjustment>();
        }
        return this.physicalAdjustments;
    }

}
