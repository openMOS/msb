package eu.openmos.model;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.bson.Document;
// import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Execution table row.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "executionTable")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionTableRow_DA extends Base implements Serializable {    
    private static final Logger logger = Logger.getLogger(ExecutionTableRow_DA.class.getName());
    private static final long serialVersionUID = 6529685098267757005L;
    
    /**
     * Execution table single entry's unique ID.
     */
    @XmlElement(name = "amlId")
    private String uniqueId;

    /**
     * Could be either the product instance id or the product model id.
     */
    @XmlElement(name = "productId")
    private String productId;

    /**
     * Recipe to be executed.
     */
    @XmlElement(name = "recipeId")
    private String recipeId;
    
    /**
     * The user could or not decide which will be the next recipe.
     * It depends on the skill requirements of the product: 
     * if more than one skill req has the same "precedence"
     * then the user can decide via HMI what should be the next recipe.
     * TBV with Pedro Ferreira 
     * Also some optimizer will be able to update this field and notify the MSB.
     */
    @XmlElement(name = "nextRecipeId")
    private String nextRecipeId;
    
    @XmlElement(name = "nextRecipeIdPath")
    private String nextRecipeIdPath;
    
    /**
     * List of possible recipe choices.
     */
    @XmlElement(name = "possibleRecipeChoices")
    private List<String> possibleRecipeChoices;
    
    // default constructor, for reflection stuff
    public ExecutionTableRow_DA() {super();}

    /**
     * Constructor.
     * 
     * @param uniqueId
     * @param productId
     * @param recipeId
     * @param nextRecipeId
     * @param possibleRecipeChoices 
     */
    public ExecutionTableRow_DA(String uniqueId, String productId, String recipeId, 
            String nextRecipeId,
            List<String> possibleRecipeChoices
    ) 
    {
        super();
            
        this.uniqueId = uniqueId;
        this.productId = productId;
        this.recipeId = recipeId;
        this.nextRecipeId = nextRecipeId;
        this.possibleRecipeChoices = possibleRecipeChoices;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getNextRecipeId() {
        return nextRecipeId;
    }

    public void setNextRecipeId(String nextRecipeId) {
        this.nextRecipeId = nextRecipeId;
    }

    public String getNextRecipeIdPath() {
        return nextRecipeIdPath;
    }

    public void setNextRecipeIdPath(String nextRecipeIdPath) {
        this.nextRecipeIdPath = nextRecipeIdPath;
    }
    
    public List<String> getPossibleRecipeChoices() {
        return possibleRecipeChoices;
    }

    public void setPossibleRecipeChoices(List<String> possibleRecipeChoices) {
        this.possibleRecipeChoices = possibleRecipeChoices;
    }
        
     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonRow - BSON to be deserialized.
     * @return Deserialized object.
     */
    public static ExecutionTableRow_DA fromBSON(Document bsonRow) {
        return (ExecutionTableRow_DA)ExecutionTableRow_DA.fromBSON2(bsonRow, ExecutionTableRow_DA.class);
    }
    
    public static ExecutionTableRow_DA createExecutionTableRow_DA(ExecutionTableRow row) 
    {
      ExecutionTableRow_DA row_da = new ExecutionTableRow_DA(row.getUniqueId(), row.getProductId(), 
              row.getRecipeId(), row.getNextRecipeId(), row.getPossibleRecipeChoices());
        return row_da;
    }
}