package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import org.bson.Document;

/**
 * Concrete class for logical location management.
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class LogicalLocation extends Location implements Serializable {
    private static final long serialVersionUID = 6529685098267757502L;
    
    /**
     * Logical location of the product.
     */
    String location;
    /**
     * Product instance ID.
     */
    private String productId;
    
    private static final int FIELDS_COUNT = 3;

    // reflection stuff
    public LogicalLocation() {super();}
    
    public LogicalLocation(String location, String productId, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.location = location;        
        this.productId = productId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    } 

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 location,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    public String toString_OLD() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(location);
//        
//        builder.append(SerializationConstants.TOKEN_LOGICAL_LOCATION);
//        builder.append(productId);
//        
////        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
////        String stringRegisteredTimestamp = sdf.format(this.registered);
////        builder.append(SerializationConstants.TOKEN_LOGICAL_LOCATION);
////        builder.append(stringRegisteredTimestamp);
//builder.append(registered);
//        
//        return builder.toString();        
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
 
 location,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static LogicalLocation fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_LOGICAL_LOCATION);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("LogicalLocation - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new LogicalLocation(
//                tokenizer.nextToken(),      // location 
//                tokenizer.nextToken(),       // product instance id 
//                sdf.parse(tokenizer.nextToken())             // registered
////                tokenizer.nextToken()             // registered
//        );
//        
//    }

    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 location,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        doc.append("location", location);
//        doc.append("productId", productId);
//        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
//        
//        return doc;
    }
}
