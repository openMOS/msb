
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.Order;
import eu.openmos.model.SubSystem;


/**
 * <p>Java class for initializePlatform complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="initializePlatform">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cyberPhysicalAgentDescriptions" type="{http://cloudinterface.agentcloud.openmos.eu/}subSystem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="orders" type="{http://cloudinterface.agentcloud.openmos.eu/}order" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "initializePlatform", propOrder = {
    "cyberPhysicalAgentDescriptions",
    "orders"
})
public class InitializePlatform {

    protected List<SubSystem> cyberPhysicalAgentDescriptions;
    protected List<Order> orders;

    /**
     * Gets the value of the cyberPhysicalAgentDescriptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cyberPhysicalAgentDescriptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCyberPhysicalAgentDescriptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubSystem }
     * 
     * 
     */
    public List<SubSystem> getCyberPhysicalAgentDescriptions() {
        if (cyberPhysicalAgentDescriptions == null) {
            cyberPhysicalAgentDescriptions = new ArrayList<SubSystem>();
        }
        return this.cyberPhysicalAgentDescriptions;
    }

    /**
     * Gets the value of the orders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Order }
     * 
     * 
     */
    public List<Order> getOrders() {
        if (orders == null) {
            orders = new ArrayList<Order>();
        }
        return this.orders;
    }

}
