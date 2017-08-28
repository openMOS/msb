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
import eu.openmos.model.SkillType;

/**
 *
 * @author valerio.gentile
 */
public class SkillTypeTest {
    
    public static SkillType getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        SkillType skillType;
        
        String skillTypeDefaultValue = "skillTypeDefaultValue";
        String skillTypeDescription = "skillTypeDescription";
        String skillTypeUniqueId = "skillTypeUniqueId";
        String skillTypeLowerBound = "skillTypeLowerBound";
        String skillTypeUpperBound = "skillTypeUpperBound";
        String skillTypeName = "skillTypeName";
        String skillTypeUnit = "skillTypeUnit";
        String skillTypeType = "skillTypeType";
        
        skillType = new SkillType(
                skillTypeUniqueId, 
                skillTypeName, 
                skillTypeDescription, 
                false,
                registeredTimestamp               
        );
        
        return skillType;
    }
    
    public static List<SkillType> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
