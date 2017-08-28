package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.Module;
import eu.openmos.model.Skill;

/**
 *
 * @author valerio.gentile
 */
public class ModuleTest {
    
    public static Module getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        List<Module> internalModules = new LinkedList<>();
        
        List<Skill> ls = SkillTest.getTestListWithoutRecipes();
        for (Skill s : ls)
            s.setSubSystemId("uniqueModuleId");
        Module module = new Module(
                "uniqueModuleId", 
                "nameModule", 
                "descriptionModule", 
                true,
                SkillTest.getTestListWithoutRecipes(),
                PhysicalPortTest.getTestList(),
                internalModules,
                "address",
                "status",
                "manifacturer",
                null,   // parentId
                null,   // parentType
                registeredTimestamp);
        
        return module;
    }

    public static Module getTestObject(String parentId, int pos)
    {
        Date registeredTimestamp = new Date();
        
        List<Module> internalModules = new LinkedList<>();
                
        Module equipment = new Module(
                parentId + " - equipment " + pos,         // uniqueId", 
                parentId + " - equipment " + pos + " name", 
                parentId + " - equipment " + pos + " description", 
//                ExecutionTableTest.getTestObject(parentId + " - equipment " + pos, pos), 
                true,
                SkillTest.getTestListWithoutRecipes(),
                PhysicalPortTest.getTestList(),
                internalModules,
                "address",
                "status",
                "manifacturer",
                null,   // parentId
                null,   // parentType
                registeredTimestamp);
        
        return equipment;
    }
    
    public static List<Module> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }

    public static List<Module> getTestList(String parentId) {
        return new LinkedList<>(Arrays.asList(getTestObject(parentId, 1)));        
    }
}
