package eu.openmos.model;

import eu.openmos.agentcloud.data.*;
import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.ListsToString;
import eu.openmos.model.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bson.Document;

/**
 * Describes workstation and transport:
 *
 * into the cloud, the agent that arrives to the cloud from the Manufacturing Service Bus to be created (the old
 * SubSystem class);
 *
 * into the MSB, the device adapter information (the old RegFile class)
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
@XmlRootElement(name = "deviceAdapter")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubSystem
{

  /**
   * WP3 semantic model alignment Equipment id, it's the agent unique identifier
   */
  @XmlElement(name = "name")
  private String name;    // was equipmentId
  /**
   * WP3 semantic model alignment. Agent execution table.
   */
  private ExecutionTable executionTable;
  /**
   * The skills the agent is capable of performing (Resource/Transport Agent).
   */
  @XmlElement(name = "skills")
  private List<Skill> skills;
  /**
   * The recipes the agent can apply (Resource/Transport Agent).
   */
  @XmlElement(name = "recipes")
  private List<Recipe> recipes;
  /**
   * WP3 semantic model alignment List of inner equipments.
   */
  @XmlElement(name = "devices")
  private List<Equipment> equipments;
  /**
   * The Physical Location of the device abstracted by this agent.
   */
  @XmlElement(name = "physicalLocation")
  private PhysicalLocation physicalLocation;
  /**
   * The Logical location of the agent. TBV if we need it or not
   */
  @XmlElement(name = "logicalLocation")
  private LogicalLocation logicalLocation;
//    /**
//     * Agent's Java class.
//     * TBV if we need it or not
//     */
//    private String agentClass;
  /**
   * Agent's type. TBV if we need it or not
   *
   * TODO: check the value if "resource", "transport", "df_resource..."
   */
  @XmlElement(name = "type")
  private String type;
  /**
   * WP3 semantic model alignment. Agent timestamp.
   */
  private Date registeredTimestamp;

//    private static final int FIELDS_COUNT = 10;
  private static final int FIELDS_COUNT = 9;

  /**
   * Default constructor, for reflection purpose.
   */
  public SubSystem()
  {
  }

  /**
   * Parameterized constructor.
   *
   * @param equipmentId - the equipment id, aka the agent unique identifier
   * @param skills - The skills the agent is capable of performing (Resource/ Transport Agent).
   * @param recipes - The recipes the agent can apply (Resource/Transport Agent).
   * @param equipments - The inner equipment in case of workstation.
   * @param physicalLocation - The Physical Location of the device abstracted by this agent.
   * @param logicalLocation - The skill requirements of the agent (Product Agent).
   * @param agentClass - Agent's class - TBV if necessary.
   * @param type - Agent's type - TBV if necessary.
   * @param registeredTimestamp - the agent creation time
   */
  public SubSystem(String name,
          ExecutionTable executionTable,
          List<Skill> skills,
          List<Recipe> recipes,
          List<Equipment> equipments,
          PhysicalLocation physicalLocation,
          LogicalLocation logicalLocation,
          //            String agentClass, 
          String type,
          Date registeredTimestamp
  )
  {
//        this.equipmentId = equipmentId;
    this.name = name;
    this.registeredTimestamp = registeredTimestamp;
    this.executionTable = executionTable;
    this.skills = skills;
    this.recipes = recipes;
    this.equipments = equipments;
    this.physicalLocation = physicalLocation;
    this.logicalLocation = logicalLocation;
//        this.agentClass = agentClass;
    this.type = type;
  }

  public PhysicalLocation getPhysicalLocation()
  {
    return physicalLocation;
  }

  public void setPhysicalLocation(PhysicalLocation physicalLocation)
  {
    this.physicalLocation = physicalLocation;
  }

  public LogicalLocation getLogicalLocation()
  {
    return logicalLocation;
  }

  public void setLogicalLocation(LogicalLocation logicalLocation)
  {
    this.logicalLocation = logicalLocation;
  }

//    public String getAgentClass() {
//        return agentClass;
//    }
//
//    public void setAgentClass(String agentClass) {
//        this.agentClass = agentClass;
//    }
//
  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public List<Skill> getSkills()
  {
    return skills;
  }

  public void setSkills(List<Skill> skills)
  {
    this.skills = skills;
  }

  public List<Recipe> getRecipes()
  {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes)
  {
    this.recipes = recipes;
  }

//    public String getEquipmentId() {
//        return equipmentId;
//    }
//
//    public void setEquipmentId(String equipmentId) {
//        this.equipmentId = equipmentId;
//    }
//
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ExecutionTable getExecutionTable()
  {
    return executionTable;
  }

  public void setExecutionTable(ExecutionTable executionTable)
  {
    this.executionTable = executionTable;
  }

  public List<Equipment> getEquipments()
  {
    return equipments;
  }

  public void setEquipments(List<Equipment> equipments)
  {
    this.equipments = equipments;
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
   * equipment id, execution table, skills, recipes, equipments, physical location, logical location, agent java class,
   * agent type, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
//        builder.append(equipmentId);
    builder.append(name);

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(executionTable.toString());

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(ListsToString.writeSkills(skills));

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(ListsToString.writeRecipes(recipes));

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(ListsToString.writeEquipments(equipments));

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(physicalLocation.toString());

    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(logicalLocation.toString());

//        builder.append(Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
//        builder.append(agentClass);
//        
    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(type);

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    builder.append(SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
    builder.append(stringRegisteredTimestamp);

    return builder.toString();
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * equipment id, execution table, skills, recipes, equipments, physical location, logical location, agent java class,
   * agent type, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   * @throws java.text.ParseException
   */
  public static SubSystem fromString(String object) throws ParseException
  {
    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("CyberPhysicalAgentDescription - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    return new SubSystem(
            tokenizer.nextToken(), // equipment id
            ExecutionTable.fromString(tokenizer.nextToken()), // execution table
            StringToLists.readSkills(tokenizer.nextToken()), // skills
            StringToLists.readRecipes(tokenizer.nextToken()), // recipes
            StringToLists.readEquipments(tokenizer.nextToken()), // equipments
            PhysicalLocation.fromString(tokenizer.nextToken()), // physical location
            LogicalLocation.fromString(tokenizer.nextToken()), // logical location
            //                tokenizer.nextToken(),                                          // agent class
            tokenizer.nextToken(), // agent type
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * To check if the object is valid.... for now it returns always "true" Used into the optimizeragent.
   *
   * @return always true
   */
  public boolean isValid()
  {
    return true;
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * equipment id, execution table, skills, recipes, equipments, physical location, logical location, agent java class,
   * agent type, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return BSON form of the object.
   */
  public Document toBSON()
  {
    Document doc = new Document();

    List<String> skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());
    List<String> recipeIds = recipes.stream().map(recipe -> recipe.getUniqueId()).collect(Collectors.toList());
    List<String> equipmentIds = equipments.stream().map(equipment -> equipment.getUniqueId()).collect(Collectors.toList());

//        doc.append("id", equipmentId);
    doc.append("id", name);
    doc.append("executionTableId", executionTable.getUniqueId());
    doc.append("skillIds", skillIds);
    doc.append("recipeIds", recipeIds);
    doc.append("equipmentIds", equipmentIds);
    doc.append("physicalLocation", physicalLocation.toBSON());
    doc.append("logicalLocation", logicalLocation.toBSON());
    doc.append("type", type);
    doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));

    return doc;
  }
}
