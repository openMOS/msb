/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import org.bson.Document;
import eu.openmos.model.utilities.SerializationConstants;

/**
     * WP3 semantic model alignment.
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Component {
    /**
     * Component unique identifier.
     */
    private String uniqueId;
    /**
     * Component name.
     */
    private String name;
    /**
     * Component description.
     */
    private String description;
    /**
     * Component timestamp.
    */
    private Date registeredTimestamp;
    
    private static final int FIELDS_COUNT = 4; 

    // reflection stuff
    public Component() {}
    
    public Component(String uniqueId, String name, String description, Date registeredTimestamp) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.registeredTimestamp = registeredTimestamp;
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
     * uniqueId,
     * name,
     * description,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uniqueId);
        
        builder.append(SerializationConstants.TOKEN_COMPONENT);
        builder.append(name);
        
        builder.append(SerializationConstants.TOKEN_COMPONENT);
        builder.append(description);

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_COMPONENT);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();
    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
    public static Component fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_COMPONENT);
        
        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("Component - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new Component(
                tokenizer.nextToken(),      // uniqueId 
                tokenizer.nextToken(),       // name 
                tokenizer.nextToken(),      // description 
                sdf.parse(tokenizer.nextToken())             // registeredTimestamp
        );
    }

     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();  
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;
    }
    
}
