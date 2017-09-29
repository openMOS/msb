package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Modules that compose subsystems.
 * Can have internal modules inside as well.
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Module extends Equipment implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());
    private static final long serialVersionUID = 6529685098267757011L;
    
    /**
     * List of internal modules, can be empty.
     */
    protected List<Module> internalModules;
    
    /**
     * Pointer to the parent equipment, can be null.
     */
    protected String parentId;
    /**
     * Type of parent equipment, can be a subsystem or another module.
     */
    protected String parentType;

    public List<Module> getInternalModules() {
        return internalModules;
    }

    public void setInternaleModules(List<Module> internalModules) {
        this.internalModules = internalModules;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
    
    

    /**
     * Empty constructor, for reflection purpose.
     */
    public Module() {super();}

    /**
     * Parameterized constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param connected
     * @param skills
     * @param ports
     * @param internalModules
     * @param address
     * @param status
     * @param manufacturer
     * @param registeredTimestamp 
     */
    public Module(
            String uniqueId, 
            String name, 
            String description, 
            boolean connected,
            List<Skill> skills,
            List<PhysicalPort> ports,
            List<Module> internalModules,
            String address,
            String status,
            String manufacturer,
            String parentId,
            String parentType,
            Date registeredTimestamp
    ) 
    {
        super(uniqueId, name, description, connected, skills, ports, address, status, manufacturer, registeredTimestamp);

        this.internalModules = internalModules;
        this.parentId = parentId;
        this.parentType = parentType;
    }    

     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = null;
        if (skills != null)
            skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        
        List<String> physicalPortIds = null;
        if (physicalPorts != null)
            physicalPortIds = physicalPorts.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());        
        
        List<String> moduleIds = null;
        if (internalModules != null)
            moduleIds = internalModules.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("uniqueId", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("connected", connected);
        doc.append("skillIds", skillIds);        
        doc.append("physicalPortIds", physicalPortIds);        
        doc.append("moduleIds", moduleIds);        
        doc.append("address", address);
        doc.append("status", status);
        doc.append("manifacturer", manufacturer);
        doc.append("parentId", parentId);
        doc.append("parentType", parentType);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }
}
