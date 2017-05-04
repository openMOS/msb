
package eu.openmos.agentcloud.ws.productionoptimizer.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.agentcloud.data.recipe.Recipe;


/**
 * <p>Java class for optimizeResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="optimizeResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="optimizedRecipes" type="{http://productionoptimizer.optimizer.agentcloud.openmos.eu/}recipe" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "optimizeResponse", propOrder = {
    "optimizedRecipes"
})
public class OptimizeResponse {

    protected List<Recipe> optimizedRecipes;

    /**
     * Gets the value of the optimizedRecipes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the optimizedRecipes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOptimizedRecipes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recipe }
     * 
     * 
     */
    public List<Recipe> getOptimizedRecipes() {
        if (optimizedRecipes == null) {
            optimizedRecipes = new ArrayList<Recipe>();
        }
        return this.optimizedRecipes;
    }

}
