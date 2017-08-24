/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.Skill;

/**
 *
 * @author valerio.gentile
 */
public class SkillTest {
    
    public static Skill getTestObject()
    {
        Date registeredTimestamp = new Date();
                
        /*
     * @param description - Skill's description.
     * @param uniqueId - Skill's ID.
     * @param kpis - KPIs the skill needs to respect.
     * @param name - Skill's name.
     * @param parameters - Parameters the skill needs to meet.
     * @param type - Skills' type.
     * @param classificationType - Skill's classification type
     * @param skillRequirements - list of skill requirements
     * @param recipes - list of recipes
     * @param registeredTimestamp - registration timestamp        
        */
        
        Skill skill = new Skill(
                "description", 
                "uniqueId", 
                KPITest.getTestList(), 
                "name", 
                "label",
                ParameterTest.getTestList(), 
                "weld", 
                0, 
                // SkillRequirementTest.getTestList(), 
                null,
                RecipeTest.getTestRecipeIdsList(),
                ModuleTest.getTestObject().getUniqueId(),
                registeredTimestamp);
        
        return skill;
    }
    
    public static Skill getTestObjectWithoutRecipes()
    {
        Date registeredTimestamp = new Date();
                
        /*
     * @param description - Skill's description.
     * @param uniqueId - Skill's ID.
     * @param kpis - KPIs the skill needs to respect.
     * @param name - Skill's name.
     * @param parameters - Parameters the skill needs to meet.
     * @param type - Skills' type.
     * @param classificationType - Skill's classification type
     * @param skillRequirements - list of skill requirements
     * @param recipes - list of recipes
     * @param registeredTimestamp - registration timestamp        
        */
        
        Skill skill = new Skill(
                "description", 
                "uniqueId", 
                KPITest.getTestList(), 
                "name", 
                "label",
                ParameterTest.getTestList(), 
                "classificationType", 
                0, 
                // SkillRequirementTest.getTestList(), 
                null,
                null, 
                // ModuleTest.getTestObject().getUniqueId(),
                null,
                registeredTimestamp);
        
        return skill;
    }
    
    public static List<Skill> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
    public static List<Skill> getTestListWithoutRecipes()
    {
        return new LinkedList<>(Arrays.asList(getTestObjectWithoutRecipes()));        
    }
}
