
package eu.openmos.agentcloud.ws.productionoptimizer.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for initializeOptimizerResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="initializeOptimizerResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="productionOptimizerResponseBean" type="{http://productionoptimizer.optimizer.agentcloud.openmos.eu/}productionOptimizerResponseBean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "initializeOptimizerResponse", propOrder = {
    "productionOptimizerResponseBean"
})
public class InitializeOptimizerResponse {

    protected ProductionOptimizerResponseBean productionOptimizerResponseBean;

    /**
     * Gets the value of the productionOptimizerResponseBean property.
     * 
     * @return
     *     possible object is
     *     {@link ProductionOptimizerResponseBean }
     *     
     */
    public ProductionOptimizerResponseBean getProductionOptimizerResponseBean() {
        return productionOptimizerResponseBean;
    }

    /**
     * Sets the value of the productionOptimizerResponseBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductionOptimizerResponseBean }
     *     
     */
    public void setProductionOptimizerResponseBean(ProductionOptimizerResponseBean value) {
        this.productionOptimizerResponseBean = value;
    }

}
