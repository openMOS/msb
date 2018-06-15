
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getFilteredRecipeExecutionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getFilteredRecipeExecutionData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipeExecutionDataFilter" type="{http://cloudinterface.agentcloud.openmos.eu/}recipeExecutionDataFilter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFilteredRecipeExecutionData", propOrder = {
    "recipeExecutionDataFilter"
})
public class GetFilteredRecipeExecutionData {

    protected RecipeExecutionDataFilter recipeExecutionDataFilter;

    /**
     * Gets the value of the recipeExecutionDataFilter property.
     * 
     * @return
     *     possible object is
     *     {@link RecipeExecutionDataFilter }
     *     
     */
    public RecipeExecutionDataFilter getRecipeExecutionDataFilter() {
        return recipeExecutionDataFilter;
    }

    /**
     * Sets the value of the recipeExecutionDataFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecipeExecutionDataFilter }
     *     
     */
    public void setRecipeExecutionDataFilter(RecipeExecutionDataFilter value) {
        this.recipeExecutionDataFilter = value;
    }

}
