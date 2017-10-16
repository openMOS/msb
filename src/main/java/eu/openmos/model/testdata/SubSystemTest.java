/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.testdata;

// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class SubSystemTest {

    public static SubSystem getTestObject() {

        SubSystem cpad;

        String equipmentId = "equipmentId";
//        String productId = "productId";
//        String modelId = "modelId";
//        String recipeId = "recipeId";        
        String agentClass = "cpadAgentClass";
        String type = "cpadType";
        
        Date registeredTimestamp = new Date();
        
//        List<String> parameters = new LinkedList<>(Arrays.asList("parameters"));

        List<Parameter> skillParameters = ParameterTest.getTestList();
        List<KPISetting> kpiSettings = KPISettingTest.getTestList();
        List<ParameterSetting> parameterSettings = ParameterSettingTest.getTestList();
        List<SkillRequirement> skillRequirements = SkillRequirementTest.getTestList();
        List<Recipe> recipes = RecipeTest.getTestList();
        List<KPI> kpis = KPITest.getTestList();
        List<Skill> skills = SkillTest.getTestList();
        List<PhysicalPort> ports = PhysicalPortTest.getTestList();
        List<Module> internalModules = ModuleTest.getTestList();
        System.out.println("internalModules" + internalModules);
        
        PhysicalLocation physicalLocation = PhysicalLocationTest.getTestObject();
        LogicalLocation logicalLocation = LogicalLocationTest.getTestObject();
        
    ExecutionTable executionTable = ExecutionTableTest.getTestObject();
    
    cpad = new SubSystem(equipmentId, equipmentId, "description", 
            executionTable, true, skills, ports, recipes, 
            internalModules, "address", "status", "manufacturer", 
            physicalLocation, logicalLocation, type, registeredTimestamp);
    
        return cpad;        
    }    
}

