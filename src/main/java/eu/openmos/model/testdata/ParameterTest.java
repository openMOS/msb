package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.Parameter;

/**
 *
 * @author valerio.gentile
 */
public class ParameterTest {
    
    public static Parameter getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        Parameter parameter;
        
        String parameterDefaultValue = "parameterDefaultValue";
        String parameterDescription = "parameterDescription";
        String parameterUniqueId = "parameterUniqueId";
        String parameterLowerBound = "parameterLowerBound";
        String parameterUpperBound = "parameterUpperBound";
        String parameterName = "parameterName";
        String parameterUnit = "parameterUnit";
        String parameterType = "parameterType";
        
        parameter = new Parameter(
                parameterDefaultValue, 
                parameterDescription, 
                parameterUniqueId, 
                parameterLowerBound, 
                parameterUpperBound, 
                parameterName, 
                parameterUnit,
                parameterType,
                "parameterValueType",
                registeredTimestamp               
        );
        
        return parameter;
    }
    
    public static List<Parameter> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
