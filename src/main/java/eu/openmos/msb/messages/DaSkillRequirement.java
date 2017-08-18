package eu.openmos.msb.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for skillRequirement complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="skillRequirement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "skillRequirement", propOrder =
{
  "description",
  "name",
  "type",
  "uniqueId"
})
public class DaSkillRequirement
{

  private String description;
  private String name;
  private int type;
  private String uniqueId;

  /**
   * Gets the value of the description property.
   *
   * @return possible object is {@link String }
   *
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value allowed object is {@link String }
   *
   */
  public void setDescription(String value)
  {
    this.description = value;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is {@link String }
   *
   */
  public String getName()
  {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is {@link String }
   *
   */
  public void setName(String value)
  {
    this.name = value;
  }

  /**
   * Gets the value of the type property.
   *
   */
  public int getType()
  {
    return type;
  }

  /**
   * Sets the value of the type property.
   *
   */
  public void setType(int value)
  {
    this.type = value;
  }

  /**
   * Gets the value of the uniqueId property.
   *
   * @return possible object is {@link String }
   *
   */
  public String getUniqueId()
  {
    return uniqueId;
  }

  /**
   * Sets the value of the uniqueId property.
   *
   * @param value allowed object is {@link String }
   *
   */
  public void setUniqueId(String value)
  {
    this.uniqueId = value;
  }

}
