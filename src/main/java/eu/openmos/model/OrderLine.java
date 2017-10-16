package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object used to describe the single line of a production order.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class OrderLine extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(OrderLine.class.getName());
    private static final long serialVersionUID = 6529685098267757014L;

    /**
     * Unique id of the line .
     */
    private String uniqueId;    
    /**
     * Id of the order.
     */
    private String orderId;    
    /**
     * Id of the product family, of the model of the product.
     */
    private String productId;    
    /**
     * Amount of products to be produced.
     */
    private int quantity;
    
    /**
     * Default constructor, for reflection purpose.
     */
    public OrderLine() {super();} 

    public OrderLine(String uniqueId, String orderId, String productId, int quantity, Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
