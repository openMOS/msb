package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.KPI;
import java.util.Date;

/**
 *
 * @author valerio.gentile
 */
public class KPITest {
    
    public static KPI getTestObject()
    {
        KPI kpi = new KPI(
                "description", 
                "uniqueId", 
                "name", 
                "defaultUpperBound", 
                "defaultLowerBound", 
                "currentValue", 
                "unit",
        "type",
        "valueType", new Date());
        
        return kpi;
    }
    
    public static List<KPI> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
