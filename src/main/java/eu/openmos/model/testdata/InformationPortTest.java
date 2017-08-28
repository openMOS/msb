package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.InformationPort;

/**
 *
 * @author valerio.gentile
 */
public class InformationPortTest {
    
    public static InformationPort getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        InformationPort port;
        
        String informationPortUniqueId = "informationPortUniqueId";
        String informationPortName = "informationPortName";
        String informationPortDescription = "informationPortDescription";
        String informationPortType = "informationPortType";
        String informationPortDirection = "informationPortDirection";
        
        port = new InformationPort(
                informationPortUniqueId, 
                informationPortName, 
                informationPortDescription, 
                informationPortType,
                informationPortDirection,
                KPITest.getTestList(),
                registeredTimestamp               
        );
        
        return port;
    }
    
    public static List<InformationPort> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
