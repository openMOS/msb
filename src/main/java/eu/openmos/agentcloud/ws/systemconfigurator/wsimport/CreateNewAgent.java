package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import eu.openmos.model.SubSystem;


/**
 * <p>
 * Java class for createNewAgent complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="createNewAgent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="subSystem" type="{http://cloudinterface.agentcloud.openmos.eu/}subSystem" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createNewAgent", propOrder =
{
  "subSystem"
})
public class CreateNewAgent
{

  protected SubSystem subSystem;


  /**
   * Gets the value of the SubSystem property.
   *
   * @return possible object is {@link SubSystem }
   *
   */
  public SubSystem getSubSystem()
  {
    return subSystem;
  }


  /**
   * Sets the value of the SubSystem property.
   *
   * @param value allowed object is {@link SubSystem }
   *
   */
  public void setSubSystem(SubSystem value)
  {
    this.subSystem = value;
  }

}
