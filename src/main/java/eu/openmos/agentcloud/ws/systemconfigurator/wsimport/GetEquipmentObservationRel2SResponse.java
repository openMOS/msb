
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.EquipmentObservationRel2;


/**
 * <p>Java class for getEquipmentObservationRel2sResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getEquipmentObservationRel2sResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="equipmentObservationRel2s" type="{http://cloudinterface.agentcloud.openmos.eu/}equipmentObservationRel2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEquipmentObservationRel2sResponse", propOrder = {
    "equipmentObservationRel2S"
})
public class GetEquipmentObservationRel2SResponse {

    @XmlElement(name = "equipmentObservationRel2s")
    protected List<EquipmentObservationRel2> equipmentObservationRel2S;

    /**
     * Gets the value of the equipmentObservationRel2S property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the equipmentObservationRel2S property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEquipmentObservationRel2S().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EquipmentObservationRel2 }
     * 
     * 
     */
    public List<EquipmentObservationRel2> getEquipmentObservationRel2S() {
        if (equipmentObservationRel2S == null) {
            equipmentObservationRel2S = new ArrayList<EquipmentObservationRel2>();
        }
        return this.equipmentObservationRel2S;
    }

}
