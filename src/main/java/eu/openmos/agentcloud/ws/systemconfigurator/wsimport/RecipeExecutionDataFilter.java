
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recipeExecutionDataFilter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recipeExecutionDataFilter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="alsoTriggerData" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="kpiSettingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productInstanceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recipeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startInterval" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stopInterval" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recipeExecutionDataFilter", propOrder = {
    "alsoTriggerData",
    "kpiSettingName",
    "productInstanceId",
    "recipeId",
    "startInterval",
    "stopInterval"
})
public class RecipeExecutionDataFilter {

    protected boolean alsoTriggerData;
    protected String kpiSettingName;
    protected String productInstanceId;
    protected String recipeId;
    protected String startInterval;
    protected String stopInterval;

    /**
     * Gets the value of the alsoTriggerData property.
     * 
     */
    public boolean isAlsoTriggerData() {
        return alsoTriggerData;
    }

    /**
     * Sets the value of the alsoTriggerData property.
     * 
     */
    public void setAlsoTriggerData(boolean value) {
        this.alsoTriggerData = value;
    }

    /**
     * Gets the value of the kpiSettingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKpiSettingName() {
        return kpiSettingName;
    }

    /**
     * Sets the value of the kpiSettingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKpiSettingName(String value) {
        this.kpiSettingName = value;
    }

    /**
     * Gets the value of the productInstanceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductInstanceId() {
        return productInstanceId;
    }

    /**
     * Sets the value of the productInstanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductInstanceId(String value) {
        this.productInstanceId = value;
    }

    /**
     * Gets the value of the recipeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipeId() {
        return recipeId;
    }

    /**
     * Sets the value of the recipeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipeId(String value) {
        this.recipeId = value;
    }

    /**
     * Gets the value of the startInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartInterval() {
        return startInterval;
    }

    /**
     * Sets the value of the startInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartInterval(String value) {
        this.startInterval = value;
    }

    /**
     * Gets the value of the stopInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopInterval() {
        return stopInterval;
    }

    /**
     * Sets the value of the stopInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopInterval(String value) {
        this.stopInterval = value;
    }

}
