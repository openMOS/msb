package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object used to describe a Product Instance.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 */
public class ProductInstance extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ProductInstance.class.getName());
    private static final long serialVersionUID = 6529685098267757694L;

    /**
     * Id of the product instance.
     * It could be the agent name, so that is unique into the system.
     */
    private String uniqueId;    
    /**
     * Id of the product family, of the model of the product.
     */
    private String productId;    
    /**
     * Product instance name.
     */
    private String name;
    /**
     * Product instance description.
     */
    private String description;
    /**
     * Id of the order.
     */
    private String orderId;    
    /**
     * List of parts.
     * MSB and WP4 Bari decision: we will not use parts for any demonstrator so far.
    */
    private List<Part> parts;
    /**
     * List of skill requirements that need to be executed.
     */
    private List<SkillRequirement> skillRequirements;
    /**
     * Whether the product instance is finished or not.
    */
    private boolean finished = false;
    /**
     * Timestamp of product finish.
    */
    private Date finishedTimestamp;
    
//    private static final int FIELDS_COUNT = 10;
    
    /**
     * Default constructor, for reflection purpose.
     */
    public ProductInstance() {super();} 
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueId - product instance unique id, could be the agent unique identifier
     * @param modelId - product model id
     * @param name - product instance name
     * @param description - product instance description
     * @param orderId - id of the order
     * @param components - list of parts
     * @param skillRequirements - list of skills requirements
     * @param finished - finished status
     * @param finishedTimestamp - timestamp of object finish
     * @param registeredTimestamp - timestamp of object creation
     */
    public ProductInstance(String uniqueId, String modelId, String name, 
            String description, String orderId, List<Part> parts, 
            List<SkillRequirement> skillRequirements, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.uniqueId = uniqueId;
        this.productId = modelId;
        this.name = name;
        this.description = description;
        this.orderId = orderId;
        this.parts = parts;
        this.skillRequirements = skillRequirements;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SkillRequirement> getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(List<SkillRequirement> skillRequirements) {
        this.skillRequirements = skillRequirements;
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

    public String getModelId() {
        return productId;
    }

    public void setModelId(String modelId) {
        this.productId = modelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
    
    /**
    /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 uniqueId,
 productId
 name,
 description,
 orderId,
 list of parts,
 list of skill requirements,
 finished,
 finishedTimestamp ("yyyy-MM-dd HH:mm:ss.SSS"),
 registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();        
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(productId);
//        
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(description);
//
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(orderId);
//
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(ListsToString.writeParts(parts));
//
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(ListsToString.writeSkillRequirements(skillRequirements));
//        
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(finished);
//        
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        if(this.finishedTimestamp != null) {
//            String stringFinishedTimestamp = sdf.format(this.finishedTimestamp);
//            builder.append(stringFinishedTimestamp);
//        } else builder.append("null");
//
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//    }
        
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
 
 uniqueId,
 productId
 name,
 description,
 list of parts,
 list of skill requirements,
 registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static ProductInstance fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
//        
//        System.out.println(object);
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("ProductDescription - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        String pi = tokenizer.nextToken();
//        String pmi = tokenizer.nextToken();
//        String name = tokenizer.nextToken();
//        String description = tokenizer.nextToken();
//        String orderId = tokenizer.nextToken();
//        List<Part> comps = StringToLists.readParts(tokenizer.nextToken());
//        List<SkillRequirement> skillsReq = StringToLists.readSkillRequirements(tokenizer.nextToken());
//        tokenizer.nextToken();
//        tokenizer.nextToken();
//        return new ProductInstance(
//                pi,                                 // product instance id
//                pmi,                                // product model id
//                name,                               // name
//                description,                        // description
//                orderId,                            // orderId                
//                comps,                              // list of parts
//                skillsReq,                          // list of skill requirements
//                sdf.parse(tokenizer.nextToken())    // registered
//        );
//        
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 uniqueId,
 productId
 name,
 description,
 orderId,
 list of parts,
 list of skill requirements,
 finished,
 finishedTimestamp ("yyyy-MM-dd HH:mm:ss.SSS"),
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> partIds = parts.stream().map(part -> part.getUniqueId()).collect(Collectors.toList());
        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());  
        
        doc.append("id", uniqueId);
        doc.append("modelId", productId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("orderId", orderId);
        doc.append("parts", partIds);
        doc.append("skillRequirements", skillRequirementIds);      
        doc.append("finished", finished);         
        doc.append("finishedTimestamp", finishedTimestamp == null ? null : new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.finishedTimestamp));  
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(registered));
        
        return doc;
    }

//    public static ProductInstance deserialize(String object) 
//    {        
//        ProductInstance objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (ProductInstance) in.readObject();
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
