package eu.openmos.model;

import java.util.List;
import java.util.Date;
import eu.openmos.model.utilities.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object that describes an action a device can perform.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "skill")
@XmlAccessorType(XmlAccessType.FIELD)
public class Skill extends Base implements Serializable {    
    private static final Logger logger = Logger.getLogger(Skill.class.getName());
    private static final long serialVersionUID = 6529685098267757696L;

    public static final int TYPE_ATOMIC = 0;
    public static final int TYPE_COMPOSITE = 1;
    
    /**
     * Skill ID.
     */
    @XmlElement(name = "amlId")
    private String uniqueId;
    /**
     * Skill name.
     */
    @XmlElement(name = "name")
    private String name;
    /**
     * Skill description.
     */
    @XmlElement(name = "description")
    private String description;
    /**
     * Skill type.
     * It's a sort of general description of the skill, ex "weld", "transport", ....
     * MSB alignment: changed the field type from int to string according with Pedro Ferreira.
     */
    private String type;
    /**
     * KPIs the skill needs to respect.
     * It's an optional list.
     */
    private List<KPI> kpis;
    /**
     * Parameters the skill needs to meet.
     */
    private List<Parameter> parameters;
    /**
     * Skill classification type.
     * The skill can be atomic or composite.
     * In case it's composite, it makes sense it has skill requirements attached to it.
     * MSB alignment: changed the field type from String to int according with Pedro Ferreira.
     */
    private int classificationType;    
    /**
     * The skillRequirements list has to be aligned with recipes list:
     * for every recipe we must have one skillrequirement.
    */
    private List<SkillRequirement> skillRequirements;
    /**
     * The recipes list has to be aligned with skillRequiremens list:
     * for every skillrequirement we must have one recipe.
    */
    private List<String> recipeIds;    
    /**
     * Equipment id.
    */
    private String equipmentId;

    /**
     * Revision after semantic model update 13
     */
    private String label;
    
//    private static final int FIELDS_COUNT = 12;
    
    /**
     * Default constructor, for reflection.
     */
    public Skill() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Skill's description.
     * @param uniqueId - Skill's ID.
     * @param kpis - KPIs the skill needs to respect.
     * @param name - Skill's name.
     * @param parameters - Parameters the skill needs to meet.
     * @param type - Skills' type.
     * @param classificationType - Skill's classification type
     * @param skillRequirements - list of skill requirements
     * @param recipeIds - list of recipe ids
     * @param equipmentId - equipment id
     * @param registeredTimestamp - registration timestamp
     */
    public Skill(String description, 
            String uniqueId, 
            List<KPI> kpis, 
            String name, 
            String label,
            List<Parameter> parameters, 
            String type,
            int classificationType,
            List<SkillRequirement> skillRequirements,
//            List<Recipe> recipes,
            List<String> recipeIds,
            String equipmentId,
            Date registeredTimestamp) {
          super(registeredTimestamp);

          this.description = description;
        this.uniqueId = uniqueId;
        this.kpis = kpis;
        this.name = name;
        this.label = label;
        this.parameters = parameters;
        this.type = type;
        
        this.classificationType = classificationType;
        this.skillRequirements = skillRequirements;
        this.recipeIds = recipeIds;
        this.equipmentId = equipmentId;
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

    public List<KPI> getKpis() {
        return kpis;
    }

    public void setKpis(List<KPI> kpis) {
        this.kpis = kpis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getClassificationType() {
        return classificationType;
    }

    public void setClassificationType(int classificationType) {
        this.classificationType = classificationType;
    }

    public List<SkillRequirement> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }

    public List<String> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<String> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * description,
     * uniqueId,
     * list of kpis,
     * name,
     * list of parameters,
     * type,
     * classification_type,
     * list of skill requirements,
     * list of recipe ids,
     * equipment id,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(description);
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(ListsToString.writeKPIs(kpis));
//                
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(label);
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(ListsToString.writeParameters(parameters));
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(type);
//        
//        // WP3 semantic model alignment
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(classificationType);
//
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(ListsToString.writeSkillRequirements(skillRequirements));
//
////        builder.append(Constants.TOKEN_SKILL);
////        builder.append(ListsToString.writeRecipes(recipes));
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
////        builder.append(ListsToString.writeRecipes(recipes));
//        builder.append(ListsToString.writeRecipeIds(recipeIds));
//        
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(equipmentId);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_SKILL);
//        builder.append(stringRegisteredTimestamp);
//        
//        logger.debug(getClass().getName() + " - toString - " + builder.toString());
//        return builder.toString();
//    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * description,
     * uniqueId,
     * list of kpis,
     * name,
     * list of parameters,
     * type,
     * classification_type,
     * list of skill requirements,
     * list of recipe ids,
     * equipment id,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static Skill fromString(String object) throws ParseException {
//        logger.debug(Skill.class.getName() + " - fromString - " + object);
//        
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_SKILL);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//        {
//            logger.error(Skill.class.getName() + " - fromString - " + FIELDS_COUNT + " fields expected, " + tokenizer.countTokens() + " fields received");
//            throw new ParseException("Skill - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        }
//        
//        String description = tokenizer.nextToken();
//        String uniqueId = tokenizer.nextToken();
//        List<KPI> kpis = StringToLists.readKPIs(tokenizer.nextToken());        
//        String name = tokenizer.nextToken();        
//        String label = tokenizer.nextToken();        
//        List<Parameter> parameters = StringToLists.readParameters(tokenizer.nextToken());                
//        String type = tokenizer.nextToken();
//        
//        // WP3 semantic model alignment
//        String classificationType = tokenizer.nextToken();
//        List<SkillRequirement> skillRequirements = StringToLists.readSkillRequirements(tokenizer.nextToken());        
////        List<Recipe> recipes = StringToLists.readRecipes(tokenizer.nextToken());        
//        List<String> recipeIds = StringToLists.readRecipeIds(tokenizer.nextToken());        
//        
//        String equipmentId = tokenizer.nextToken();
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        Date registered = sdf.parse(tokenizer.nextToken());
//        
//        return new Skill(
//                description, 
//                uniqueId, 
//                kpis, 
//                name, 
//                label,
//                parameters, 
//                type,
//                Integer.parseInt(classificationType),
//                skillRequirements,
//                recipeIds,
//                equipmentId,
//                registered
//        );
//    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
  description,
  uniqueId,
  list of kpis,
  name,
  list of parameters,
  type,
  classification_type,
  list of skill requirements,
  list of recipe ids,
  equipment id,
  registered ("yyyy-MM-dd HH:mm:ss.SSS")
 }
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> kpiIds = kpis.stream().map(kpi -> kpi.getUniqueId()).collect(Collectors.toList());        
        List<String> parameterIds = parameters.stream().map(parameter -> parameter.getUniqueId()).collect(Collectors.toList());
//        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getUniqueId()).collect(Collectors.toList());
        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);       
        
        doc.append("description", description);
        doc.append("id", uniqueId);
        doc.append("kpis", kpiIds);
        doc.append("name", name);
        doc.append("label", label);
        doc.append("parameters", parameterIds);        
        doc.append("type", type);     
        doc.append("classificationType", classificationType);
        doc.append("skillRequirements", skillRequirements);           
        doc.append("recipes", recipeIds);
        doc.append("equipmentId", equipmentId);
        doc.append("registered", stringRegisteredTimestamp);
    
        return doc;
    }

//    public static Skill deserialize(String object) 
//    {        
//        Skill objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (Skill) in.readObject();
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
