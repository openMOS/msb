
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.RecipeExecutionData;


/**
 * <p>Java class for getRecipeExecutionDataResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRecipeExecutionDataResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipeExecutionData" type="{http://cloudinterface.agentcloud.openmos.eu/}recipeExecutionData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRecipeExecutionDataResponse", propOrder = {
    "recipeExecutionData"
})
public class GetRecipeExecutionDataResponse {

    protected List<RecipeExecutionData> recipeExecutionData;

    /**
     * Gets the value of the recipeExecutionData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recipeExecutionData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecipeExecutionData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RecipeExecutionData }
     * 
     * 
     */
    public List<RecipeExecutionData> getRecipeExecutionData() {
        if (recipeExecutionData == null) {
            recipeExecutionData = new ArrayList<RecipeExecutionData>();
        }
        return this.recipeExecutionData;
    }

}
