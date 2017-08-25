package eu.openmos.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Execution table row.
 * 
executionTable [tableId, Map(
tableEntryId,
productId,
recipeId, 
recipeValidity, 
activityStatus,
Frequency (? -> Task 4.4))
)]

 * @author valerio.gentile
 */
public class ExecutionTableRow extends Base implements Serializable {    
    private static final Logger logger = Logger.getLogger(ExecutionTableRow.class.getName());
    private static final long serialVersionUID = 6529685098267757686L;
    
    /**
     * Execution table single entry's unique ID.
     */
    private String uniqueId;

    /**
     * Could be either the product instance id or the product model id
     */
    private String productId;

    /**
     * Next recipe to execute.
     */
    private String recipeId;
    
    /**
     * The user could or could not decide which will be the next recipe.
     * It depends on the skill reqs of the product: if more than one skill req has the same "precedence"
     * then the user can decide via HMI what should be the next recipe.
     * TBV with Pedro Ferreira 
     * Also some optimizer will be able to update this field and notify the MSB.
     */
    private String nextRecipeId;
    
    /**
     * List of possible recipe choices.
     */
    private List<String> possibleRecipeChoices;
    
//    private static final int FIELDS_COUNT = 5;    
    
    // default constructor, for reflection stuff
    public ExecutionTableRow() {super();}

    public ExecutionTableRow(String uniqueId, String productId, String recipeId, 
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

    public List<String> getPossibleRecipeChoices() {
        return possibleRecipeChoices;
    }

    public void setPossibleRecipeChoices(List<String> possibleRecipeChoices) {
        this.possibleRecipeChoices = possibleRecipeChoices;
    }
    
    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * unique id,
     * product instance id or product model id,
     * recipe id,
     * recipe validity,
     * activity status,
     * frequency
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        return uniqueId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
//                productId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
//                recipeId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
///*                
//                recipeValidity + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
//                activityStatus + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
//                frequency;
//*/
//                nextRecipeId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
//                ListsToString.writePossibleRecipeChoices(possibleRecipeChoices);
//    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
    * 
     * unique id,
     * product instance id or product model id,
     * recipe id,
     * recipe validity,
     * activity status,
     * frequency
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static ExecutionTableRow fromString(String object) 
//    throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EXECUTION_TABLE_ROW);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("ExecutionTableRow - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new ExecutionTableRow(
//                tokenizer.nextToken(),  // unique id
//                tokenizer.nextToken(),  // product instance id or product model id
//                tokenizer.nextToken(),  // recipe id
///*                
//                Boolean.parseBoolean(tokenizer.nextToken()),  // recipe validity
//                Boolean.parseBoolean(tokenizer.nextToken()),  // activity status
//                tokenizer.nextToken()    // frequency
//*/                
//                tokenizer.nextToken(),  // next recipe id
//                StringToLists.readPossibleRecipeChoices(tokenizer.nextToken())
//        );
//    }   
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * unique id,
     * product instance id or product model id,
     * recipe id,
     * recipe validity,
     * activity status,
     * frequency
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        List<String> listPossibleRecipeChoices = possibleRecipeChoices.stream().map(possibleRecipeChoice -> possibleRecipeChoice).collect(Collectors.toList());
//       
//        doc.append("id", uniqueId);
//        doc.append("productId", productId);
//        doc.append("recipeId", recipeId);
//        doc.append("nextRecipeId", nextRecipeId);
//        doc.append("possibleRecipeChoices", listPossibleRecipeChoices);
//        
//        return doc;
    }
    
    /**
     * Method that deserializes a BSON object.
     * The input BSON needs to have the following format:
     *  
     * uniqueId,
     * productId,
     * recipeId,
     * recipeValidity,
     * activityStatus,
     * frequency
     * 
     * @param bsonRow - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static ExecutionTableRow fromBSON(Document bsonRow) {
        return (ExecutionTableRow)fromBSON2(bsonRow, ExecutionTableRow.class);
//        return new ExecutionTableRow(
//            bsonRow.getString("id"),
//            bsonRow.getString("productId"),
//            bsonRow.getString("recipeId"),
//                bsonRow.getString("nextRecipeId"),
//                (List<String>)bsonRow.get("possibleRecipeChoices")
//        );
    }

//    public static ExecutionTableRow deserialize(String object) 
//    {        
//        ExecutionTableRow objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (ExecutionTableRow) in.readObject();
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