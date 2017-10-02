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
 *
 * @author valerio.gentile
 */
public class SubAssemblyInstance extends PartInstance implements Serializable {
    private static final Logger logger = Logger.getLogger(SubAssembly.class.getName());
    private static final long serialVersionUID = 6529685098267757031L;
    
    /**
     * Pointer to the subassembly (to the model subassembly).
     */
    private SubAssembly subAssembly;
    /**
     * List of internal part instances, can be empty.
     */
    private List<PartInstance> internalPartInstances;

    public SubAssemblyInstance() {
        super();
    }

    public SubAssemblyInstance(String uniqueId, String name, String description, Part part, SubAssembly subAssembly, List<PartInstance> internalPartInstances, Date registered) {
        super(uniqueId, name, description, part, registered);
        this.subAssembly = subAssembly;
        this.internalPartInstances = internalPartInstances;
    }

    public SubAssembly getSubAssembly() {
        return subAssembly;
    }

    public void setSubAssembly(SubAssembly subAssembly) {
        this.subAssembly = subAssembly;
    }

    public List<PartInstance> getInternalPartInstances() {
        return internalPartInstances;
    }

    public void setInternalPartInstances(List<PartInstance> internalPartInstances) {
        this.internalPartInstances = internalPartInstances;
    }

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> internalPartInstanceIds = null;
        if (internalPartInstances != null)
            internalPartInstanceIds = internalPartInstances.stream().map(part -> part.getUniqueId()).collect(Collectors.toList());
        
        String subAssemblyId = null;
        if (subAssembly != null)
            subAssemblyId = subAssembly.getUniqueId();
        
        doc.append("uniqueId", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("subAssemblyId", subAssemblyId);
        doc.append("partInstanceIds", internalPartInstanceIds);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(registered));
        
        return doc;
    }    
    
    
    
}
