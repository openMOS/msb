/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.testdata;

import eu.openmos.model.KPISetting;
import eu.openmos.model.ParameterSetting;
import eu.openmos.model.RecipeExecutionData;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class RecipeExecutionDataTest {
    
    public static RecipeExecutionData getTestObject()
    {
        String[] sensorValues = new String[1];
        sensorValues[0] = "RawEquipmentDataSensorValue1";
        
//        List<String> kpiIds = new LinkedList<>();        
//        kpiIds.add("kpiIds1");
//        kpiIds.add("kpiIds2");
//        List<String> parameterIds = new LinkedList<>();
//        parameterIds.add("parameterIds1");
//        parameterIds.add("parameterIds2");
        List<KPISetting> kpiSettings = new LinkedList<>();
        kpiSettings.add(KPISettingTest.getTestObject());
        List<ParameterSetting> parameterSettings = new LinkedList<>();
        parameterSettings.add(ParameterSettingTest.getTestObject());
        
        return new RecipeExecutionData(
                "productId", 
                "recipeId", 
//                kpiIds, 
//                parameterIds, 
                kpiSettings, 
                parameterSettings, 
                new Date());
//                new Date().toString());
    }
    
}
