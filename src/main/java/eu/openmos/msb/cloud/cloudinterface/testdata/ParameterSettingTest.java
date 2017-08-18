/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

// import eu.openmos.agentcloud.data.recipe.Parameter;
import eu.openmos.model.Parameter;
// import eu.openmos.agentcloud.data.recipe.ParameterSetting;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.*;

/**
 *
 * @author valerio.gentile
 */
public class ParameterSettingTest
{

  public static ParameterSetting getTestObject()
  {
    ParameterSetting parameterSetting;

    Date registeredTimestamp = new Date();

    String parameterSettingDescription = "parameterSettingDescription";
    String parameterSettingUniqueId = "parameterSettingUniqueId";
    String parameterSettingName = "parameterSettingName";
    String parameterSettingValue = "parameterSettingValue";

    Parameter parameter = ParameterTest.getTestObject();

    /*
     * @param description - Parameter Setting's description.
     * @param id - Parameter Setting's ID.
     * @param name - Parameter Setting's name.
     * @param value - Paramenter Setting's value.
     * @param parameter - pointer to the Parameter
     * @param registeredTimestamp - registration timestamp
     */
    parameterSetting = new ParameterSetting(parameterSettingDescription, parameterSettingUniqueId,
            parameterSettingName, parameterSettingValue,
            parameter, registeredTimestamp
    );

    return parameterSetting;
  }

  public static List<ParameterSetting> getTestList()
  {
    return new LinkedList<>(Arrays.asList(getTestObject()));
  }
}
