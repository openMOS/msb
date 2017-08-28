package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "skillRequirement")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillRequirement extends Base implements Serializable  {
    private static final Logger logger = Logger.getLogger(SkillRequirement.class.getName());
    private static final long serialVersionUID = 6529685098267757024L;
    
    /**
     * Skill Requirement ID.
     */
    private String uniqueId;
    /**
     * Skill Requirement name.
     */
  @XmlElement(name = "name")    
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
  @XmlElement(name = "type")    
    private String type;
  private SkillType skillType;

    /**
     * List of skill requirement ids that need to be fullfilled before this skillreq can be triggered.
     * On the HMI there will be the chance for the user to select to next recipe according
     * to this precedences list.
     */
//    private List<String> precedents;
    private List<SkillRequirement> precedents;
    
    private Part requiresPart;
    
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
     * @param classificationType - Skill Requirement classification type. REMOVED
     * @param precedenceIds - list of skill reqs ids
     * @param registeredTimestamp - Skill Requirement registration timestamp.
     */
    public SkillRequirement(
            String description, 
            String uniqueId, 
            String name, 
            String type,
            SkillType skillType,
//            String classificationType, 
//            List<String> precedents,
            List<SkillRequirement> precedents,
            Part requiresPart,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.description = description;
        this.name = name;
        this.type = type;
        this.skillType = skillType;
        
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

//    public List<String> getPrecedents() {
//        return precedents;
//    }
//
//    public void setPrecedents(List<String> precedents) {
//        this.precedents = precedents;
//    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public List<SkillRequirement> getPrecedents() {
        return precedents;
    }

    public void setPrecedents(List<SkillRequirement> precedents) {
        this.precedents = precedents;
    }

    public Part getRequiresPart() {
        return requiresPart;
    }

    public void setRequiresPart(Part requiresPart) {
        this.requiresPart = requiresPart;
    }
    
    /**
     * Method serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);

        List<String> skillRequirementIds = precedents.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());        

        return new Document("description", description)
                .append("uniqueId", uniqueId)
                .append("name", name)
                .append("type", type)
                .append("skillTypeId", skillType.getUniqueId())
                .append("precedents", skillRequirementIds)
                .append("requiresPartId", requiresPart.getUniqueId())
                .append("registered", stringRegisteredTimestamp);
    }
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonKPI - BSON to be deserialized.
     * @return Deserialized object.
     */
//    public static SkillRequirement fromBSON(Document bsonRequirement)
//    throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//                       
//        return new SkillRequirement(
//            bsonRequirement.get("description").toString(),
//            bsonRequirement.get("uniqueId").toString(),
//            bsonRequirement.get("name").toString(),
//            bsonRequirement.get("type").toString(),
//                SkillType.
//            (List<String>)bsonRequirement.get("precedents"),
//            sdf.parse(bsonRequirement.get("registered").toString())                
////            bsonRequirement.get("registered").toString()
//        );
//    }

}
