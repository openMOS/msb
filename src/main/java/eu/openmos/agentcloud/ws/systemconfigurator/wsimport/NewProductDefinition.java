
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.Product;


/**
 * <p>Java class for newProductDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="newProductDefinition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productDefinition" type="{http://cloudinterface.agentcloud.openmos.eu/}product" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newProductDefinition", propOrder = {
    "productDefinition"
})
public class NewProductDefinition {

    protected Product productDefinition;

    /**
     * Gets the value of the productDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link Product }
     *     
     */
    public Product getProductDefinition() {
        return productDefinition;
    }

    /**
     * Sets the value of the productDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Product }
     *     
     */
    public void setProductDefinition(Product value) {
        this.productDefinition = value;
    }

}
