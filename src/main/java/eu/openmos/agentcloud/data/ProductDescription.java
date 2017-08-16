/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import eu.openmos.agentcloud.data.utilities.ListsToString;
import eu.openmos.agentcloud.data.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 * Object used to describe a Product Agent.
 * After creating class Product to describe product models,
 * ProductDescription is a Product + some other fields regarding the order.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 */
public class ProductDescription {
    /**
     * WP3 semantic model alignment.
     * Id of the product instance.
     * It could be the agent name, so that is unique into the system.
     */
    private String uniqueId;    
    /**
     * WP3 semantic model alignment.
     * Id of the product family, of the model of the product.
     */
    private String modelId;    
    /**
     * Product instance's name.
     */
    private String name;
    /**
     * Product instance's description.
     */
    private String description;
    /**
     * Id of the order.
     */
    private String orderId;    
    /**
     * WP3 semantic model alignment.
     * List of components.
     * 
     * MSB and WP4 Bari decision: we will not use components for any demonstrator so far.
    */
    private List<Component> components;
    /**
     * Skills that need to be executed.
     * List of skill requirements.
     */
    private List<SkillRequirement> skillRequirements;
    /**
     * WP3 semantic model alignment.
     * Whether the product is finished or not.
    */
    private boolean finished = false;
    /**
     * WP3 semantic model alignment.
     * Timestamp of product finish.
    */
    private Date finishedTimestamp;
    /**
     * WP3 semantic model alignment.
     * Timestamp of the order line.
    */
    private Date registeredTimestamp;
    
    // private static final int FIELDS_COUNT = 7;
    private static final int FIELDS_COUNT = 10;
    
    /**
     * Default constructor, for reflection purpose.
     */
    public ProductDescription() {} 
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueId - product instance unique id, could be the agent unique identifier
     * @param modelId - product model id
     * @param name - product instance name
     * @param description - product instance description
     * @param orderId - id of the order
     * @param components - list of components
     * @param skillRequirements - list of skills requirements
     * @param finished - finished status
     * @param finishedTimestamp - timestamp of object finish
     * @param registeredTimestamp - timestamp of object creation
     */
    public ProductDescription(String uniqueId, String modelId, String name, 
            String description, String orderId, List<Component> components, 
            List<SkillRequirement> skillRequirements, Date registeredTimestamp) {
        this.uniqueId = uniqueId;
        this.modelId = modelId;
        this.name = name;
        this.description = description;
        this.orderId = orderId;
        this.components = components;
        this.skillRequirements = skillRequirements;
        this.registeredTimestamp = registeredTimestamp;
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
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Date getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(Date registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }

    
    /**
    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * uniqueId,
     * modelId
     * name,
     * description,
     * orderId,
     * list of components,
     * list of skill requirements,
     * finished,
     * finishedTimestamp ("yyyy-MM-dd HH:mm:ss.SSS"),
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();        
        builder.append(uniqueId);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(modelId);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(name);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(description);

        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(orderId);

        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(ListsToString.writeComponents(components));

        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(ListsToString.writeSkillRequirements(skillRequirements));
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(finished);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        if(this.finishedTimestamp != null) {
            String stringFinishedTimestamp = sdf.format(this.finishedTimestamp);
            builder.append(stringFinishedTimestamp);
        } else builder.append("null");

        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();
    }
        
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * uniqueId,
     * modelId
     * name,
     * description,
     * list of components,
     * list of skill requirements,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
    public static ProductDescription fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PRODUCT_DESCRIPTION);
        
        System.out.println(object);
        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("ProductDescription - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        String pi = tokenizer.nextToken();
        String pmi = tokenizer.nextToken();
        String name = tokenizer.nextToken();
        String description = tokenizer.nextToken();
        String orderId = tokenizer.nextToken();
        List<Component> comps = StringToLists.readComponents(tokenizer.nextToken());
        List<SkillRequirement> skillsReq = StringToLists.readSkillRequirements(tokenizer.nextToken());
        tokenizer.nextToken();
        tokenizer.nextToken();
        return new ProductDescription(
                pi,                                 // product instance id
                pmi,                                // product model id
                name,                               // name
                description,                        // description
                orderId,                            // orderId                
                comps,                              // list of components
                skillsReq,                          // list of skill requirements
                sdf.parse(tokenizer.nextToken())    // registeredTimestamp
        );
        
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * uniqueId,
     * modelId
     * name,
     * description,
     * orderId,
     * list of components,
     * list of skill requirements,
     * finished,
     * finishedTimestamp ("yyyy-MM-dd HH:mm:ss.SSS"),
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> componentIds = components.stream().map(component -> component.getUniqueId()).collect(Collectors.toList());
        List<String> skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("modelId", modelId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("orderId", orderId);
        doc.append("components", componentIds);
        doc.append("skillRequirements", skillRequirementIds);      
        doc.append("finished", finished);         
        doc.append("finishedTimestamp", finishedTimestamp == null ? null : new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.finishedTimestamp));  
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(registeredTimestamp));
        
        return doc;
    }
}
