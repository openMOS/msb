/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.recipe.KPI;
import eu.openmos.agentcloud.data.recipe.KPISetting;
import eu.openmos.agentcloud.data.recipe.Parameter;
import eu.openmos.agentcloud.data.recipe.ParameterSetting;
import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.agentcloud.utilities.Constants;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Describes the agent that arrives to the cloud from the Manufacturing
 * Service Bus to be created.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 *      
 */
public class CyberPhysicalAgentDescription {
    /**
     * Agent's name.
     */
    private String uniqueName;
    /**
     * Agent's Java class.
     */
    private String agentClass;
    /**
     * Agent's type.
     */
    private String type;
    /**
     * Agent's general parameters.
     */
    private List<String> agentParameters;
    /**
     * The skills the agent is capable of performing (Resource/Transport Agent).
     */
    private List<Skill> skills;
    /**
     * The recipes the agent can apply (Resource/Transport Agent).
     */
    private List<Recipe> recipes;
    /**
     * The Physical Location of the device abstracted by this agent.
     */
    private PhysicalLocation physicalLocation;
    /**
     * The Logical location of the agent.
     */
    private LogicalLocation logicalLocation;
    /**
     * The skill requirements of the agent (Product Agent).
     */
    private List<SkillRequirement> skillRequirements;

    /**
     * Default constructor.
     */
    public CyberPhysicalAgentDescription() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueName - Agent's name.
     * @param agentClass - Agent's class.
     * @param type - Agent's type.
     * @param agentParameters - Agent's general parameters.
     * @param skills - The skills the agent is capable of performing (Resource/
     * Transport Agent).
     * @param recipes - The recipes the agent can apply (Resource/Transport Agent).
     * @param physicalLocation - The Physical Location of the device abstracted 
     * by this agent.
     * @param logicalLocation - The skill requirements of the agent (Product Agent).
     * @param skillRequirements - The skill requirements of the agent (Product 
     * Agent).
     */
    public CyberPhysicalAgentDescription(String uniqueName, String agentClass, String type, List<String> agentParameters, List<Skill> skills, List<Recipe> recipes, PhysicalLocation physicalLocation, LogicalLocation logicalLocation, List<SkillRequirement> skillRequirements) {
        this.uniqueName = uniqueName;
        this.agentClass = agentClass;
        this.type = type;
        this.agentParameters = agentParameters;
        this.skills = skills;
        this.recipes = recipes;
        this.logicalLocation = logicalLocation;
        this.physicalLocation = physicalLocation;
        this.skillRequirements = skillRequirements;
    }

