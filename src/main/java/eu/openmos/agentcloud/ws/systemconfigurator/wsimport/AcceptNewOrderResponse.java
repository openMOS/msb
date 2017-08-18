
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.cloudinterface.ServiceCallStatus;


/**
 * <p>Java class for acceptNewOrderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="acceptNewOrderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceCallStatus" type="{http://cloudinterface.agentcloud.openmos.eu/}serviceCallStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acceptNewOrderResponse", propOrder = {
    "serviceCallStatus"
})
public class AcceptNewOrderResponse {

    protected ServiceCallStatus serviceCallStatus;

    /**
     * Gets the value of the serviceCallStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceCallStatus }
     *     
     */
    public ServiceCallStatus getServiceCallStatus() {
        return serviceCallStatus;
    }

    /**
     * Sets the value of the serviceCallStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceCallStatus }
     *     
     */
    public void setServiceCallStatus(ServiceCallStatus value) {
        this.serviceCallStatus = value;
    }

}
