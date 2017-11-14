
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.FinishedProductInfo;


/**
 * <p>Java class for finishedProduct complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="finishedProduct">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="finishedProductInfo" type="{http://cloudinterface.agentcloud.openmos.eu/}finishedProductInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "finishedProduct", propOrder = {
    "finishedProductInfo"
})
public class FinishedProduct {

    protected FinishedProductInfo finishedProductInfo;

    /**
     * Gets the value of the finishedProductInfo property.
     * 
     * @return
     *     possible object is
     *     {@link FinishedProductInfo }
     *     
     */
    public FinishedProductInfo getFinishedProductInfo() {
        return finishedProductInfo;
    }

    /**
     * Sets the value of the finishedProductInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FinishedProductInfo }
     *     
     */
    public void setFinishedProductInfo(FinishedProductInfo value) {
        this.finishedProductInfo = value;
    }

}
