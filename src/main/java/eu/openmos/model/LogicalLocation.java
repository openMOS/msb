package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * Concrete class for logical location management.
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class LogicalLocation extends Location implements Serializable {
    private static final long serialVersionUID = 6529685098267757010L;
    
    /**
     * Logical location of the product.
     */
    String location;
    /**
     * Product instance ID.
     */
    private String productInstanceId;
    
    /**
     * Empty constructor.
     */
    public LogicalLocation() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param location
     * @param productId
     * @param registeredTimestamp 
     */
    public LogicalLocation(String location, String productInstanceId, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.location = location;        
        this.productInstanceId = productInstanceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    } 

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }
}
