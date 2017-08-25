package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Order extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757300L;
    
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
    private List<ProductInstance> productDescriptions;
    
//    private static final int FIELDS_COUNT = 6;
    
    private static final Logger logger = Logger.getLogger(Order.class.getName());
    
    
    // for reflection purpose
    public Order() {super();}

    public Order(String uniqueId, String name, String description, int priority, 
            List<ProductInstance> productDescriptions, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.productDescriptions = productDescriptions;
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.priority = priority;
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

    public List<ProductInstance> getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(List<ProductInstance> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }    

    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * priority,
     * list of product descriptions
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();        
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_ORDER);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_ORDER);
//        builder.append(description);
//
//        builder.append(SerializationConstants.TOKEN_ORDER);
//        builder.append(priority);
//
//        builder.append(SerializationConstants.TOKEN_ORDER);
//        builder.append(ListsToString.writeProductDescriptions(productDescriptions));
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
//        builder.append(SerializationConstants.TOKEN_ORDER);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * priority,
     * list of product descriptions
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
//    public static Order fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_ORDER);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("Order - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new Order(
//                tokenizer.nextToken(),      // uniqueId 
//                tokenizer.nextToken(),       // name 
//                tokenizer.nextToken(),      // description 
//                Integer.parseInt(tokenizer.nextToken()),     // priority 
//                StringToLists.readProductDescriptions(tokenizer.nextToken()),    // list of product descriptions
//                sdf.parse(tokenizer.nextToken())             // registeredTimestamp
//        );        
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * priority,
     * list of product descriptions
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> productDescriptionIds = productDescriptions.stream().map(pd -> pd.getUniqueId()).collect(Collectors.toList());
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("priority", priority);
        doc.append("productDescriptions", productDescriptionIds);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("ORDER TOBSON: " + doc.toString());
        
        return doc;
    }
}