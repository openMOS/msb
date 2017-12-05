
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.ExecutionTable;


/**
 * <p>Java class for getExecutionTableBySubSystemIdResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getExecutionTableBySubSystemIdResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="executionTable" type="{http://cloudinterface.agentcloud.openmos.eu/}executionTable" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getExecutionTableBySubSystemIdResponse", propOrder = {
    "executionTable"
})
public class GetExecutionTableBySubSystemIdResponse {

    protected ExecutionTable executionTable;

    /**
     * Gets the value of the executionTable property.
     * 
     * @return
     *     possible object is
     *     {@link ExecutionTable }
     *     
     */
    public ExecutionTable getExecutionTable() {
        return executionTable;
    }

    /**
     * Sets the value of the executionTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExecutionTable }
     *     
     */
    public void setExecutionTable(ExecutionTable value) {
        this.executionTable = value;
    }

}
