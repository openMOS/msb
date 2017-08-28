package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.PhysicalPort;

/**
 *
 * @author valerio.gentile
 */
public class PhysicalPortTest {
    
    public static PhysicalPort getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        PhysicalPort port;
        
        String physicalPortUniqueId = "physicalPortUniqueId";
        String physicalPortName = "physicalPortName";
        String physicalPortDescription = "physicalPortDescription";
        String physicalPortType = "physicalPortType";
        String physicalPortDirection = "physicalPortDirection";
        
        port = new PhysicalPort(
                physicalPortUniqueId, 
                physicalPortName, 
                physicalPortDescription, 
                physicalPortType,
                physicalPortDirection,
                registeredTimestamp               
        );
        
        return port;
    }
    
    public static List<PhysicalPort> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
