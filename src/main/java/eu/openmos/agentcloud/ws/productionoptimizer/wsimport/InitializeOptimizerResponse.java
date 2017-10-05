
package eu.openmos.agentcloud.ws.productionoptimizer.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per initializeOptimizerResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
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
     * Recupera il valore della proprietà productionOptimizerResponseBean.
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
     * Imposta il valore della proprietà productionOptimizerResponseBean.
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
