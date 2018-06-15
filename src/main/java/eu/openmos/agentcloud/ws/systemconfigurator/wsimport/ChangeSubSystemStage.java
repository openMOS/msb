
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for changeSubSystemStage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="changeSubSystemStage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subSystemId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newStage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "changeSubSystemStage", propOrder = {
    "subSystemId",
    "newStage"
})
public class ChangeSubSystemStage {

    protected String subSystemId;
    protected String newStage;

    /**
     * Gets the value of the subSystemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubSystemId() {
        return subSystemId;
    }

    /**
     * Sets the value of the subSystemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubSystemId(String value) {
        this.subSystemId = value;
    }

    /**
     * Gets the value of the newStage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewStage() {
        return newStage;
    }

    /**
     * Sets the value of the newStage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewStage(String value) {
        this.newStage = value;
    }

}