    public List<SkillRequirement> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
    }

    public PhysicalLocation getPhysicalLocation() {
        return physicalLocation;
    }

    public void setPhysicalLocation(PhysicalLocation physicalLocation) {
        this.physicalLocation = physicalLocation;
    }

    public LogicalLocation getLogicalLocation() {
        return logicalLocation;
    }

    public void setLogicalLocation(LogicalLocation logicalLocation) {
        this.logicalLocation = logicalLocation;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getAgentClass() {
        return agentClass;
    }

    public void setAgentClass(String agentClass) {
        this.agentClass = agentClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAgentParameters() {
        return agentParameters;
    }

    public void setAgentParameters(List<String> agentParameters) {
        this.agentParameters = agentParameters;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uniqueName);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        builder.append(agentClass);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        builder.append(type);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        if(agentParameters == null || agentParameters.isEmpty())
            builder.append(" ");
        else {
            for(String parameter : agentParameters) {            
                builder.append(parameter);
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_AGENT_PARAMETER);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        if(skills == null || skills.isEmpty())
            builder.append(" ");
        else {
            for(Skill skill : skills) {         
                builder.append(skill.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_LIST_ITEM);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        if(recipes == null || recipes.isEmpty())
            builder.append(" ");
        else {
            for(Recipe recipe : recipes) {         
                builder.append(recipe.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE_LIST_ITEM);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        builder.append(physicalLocation.toString());
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        builder.append(logicalLocation.toString());
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        if(skillRequirements == null || skillRequirements.isEmpty())
            builder.append(" ");
        else {
            for(SkillRequirement skillReq : skillRequirements) {
                builder.append(skillReq.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
            }
        }
        
        return builder.toString();
    }
    
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static CyberPhysicalAgentDescription fromString(String object) {        
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION);
        String uniqueName = tokenizer.nextToken();
        String agentClass = tokenizer.nextToken();
        String type = tokenizer.nextToken();
        StringTokenizer agentParametersTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_AGENT_PARAMETER);
        List<String> agentParameters = new LinkedList<>();
        while(agentParametersTokenizer.hasMoreTokens()) {
            String token = agentParametersTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                agentParameters.add(token);
        }
        StringTokenizer skillsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_LIST_ITEM);
        List<Skill> skills = new LinkedList<>();
        while(skillsTokenizer.hasMoreTokens()) {
            String token = skillsTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                skills.add(Skill.fromString(token));
        }
        StringTokenizer recipesTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE_LIST_ITEM);
        List<Recipe> recipes = new LinkedList<>();
        while(recipesTokenizer.hasMoreTokens()) {
            String token = recipesTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                recipes.add(Recipe.fromString(token));
        }
        PhysicalLocation physicalLocation = PhysicalLocation.fromString(tokenizer.nextToken());
        LogicalLocation logicalLocation = LogicalLocation.fromString(tokenizer.nextToken());
        
        StringTokenizer skillRequirementsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
        List<SkillRequirement> skillRequirements = new LinkedList<>();
        while(skillRequirementsTokenizer.hasMoreTokens()) {
            String token = skillRequirementsTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                skillRequirements.add(SkillRequirement.fromString(token));
        }
        
        return new CyberPhysicalAgentDescription(uniqueName, agentClass, type, agentParameters, skills, recipes, physicalLocation, logicalLocation, skillRequirements);
    }
    
    public boolean isValid() {
        return true;
    }

    /**
     * Testing purposes only, To be removed.
     */
    public static CyberPhysicalAgentDescription getTestObject(String toString) {
CyberPhysicalAgentDescription cpad;
        String cpadUniqueName = "asd";
        String cpadAgentClass = "asdsa";
        String cpadType = "asdas";
        List<String> cpadParameters = new LinkedList<>(Arrays.asList("asdsad"));
            Skill s;
                String skillDescription = "SkillDescription";
                String skillUniqueId = "SkillUniqueId";
                    KPI kpi;
                    String kpiDescription = "SkillKpiDescription";
                    String kpiUniqueId = "SkillKpiUniqueId";
                    String kpiName = "SkillKpiName";
                    String kpiDefaultUpperBound = "SkillKpiDefaultUpperBound";
                    String kpiDefaultLowerBound = "SkillKpiDefaultLowerBound";
                    String kpiCurrentValue = "SkillKpiCurrentValue";
                    String kpiUnit = "SkillKpiUnit";
                    kpi = new KPI(kpiDescription, kpiUniqueId, kpiName, kpiDefaultUpperBound, kpiDefaultLowerBound, kpiCurrentValue, kpiUnit);
                List<KPI> skillKpis = new LinkedList<>(Arrays.asList(kpi));
                String skillName = "SkillName";
                    Parameter p;
                    String parameterDefaultValue = "SkillParameterDefaultValue";
                    String parameterDescription = "SkillParameterDescription";
                    String parameterUniqueId = "SkillParameterUniqueId";
                    String parameterLowerBound = "SkillParameterLowerBound";
                    String parameterUpperBound = "SkillParameterUpperBound";
                    String parameterName = "SkillParameterName";
                    String parameterUnit = "SkillParameterUnit";
                    p = new Parameter(parameterDefaultValue, parameterDescription, parameterUniqueId, parameterLowerBound, parameterUpperBound, parameterName, parameterUnit);
                List<Parameter> skillParameters = new LinkedList<>(Arrays.asList(p));
                int skillType = 0;
            s = new Skill(skillDescription, skillUniqueId, skillKpis, skillName, skillParameters, skillType);
        List<Skill> cpadSkills = new LinkedList<>(Arrays.asList(s));
            Recipe r;
                String recipeDescription = "asdsad";
                String recipeUniqueId = "asda";
                    KPISetting recipeKpiSetting;
                    String kpiSettingDescription = "asdsad";
                    String kpiSettingId = "asda";
                    String kpiSettingName = "asdsa";
                    String kpiSettingValue = "asds";
                    recipeKpiSetting = new KPISetting(kpiSettingDescription, kpiSettingId, kpiSettingName, kpiSettingValue);
                List<KPISetting> recipeKpiSettings = new LinkedList<>(Arrays.asList(recipeKpiSetting));
                String recipeName = "asda";
                    ParameterSetting recipePS;
                    String parameterSettingDescription = "asdsad";
                    String parameterSettingId = "asda";
                    String parameterSettingName = "asdsa";
                    String parameterSettingValue = "asds";
                    recipePS = new ParameterSetting(parameterSettingDescription, parameterSettingId, parameterSettingName, parameterSettingValue);
                List<ParameterSetting> recipeParameterSettings = new LinkedList<>(Arrays.asList(recipePS));
                String recipeUniqueAgentName = "asdsad";
                    SkillRequirement recipeSR;
                    String skillRequirementDescription = "asdsad";
                    String skillRequirementUniqueId = "asda";
                    String skillRequirementName = "asdsa";
                    int skillRequirementType = 0;
                    recipeSR = new SkillRequirement(skillRequirementDescription, skillRequirementUniqueId, skillRequirementName, skillRequirementType);
                List<SkillRequirement> recipeSkillRequirements = new LinkedList<>(Arrays.asList(recipeSR));
            r = new Recipe(recipeDescription, recipeUniqueId, recipeKpiSettings, recipeName, recipeParameterSettings, recipeUniqueAgentName, recipeSkillRequirements);
        List<Recipe> cpadRecipes = new LinkedList<>(Arrays.asList(r));
        PhysicalLocation cpadPl;
           String physicalLocationReferenceFrameName = "asdas"; 
           long physicalLocationX = 0;
           long physicalLocationY = 0;
           long physicalLocationZ = 0;
           long physicalLocationAlpha = 0;
           long physicalLocationBeta = 0;
           long physicalLocationGamma = 0;
        cpadPl = new PhysicalLocation(physicalLocationReferenceFrameName, physicalLocationX, physicalLocationY, physicalLocationZ, physicalLocationAlpha, physicalLocationBeta, physicalLocationGamma);
        LogicalLocation cpadLl = new LogicalLocation("asdad");
            SkillRequirement cpadSR;
                String cpadSkillRequirementDescription = "asdsad";
                String cpadSkillRequirementUniqueId = "asda";
                String cpadSkillRequirementName = "asdsa";
                int cpadSkillRequirementType = 0;
            cpadSR = new SkillRequirement(cpadSkillRequirementDescription, cpadSkillRequirementUniqueId, cpadSkillRequirementName, cpadSkillRequirementType);
        List<SkillRequirement> cpadSkillRequirements = new LinkedList<>(Arrays.asList(recipeSR));
        cpad = new CyberPhysicalAgentDescription(cpadUniqueName, cpadAgentClass, cpadType, cpadParameters, cpadSkills, cpadRecipes, cpadPl, cpadLl, cpadSkillRequirements);
        
        return cpad;        
    }
}
