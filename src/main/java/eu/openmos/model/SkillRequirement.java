/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;

import eu.openmos.model.utilities.ListsToString;
import java.util.Date;
import java.util.StringTokenizer;
import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.bson.Document;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "skillRequirement")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillRequirement
{
// WP3 semantic model alignment.
//    public static final int TYPE_ATOMIC = 0;
//    public static final int TYPE_COMPOSITE = 1;

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
   * Skill Requirement type. MSB and WP4 alignment: this type is the same as skill type. In the future, it could switch
   * to a class definition, as PEDRO Ferreira recommends, but..... for now we keep it as it is.
   */
  // private int type;    
  @XmlElement(name = "type")
  private String type;

  /**
   * WP3 semantic model alignment. Skills classification type. MSB and WP4 alignment: we don't need it.
   */
  // private String classificationType;    
  /**
   * List of skill requirement ids that need to be fullfilled before this skillreq can be triggered. On the HMI there
   * will be the chance for the user to select to next recipe according to this precedences list.
   */
  private List<String> precedents;

  /**
   * WP3 semantic model alignment.
   */
  private Date registeredTimestamp;

//    private static final int FIELDS_COUNT = 6;
  private static final int FIELDS_COUNT = 5;

  /**
   * Default constructor, for reflection
   */
  public SkillRequirement()
  {
  }

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
  )
  {
    this.description = description;
//        this.uniqueId = uniqueId;
    this.name = name;
    this.type = type;

    // WP3 semantic model alignment
//        this.classificationType = classificationType;
    this.precedents = precedents;
    this.registeredTimestamp = registeredTimestamp;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

//    public String getUniqueId() {
//        return uniqueId;
//    }
//
//    public void setUniqueId(String uniqueId) {
//        this.uniqueId = uniqueId;
//    }
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

//    public String getClassificationType() {
//        return classificationType;
//    }
//
//    public void setClassificationType(String classificationType) {
//        this.classificationType = classificationType;
//    }
  public List<String> getPrecedents()
  {
    return precedents;
  }

  public void setPrecedents(List<String> precedents)
  {
    this.precedents = precedents;
  }

  public Date getRegisteredTimestamp()
  {
    return registeredTimestamp;
  }

  public void setRegisteredTimestamp(Date registeredTimestamp)
  {
    this.registeredTimestamp = registeredTimestamp;
  }

  /**
   * Method that serializes the object. The returned string has the following format:
   *
   * description, uniqueId, name, type, precedences list, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);

    return description + SerializationConstants.TOKEN_SKILL_REQUIREMENT
            //                + uniqueId + SerializationConstants.TOKEN_SKILL_REQUIREMENT 
            + name + SerializationConstants.TOKEN_SKILL_REQUIREMENT
            + type + SerializationConstants.TOKEN_SKILL_REQUIREMENT
            + ListsToString.writeSkillRequirementIds(precedents) + SerializationConstants.TOKEN_SKILL_REQUIREMENT
            + stringRegisteredTimestamp;
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * description, uniqueId, name, type, precedences list, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   * @throws java.text.ParseException
   */
  public static SkillRequirement fromString(String object) throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_SKILL_REQUIREMENT);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("SkillRequirement - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    return new SkillRequirement(
            tokenizer.nextToken(), // description
            //                tokenizer.nextToken(),                      // uniqueID
            tokenizer.nextToken(), // name
            tokenizer.nextToken(), // type
            StringToLists.readSkillRequirementIds(tokenizer.nextToken()), // precedence ids list
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * {
   * description, uniqueId, name, type, precedences list, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS") }
   *
   * @return BSON Document format of the object.
   */
  public Document toBSON()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);

    return new Document("description", description)
            //                .append("id", uniqueId)
            .append("name", name)
            .append("type", type)
            .append("precedents", precedents)
            .append("registered", stringRegisteredTimestamp);
  }

  /**
   * Method that deserializes a BSON object. The input BSON needs to have the following format:
   *
   * description, unique id, name, type, precedence ids list, registered
   *
   * @param bsonKPI - BSON to be deserialized.
   * @return Deserialized object.
   */
  public static SkillRequirement fromBSON(Document bsonRequirement)
          throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    return new SkillRequirement(
            bsonRequirement.get("description").toString(),
            //            bsonRequirement.get("id").toString(),
            bsonRequirement.get("name").toString(),
            bsonRequirement.get("type").toString(),
            (List<String>) bsonRequirement.get("precedents"),
            sdf.parse(bsonRequirement.get("registered").toString())
    );
  }
}
