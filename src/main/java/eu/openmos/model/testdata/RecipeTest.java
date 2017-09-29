package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.Recipe;

/**
 *
 * @author valerio.gentile
 */
public class RecipeTest {
    
    public static Recipe getTestObject(String equipmentId)
    {
        Date registeredTimestamp = new Date();
                
        /*
     * @param description - Recipe's description.
     * @param uniqueId - Recipe's ID.
     * @param kpisSetting - Recipe's KPI Settings. These must match the skill's 
     * KPIs.
     * @param name - Recipe's name.
     * @param parameterSettings - Recipe's parameter settings. These must match 
     * the skill's parameters.
     * @param uniqueAgentName - The agent capable of executing this recipe.
     * @param skillRequirements - The skills necessary to execute this recipe.
     * @param skills
     * @param equipment
     * @param registeredTimestamp        
        */
        
        Recipe recipe = new Recipe(
                "description", 
                "uniqueId", 
                KPISettingTest.getTestList(), 
                "name", 
                ParameterSettingTest.getTestList(), 
                "uniqueAgentName", 
                SkillRequirementTest.getTestList(), 
                SkillTest.getTestObjectWithoutRecipes(), 
                ControlPortTest.getTestObject(),
                // ModuleTest.getTestObject(), 
                // SubSystemTest.getTestObject(),
                equipmentId,    // SubSystemTest.getTestObject().getUniqueId(),
                "recipeMSBProtocolEndpoint",
                true,
                registeredTimestamp);
        
        return recipe;
    }
    
    public static List<Recipe> getTestList(String equipmentId)
    {
        return new LinkedList<>(Arrays.asList(getTestObject(equipmentId)));        
    }

    public static List<String> getTestRecipeIdsList(String equipmentId)
    {
        return new LinkedList<>(Arrays.asList(getTestObject(equipmentId).getUniqueId()));        
    }
}
