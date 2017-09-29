package eu.openmos.model;

import java.util.List;
import java.util.Date;
import eu.openmos.model.utilities.*;
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
    private static final long serialVersionUID = 6529685098267757026L;

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
//    @Deprecated
    private String type;
    private SkillType skillType;
    /**
     * KPIs the skill needs to respect.
     * It's an optional list.
     */
//    @Deprecated
    private List<KPI> kpis;
    /**
     * Information ports that have KPIS.
     */
    private List<InformationPort> informationPorts;
    /**
     * Parameters the skill needs to meet.
     */
//    @Deprecated
    private List<Parameter> parameters;
    /**
     * Parameter ports that have parameters.
     */
    private List<ParameterPort> parameterPorts;
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
     * 
     * Atomic skills have only one skillreq, composite skills have many of them.
    */
//    @Deprecated
    private List<SkillRequirement> skillRequirements;
    /**
     * The recipes list has to be aligned with skillRequiremens list:
     * for every skillrequirement we must have one recipe.
    */
//    @Deprecated
    private List<String> recipeIds;    
    /**
     * Control ports that link to recipes.
     */
    private List<ControlPort> controlPorts;
    /**
     * Equipment id.
    */
    private String subSystemId;

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
            List<InformationPort> informationPorts,
            String name, 
            String label,
            List<Parameter> parameters, 
            List<ParameterPort> parameterPorts,
            String type,
            SkillType skillType,
            int classificationType,
            List<SkillRequirement> skillRequirements,
//            List<Recipe> recipes,
            List<String> recipeIds,
            List<ControlPort> controlPorts,
            String subSystemId,
            Date registeredTimestamp) {
          super(registeredTimestamp);

          this.description = description;
        this.uniqueId = uniqueId;
        
        this.kpis = kpis;
        this.informationPorts = informationPorts;
        
        this.name = name;
        this.label = label;
        this.parameters = parameters;
        this.parameterPorts = parameterPorts;
        
        this.type = type;
        this.skillType = skillType;
        this.skillRequirements = skillRequirements;
        
        this.classificationType = classificationType;
        
        this.recipeIds = recipeIds;
        this.controlPorts = controlPorts;
        
        this.subSystemId = subSystemId;
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

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String equipmentId) {
        this.subSystemId = equipmentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public List<InformationPort> getInformationPorts() {
        return informationPorts;
    }

    public void setInformationPorts(List<InformationPort> informationPorts) {
        this.informationPorts = informationPorts;
    }

    public List<ParameterPort> getParameterPorts() {
        return parameterPorts;
    }

    public void setParameterPorts(List<ParameterPort> parameterPorts) {
        this.parameterPorts = parameterPorts;
    }

    public List<ControlPort> getControlPorts() {
        return controlPorts;
    }

    public void setControlPorts(List<ControlPort> controlPorts) {
        this.controlPorts = controlPorts;
    }
    
     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> kpiIds = null;
        if (kpis != null)
            kpiIds = kpis.stream().map(kpi -> kpi.getUniqueId()).collect(Collectors.toList());        
        
        List<String> parameterIds = null;
        if (parameters != null)
            parameterIds = parameters.stream().map(parameter -> parameter.getUniqueId()).collect(Collectors.toList());
        
//        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getUniqueId()).collect(Collectors.toList());
        List<String> skillRequirementIds = null;
        if (skillRequirements != null)
            skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());

        List<String> controlPortIds = null;
        if (controlPorts != null)
            controlPortIds = controlPorts.stream().map(controlPort -> controlPort.getUniqueId()).collect(Collectors.toList());        
        
        List<String> parameterPortIds = null;
        if (parameterPorts != null)
            parameterPortIds = parameterPorts.stream().map(parameterPort -> parameterPort.getUniqueId()).collect(Collectors.toList());        
        
        List<String> informationPortIds = null;
        if (informationPorts != null)
            informationPortIds = informationPorts.stream().map(informationPort -> informationPort.getUniqueId()).collect(Collectors.toList());        
        
        String skillTypeId = null;
        if (skillType != null)
            skillTypeId = skillType.getUniqueId();
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);       
        
        doc.append("description", description);
        doc.append("id", uniqueId);
        doc.append("kpis", kpiIds);
        doc.append("informationPortIds", informationPortIds);
        doc.append("name", name);
        doc.append("label", label);
        doc.append("parameters", parameterIds);      
        doc.append("parameterPortIds", parameterPortIds);
        doc.append("type", type);     
        doc.append("skillType", skillTypeId);     
        doc.append("classificationType", classificationType);
        doc.append("skillRequirements", skillRequirementIds);           
        doc.append("recipes", recipeIds);
        doc.append("controlPortIds", controlPortIds);
        doc.append("equipmentId", subSystemId);
        doc.append("registered", stringRegisteredTimestamp);
    
        return doc;
    }
}
