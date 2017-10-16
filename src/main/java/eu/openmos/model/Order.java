package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Order extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757012L;
    
    /**
     * Order ID.
     */
    private String uniqueId;
    /**
     * Order name.
     */
    private String name;
    /**
     * Order description.
     */
    private String description;
    /**
     * Order priority.
     */
    private int priority;    
    /**
     * Order lines.
     */
    private List<OrderLine> lines;
    
    private static final Logger logger = Logger.getLogger(Order.class.getName());
    
    public Order() {super();}

    public Order(String uniqueId, String name, String description, int priority, 
            List<OrderLine> lines, Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.lines = lines;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<OrderLine> getOrderLines() {
        return lines;
    }

    public void setOrderLines(List<OrderLine> lines) {
        this.lines = lines;
    }    

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> orderLineIds = null;
        if (lines != null)
            orderLineIds = lines.stream().map(line -> line.getUniqueId()).collect(Collectors.toList());
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        doc.append(DatabaseConstants.PRIORITY, priority);
        doc.append(DatabaseConstants.ORDER_LINE_IDS, orderLineIds);
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("ORDER TOBSON: " + doc.toString());
        
        return doc;
    }
}
