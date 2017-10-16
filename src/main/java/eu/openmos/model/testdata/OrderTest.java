package eu.openmos.model.testdata;

import eu.openmos.model.Part;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.PartInstance;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class OrderTest {
    
    public static OrderInstance getTestObject()
    {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String now = sdf.format(new Date());

            OrderInstance o = new OrderInstance();
        o.setUniqueId("order_unique_id_" + now);
        o.setDescription("my description_" + now);
        o.setName("order_name_" + now);
        o.setPriority(1);
        List<ProductInstance> lpd = new LinkedList();
        for (int y = 1000; y < 1500; y+=100)
        {
            ProductInstance pd1 = new ProductInstance();

            String pdName = "productionDescription_" + y + "_" + now;

            List<SkillRequirement> lsr = new LinkedList();
            for (int i = 0; i < 10; i++)
            {
                SkillRequirement sr = new SkillRequirement();
                sr.setDescription(pdName + "_skillRequirementDescription_" + i);
//                sr.setUniqueId(pdName + "_skillRequirementUniqueId_" + i);
                sr.setName(pdName + "_skillRequirementName_" + i);
                sr.setType("weld");
//                 sr.setPrecedents(new LinkedList<SkillRequirement>());
                sr.setPrecedents(new LinkedList<SkillReqPrecedent>());
                sr.setRegistered(new Date());

                // pd1.getSkillRequirements().add(sr);
                lsr.add(sr);
            }
            List<PartInstance> comps = new LinkedList();
            Part p1 = new Part("uniqueCpID", "CpName", "CpDescription", new Date());
            PartInstance c1 = new PartInstance("uniqueCpinstanceID", "CpinstanceName", "CpinstanceDescription", p1, new Date());
            comps.add(c1);
            pd1.setParts(comps);
            pd1.setDescription("product description");
            pd1.setProductId("model_xpto");
            pd1.setName(pdName);
            pd1.setOrderId(o.getUniqueId());
            pd1.setRegistered(new Date());
//            pd1.setSkillRequirements(lsr);
            
            pd1.setUniqueId("pd" + y + "uniqueid_" + now.toString());
                
            // o.getProductDescriptions().add(pd1);
            lpd.add(pd1);
        }        
        o.setProductInstances(lpd);
        o.setRegistered(new Date());
        return o;
    }
    
    public static List<OrderInstance> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
