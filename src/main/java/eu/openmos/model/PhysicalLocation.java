package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * The physical location of a product instance.
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class PhysicalLocation extends Location implements Serializable {
    private static final long serialVersionUID = 6529685098267757501L;
    
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
    private String productId;
    
//    private static final int FIELDS_COUNT = 9;
    
    // reflection stuff
    public PhysicalLocation() {super();}
    
    public PhysicalLocation(String referenceFrameName, 
            long x, long y, long z, 
            long alpha, long beta, long gamma,
            String productId, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.referenceFrameName = referenceFrameName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        
        this.productId = productId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 referenceFrameName,
 x,
 y,
 z,
 alpha,
 beta,
 gamma,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        
//        builder.append(referenceFrameName);
//        
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(x);
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(y);
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(z);
//        
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(alpha);
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(beta);
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(gamma);
//        
//        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//        builder.append(productId);
//        
////        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
////        String stringRegisteredTimestamp = sdf.format(this.registered);
////        builder.append(SerializationConstants.TOKEN_PHYSICAL_LOCATION);
////        builder.append(stringRegisteredTimestamp);
//
//builder.append(registered);
//
//        return builder.toString();
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
 
 referenceFrameName,
 x,
 y,
 z,
 alpha,
 beta,
 gamma,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static PhysicalLocation fromString(String object) throws ParseException  {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PHYSICAL_LOCATION);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("PhysicalLocation - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new PhysicalLocation(
//                tokenizer.nextToken(),                          // referenceFrameName
//                Long.parseLong(tokenizer.nextToken()),          // x
//                Long.parseLong(tokenizer.nextToken()),          // y 
//                Long.parseLong(tokenizer.nextToken()),          // z
//                Long.parseLong(tokenizer.nextToken()),          // alpha
//                Long.parseLong(tokenizer.nextToken()),          // beta
//                Long.parseLong(tokenizer.nextToken()),          // gamma
//                tokenizer.nextToken(),                          // product instance id
//                sdf.parse(tokenizer.nextToken())             // registered                
////                tokenizer.nextToken()             // registered                
//        );
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 referenceFrameName,
 x,
 y,
 z,
 alpha,
 beta,
 gamma,
 product instance id,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        doc.append("referenceFrameName", referenceFrameName);
//        doc.append("x", x);
//        doc.append("y", y);
//        doc.append("z", z);
//        doc.append("alpha", alpha);
//        doc.append("beta", beta);
//        doc.append("gamma", gamma);
//        doc.append("productId", productId);
//        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
//        
//        return doc;
    }
}
