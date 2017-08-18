/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.Equipment;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class EquipmentTest
{

//    public static Equipment getTestObject()
//    {
//        Date registeredTimestamp = new Date();
//                
//        Equipment equipment = new Equipment(
//                "uniqueId", 
//                "name", 
//                "description", 
//                ExecutionTableTest.getTestObject(), 
//                true,
//                SkillTest.getTestListWithoutRecipes(),
//                registeredTimestamp);
//        
//        return equipment;
//    }
  public static Equipment getTestObject(String parentId, int pos)
  {
    Date registeredTimestamp = new Date();

    Equipment equipment = new Equipment(
            parentId + " - equipment " + pos, // uniqueId", 
            parentId + " - equipment " + pos + " name",
            parentId + " - equipment " + pos + " description",
            ExecutionTableTest.getTestObject(parentId + " - equipment " + pos, pos),
            true,
            SkillTest.getTestListWithoutRecipes(),
            "address",
            "status",
            "manifacturer",
            registeredTimestamp);

    return equipment;
  }

//    public static List<Equipment> getTestList()
//    {
//        return new LinkedList<>(Arrays.asList(getTestObject()));        
//    }
  public static List<Equipment> getTestList(String parentId)
  {
    return new LinkedList<>(Arrays.asList(getTestObject(parentId, 1)));
  }
}
