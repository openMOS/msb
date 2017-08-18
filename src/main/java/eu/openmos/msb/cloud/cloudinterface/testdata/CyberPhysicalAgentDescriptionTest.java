/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
import eu.openmos.agentcloud.data.LogicalLocation;
import eu.openmos.agentcloud.data.PhysicalLocation;
// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.ParameterSetting;
// import eu.openmos.agentcloud.data.recipe.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
// import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import java.util.Date;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class CyberPhysicalAgentDescriptionTest
{

  public static CyberPhysicalAgentDescription getTestObject()
  {

    CyberPhysicalAgentDescription cpad;

    String equipmentId = "CyberPhysicalAgentDescriptionEquipmentId";
//        String productId = "productId";
//        String modelId = "modelId";
//        String recipeId = "recipeId";        
    String agentClass = "CyberPhysicalAgentDescriptionAgentClass";
    String type = "CyberPhysicalAgentDescriptionType";

    Date registeredTimestamp = new Date();

//        List<String> parameters = new LinkedList<>(Arrays.asList("parameters"));
    List<Parameter> skillParameters = ParameterTest.getTestList();
    List<KPISetting> kpiSettings = KPISettingTest.getTestList();
    List<ParameterSetting> parameterSettings = ParameterSettingTest.getTestList();
    List<SkillRequirement> skillRequirements = SkillRequirementTest.getTestList();
    List<Recipe> recipes = RecipeTest.getTestList();
    List<KPI> kpis = KPITest.getTestList();
    List<Skill> skills = SkillTest.getTestList();

    PhysicalLocation physicalLocation = PhysicalLocationTest.getTestObject();
    LogicalLocation logicalLocation = LogicalLocationTest.getTestObject();

    List<Equipment> equipments = EquipmentTest.getTestList(equipmentId);
    ExecutionTable executionTable = ExecutionTableTest.getTestObject(equipmentId, 6);

    cpad = new CyberPhysicalAgentDescription(
            equipmentId,
            executionTable,
            skills,
            recipes,
            equipments,
            physicalLocation,
            logicalLocation,
            //agentClass, 
            type,
            registeredTimestamp
    );
    return cpad;
  }

  public static CyberPhysicalAgentDescription getTestObject(String cpadId, String cpadType)
  {

    CyberPhysicalAgentDescription cpad;

//        String equipmentId = cpadId;        // "CyberPhysicalAgentDescriptionEquipmentId";
//        String productId = "productId";
//        String modelId = "modelId";
//        String recipeId = "recipeId";        
//        String agentClass = "CyberPhysicalAgentDescriptionAgentClass";
    String type = cpadType;         // "CyberPhysicalAgentDescriptionType";

    Date registeredTimestamp = new Date();

//        List<String> parameters = new LinkedList<>(Arrays.asList("parameters"));
    List<Parameter> skillParameters = ParameterTest.getTestList();
    List<KPISetting> kpiSettings = KPISettingTest.getTestList();
    List<ParameterSetting> parameterSettings = ParameterSettingTest.getTestList();
    List<SkillRequirement> skillRequirements = SkillRequirementTest.getTestList();
    List<Recipe> recipes = RecipeTest.getTestList();
    List<KPI> kpis = KPITest.getTestList();
    List<Skill> skills = SkillTest.getTestList();

    PhysicalLocation physicalLocation = PhysicalLocationTest.getTestObject();
    LogicalLocation logicalLocation = LogicalLocationTest.getTestObject();

//        List<Equipment> equipments = EquipmentTest.getTestList();
    List<Equipment> equipments = EquipmentTest.getTestList(cpadId);
    ExecutionTable executionTable = ExecutionTableTest.getTestObject(cpadId, 6);

    cpad = new CyberPhysicalAgentDescription(
            cpadId,
            executionTable,
            skills,
            recipes,
            equipments,
            physicalLocation,
            logicalLocation,
            //agentClass, 
            type,
            registeredTimestamp
    );
    return cpad;
  }
}
