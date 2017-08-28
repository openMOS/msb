package eu.openmos.model.testdata;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import eu.openmos.model.KPI;
import eu.openmos.model.KPISetting;

/**
 *
 * @author valerio.gentile
 */
public class KPISettingTest {

    public static KPISetting getTestObject()
    {
        KPISetting kpiSetting;
        
        String kpiSettingDescription = "kpiSettingDescription";
        String kpiSettingId = "kpiSettingId";
        String kpiSettingName = "kpiSettingName";
        String kpiSettingValue = "kpiSettingValue";
        KPI kpi = KPITest.getTestObject();
  
        Date d = new Date();
        kpiSetting = new KPISetting(
                kpiSettingDescription, 
                kpiSettingId, 
                kpiSettingName, 
                kpi,
                "kpiSettingType",
                "kpiSettingUnit",
                kpiSettingValue, 
                d);
        
        return kpiSetting;
    }
    
    public static List<KPISetting> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }    
}
