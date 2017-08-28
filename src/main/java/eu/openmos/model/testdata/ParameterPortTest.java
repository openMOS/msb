package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.ParameterPort;

/**
 *
 * @author valerio.gentile
 */
public class ParameterPortTest {
    
    public static ParameterPort getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        ParameterPort port;
        
        String parameterPortUniqueId = "parameterPortUniqueId";
        String parameterPortName = "parameterPortName";
        String parameterPortDescription = "parameterPortDescription";
        String parameterPortType = "parameterPortType";
        String parameterPortDirection = "parameterPortDirection";
        
        port = new ParameterPort(
                parameterPortUniqueId, 
                parameterPortName, 
                parameterPortDescription, 
                parameterPortType,
                parameterPortDirection,
                ParameterTest.getTestList(),
                registeredTimestamp               
        );
        
        return port;
    }
    
    public static List<ParameterPort> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
