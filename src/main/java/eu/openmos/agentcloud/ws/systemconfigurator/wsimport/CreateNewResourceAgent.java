
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.SubSystem;


/**
 * <p>Classe Java per createNewResourceAgent complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="createNewResourceAgent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cyberPhysicalAgentDescription" type="{http://cloudinterface.agentcloud.openmos.eu/}subSystem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewResourceAgent", propOrder = {
    "cyberPhysicalAgentDescription"
})
public class CreateNewResourceAgent {

    protected SubSystem cyberPhysicalAgentDescription;

    /**
     * Recupera il valore della proprietà cyberPhysicalAgentDescription.
     * 
     * @return
     *     possible object is
     *     {@link SubSystem }
     *     
     */
    public SubSystem getCyberPhysicalAgentDescription() {
        return cyberPhysicalAgentDescription;
    }

    /**
     * Imposta il valore della proprietà cyberPhysicalAgentDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link SubSystem }
     *     
     */
    public void setCyberPhysicalAgentDescription(SubSystem value) {
        this.cyberPhysicalAgentDescription = value;
    }

}
