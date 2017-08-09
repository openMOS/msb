
package eu.openmos.msb.messages.classes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for recipe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="recipe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kpisSetting" type="{http://recipesmanagement.fakemsb.openmos.eu/}kpiSetting" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parametersSetting" type="{http://recipesmanagement.fakemsb.openmos.eu/}parameterSetting" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="skillRequirements" type="{http://recipesmanagement.fakemsb.openmos.eu/}skillRequirement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="uniqueAgentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uniqueId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recipe", propOrder = {
    "description",
    "kpisSetting",
    "name",
    "parametersSetting",
    "skillRequirements",
    "uniqueAgentName",
    "uniqueId"
})
public class Recipe {

    protected String description;
    @XmlElement(nillable = true)
    protected List<KPISetting> kpisSetting;
    protected String name;
    @XmlElement(nillable = true)
    protected List<ParameterSetting> parametersSetting;
    @XmlElement(nillable = true)
    protected List<SkillRequirement> skillRequirements;
    protected String uniqueAgentName;
    protected String uniqueId;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the kpisSetting property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kpisSetting property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKpisSetting().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KPISetting }
     * 
     * 
     */
    public List<KPISetting> getKpisSetting() {
        if (kpisSetting == null) {
            kpisSetting = new ArrayList<KPISetting>();
        }
        return this.kpisSetting;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the parametersSetting property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parametersSetting property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParametersSetting().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterSetting }
     * 
     * 
     */
    public List<ParameterSetting> getParametersSetting() {
        if (parametersSetting == null) {
            parametersSetting = new ArrayList<ParameterSetting>();
        }
        return this.parametersSetting;
    }

    /**
     * Gets the value of the skillRequirements property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the skillRequirements property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSkillRequirements().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SkillRequirement }
     * 
     * 
     */
    public List<SkillRequirement> getSkillRequirements() {
        if (skillRequirements == null) {
            skillRequirements = new ArrayList<SkillRequirement>();
        }
        return this.skillRequirements;
    }

    /**
     * Gets the value of the uniqueAgentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueAgentName() {
        return uniqueAgentName;
    }

    /**
     * Sets the value of the uniqueAgentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueAgentName(String value) {
        this.uniqueAgentName = value;
    }

    /**
     * Gets the value of the uniqueId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Sets the value of the uniqueId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueId(String value) {
        this.uniqueId = value;
    }

}
