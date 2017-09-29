package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * The physical location of a product instance.
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PhysicalLocation extends Location implements Serializable {
    private static final long serialVersionUID = 6529685098267757020L;
    
    private String referenceFrameName;
    private long x;
    private long y;
    private long z;
    private long alpha;
    private long beta;
    private long gamma;

    /**
     * Product instance ID.
     */
    private String productInstanceId;    
    
    /**
     * Empty constructor, for reflection purpose.
     */
    public PhysicalLocation() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param referenceFrameName
     * @param x
     * @param y
     * @param z
     * @param alpha
     * @param beta
     * @param gamma
     * @param productId
     * @param registeredTimestamp 
     */
    public PhysicalLocation(String referenceFrameName, 
            long x, long y, long z, 
            long alpha, long beta, long gamma,
            String productInstanceId, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.referenceFrameName = referenceFrameName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        
        this.productInstanceId = productInstanceId;
        this.registered = registeredTimestamp;
    }

    public String getReferenceFrameName() {
        return referenceFrameName;
    }

    public void setReferenceFrameName(String referenceFrameName) {
        this.referenceFrameName = referenceFrameName;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public long getAlpha() {
        return alpha;
    }

    public void setAlpha(long alpha) {
        this.alpha = alpha;
    }

    public long getBeta() {
        return beta;
    }

    public void setBeta(long beta) {
        this.beta = beta;
    }

    public long getGamma() {
        return gamma;
    }

    public void setGamma(long gamma) {
        this.gamma = gamma;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productId) {
        this.productInstanceId = productId;
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
