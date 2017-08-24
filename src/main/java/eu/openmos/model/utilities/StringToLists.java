/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.utilities;

import eu.openmos.model.Part;
import eu.openmos.model.ProductInstance;
// import eu.openmos.agentcloud.data.recipe.Equipment;
// import eu.openmos.agentcloud.data.recipe.ExecutionTableRow;
// import eu.openmos.agentcloud.data.recipe.KPI;
// import eu.openmos.agentcloud.data.recipe.KPISetting;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.ParameterSetting;
// import eu.openmos.agentcloud.data.recipe.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
// import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import eu.openmos.model.*;

/**
 *
 * @author valerio.gentile
 */
public class StringToLists {
    
//    public static List<KPISetting> readKPISettings(String kpiSettingsString) throws ParseException
//    {
//        StringTokenizer kpiSettingsTokenizer = new StringTokenizer(kpiSettingsString, SerializationConstants.TOKEN_KPI_SETTING_LIST_ITEM);
//        List<KPISetting> kpiSettings = new LinkedList<>();
//        while(kpiSettingsTokenizer.hasMoreTokens()) {
//            String token = kpiSettingsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                kpiSettings.add(KPISetting.fromString(token));
//                kpiSettings.add(KPISetting.deserialize(token));
//        }
//        return kpiSettings;
//    }
//
//    public static List<ParameterSetting> readParameterSettings(String parameterSettingsString)
//            throws ParseException
//    {
//        StringTokenizer parameterSettingsTokenizer = new StringTokenizer(parameterSettingsString, SerializationConstants.TOKEN_PARAMETER_SETTING_LIST_ITEM);
//        List<ParameterSetting> parameterSettings = new LinkedList<>();
//        while(parameterSettingsTokenizer.hasMoreTokens()) {
//            String token = parameterSettingsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                parameterSettings.add(ParameterSetting.fromString(token));
//                parameterSettings.add(ParameterSetting.deserialize(token));            
//        }
//        return parameterSettings;
//    }
//    
//    public static List<SkillRequirement> readSkillRequirements(String skillRequirementsString)
//            throws ParseException
//    {
//        StringTokenizer skillRequirementsTokenizer = new StringTokenizer(skillRequirementsString, SerializationConstants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
//        List<SkillRequirement> skillRequirements = new LinkedList<>();
//        while(skillRequirementsTokenizer.hasMoreTokens()) {
//            String token = skillRequirementsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                skillRequirements.add(SkillRequirement.fromString(token));
//                skillRequirements.add(SkillRequirement.deserialize(token));
//        }
//        return skillRequirements;
//    }
//    
//    public static List<String> readSkillRequirementIds(String skillRequirementIdsString)
//            throws ParseException
//    {
//        StringTokenizer skillRequirementIdsTokenizer = new StringTokenizer(skillRequirementIdsString, SerializationConstants.TOKEN_SKILL_REQUIREMENT_ID_LIST_ITEM);
//        List<String> skillRequirementIds = new LinkedList<>();
//        while(skillRequirementIdsTokenizer.hasMoreTokens()) {
//            String token = skillRequirementIdsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                skillRequirementIds.add(token);
//        }
//        return skillRequirementIds;
//    }
//    
//    public static List<Skill> readSkills(String skillsString)
//            throws ParseException
//    {
//        StringTokenizer skillsTokenizer = new StringTokenizer(skillsString, SerializationConstants.TOKEN_SKILL_LIST_ITEM);
//        List<Skill> skills = new LinkedList<>();
//        while(skillsTokenizer.hasMoreTokens()) {
//            String token = skillsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                skills.add(Skill.fromString(token));
//                skills.add(Skill.deserialize(token));
//        }
//        return skills;
//    }
//    
//    public static List<String> readInternalModuleIds(String internalModuleIdsString)
//            throws ParseException
//    {
//        StringTokenizer internalModuleIdsTokenizer = new StringTokenizer(internalModuleIdsString, SerializationConstants.TOKEN_INTERNAL_MODULE_ID_LIST_ITEM);
//        List<String> internalModuleIds = new LinkedList<>();
//        while(internalModuleIdsTokenizer.hasMoreTokens()) {
//            String token = internalModuleIdsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                internalModuleIds.add(token);
//        }
//        return internalModuleIds;
//    }
//    
//    public static List<ExecutionTableRow> readExecutionTableRows(String executionTableRowsString)
//            throws ParseException
//    {
//        StringTokenizer executionTableRowsTokenizer = new StringTokenizer(executionTableRowsString, SerializationConstants.TOKEN_EXECUTION_TABLE_ROW_LIST_ITEM);
//        List<ExecutionTableRow> executionTableRows = new LinkedList<>();
//        while(executionTableRowsTokenizer.hasMoreTokens()) {
//            String token = executionTableRowsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                executionTableRows.add(ExecutionTableRow.fromString(token));
//                executionTableRows.add(ExecutionTableRow.deserialize(token));
//        }
//        return executionTableRows;
//    }
//    
//    public static List<KPI> readKPIs(String kpisString)
//            throws ParseException
//    {
//        StringTokenizer kpisTokenizer = new StringTokenizer(kpisString, SerializationConstants.TOKEN_KPI_LIST_ITEM);
//        List<KPI> kpis = new LinkedList<>();
//        while(kpisTokenizer.hasMoreTokens()) {
//            String token = kpisTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                kpis.add(KPI.fromString(token));
//                kpis.add(KPI.deserialize(token));
//        }
//        return kpis;
//    }
//    
//    public static List<Parameter> readParameters(String parametersString)
//            throws ParseException
//    {
//        StringTokenizer parametersTokenizer = new StringTokenizer(parametersString, SerializationConstants.TOKEN_PARAMETER_LIST_ITEM);
//        List<Parameter> parameters = new LinkedList<>();
//        while(parametersTokenizer.hasMoreTokens()) {
//            String token = parametersTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                parameters.add(Parameter.fromString(token));
//                parameters.add(Parameter.deserialize(token));
//        }
//        return parameters;
//    }
//    
//    public static List<Recipe> readRecipes(String recipesString)
//            throws ParseException
//    {
//        StringTokenizer recipesTokenizer = new StringTokenizer(recipesString, SerializationConstants.TOKEN_RECIPE_LIST_ITEM);
//        List<Recipe> recipes = new LinkedList<>();
//        while(recipesTokenizer.hasMoreTokens()) {
//            String token = recipesTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                recipes.add(Recipe.fromString(token));
//                recipes.add(Recipe.deserialize(token));
//        }
//        return recipes;
//    }
//
//    public static List<String> readRecipeIds(String recipeIdsString)
//            throws ParseException
//    {
//        StringTokenizer recipeIdsTokenizer = new StringTokenizer(recipeIdsString, SerializationConstants.TOKEN_RECIPE_ID_LIST_ITEM);
//        List<String> recipeIds = new LinkedList<>();
//        while(recipeIdsTokenizer.hasMoreTokens()) {
//            String token = recipeIdsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                recipeIds.add(token);
//        }
//        return recipeIds;
//    }
//
//    public static List<String> readKPIIds(String kpiIdsString)
//            throws ParseException
//    {
//        StringTokenizer kpiIdsTokenizer = new StringTokenizer(kpiIdsString, SerializationConstants.TOKEN_KPI_ID_LIST_ITEM);
//        List<String> kpiIds = new LinkedList<>();
//        while(kpiIdsTokenizer.hasMoreTokens()) {
//            String token = kpiIdsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                kpiIds.add(token);
//        }
//        return kpiIds;
//    }
//
//    public static List<String> readParameterIds(String parameterIdsString)
//            throws ParseException
//    {
//        StringTokenizer parameterIdsTokenizer = new StringTokenizer(parameterIdsString, SerializationConstants.TOKEN_PARAMETER_ID_LIST_ITEM);
//        List<String> parameterIds = new LinkedList<>();
//        while(parameterIdsTokenizer.hasMoreTokens()) {
//            String token = parameterIdsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                parameterIds.add(token);
//        }
//        return parameterIds;
//    }
//
//    public static String[] readSensorValues(String sensorValuesString)
//            throws ParseException
//    {
//        StringTokenizer sensorValuesTokenizer = new StringTokenizer(sensorValuesString, SerializationConstants.TOKEN_SENSOR_VALUE_LIST_ITEM);
//        String[] arraySensorValues = new String[sensorValuesTokenizer.countTokens()];
//        
//        int i = 0;
//        while(sensorValuesTokenizer.hasMoreTokens()) {
//            String token = sensorValuesTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                arraySensorValues[i++] = token;
//        }
//        
//        return arraySensorValues;
//    }
//    
//    public static List<ProductInstance> readProductDescriptions(String productDescriptionsString)
//            throws ParseException
//    {
//        StringTokenizer productDescriptionsTokenizer = new StringTokenizer(productDescriptionsString, SerializationConstants.TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM);
//        List<ProductInstance> productDescriptions = new LinkedList<>();
//        while(productDescriptionsTokenizer.hasMoreTokens()) {
//            String token = productDescriptionsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                productDescriptions.add(ProductInstance.fromString(token));
//                productDescriptions.add(ProductInstance.deserialize(token));
//        }
//        return productDescriptions;
//    }
//
//    public static List<Part> readParts(String componentsString)
//            throws ParseException
//    {
//        StringTokenizer componentsTokenizer = new StringTokenizer(componentsString, SerializationConstants.TOKEN_COMPONENT_LIST_ITEM);
//        List<Part> components = new LinkedList<>();
//        while(componentsTokenizer.hasMoreTokens()) {
//            String token = componentsTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
////                components.add(Part.fromString(token));
//                components.add(Part.deserialize(token));
//        }
//        return components;
//    }
//
////    public static List<Equipment> readEquipments(String equipmentsString)
////            throws ParseException
////    {
////        StringTokenizer equipmentsTokenizer = new StringTokenizer(equipmentsString, SerializationConstants.TOKEN_EQUIPMENT_LIST_ITEM);
////        List<Equipment> equipments = new LinkedList<>();
////        while(equipmentsTokenizer.hasMoreTokens()) {
////            String token = equipmentsTokenizer.nextToken();
////            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//////                equipments.add(Equipment.fromString(token));
////                equipments.add(Equipment.deserialize(token));
////        }
////        return equipments;
////    }
//
//    public static List<String> readPossibleRecipeChoices(String possibleRecipeChoicesString)
//            throws ParseException
//    {
//        StringTokenizer possibleRecipeChoicesTokenizer = new StringTokenizer(possibleRecipeChoicesString, SerializationConstants.TOKEN_POSSIBLE_RECIPE_CHOICE_LIST_ITEM);
//        List<String> possibleRecipeChoices = new LinkedList<>();
//        while(possibleRecipeChoicesTokenizer.hasMoreTokens()) {
//            String token = possibleRecipeChoicesTokenizer.nextToken();
//            if(!token.isEmpty() && !token.equals(SerializationConstants.EMPTY_LIST))
//                possibleRecipeChoices.add(token);
//        }
//        return possibleRecipeChoices;
//    }

}
