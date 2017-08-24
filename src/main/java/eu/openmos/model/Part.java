package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * A product can be made of parts.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Part extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Part.class.getName());
    private static final long serialVersionUID = 6529685098267757692L;

    /**
     * Part unique identifier.
     */
    private String uniqueId;
    /**
     * Part name.
     */
    private String name;
    /**
     * Part description.
     */
    private String description;
    
//    private static final int FIELDS_COUNT = 4; 

    // reflection stuff
    public Part() {super();}
    
    public Part(String uniqueId, String name, String description, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
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
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_COMPONENT);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_COMPONENT);
//        builder.append(description);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_COMPONENT);
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
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
////    public static Part fromString(String object) throws ParseException {
////        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_COMPONENT);
////        
////        if (tokenizer.countTokens() != FIELDS_COUNT)
////            throw new ParseException("Component - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
////        
////        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
////        
////        return new Part(
////                tokenizer.nextToken(),      // uniqueId 
////                tokenizer.nextToken(),       // name 
////                tokenizer.nextToken(),      // description 
////                sdf.parse(tokenizer.nextToken())             // registered
////        );
////    }

     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 uniqueId,
 name,
 description,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();        
//        Document doc = new Document();  
//        
//        doc.append("id", uniqueId);
//        doc.append("name", name);
//        doc.append("description", description);
//        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
//        
//        return doc;
    }
    
//    public static Part deserialize(String object) 
//    {        
//        Part objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (Part) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }
}
