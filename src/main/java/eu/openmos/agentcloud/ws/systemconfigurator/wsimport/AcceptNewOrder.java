
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.data.Order;


/**
 * <p>Java class for acceptNewOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="acceptNewOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newOrder" type="{http://cloudinterface.agentcloud.openmos.eu/}order" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acceptNewOrder", propOrder = {
    "newOrder"
})
public class AcceptNewOrder {

    protected Order newOrder;

    /**
     * Gets the value of the newOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Order }
     *     
     */
    public Order getNewOrder() {
        return newOrder;
    }

    /**
     * Sets the value of the newOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Order }
     *     
     */
    public void setNewOrder(Order value) {
        this.newOrder = value;
    }

}
