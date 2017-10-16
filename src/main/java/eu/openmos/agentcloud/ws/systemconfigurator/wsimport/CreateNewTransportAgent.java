
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.SubSystem;


/**
 * <p>Java class for createNewTransportAgent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createNewTransportAgent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cyberPhysicalAgentDescription" type="{http://cloudinterface.agentcloud.openmos.eu/}subSystem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewTransportAgent", propOrder = {
    "cyberPhysicalAgentDescription"
})
public class CreateNewTransportAgent {

    protected SubSystem cyberPhysicalAgentDescription;

    /**
     * Gets the value of the cyberPhysicalAgentDescription property.
     * 
     * @return
     *     possible object is
     *     {@link SubSystem }
     *     
     */
    public SubSystem getCyberPhysicalAgentDescription() {
        return cyberPhysicalAgentDescription;
    }

    /**
     * Sets the value of the cyberPhysicalAgentDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubSystem }
     *     
     */
    public void setCyberPhysicalAgentDescription(SubSystem value) {
        this.cyberPhysicalAgentDescription = value;
    }

}
