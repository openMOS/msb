
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.ProcessAssessment;


/**
 * <p>Java class for getProcessAssessmentsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getProcessAssessmentsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="processAssessments" type="{http://cloudinterface.agentcloud.openmos.eu/}processAssessment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getProcessAssessmentsResponse", propOrder = {
    "processAssessments"
})
public class GetProcessAssessmentsResponse {

    protected List<ProcessAssessment> processAssessments;

    /**
     * Gets the value of the processAssessments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processAssessments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessAssessments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessAssessment }
     * 
     * 
     */
    public List<ProcessAssessment> getProcessAssessments() {
        if (processAssessments == null) {
            processAssessments = new ArrayList<ProcessAssessment>();
        }
        return this.processAssessments;
    }

}
