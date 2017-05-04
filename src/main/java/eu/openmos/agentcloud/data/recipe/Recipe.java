/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Object that describe a recipe of the system.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 */
public class Recipe {    
    /**
     * Recipe's description.
     */
    private String description;
    /**
     * Recipe's ID.
     */
    private String uniqueId;
    /**
     * Recipe's KPI Settings. These must match the skill's KPIs.
     */
    private List<KPISetting> kpisSetting;
    /**
     * Recipe's name.
     */
    private String name;
    /**
     * Recipe's parameter settings. These must match the skill's parameters.
     */
    private List<ParameterSetting> parametersSetting;
    /**
     * The agent capable of executing this recipe.
     */
    private String uniqueAgentName;
    /**
     * The skills necessary to execute this recipe.
     */
    private List<SkillRequirement> skillRequirements;

    /**
     * Default constructor.
     */
    public Recipe() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Recipe's description.
     * @param uniqueId - Recipe's ID.
     * @param kpisSetting - Recipe's KPI Settings. These must match the skill's 
     * KPIs.
     * @param name - Recipe's name.
     * @param parametersSetting - Recipe's parameter settings. These must match 
     * the skill's parameters.
     * @param uniqueAgentName - The agent capable of executing this recipe.
     * @param skillRequirements - The skills necessary to execute this recipe.
     */
    public Recipe(String description, String uniqueId, List<KPISetting> kpisSetting, String name, List<ParameterSetting> parametersSetting, String uniqueAgentName, List<SkillRequirement> skillRequirements) {
        this.description = description;
        this.uniqueId = uniqueId;
        this.kpisSetting = kpisSetting;
        this.name = name;
        this.parametersSetting = parametersSetting;
        this.uniqueAgentName = uniqueAgentName;
        this.skillRequirements = skillRequirements;
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

    public List<KPISetting> getKpisSetting() {
        return kpisSetting;
    }

    public void setKpisSetting(List<KPISetting> kpisSetting) {
        this.kpisSetting = kpisSetting;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterSetting> getParametersSetting() {
        return parametersSetting;
    }

    public void setParametersSetting(List<ParameterSetting> parametersSetting) {
        this.parametersSetting = parametersSetting;
    } 

     /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(description);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        builder.append(uniqueId);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        if(kpisSetting == null || kpisSetting.isEmpty())
            builder.append(" ");
        else {
            for(KPISetting kpiSetting : kpisSetting) {
                builder.append(kpiSetting.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING_LIST_ITEM);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        builder.append(name);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        if(parametersSetting == null || parametersSetting.isEmpty())
            builder.append(" ");
        else {
            for(ParameterSetting parameterSetting : parametersSetting) {
                builder.append(parameterSetting.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER_SETTING_LIST_ITEM);
            }
         }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        builder.append(uniqueAgentName);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        if(skillRequirements == null || skillRequirements.isEmpty())
            builder.append(" ");
        else {
            for(SkillRequirement skillRequirement : skillRequirements) {
                builder.append(skillRequirement.toString());
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
    public static Recipe fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_RECIPE);
        String description = tokenizer.nextToken();
        String uniqueId = tokenizer.nextToken();
        StringTokenizer kpisSettingsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING_LIST_ITEM);
        List<KPISetting> kpisSettings = new LinkedList<>();
        while(kpisSettingsTokenizer.hasMoreTokens()) {
            String token = kpisSettingsTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                kpisSettings.add(KPISetting.fromString(token));
        }
        
        String name = tokenizer.nextToken();
        
        StringTokenizer parametersSettingsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER_SETTING_LIST_ITEM);
        List<ParameterSetting> parametersSettings = new LinkedList<>();
        while(parametersSettingsTokenizer.hasMoreTokens()) {
            String token = parametersSettingsTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                parametersSettings.add(ParameterSetting.fromString(token));
        }
        
        String uniqueAgentName = tokenizer.nextToken();
        
        StringTokenizer skillRequirementsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
        List<SkillRequirement> skillRequirements = new LinkedList<>();
        while(skillRequirementsTokenizer.hasMoreTokens()) {
            String token = skillRequirementsTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                skillRequirements.add(SkillRequirement.fromString(token));
        }
        
        return new Recipe(description, uniqueId, kpisSettings, name, parametersSettings, uniqueAgentName, skillRequirements);
    }    
}
