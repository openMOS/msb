package eu.openmos.model;

import eu.openmos.model.utilities.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object used to describe a Product.
 * As decided in Masmec meeting in july 2017, the system must have the concept of "product"
 * independently from orders line. 
 * With product we mean product model (product A, product B, etc) and not product instance,
 * that is part of the order.
 * 
 * The list of product (product models) will come from the MSB.
 * The HMI will have a view for creating orders and sending them down to MSB.
 * 
 * Don't know yet if products (product models) need to be stored into the cloud platform database.
 * For now i don't store them. Guess having the class is enough for HMI.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * 
 */
public class Product extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Product.class.getName());
    private static final long serialVersionUID = 6529685098267757023L;
    
    /**
     * Id of the product family, of the model of the product (product A, product B, etc).
     */
    private String uniqueId;    
    /**
     * Product model name.
     */
    private String name;
    /**
     * Product model description.
     */
    private String description;
    /**
     * List of parts (components).
     * MSB and WP4 Bari decision: we will not use parts (components) for any demonstrator so far.
    */
    private List<Part> parts;
    /**
     * List of skills requirements that need to be executed.
     */
    private List<SkillRequirement> skillRequirements;
    
//    private static final int FIELDS_COUNT = 6;
    
    /**
     * Default constructor, for reflection purpose.
     */
    public Product() {super();} 
    
    /**
     * Parameterized constructor.
     * 
     * @param modelId - product model id
     * @param name - product instance name
     * @param description - product instance description
     * @param parts - list of parts
     * @param skillRequirements - list of skills requirements
     * @param registeredTimestamp - timestamp of object creation
     */
    public Product(String uniqueId, String name, 
            String description, List<Part> parts, 
            List<SkillRequirement> skillRequirements, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
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
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> partIds = null;
        if (parts != null)
            partIds = parts.stream().map(part -> part.getUniqueId()).collect(Collectors.toList());
        
        List<String> skillRequirementIds = null;
        if (skillRequirements != null)
            skillRequirementIds = skillRequirements.stream().map(skillRequirement -> skillRequirement.getName()).collect(Collectors.toList());        
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        doc.append(DatabaseConstants.PART_IDS, partIds);
        doc.append(DatabaseConstants.SKILL_REQUIREMENT_IDS, skillRequirementIds);      
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(registered));
        
        return doc;
    }
}
