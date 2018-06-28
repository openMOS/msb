package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
//@XmlRootElement(name = "skillRequirement")
//@XmlAccessorType(XmlAccessType.FIELD)
public class SkillRequirement extends Base implements Serializable  {
    private static final Logger logger = Logger.getLogger(SkillRequirement.class.getName());
    private static final long serialVersionUID = 6529685098267757028L;
    
    /**
     * Skill Requirement ID.
     */
    private String uniqueId;
    /**
     * Skill Requirement name.
     */
  //@XmlElement(name = "name")    
    private String name;
    /**
     * Skill Requirement description.
     */
    private String description;

    /**
     * Skill Requirement type.
     * MSB and WP4 alignment: this type is the same as skill type.
     * In the future, it could switch to a class definition, as PEDRO Ferreira recommends,
     * but..... for now we keep it as it is.
     */
    // private int type;    
  //@XmlElement(name = "type")    
    private String type;
  private SkillType skillType;

    /**
     * List of skill requirement ids that need to be fullfilled before this skillreq can be triggered.
     * On the HMI there will be the chance for the user to select to next recipe according
     * to this precedences list.
     */
//    private List<String> precedentIds;
    private List<SkillReqPrecedent> precedents;
    
    private Part requiresPart;
    
    
    private List<String> recipeIDs;
    /**
     * Default constructor, for reflection
     */
    public SkillRequirement() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Skill Requirement description.
     * @param uniqueId - Skill Requirement unique ID.
     * @param name - Skill Requirement name.
     * @param type - Skill Requirement type.
     * @param skillType
     * @param precedents
     * @param requiresPart
     * @param registeredTimestamp - Skill Requirement registration timestamp.
     */
    public SkillRequirement(
            String description, 
            String uniqueId, 
            String name, 
            String type,
            SkillType skillType,
            List<SkillReqPrecedent> precedents,
            Part requiresPart,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.description = description;
        this.name = name;
        this.type = type;
        this.skillType = skillType;
        
        // this.precedentIds = precedentIds;
        this.precedents = precedents;        
        this.requiresPart = requiresPart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public List<SkillReqPrecedent> getPrecedents() {
        return precedents;
    }
    public void setPrecedents(List<SkillReqPrecedent> precedents) {
        this.precedents = precedents;
    }
    
    public Part getRequiresPart() {
        return requiresPart;
    }

    public void setRequiresPart(Part requiresPart) {
        this.requiresPart = requiresPart;
    }
    
     public List<String> getRecipeIDs() {
        return recipeIDs;
    }
    public void setRecipeIDs(List<String> recipeIDs) {
        this.recipeIDs = recipeIDs;
    }
    /**
     * Method serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);

        List<String> precedentIds = null;
        if (precedents != null)
            precedentIds = precedents.stream().map(precedent -> precedent.getUniqueId()).collect(Collectors.toList());        

        String skillTypeId = null;
        if (skillType != null)
            skillTypeId = skillType.getUniqueId();
        
        String requiresPartId = null;
        if (requiresPart != null)
            requiresPartId = requiresPart.getUniqueId();
        
        return new Document(DatabaseConstants.DESCRIPTION, description)
                .append(DatabaseConstants.UNIQUE_ID, uniqueId)
                .append(DatabaseConstants.NAME, name)
                .append(DatabaseConstants.TYPE, type)
                .append(DatabaseConstants.SKILL_TYPE_ID, skillTypeId)
                .append(DatabaseConstants.PRECEDENT_IDS, precedentIds)
                .append(DatabaseConstants.REQUIRES_PART_ID, requiresPartId)
                .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
    }

}
