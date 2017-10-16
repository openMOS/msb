package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
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
public class SubAssembly extends Part implements Serializable {
    private static final Logger logger = Logger.getLogger(SubAssembly.class.getName());
    private static final long serialVersionUID = 6529685098267757030L;

    /**
     * List of internal parts, can be empty.
     */
    private List<Part> internalParts;
    /**
     * List of skills requirements that need to be executed.
     */
    private List<SkillRequirement> skillRequirements;

    public SubAssembly() {super();}

    public SubAssembly(String uniqueId, String name, String description, List<Part> internalParts, List<SkillRequirement> skillRequirements, Date registered) {
        super(uniqueId, name, description, registered);
        this.internalParts = internalParts;
        this.skillRequirements = skillRequirements;
    }

    public List<Part> getInternalParts() {
        return internalParts;
    }

    public void setInternalParts(List<Part> internalParts) {
        this.internalParts = internalParts;
    }

    public List<SkillRequirement> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> internalPartIds = null;
        if (internalParts != null)
            internalPartIds = internalParts.stream().map(part -> part.getUniqueId()).collect(Collectors.toList());
        
        List<String> skillRequirementIds = null;
        if (skillRequirements != null)
            skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());        
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        doc.append(DatabaseConstants.PART_IDS, internalPartIds);
        doc.append(DatabaseConstants.SKILL_REQUIREMENT_IDS, skillRequirementIds);      
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(registered));
        
        return doc;
    }
    
    
    
    
}
