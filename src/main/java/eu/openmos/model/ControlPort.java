package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Control ports implementation.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ControlPort extends Port implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757002L;
    
    public ControlPort() {super();}

    public ControlPort(String uniqueId, String name, String description, 
            String type, String direction, Date registeredTimestamp) {
        super(uniqueId, name, description, type, direction, registeredTimestamp);
    }    

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonPort - BSON to be deserialized.
     * @return deserialized object.
     */
    public static ControlPort fromBSON(Document bsonPort)
    {
        return (ControlPort)ControlPort.fromBSON2(bsonPort, ControlPort.class);
    }        
}
