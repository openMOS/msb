package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.ControlPort;

/**
 *
 * @author valerio.gentile
 */
public class ControlPortTest {
    
    public static ControlPort getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        ControlPort port;
        
        String controlPortUniqueId = "controlPortUniqueId";
        String controlPortName = "controlPortName";
        String controlPortDescription = "controlPortDescription";
        String controlPortType = "controlPortType";
        String controlPortDirection = "controlPortDirection";
        
        port = new ControlPort(
                controlPortUniqueId, 
                controlPortName, 
                controlPortDescription, 
                controlPortType,
                controlPortDirection,
                registeredTimestamp               
        );
        
        return port;
    }
    
    public static List<ControlPort> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
