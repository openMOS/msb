package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PhysicalPort extends Port implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757018L;
    
    public PhysicalPort() {super();}

    public PhysicalPort(String uniqueId, String name, String description, 
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
     * @return Deserialized object.
     */
    public static PhysicalPort fromBSON(Document bsonPort)
    {
            return (PhysicalPort)PhysicalPort.fromBSON2(bsonPort, PhysicalPort.class);
    }        
}
