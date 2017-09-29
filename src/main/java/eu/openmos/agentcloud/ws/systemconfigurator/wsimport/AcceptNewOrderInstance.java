
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.OrderInstance;


/**
 * <p>Java class for acceptNewOrderInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="acceptNewOrderInstance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newOrder" type="{http://cloudinterface.agentcloud.openmos.eu/}orderInstance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "acceptNewOrderInstance", propOrder = {
    "newOrder"
})
public class AcceptNewOrderInstance {

    protected OrderInstance newOrder;

    /**
     * Gets the value of the newOrder property.
     * 
     * @return
     *     possible object is
     *     {@link OrderInstance }
     *     
     */
    public OrderInstance getNewOrder() {
        return newOrder;
    }

    /**
     * Sets the value of the newOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInstance }
     *     
     */
    public void setNewOrder(OrderInstance value) {
        this.newOrder = value;
    }

}
