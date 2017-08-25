package eu.openmos.model;

import java.util.Date;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
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
    private static final long serialVersionUID = 6529685098267757697L;
    
    /**
     * Skill Requirement ID.
     */
//    private String uniqueId;
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

    /**
     * List of skill requirement ids that need to be fullfilled before this skillreq can be triggered.
     * On the HMI there will be the chance for the user to select to next recipe according
     * to this precedences list.
     */
    private List<String> precedents;
    
//    private static final int FIELDS_COUNT = 5;

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
//            String uniqueId, 
            String name, 
            String type,
//            String classificationType, 
            List<String> precedents,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
//        this.uniqueId = uniqueId;
        this.description = description;
        this.name = name;
        this.type = type;
        
        this.precedents = precedents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getUniqueId() {
//        return uniqueId;
//    }
//
//    public void setUniqueId(String uniqueId) {
//        this.uniqueId = uniqueId;
//    }

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

    public List<String> getPrecedents() {
        return precedents;
    }

    public void setPrecedents(List<String> precedents) {
        this.precedents = precedents;
    }
    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * description,
     * uniqueId,
     * name,
     * type,
     * precedences list,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//
//        return description + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
////                + uniqueId + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
//                + name + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
//                + type + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
//                + ListsToString.writeSkillRequirementIds(precedents) + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
//                + stringRegisteredTimestamp;
//    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * description,
     * uniqueId,
     * name,
     * type,
     * precedences list,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static SkillRequirement fromString(String object) throws ParseException
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_SKILL_REQUIREMENT);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("SkillRequirement - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new SkillRequirement(
//                tokenizer.nextToken(),                      // description
////                tokenizer.nextToken(),                      // uniqueID
//                tokenizer.nextToken(),                      // name
//                tokenizer.nextToken(),                      // type
//                StringToLists.readSkillRequirementIds(tokenizer.nextToken()),                      // precedence ids list
//                sdf.parse(tokenizer.nextToken())             // registered
//        );
//    }
    
    /**
     * Method serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
 description,
 uniqueId,
 name,
 type,
 precedences list,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
 } 
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//
//        return new Document("description", description)
////                .append("id", uniqueId)
//                .append("name", name)
//                .append("type", type)
//                .append("precedents", precedents)
//                .append("registered", stringRegisteredTimestamp);
    }
    
    /**
     * Method that deserializes a BSON object.
     * The input BSON needs to have the following format:
     * 
     * description,
     * unique id,
     * name,
     * type,
     * precedence ids list,
     * registered
     * 
     * @param bsonKPI - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static SkillRequirement fromBSON(Document bsonRequirement)
    throws ParseException {
        return (SkillRequirement)fromBSON2(bsonRequirement, SkillRequirement.class);
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//                       
//        return new SkillRequirement(
//            bsonRequirement.get("description").toString(),
////            bsonRequirement.get("id").toString(),
//            bsonRequirement.get("name").toString(),
//            bsonRequirement.get("type").toString(),
//            (List<String>)bsonRequirement.get("precedents"),
//            sdf.parse(bsonRequirement.get("registered").toString())                
////            bsonRequirement.get("registered").toString()
//        );
    }

//    public static SkillRequirement deserialize(String object) 
//    {        
//        SkillRequirement objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (SkillRequirement) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }
}
