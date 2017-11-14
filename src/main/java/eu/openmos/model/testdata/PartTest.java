package eu.openmos.model.testdata;

import eu.openmos.model.Part;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.PhysicalPort;

/**
 *
 * @author valerio.gentile
 */
public class PartTest {
    
    public static Part getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        Part part;
        
        String partUniqueId = "partUniqueId";
        String partName = "partName";
        String partDescription = "partDescription";
        String partType = "partType";
        
        part = new Part(partUniqueId, partName, partType, registeredTimestamp);
        
        return part;
    }
    
    public static List<Part> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
