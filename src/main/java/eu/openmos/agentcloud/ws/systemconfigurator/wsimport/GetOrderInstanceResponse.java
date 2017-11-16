
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.OrderInstance;


/**
 * <p>Java class for getOrderInstanceResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getOrderInstanceResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderInstance" type="{http://cloudinterface.agentcloud.openmos.eu/}orderInstance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getOrderInstanceResponse", propOrder = {
    "orderInstance"
})
public class GetOrderInstanceResponse {

    protected OrderInstance orderInstance;

    /**
     * Gets the value of the orderInstance property.
     * 
     * @return
     *     possible object is
     *     {@link OrderInstance }
     *     
     */
    public OrderInstance getOrderInstance() {
        return orderInstance;
    }

    /**
     * Sets the value of the orderInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInstance }
     *     
     */
    public void setOrderInstance(OrderInstance value) {
        this.orderInstance = value;
    }

}
