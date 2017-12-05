
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.ProductInstance;


/**
 * <p>Java class for startedProduct complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="startedProduct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productInstance" type="{http://cloudinterface.agentcloud.openmos.eu/}productInstance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startedProduct", propOrder = {
    "productInstance"
})
public class StartedProduct {

    protected ProductInstance productInstance;

    /**
     * Gets the value of the productInstance property.
     * 
     * @return
     *     possible object is
     *     {@link ProductInstance }
     *     
     */
    public ProductInstance getProductInstance() {
        return productInstance;
    }

    /**
     * Sets the value of the productInstance property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductInstance }
     *     
     */
    public void setProductInstance(ProductInstance value) {
        this.productInstance = value;
    }

}
