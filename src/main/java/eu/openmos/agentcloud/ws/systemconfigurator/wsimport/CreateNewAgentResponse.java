
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.cloudinterface.AgentStatus;


/**
 * <p>Java class for createNewAgentResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createNewAgentResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="agentStatus" type="{http://cloudinterface.agentcloud.openmos.eu/}agentStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewAgentResponse", propOrder = {
    "agentStatus"
})
public class CreateNewAgentResponse {

    protected AgentStatus agentStatus;

    /**
     * Gets the value of the agentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link AgentStatus }
     *     
     */
    public AgentStatus getAgentStatus() {
        return agentStatus;
    }

    /**
     * Sets the value of the agentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link AgentStatus }
     *     
     */
    public void setAgentStatus(AgentStatus value) {
        this.agentStatus = value;
    }

}
