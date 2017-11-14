
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.Recipe;


/**
 * <p>Java class for getRecipesBySubSystemIdResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRecipesBySubSystemIdResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recipes" type="{http://cloudinterface.agentcloud.openmos.eu/}recipe" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRecipesBySubSystemIdResponse", propOrder = {
    "recipes"
})
public class GetRecipesBySubSystemIdResponse {

    protected List<Recipe> recipes;

    /**
     * Gets the value of the recipes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recipes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecipes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Recipe }
     * 
     * 
     */
    public List<Recipe> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<Recipe>();
        }
        return this.recipes;
    }

}
