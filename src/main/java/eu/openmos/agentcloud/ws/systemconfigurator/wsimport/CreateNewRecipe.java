
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.Recipe;


/**
 * <p>Java class for createNewRecipe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createNewRecipe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipe" type="{http://cloudinterface.agentcloud.openmos.eu/}recipe" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewRecipe", propOrder = {
    "recipe"
})
public class CreateNewRecipe {

    protected Recipe recipe;

    /**
     * Gets the value of the recipe property.
     * 
     * @return
     *     possible object is
     *     {@link Recipe }
     *     
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Sets the value of the recipe property.
     * 
     * @param value
     *     allowed object is
     *     {@link Recipe }
     *     
     */
    public void setRecipe(Recipe value) {
        this.recipe = value;
    }

}
