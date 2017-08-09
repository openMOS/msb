
package eu.openmos.msb.messages.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for physicalLocation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="physicalLocation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cloudinterface.agentcloud.openmos.eu/}location">
 *       &lt;sequence>
 *         &lt;element name="alpha" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="beta" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="gamma" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="referenceFrameName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="x" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="y" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="z" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "physicalLocation", propOrder = {
    "alpha",
    "beta",
    "gamma",
    "referenceFrameName",
    "x",
    "y",
    "z"
})
public class PhysicalLocation
    extends Location
{

    protected long alpha;
    protected long beta;
    protected long gamma;
    protected String referenceFrameName;
    protected long x;
    protected long y;
    protected long z;

    /**
     * Gets the value of the alpha property.
     * 
     */
    public long getAlpha() {
        return alpha;
    }

    /**
     * Sets the value of the alpha property.
     * 
     */
    public void setAlpha(long value) {
        this.alpha = value;
    }

    /**
     * Gets the value of the beta property.
     * 
     */
    public long getBeta() {
        return beta;
    }

    /**
     * Sets the value of the beta property.
     * 
     */
    public void setBeta(long value) {
        this.beta = value;
    }

    /**
     * Gets the value of the gamma property.
     * 
     */
    public long getGamma() {
        return gamma;
    }

    /**
     * Sets the value of the gamma property.
     * 
     */
    public void setGamma(long value) {
        this.gamma = value;
    }

    /**
     * Gets the value of the referenceFrameName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceFrameName() {
        return referenceFrameName;
    }

    /**
     * Sets the value of the referenceFrameName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceFrameName(String value) {
        this.referenceFrameName = value;
    }

    /**
     * Gets the value of the x property.
     * 
     */
    public long getX() {
        return x;
    }

    /**
     * Sets the value of the x property.
     * 
     */
    public void setX(long value) {
        this.x = value;
    }

    /**
     * Gets the value of the y property.
     * 
     */
    public long getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     */
    public void setY(long value) {
        this.y = value;
    }

    /**
     * Gets the value of the z property.
     * 
     */
    public long getZ() {
        return z;
    }

    /**
     * Sets the value of the z property.
     * 
     */
    public void setZ(long value) {
        this.z = value;
    }

}
