package eu.openmos.model;

import java.util.Date;
import java.util.List;
import eu.openmos.model.utilities.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import org.bson.Document;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
       

/**
 * Object that describe a recipe of the system.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class Recipe extends Base implements Serializable {    
    private static final Logger logger = Logger.getLogger(Recipe.class.getName());
    private static final long serialVersionUID = 6529685098267757695L;

    /**
     * Recipe ID.
     */
    @XmlElement(name = "amlId")
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
     * The agent capable of executing this recipe.
     * 
     * MSB alignment.
     * on the msb side they have the adapter id, and on the adapter class they have the agent id
     */
    private String uniqueAgentName;
    /**
     * Recipe's KPI Settings. These must match the skill's KPIs.
     */
    private List<KPISetting> kpiSettings;
    /**
     * Recipe's parameter settings. These must match the skill's parameters.
     */
    private List<ParameterSetting> parameterSettings;
    /**
     * The skills necessary to execute this recipe.
     */
    @XmlElement(name = "skillRequirements")
    private List<SkillRequirement> skillRequirements;
    /**
     * Pointer to the skill.
     */
    @XmlElement(name = "skill")
    private Skill skill;
    /**
     * Recipe's equipment.
     */
    private Equipment equipment;
    /**
     * Whether the recipe is optimized or not.
     */
    private boolean optimized = false;
    /**
     * Last optimization timestamp.
    */
    private Date lastOptimizationTimestamp;
    
    /**
     * Whether the recipe is valid or not.
     */
    @XmlElement(name = "valid")
    private boolean valid = false;

    /** 
     * MSB alignment.
     */
    private String msbProtocolEndpoint;

