
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;


/**
 * <p>Java class for createNewAgent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createNewAgent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cyberPhysicalAgentDescription" type="{http://cloudinterface.agentcloud.openmos.eu/}cyberPhysicalAgentDescription" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewAgent", propOrder = {
    "cyberPhysicalAgentDescription"
})
public class CreateNewAgent {

    protected CyberPhysicalAgentDescription cyberPhysicalAgentDescription;

    /**
     * Gets the value of the cyberPhysicalAgentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link CyberPhysicalAgentDescription }
     *     
     */
    public CyberPhysicalAgentDescription getCyberPhysicalAgentDescription() {
        return cyberPhysicalAgentDescription;
    }

    /**
     * Sets the value of the cyberPhysicalAgentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link CyberPhysicalAgentDescription }
     *     
     */
    public void setCyberPhysicalAgentDescription(CyberPhysicalAgentDescription value) {
        this.cyberPhysicalAgentDescription = value;
    }

}
