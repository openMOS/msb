package eu.openmos.model;

import eu.openmos.model.utilities.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bson.Document;

/**
 * Abstract base class for SubSytem and Module classes.
 * 
 * @see SubSystem
 * @see Module
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Equipment extends Base implements Serializable {    
        private static final long serialVersionUID = 6529685098267757003L;
    
    /**
     * Equipment ID.
     */
    @XmlElement(name = "amlId")    
    protected String uniqueId;
    
    /**
     * Equipment name.
     */
    @XmlElement(name = "name")
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
    @XmlElement(name = "address")
    protected String address = "";

    /**
     * Equipment status.
     */
    @XmlElement(name = "status")
    protected String status = "";       // could be the "connected" field?

    /**
     * Equipment manufacturer.
     */
    protected String manufacturer = "";
    
    /**
     * List of physical physicalPorts.
     */
    protected List<PhysicalPort> physicalPorts;
    
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
        
     /**
     * Method that serializes the object into a BSON document.
     * Useless, because this is just the abstract class.
     * 
     * @return BSON form of the object. 
     */
/*    
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        List<String> physicalPortIds = skills.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());        
        List<String> moduleIds = skills.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("connected", connected);
        doc.append("skillIds", skillIds);        
        doc.append("physicalPortIds", physicalPortIds);        
        doc.append("moduleIds", moduleIds);        
        doc.append("address", address);
        doc.append("status", status);
        doc.append("manifacturer", manufacturer);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }
*/

}
