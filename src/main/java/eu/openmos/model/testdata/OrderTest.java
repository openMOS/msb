package eu.openmos.model.testdata;

import eu.openmos.model.Order;
import eu.openmos.model.OrderLine;
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
    
    public static Order getTestObject()
    {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String now = sdf.format(new Date());

            Order o = new Order();
        o.setUniqueId("order_unique_id_" + now);
        o.setDescription("my description_" + now);
        o.setName("order_name_" + now);
        o.setPriority(1);
        List<OrderLine> lpd = new LinkedList();
        for (int y = 1; y < 5; y+=1)
        {
            OrderLine pd1 = new OrderLine();

            pd1.setUniqueId(o.getUniqueId() + "_" + "line" + y);
            pd1.setOrderId(o.getUniqueId());
            pd1.setProductId("product" + y);
            pd1.setQuantity(y*2);
            pd1.setRegistered(new Date());
                
            lpd.add(pd1);
        }        
        o.setOrderLines(lpd);
        o.setRegistered(new Date());
        return o;
    }
    
    public static List<Order> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
