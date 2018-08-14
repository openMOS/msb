package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.ListsToString;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Describes workstation and transport:
 *
 * into the cloud, the agent that arrives to the cloud from the Manufacturing Service Bus to be created (the old SubSystem class);
 *
 * into the MSB, the device adapter information (the old RegFile class)
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
// @XmlRootElement(name = "deviceAdapter")
// @XmlAccessorType(XmlAccessType.FIELD)
public class SubSystem extends Equipment implements Serializable
{

  private static final Logger logger = Logger.getLogger(SubSystem.class.getName());
  private static final long serialVersionUID = 6529685098267757032L;

  /**
   * Agent execution table.
   */
  private ExecutionTable executionTable;

  /**
   * The recipes the agent can apply (Resource/Transport Agent).
   */
//    @XmlElement(name = "recipes")
  private List<Recipe> recipes;

  /**
   * The Physical Location of the device abstracted by this agent.
   */
//    @XmlElement(name = "physicalLocation")
  private PhysicalLocation physicalLocation;

  /**
   * The Logical location of the agent. TBV if we need it or not
   */
//    @XmlElement(name = "logicalLocation")
  private LogicalLocation logicalLocation;

  /**
   * Agent's type. TBV if we need it or not
   *
   * TODO: check the value if "resource", "transport", "df_resource..." Refactoring from type to ssType (sub system type)
   */
//    @XmlElement(name = "type")    
  private String ssType;

  private String state;

  private transient String statePath;

  private String stage;

  private transient String stagePath;

  private boolean valid = true;

  private transient String changeRecipeObjectID;

  private transient String changeRecipeMethodID;
  /**
   * List of internal modules.
   */
  protected List<Module> internalModules;

  public List<Module> getModules()
  {
    return internalModules;
  }

  public void setInternalModules(List<Module> internalModules)
  {
    this.internalModules = internalModules;
  }

  public String getChangeRecipeObjectID()
  {
    return changeRecipeObjectID;
  }

  public void setChangeRecipeObjectID(String changeRecipeObjectID)
  {
    this.changeRecipeObjectID = changeRecipeObjectID;
  }

  public String getChangeRecipeMethodID()
  {
    return changeRecipeMethodID;
  }

  public void setChangeRecipeMethodID(String changeRecipeMethodID)
  {
    this.changeRecipeMethodID = changeRecipeMethodID;
  }
//    private static final int FIELDS_COUNT = 15;

  /**
   * Default constructor, for reflection purpose.
   */
  public SubSystem()
  {
    super();
  }

  /**
   * Parameterized constructor.
   *
   * @param uniqueId
   * @param name
   * @param description
   * @param executionTable
   * @param connected
   * @param ports
   * @param physicalAdjustmentParameters
   * @param skills - The skills the agent is capable of performing (Resource/ Transport Agent).
   * @param internalModules
   * @param address
   * @param recipes - The recipes the agent can apply (Resource/Transport Agent).
   * @param status
   * @param physicalLocation - The Physical Location of the device abstracted by this agent.
   * @param manufacturer
   * @param logicalLocation - The skill requirements of the agent (Product Agent).
   * @param type - Agent's type - TBV if necessary.
   * @param registeredTimestamp - the agent creation time
   */
  public SubSystem(
          String uniqueId,
          String name,
          String description,
          ExecutionTable executionTable,
          boolean connected,
          List<Skill> skills,
          List<PhysicalPort> ports,
          List<PhysicalAdjustmentParameter> physicalAdjustmentParameters,
          List<Recipe> recipes,
          List<Module> internalModules,
          String address,
          String status,
          String manufacturer,
          PhysicalLocation physicalLocation,
          LogicalLocation logicalLocation,
          String type,
          Date registeredTimestamp
  )
  {
    super(uniqueId, name, description, connected, skills, ports,
            physicalAdjustmentParameters,
            address, status, manufacturer, registeredTimestamp);

    this.executionTable = executionTable;
    this.recipes = recipes;
    this.physicalLocation = physicalLocation;
    this.logicalLocation = logicalLocation;
    this.ssType = type;

    this.internalModules = internalModules;
  }

  public ExecutionTable getExecutionTable()
  {
    return executionTable;
  }

  public void setExecutionTable(ExecutionTable executionTable)
  {
    this.executionTable = executionTable;
  }

