package eu.openmos.model;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;
import com.mongodb.util.JSON;



/**
 * Base class for semantic model implementation.
 * Holds the registered field and some utilities for JSON and BSON conversion.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public abstract class Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757683L;
    
    private static final Logger logger = Logger.getLogger(Base.class.getName());
    
    /**
     * Timestamp attached to every model class.
     */
    protected Date registered;
    
    public Base() {}
        
    public Base(Date registered) 
    {
        this.registered = registered;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }
    
    public String toJSON2() {
        String jsonInString = null;
        String jsonInFormattedString;
        
		ObjectMapper mapper = new ObjectMapper();

		try {
			// Convert object to JSON string
			jsonInString = mapper.writeValueAsString(this);
			// System.out.println(jsonInString);

			// Convert object to JSON string and pretty print
			jsonInFormattedString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
			// System.out.println(jsonInFormattedString);
                        
                        
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
                return jsonInString;
	}    

    public Document toBSON2() {
        return Document.parse(toJSON2());        
    }

    public static Object fromJSON2(String jsonString, Class className) 
    {
        Object obj = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Convert JSON string to Object
            obj = mapper.readValue(jsonString, className);
        } catch (JsonGenerationException e) {
            logger.error(e);
        } catch (JsonMappingException e) {
            logger.error(e);
        } catch (IOException e) {
            logger.error(e);
        }
                
        return obj;
    }    

    public static Object fromBSON2(Document bsonDocument, Class className)
    {
        String json = JSON.serialize(bsonDocument);
        return fromJSON2(json, className);
    }
        

/*    
    public byte[] serialize() throws IOException {
        String outString = null;
    
        try 
        {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
            ObjectOutputStream out = new ObjectOutputStream(bOut);
            out.writeObject(this);
            out.close();
            bOut.close();
            byte[] bytes = bOut.toByteArray();
            logger.debug("in base class, " + bytes.length);
            return bytes;
        }
        catch(IOException i) 
        {
            logger.error(i);
        }     
    }
    public String serialize() throws IOException {
        String outString = null;
    
        try 
        {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
            ObjectOutputStream out = new ObjectOutputStream(bOut);
            out.writeObject(this);
            out.close();
            bOut.close();
            outString = bOut.toString();
            return outString;
        }
        catch(IOException i) 
        {
            logger.error(i);
        }     
    }
*/    

}
