/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.bson.Document;

/**
executionTable [tableId, Map(
tableEntryId,
productId,
recipeId, 
recipeValidity, 
activityStatus,
Frequency (? -> Task 4.4))
)]

     * WP3 semantic model alignment.
 * @author valerio.gentile
 */
public class ExecutionTableRow {    
    /**
     * WP3 semantic model alignment.
     * Execution table single entry's unique ID.
     */
    private String uniqueId;
    /**
     * WP3 semantic model alignment.
     * TODO check with Lboro if it is the product model id (or the product instance id).
     * 16/06/2017 - checked with lboro it could be either the product instance id or the product model id
     * Product model ID.
     */
    private String productId;
    /**
     * WP3 semantic model alignment.
     * Recipe's ID.
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
     * WP3 semantic model alignment.
     * Is the recipe valid or not.
     */
    private boolean recipeValidity;
    /**
     * WP3 semantic model alignment.
     * Is the recipe active or not.
     */
    private boolean activityStatus;
    /**
     * WP3 semantic model alignment.
     * Execution table single row frequency.
     * 
     * MSB alignment.
     * We don't need any frequency anymore.
     */
    private String frequency;

    private static final int FIELDS_COUNT = 6;
    
    // default constructor, for reflection stuff
    public ExecutionTableRow() {}

    public ExecutionTableRow(String uniqueId, String productId, String recipeId, boolean recipeValidity, boolean activityStatus, String frequency) {
        this.uniqueId = uniqueId;
        this.productId = productId;
        this.recipeId = recipeId;
        this.recipeValidity = recipeValidity;
        this.activityStatus = activityStatus;
        this.frequency = frequency;
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

    public boolean isRecipeValidity() {
        return recipeValidity;
    }

    public void setRecipeValidity(boolean recipeValidity) {
        this.recipeValidity = recipeValidity;
    }

    public boolean isActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(boolean activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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
    @Override
    public String toString() {
        return uniqueId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
                productId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
                recipeId + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
                recipeValidity + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
                activityStatus + SerializationConstants.TOKEN_EXECUTION_TABLE_ROW + 
                frequency;
    }
   
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
    public static ExecutionTableRow fromString(String object) 
    throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EXECUTION_TABLE_ROW);

        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("ExecutionTableRow - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        return new ExecutionTableRow(
                tokenizer.nextToken(),  // unique id
                tokenizer.nextToken(),  // product instance id or product model id
                tokenizer.nextToken(),  // recipe id
                Boolean.parseBoolean(tokenizer.nextToken()),  // recipe validity
                Boolean.parseBoolean(tokenizer.nextToken()),  // activity status
                tokenizer.nextToken()    // frequency
        );
    }   
    
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
        Document doc = new Document();
        
        doc.append("id", uniqueId);
        doc.append("productId", productId);
        doc.append("recipeId", recipeId);
        doc.append("recipeValidity", recipeValidity);
        doc.append("activityStatus", activityStatus);
        doc.append("frequency", frequency);
        
        return doc;
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
        return new ExecutionTableRow(
            bsonRow.getString("id"),
            bsonRow.getString("productId"),
            bsonRow.getString("recipeId"),
            bsonRow.getBoolean("recipeValidity"),
            bsonRow.getBoolean("activityStatus"),
            bsonRow.getString("frequency")     
        );
    }
}