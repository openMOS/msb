
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.EquipmentAssessment;


/**
 * <p>Java class for getEquipmentAssessmentsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getEquipmentAssessmentsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="equipmentAssessments" type="{http://cloudinterface.agentcloud.openmos.eu/}equipmentAssessment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEquipmentAssessmentsResponse", propOrder = {
    "equipmentAssessments"
})
public class GetEquipmentAssessmentsResponse {

    protected List<EquipmentAssessment> equipmentAssessments;

    /**
     * Gets the value of the equipmentAssessments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the equipmentAssessments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEquipmentAssessments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EquipmentAssessment }
     * 
     * 
     */
    public List<EquipmentAssessment> getEquipmentAssessments() {
        if (equipmentAssessments == null) {
            equipmentAssessments = new ArrayList<EquipmentAssessment>();
        }
        return this.equipmentAssessments;
    }

}