//    private static final int FIELDS_COUNT = 14;
    
    /**
     * Default constructor, for reflection
     */
    public Recipe() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Recipe's description.
     * @param uniqueId - Recipe's ID.
     * @param kpisSetting - Recipe's KPI Settings. These must match the skill's 
     * KPIs.
     * @param name - Recipe's name.
     * @param parameterSettings - Recipe's parameter settings. These must match 
     * the skill's parameters.
     * @param uniqueAgentName - The agent capable of executing this recipe.
     * @param skillRequirements - The skills necessary to execute this recipe.
     * @param skill
     * @param equipment
     * @param msbProtocolEndpoint,
     * @param registeredTimestamp
     */
    public Recipe(String description, 
            String uniqueId, 
            List<KPISetting> kpiSettings, 
            String name, 
            List<ParameterSetting> parameterSettings, 
            String uniqueAgentName, 
            List<SkillRequirement> skillRequirements,
            Skill skill,
            Equipment equipment,
            String msbProtocolEndpoint,
            boolean valid,
            Date registeredTimestamp) {
        super(registeredTimestamp);

        this.description = description;
        this.uniqueId = uniqueId;
        this.kpiSettings = kpiSettings;
        this.name = name;
        this.parameterSettings = parameterSettings;
        this.uniqueAgentName = uniqueAgentName;
        this.skillRequirements = skillRequirements;
        
        this.skill = skill;
        this.equipment = equipment;
        this.msbProtocolEndpoint = msbProtocolEndpoint;        
        this.valid = valid;
    }

    public List<SkillRequirement> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }

    public String getUniqueAgentName() {
        return uniqueAgentName;
    }

    public void setUniqueAgentName(String uniqueAgentName) {
        this.uniqueAgentName = uniqueAgentName;
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

    public List<KPISetting> getKpiSettings() {
        return kpiSettings;
    }

    public void setKpiSettings(List<KPISetting> kpiSettings) {
        this.kpiSettings = kpiSettings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterSetting> getParameterSettings() {
        return parameterSettings;
    }

    public void setParameterSettings(List<ParameterSetting> parameterSettings) {
        this.parameterSettings = parameterSettings;
    } 

    public Skill getSkill() {
        return skill; 
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public Date getLastOptimizationTimestamp() {
        return lastOptimizationTimestamp;
    }

    public void setLastOptimizationTimestamp(Date lastOptimizationTimestamp) {
        this.lastOptimizationTimestamp = lastOptimizationTimestamp;
    }

    public String getMsbProtocolEndpoint() {
        return msbProtocolEndpoint;
    }

    public void setMsbProtocolEndpoint(String msbProtocolEndpoint) {
        this.msbProtocolEndpoint = msbProtocolEndpoint;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
        
     /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * description,
     * uniqueId,
     * list of kpi settings,
     * name,
     * list of parameter settings,
     * unique agent name,
     * list of skill requirements,     
     * list of skills,
     * equipment,
     * optimized,
     * lastOptimizationTimestamp,
     * msbProtocolEndpoint,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        
//        builder.append(description);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);       
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(ListsToString.writeKPISettings(kpiSettings));
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(ListsToString.writeParameterSettings(parameterSettings));
//
//        builder.append(SerializationConstants.TOKEN_RECIPE);        
//        builder.append(uniqueAgentName);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);        
//        builder.append(ListsToString.writeSkillRequirements(skillRequirements));
//        
//        // WP3 semantic model alignment
//        builder.append(SerializationConstants.TOKEN_RECIPE);        
//        builder.append(skill.toString());
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(equipment.toString());
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(optimized);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        if(lastOptimizationTimestamp != null) builder.append(lastOptimizationTimestamp.toString());           
//        else builder.append("null");
//                
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(msbProtocolEndpoint);
//                        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        builder.append(valid);
//                        
//        builder.append(SerializationConstants.TOKEN_RECIPE);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * description,
     * uniqueId,
     * list of kpi settings,
     * name,
     * list of parameter settings,
     * unique agent name,
     * list of skill requirements,     
     * list of skills,
     * equipment,
     * optimized,
     * lastOptimizationTimestamp,
     * msbProtocolEndpoint,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static Recipe fromString_OLD(String object) 
//    throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_RECIPE);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("Recipe - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        String description = tokenizer.nextToken();
//        String uniqueId = tokenizer.nextToken();        
//        List<KPISetting> kpiSettings = StringToLists.readKPISettings(tokenizer.nextToken());        
//        String name = tokenizer.nextToken();
//        List<ParameterSetting> parameterSettings = StringToLists.readParameterSettings(tokenizer.nextToken());        
//        String uniqueAgentName = tokenizer.nextToken();
//        List<SkillRequirement> skillRequirements = StringToLists.readSkillRequirements(tokenizer.nextToken());        
//        
//        // WP3 semantic model alignment
//        Skill skill = Skill.fromString(tokenizer.nextToken());     
//        Equipment equipment = Equipment.fromString(tokenizer.nextToken());
//        
//        boolean optimized = Boolean.parseBoolean(tokenizer.nextToken());
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);        
//        String lastOptimizationTimestampString = tokenizer.nextToken();
//        Date lastOptimizationTimestamp = null;
//        if(!lastOptimizationTimestampString.equals("null")) lastOptimizationTimestamp = sdf.parse(lastOptimizationTimestampString);
//        
//        String msbProtocolEndpoint = tokenizer.nextToken();
//
//        boolean valid = Boolean.parseBoolean(tokenizer.nextToken());
//        
//        Date registered = sdf.parse(tokenizer.nextToken());
//        
//        Recipe r = new Recipe(
//                description, 
//                uniqueId, 
//                kpiSettings, 
//                name, 
//                parameterSettings, 
//                uniqueAgentName, 
//                skillRequirements,
//                skill,
//                equipment,
//                msbProtocolEndpoint,
//                valid,
//                registered
//        );
//        r.setOptimized(optimized);
//        r.setLastOptimizationTimestamp(lastOptimizationTimestamp);
//        return r;
//    } 
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
  "description": description,
  "id": unique id,
  "kpiSettings": kpiSettings.toBSON(),
  "name": name,
  "parameterSettings": parameterSettings.toBSON(),
  "uniqueAgentName": uniqueAgentName,
  "skillRequirements": skillRequirements.toBSON(),
  "skillId": skillId,
  "equipmentId": equipmentId,
  "optimized": optimized,
  "lastOptimizationTimestamp": lastOptimizationTimestamp,
  "registered": registered,
 }
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> kpiSettingIds = kpiSettings.stream().map(kpiSetting -> kpiSetting.getUniqueId()).collect(Collectors.toList());
        List<String> parameterSettingIds = parameterSettings.stream().map(parameterSetting -> parameterSetting.getUniqueId()).collect(Collectors.toList());        
//        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getUniqueId()).collect(Collectors.toList());        
        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());        

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        doc.append("description", description);
        doc.append("id", uniqueId);
        doc.append("kpiSettings", kpiSettingIds);
        doc.append("name", name);
        doc.append("parameterSettings", parameterSettingIds);        
        doc.append("uniqueAgentName", uniqueAgentName);
        doc.append("skillRequirements", skillRequirementIds);           
        doc.append("skillId", skill.getUniqueId());
        doc.append("equipmentId", equipment.getUniqueId());
        doc.append("optimized", optimized);
        doc.append("lastOptimizationTimestamp", this.lastOptimizationTimestamp == null ? "null" : sdf.format(this.lastOptimizationTimestamp));
        doc.append("msbProtocolEndpoint", msbProtocolEndpoint);
        
        doc.append("valid", valid);
        
        doc.append("registered", sdf.format(this.registered));
        
        return doc;
    }

//    public static Recipe deserialize(String object) 
//    {        
//        Recipe objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (Recipe) in.readObject();
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
