/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import org.bson.Document;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class LogicalLocation extends Location {
    /**
     * Logical location of the product.
     */
    String location;
    /**
     * WP3 semantic model alignment.
     * Product instance ID.
     */
    private String productId;
    /**
     * WP3 semantic model alignment.
     * Product location at the given time.
    */
    private Date registeredTimestamp;    
    
    private static final int FIELDS_COUNT = 3;

    // reflection stuff
    public LogicalLocation() {}
    
    public LogicalLocation(String location, String productId, Date registeredTimestamp) {
        this.location = location;
        
        // WP3 semantic model stuff
        this.productId = productId;
        this.registeredTimestamp = registeredTimestamp;
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

    public Date getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(Date registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * location,
     * product instance id,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(location);
        
        builder.append(SerializationConstants.TOKEN_LOGICAL_LOCATION);
        builder.append(productId);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_LOGICAL_LOCATION);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();        
    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * location,
     * product instance id,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
    public static LogicalLocation fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_LOGICAL_LOCATION);
        
        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("LogicalLocation - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new LogicalLocation(
                tokenizer.nextToken(),      // location 
                tokenizer.nextToken(),       // product instance id 
                sdf.parse(tokenizer.nextToken())             // registeredTimestamp
        );
        
    }

    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * location,
     * product instance id,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append("location", location);
        doc.append("productId", productId);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;
    }
}
