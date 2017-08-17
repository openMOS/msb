/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.utilities;

import eu.openmos.model.Component;
import eu.openmos.model.ProductDescription;
// import eu.openmos.agentcloud.data.recipe.Equipment;
// import eu.openmos.agentcloud.data.recipe.ExecutionTableRow;
import eu.openmos.model.KPI;
import eu.openmos.model.KPISetting;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.ParameterSetting;
// import eu.openmos.agentcloud.data.recipe.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
// import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.model.*;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class ListsToString {
    
    public static String writeSkillRequirements(List<SkillRequirement> skillRequirements)
    {
        StringBuilder builder = new StringBuilder();

        if(skillRequirements == null || skillRequirements.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(SkillRequirement skillRequirement : skillRequirements) {
                builder.append(skillRequirement.toString());
                builder.append(SerializationConstants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
            }
        }
        return builder.toString();
    }
    
    public static String writeSkillRequirementIds(List<String> skillRequirementIds)
    {
        StringBuilder builder = new StringBuilder();

        if(skillRequirementIds == null || skillRequirementIds.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String skillRequirementId : skillRequirementIds) {
                builder.append(skillRequirementId);
                builder.append(SerializationConstants.TOKEN_SKILL_REQUIREMENT_ID_LIST_ITEM);
            }
        }
        return builder.toString();
    }
    
    public static String writeSkills(List<Skill> skills)
    {
        StringBuilder builder = new StringBuilder();

        if(skills == null || skills.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(Skill skill : skills) {
                builder.append(skill.toString());
                builder.append(SerializationConstants.TOKEN_SKILL_LIST_ITEM);
            }
        }
        return builder.toString();
    }
    
    public static String writeKPISettings(List<KPISetting> kpiSettings)
    {
        StringBuilder builder = new StringBuilder();

        if(kpiSettings == null || kpiSettings.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(KPISetting kpiSetting : kpiSettings) {
                builder.append(kpiSetting.toString());
                builder.append(SerializationConstants.TOKEN_KPI_SETTING_LIST_ITEM);
            }
        }
        return builder.toString();
    }
    
    public static String writeKPIs(List<KPI> kpis)
    {
        StringBuilder builder = new StringBuilder();

        if(kpis == null || kpis.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(KPI kpi : kpis) {
                builder.append(kpi.toString());
                builder.append(SerializationConstants.TOKEN_KPI_LIST_ITEM);
            }
        }
        return builder.toString();
    }
        
    public static String writeParameterSettings(List<ParameterSetting> parameterSettings)
    {
        StringBuilder builder = new StringBuilder();

        if(parameterSettings == null || parameterSettings.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(ParameterSetting parameterSetting : parameterSettings) {
                builder.append(parameterSetting.toString());
                builder.append(SerializationConstants.TOKEN_PARAMETER_SETTING_LIST_ITEM);
            }
         }
        return builder.toString();
    }

    public static String writeParameters(List<Parameter> parameters)
    {
        StringBuilder builder = new StringBuilder();

        if(parameters == null || parameters.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            parameters.stream().map((parameter) -> {
                builder.append(parameter.toString());
                return parameter;
            }).forEach((_item) -> {
                builder.append(SerializationConstants.TOKEN_PARAMETER_LIST_ITEM);
            });
        }
        return builder.toString();
    }
    
    public static String writeExecutionTableRows(List<ExecutionTableRow> executionTableRows)
    {
        StringBuilder builder = new StringBuilder();

        if(executionTableRows == null || executionTableRows.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(ExecutionTableRow executionTableRow : executionTableRows) {
                builder.append(executionTableRow.toString());
                builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE_ROW_LIST_ITEM);
            }
         }
        return builder.toString();
    }

    public static String writeRecipes(List<Recipe> recipes)
    {
        StringBuilder builder = new StringBuilder();

        if(recipes == null || recipes.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(Recipe recipe : recipes) {
                builder.append(recipe.toString());
                builder.append(SerializationConstants.TOKEN_RECIPE_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
    public static String writeRecipeIds(List<String> recipeIds)
    {
        StringBuilder builder = new StringBuilder();

        if(recipeIds == null || recipeIds.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String recipeId : recipeIds) {
                builder.append(recipeId);
                builder.append(SerializationConstants.TOKEN_RECIPE_ID_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
    public static String writeKPIIds(List<String> kpiIds)
    {
        StringBuilder builder = new StringBuilder();

        if(kpiIds == null || kpiIds.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String kpiId : kpiIds) {
                builder.append(kpiId);
                builder.append(SerializationConstants.TOKEN_KPI_ID_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
    public static String writeParameterIds(List<String> parameterIds)
    {
        StringBuilder builder = new StringBuilder();

        if(parameterIds == null || parameterIds.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String parameterId : parameterIds) {
                builder.append(parameterId);
                builder.append(SerializationConstants.TOKEN_PARAMETER_ID_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
    public static String writeSensorValues(String[] sensorValues)
    {
        StringBuilder builder = new StringBuilder();

        if(sensorValues == null || sensorValues.length == 0)
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String sensorValue : sensorValues) {
                builder.append(sensorValue.toString());
                builder.append(SerializationConstants.TOKEN_SENSOR_VALUE_LIST_ITEM); 
            }
        }
        return builder.toString();
    }

    public static String writeProductDescriptions(List<ProductDescription> productDescriptions)
    {
        StringBuilder builder = new StringBuilder();

        if(productDescriptions == null || productDescriptions.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(ProductDescription productDescription : productDescriptions) {
                builder.append(productDescription.toString());
                builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
    public static String writeComponents(List<Component> components)
    {
        StringBuilder builder = new StringBuilder();

        if(components == null || components.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(Component component : components) {
                builder.append(component.toString());
                builder.append(SerializationConstants.TOKEN_COMPONENT_LIST_ITEM); 
            }
        }
        return builder.toString();
    }

    public static String writeEquipments(List<Equipment> equipments)
    {
        StringBuilder builder = new StringBuilder();

        if(equipments == null || equipments.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(Equipment equipment : equipments) {
                builder.append(equipment.toString());
                builder.append(SerializationConstants.TOKEN_EQUIPMENT_LIST_ITEM); 
            }
        }
        return builder.toString();
    }

    public static String writePossibleRecipeChoices(List<String> possibleRecipeChoices)
    {
        StringBuilder builder = new StringBuilder();

        if(possibleRecipeChoices == null || possibleRecipeChoices.isEmpty())
            builder.append(SerializationConstants.EMPTY_LIST);
        else {
            for(String possibleRecipeChoice : possibleRecipeChoices) {
                builder.append(possibleRecipeChoices);
                builder.append(SerializationConstants.TOKEN_POSSIBLE_RECIPE_CHOICE_LIST_ITEM); 
            }
        }
        return builder.toString();
    }
    
}
