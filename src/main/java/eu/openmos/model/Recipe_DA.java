package eu.openmos.model;

import java.util.Date;
import java.util.List;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;

@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class Recipe_DA extends Base implements Serializable
{

  private static final Logger logger = Logger.getLogger(Recipe_DA.class.getName());
  private static final long serialVersionUID = 6529685098267757025L;

  /**
   * Recipe ID.
   */
  @XmlElement(name = "uniqueId")
  private String uniqueId;
  /**
   * Recipe name.
   */
  @XmlElement(name = "name")
  private String name;
  /**
   * Recipe description.
   */
  @XmlElement(name = "description")
  private String description;
  /**
   * Whether the recipe is valid or not.
   */
  @XmlElement(name = "valid")
  private boolean valid = false;
  /**
   * Recipe's parameter settings. These must match the skill's parameters.
   */
  @XmlElement(name = "parameterSettings")
  private List<ParameterSetting> parameterSettings;
  /**
   * The skills necessary to execute this recipe.
   */
  @XmlElement(name = "skillRequirements")
  private List<SkillRequirement> skillRequirements;

  /**
   * Pointer to the skill.
   */
  @XmlElement(name = "skill_id")
  private String skill_id;

  /**
   * Default constructor, for reflection
   */
  public Recipe_DA()
  {
    super();
  }

  /**
   * Parameterized constructor.
   *
   * @param description - Recipe's description.
   * @param uniqueId - Recipe's ID.
   * @param name - Recipe's name.
   * @param parameterSettings - Recipe's parameter settings. These must match the skill's parameters.
   * @param skillRequirements - The skills necessary to execute this recipe.
   * @param skill_id
   * @param valid
   * @param registeredTimestamp
   */
  public Recipe_DA(String description,
          String uniqueId,
          String name,
          List<ParameterSetting> parameterSettings,
          List<SkillRequirement> skillRequirements,
          String skill_id,
          boolean valid,
          Date registeredTimestamp)
  {
    super(registeredTimestamp);

    this.description = description;
    this.uniqueId = uniqueId;
    this.name = name;
    this.parameterSettings = parameterSettings;
    this.skillRequirements = skillRequirements;
    this.skill_id = skill_id;
    this.valid = valid;
  }

  public List<SkillRequirement> getSkillRequirements()
  {
    return skillRequirements;
  }

  public void setSkillRequirements(List<SkillRequirement> skillRequirements)
  {
    this.skillRequirements = skillRequirements;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId)
  {
    this.uniqueId = uniqueId;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<ParameterSetting> getParameterSettings()
  {
    return parameterSettings;
  }

  public void setParameterSettings(List<ParameterSetting> parameterSettings)
  {
    this.parameterSettings = parameterSettings;
  }

  public String getSkill_id()
  {
    return skill_id;
  }

  public void setSkill_id(String skill_id)
  {
    this.skill_id = skill_id;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void setValid(boolean valid)
  {
    this.valid = valid;
  }

  public static Recipe_DA createRecipe_DA(Recipe recipe)
  {
    Recipe_DA recipe_da = new Recipe_DA();
    recipe_da.setDescription(recipe.getDescription());
    recipe_da.setName(recipe.getName());
    recipe_da.setParameterSettings(recipe.getParameterSettings());
    recipe_da.setRegistered(recipe.getRegistered());
    recipe_da.setSkill_id(recipe.getSkill().getUniqueId());
    recipe_da.setSkillRequirements(recipe.getSkillRequirements());
    recipe_da.setUniqueId(recipe.getUniqueId());
    recipe_da.setValid(recipe.isValid());
    return recipe_da;
  }

}
