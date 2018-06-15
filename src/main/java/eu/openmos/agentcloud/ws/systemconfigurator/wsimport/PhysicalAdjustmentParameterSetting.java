
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.Base;
import eu.openmos.model.PhysicalAdjustmentParameter;


/**
 * <p>Java class for physicalAdjustmentParameterSetting complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="physicalAdjustmentParameterSetting">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cloudinterface.agentcloud.openmos.eu/}base">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalAdjustmentParameter" type="{http://cloudinterface.agentcloud.openmos.eu/}physicalAdjustmentParameter" minOccurs="0"/>
 *         &lt;element name="uniqueId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "physicalAdjustmentParameterSetting", propOrder = {
    "description",
    "name",
    "physicalAdjustmentParameter",
    "uniqueId",
    "value"
})
public class PhysicalAdjustmentParameterSetting
    extends Base
{

    protected String description;
    protected String name;
    protected PhysicalAdjustmentParameter physicalAdjustmentParameter;
    protected String uniqueId;
    protected String value;

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
     * Gets the value of the physicalAdjustmentParameter property.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalAdjustmentParameter }
     *     
     */
    public PhysicalAdjustmentParameter getPhysicalAdjustmentParameter() {
        return physicalAdjustmentParameter;
    }

    /**
     * Sets the value of the physicalAdjustmentParameter property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalAdjustmentParameter }
     *     
     */
    public void setPhysicalAdjustmentParameter(PhysicalAdjustmentParameter value) {
        this.physicalAdjustmentParameter = value;
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

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
