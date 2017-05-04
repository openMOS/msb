
package eu.openmos.agentcloud.ws.productionoptimizer.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.optimizer.data.OptimizationParameter;


/**
 * <p>Java class for reparametrizeOptimizer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reparametrizeOptimizer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="newParameters" type="{http://productionoptimizer.optimizer.agentcloud.openmos.eu/}optimizationParameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reparametrizeOptimizer", propOrder = {
    "newParameters"
})
public class ReparametrizeOptimizer {

    protected List<OptimizationParameter> newParameters;

    /**
     * Gets the value of the newParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the newParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNewParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OptimizationParameter }
     * 
     * 
     */
    public List<OptimizationParameter> getNewParameters() {
        if (newParameters == null) {
            newParameters = new ArrayList<OptimizationParameter>();
        }
        return this.newParameters;
    }

}
