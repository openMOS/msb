/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

// import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.model.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class SkillTest
{

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
            "skillDescription",
            "skillUniqueId",
            KPITest.getTestList(),
            "skillName",
            "skillLabel",
            ParameterTest.getTestList(),
            "weld",
            0,
            null, // SkillRequirementTest.getTestList(), 
            Arrays.asList("recipeid1"),
            "skillequipmentId",
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
            "skillDescription",
            "skillUniqueId",
            KPITest.getTestList(),
            "skillName",
            "skillLabel",
            ParameterTest.getTestList(),
            "weld",
            0,
            null, // SkillRequirementTest.getTestList(),
            Arrays.asList("recipeid1", "recipeid2"),
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
