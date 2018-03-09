
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for physicalAdjustment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="physicalAdjustment">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cloudinterface.agentcloud.openmos.eu/}equipmentAdjustment">
 *       &lt;sequence>
 *         &lt;element name="physicalAdjustmentParameterSetting" type="{http://cloudinterface.agentcloud.openmos.eu/}physicalAdjustmentParameterSetting" minOccurs="0"/>
 *         &lt;element name="physicalAdjustmentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "physicalAdjustment", propOrder = {
    "physicalAdjustmentParameterSetting",
    "physicalAdjustmentType"
})
public class PhysicalAdjustment
    extends EquipmentAdjustment
{

    protected PhysicalAdjustmentParameterSetting physicalAdjustmentParameterSetting;
    protected String physicalAdjustmentType;

    /**
     * Gets the value of the physicalAdjustmentParameterSetting property.
     * 
     * @return
     *     possible object is
     *     {@link PhysicalAdjustmentParameterSetting }
     *     
     */
    public PhysicalAdjustmentParameterSetting getPhysicalAdjustmentParameterSetting() {
        return physicalAdjustmentParameterSetting;
    }

    /**
     * Sets the value of the physicalAdjustmentParameterSetting property.
     * 
     * @param value
     *     allowed object is
     *     {@link PhysicalAdjustmentParameterSetting }
     *     
     */
    public void setPhysicalAdjustmentParameterSetting(PhysicalAdjustmentParameterSetting value) {
        this.physicalAdjustmentParameterSetting = value;
    }

    /**
     * Gets the value of the physicalAdjustmentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalAdjustmentType() {
        return physicalAdjustmentType;
    }

    /**
     * Sets the value of the physicalAdjustmentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalAdjustmentType(String value) {
        this.physicalAdjustmentType = value;
    }

}
