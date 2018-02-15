package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Abstract base class for SubSytem and Module classes.
 * 
 * @see SubSystem
 * @see Module
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
// @XmlRootElement(name = "device")
// @XmlAccessorType(XmlAccessType.FIELD)
public abstract class Equipment extends Base implements Serializable {    
        private static final long serialVersionUID = 6529685098267757003L;
    
    /**
     * Equipment ID.
     */
//    @XmlElement(name = "amlId")    
    protected String uniqueId;
    
    /**
     * Equipment name.
     */
//    @XmlElement(name = "name")
    protected String name;
    
    /**
     * Equipment description.
     */
    protected String description;
    
    /*
     * Is the equipment connected or not.
    */
    protected boolean connected = false;   
    
    /**
     * List of skills.
     */
    protected List<Skill> skills;

    /**
     * Which address?
     */
//    @XmlElement(name = "address")
    protected String address = "";

    /**
     * Equipment status.
     */
//    @XmlElement(name = "status")
    protected String status = "";       // could be the "connected" field?

    /**
     * Equipment manufacturer.
     */
    protected String manufacturer = "";
    
    /**
     * List of physical physicalPorts.
     */
    protected List<PhysicalPort> physicalPorts;
    
    /**
     * List of physical adhustment parameters.
     */
    protected List<PhysicalAdjustmentParameter> physicalAdjustmentParameters;
    
    // for reflection purpose
    public Equipment() {super();}

    /**
     * Constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param connected
     * @param skills
     * @param ports
     * @param address
     * @param status
     * @param manufacturer
     * @param registeredTimestamp 
     */
    public Equipment(
            String uniqueId, 
            String name, 
            String description, 
            boolean connected,
            List<Skill> skills,
            List<PhysicalPort> ports,
            List<PhysicalAdjustmentParameter> physicalAdjustmentParameters,
            String address,
            String status,
            String manufacturer,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.connected = connected;
        this.skills = skills;
        this.physicalPorts = ports;
        this.physicalAdjustmentParameters = physicalAdjustmentParameters;
        
        this.address = address;
        this.status = status;
        this.manufacturer = manufacturer;
    }

    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }        

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<PhysicalPort> getPhysicalPorts() {
        return physicalPorts;
    }

    public void setPhysicalPorts(List<PhysicalPort> ports) {
        this.physicalPorts = ports;
    }

    public List<PhysicalAdjustmentParameter> getPhysicalAdjustmentParameters() {
        return physicalAdjustmentParameters;
    }

    public void setPhysicalAdjustmentParameters(List<PhysicalAdjustmentParameter> physicalAdjustmentParameters) {
        this.physicalAdjustmentParameters = physicalAdjustmentParameters;
    }
    
    
}