  public List<Recipe> getRecipes()
  {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes)
  {
    this.recipes = recipes;
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

  public String getSsType()
  {
    return ssType;
  }

  public void setSsType(String ssType)
  {
    this.ssType = ssType;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public String getStatePath()
  {
    return statePath;
  }

  public void setStatePath(String statePath)
  {
    this.statePath = statePath;
  }

  public String getStage()
  {
    return stage;
  }

  public void setStage(String stage)
  {
    this.stage = stage;
  }

  /**
   * To check if the object is valid.... for now it returns always "true" Used into the optimizeragent.
   *
   * @return always true
   */
  public boolean isValid()
  {
    return this.valid;
  }

  public String getStagePath()
  {
    return stagePath;
  }

  public void setStagePath(String stagePath)
  {
    this.stagePath = stagePath;
  }

  /**
   * Method that serializes the object into a BSON document.
   *
   * @return BSON form of the object.
   */
  public Document toBSON()
  {
    Document doc = new Document();

    logger.trace("subsystem-toBSON - 1");
    List<String> skillIds = null;
    if (skills != null)
    {
      skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());
    }

    logger.trace("subsystem-toBSON - 2");
    List<String> physicalPortIds = null;
    if (physicalPorts != null)
    {
      physicalPortIds = physicalPorts.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());
    }

    logger.trace("subsystem-toBSON - 2.5");
    List<String> physicalAdjustmentParameterIds = null;
    if (physicalAdjustmentParameters != null)
    {
      physicalAdjustmentParameterIds = physicalAdjustmentParameters.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());
    }

    logger.trace("subsystem-toBSON - 3");
    List<String> recipeIds = null;
    if (recipes != null)
    {
      recipeIds = recipes.stream().map(recipe -> recipe.getUniqueId()).collect(Collectors.toList());
    }

    logger.trace("subsystem-toBSON - 3");
    List<String> internalModuleIds = null;
    if (internalModules != null)
    {
      internalModuleIds = internalModules.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());
    }

    logger.trace("subsystem-toBSON - 4");

    String executionTableId = null;
    if (executionTable != null)
    {
      executionTableId = executionTable.getUniqueId();
    }

    doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
    doc.append(DatabaseConstants.NAME, name);
    doc.append(DatabaseConstants.DESCRIPTION, description);
    doc.append(DatabaseConstants.EXECUTION_TABLE_ID, executionTableId);
    doc.append(DatabaseConstants.CONNECTED, connected);
    doc.append(DatabaseConstants.SKILL_IDS, skillIds);

    logger.trace("subsystem-toBSON - 5");
    doc.append(DatabaseConstants.PHYSICAL_PORT_IDS, physicalPortIds);

    logger.trace("subsystem-toBSON - 5.5");
    doc.append(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS, physicalAdjustmentParameterIds);

    logger.trace("subsystem-toBSON - 6");
    doc.append(DatabaseConstants.RECIPE_IDS, recipeIds);
    doc.append(DatabaseConstants.INTERNAL_MODULE_IDS, internalModuleIds);
    doc.append(DatabaseConstants.ADDRESS, address);
    doc.append(DatabaseConstants.STATUS, status);
    doc.append(DatabaseConstants.MANUFACTURER, manufacturer);

    if (physicalLocation != null)
    {
      doc.append(DatabaseConstants.PHYSICAL_LOCATION, physicalLocation.toBSON());
    }
    else
    {
      doc.append(DatabaseConstants.PHYSICAL_LOCATION, null);
    }

    if (logicalLocation != null)
    {
      doc.append(DatabaseConstants.LOGICAL_LOCATION, logicalLocation.toBSON());
    }
    else
    {
      doc.append(DatabaseConstants.LOGICAL_LOCATION, null);
    }

    doc.append(DatabaseConstants.TYPE, ssType);
    doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));

    logger.trace("subsystem-toBSON - 7");

    return doc;
  }

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("SubSystem = ")
            .append(uniqueId).append("\n");
    builder.append(name).append("\n");
    builder.append(description).append("\n");
    builder.append(executionTable.toString()).append("\n");
    builder.append(connected).append("\n");
    builder.append(ListsToString.writeSkills(skills)).append("\n");

    builder.append(ListsToString.writePhysicalPorts(physicalPorts)).append("\n");
    builder.append(ListsToString.writePhysicalAdjustmentParameters(physicalAdjustmentParameters)).append("\n");

    builder.append(ListsToString.writeRecipes(recipes)).append("\n");
    builder.append(address).append("\n");
    builder.append(status).append("\n");
    builder.append(manufacturer).append("\n");

    if (internalModules != null)
    {
      for (Module m : internalModules)
      {
        builder.append(m.toString());
      }
    }
    builder.append("\n");

    if (physicalLocation != null)
    {
      builder.append(physicalLocation.toString()).append("\n");
    }
    else
    {
      builder.append("physicalLocation is null").append("\n");
    }
    if (logicalLocation != null)
    {
      builder.append(logicalLocation.toString()).append("\n");
    }
    else
    {
      builder.append("logicalLocation is null").append("\n");
    }
    builder.append(ssType).append("\n");
    builder.append(registered).append("\n");

    return builder.toString();
  }
}
