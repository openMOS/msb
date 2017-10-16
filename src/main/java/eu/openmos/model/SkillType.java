package eu.openmos.model;

import java.util.Date;
import java.io.Serializable;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Skill type implementation.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class SkillType extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757029L;
    
    /**
     * Skill type ID.
     */
    private String uniqueId;
    /**
     * Skill type name.
     */
    private String name;
    /**
     * Skill type description.
     */
    private String description;
    /**
     * Skill type decision.
     */
    private boolean decision;
    
    /**
     * Default constructor.
     */
    public SkillType() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueId - Skill type ID.
     * @param name - Skill type name.
     * @param description - Skill type description.
     * @param decision - Skill type decision
     * @param registeredTimestamp
     */
    public SkillType(String uniqueId, String name, String description, 
            boolean decision, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;        
        this.decision = decision;
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

    public boolean getDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
        
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format: 
 {
  "uniqueId": unique id,
  "name": name,
  "description": description,
  "type": type,
  "direction": direction,
  "registered": registered
 } 
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }
    
    /**
     * Method that deserializes a BSON object.
     * The input BSON needs to have the following format:
 {
  "uniqueId": unique id,
  "name": name,
  "description": description,
  "type": type,
  "direction": direction,
  "registered": registered
 } 
     * @param bsonPort - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static SkillType fromBSON(Document bsonPort)
    throws ParseException 
    {
            return (SkillType)SkillType.fromBSON2(bsonPort, SkillType.class);
    }    
}