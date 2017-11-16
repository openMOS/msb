
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.RecipeExecutionData;


/**
 * <p>Java class for newRecipeExecutionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="newRecipeExecutionData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipeExecutionData" type="{http://cloudinterface.agentcloud.openmos.eu/}recipeExecutionData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "newRecipeExecutionData", propOrder = {
    "recipeExecutionData"
})
public class NewRecipeExecutionData {

    protected RecipeExecutionData recipeExecutionData;

    /**
     * Gets the value of the recipeExecutionData property.
     * 
     * @return
     *     possible object is
     *     {@link RecipeExecutionData }
     *     
     */
    public RecipeExecutionData getRecipeExecutionData() {
        return recipeExecutionData;
    }

    /**
     * Sets the value of the recipeExecutionData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecipeExecutionData }
     *     
     */
    public void setRecipeExecutionData(RecipeExecutionData value) {
        this.recipeExecutionData = value;
    }

}
