/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.recipe.Recipe;
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
 * Object that represents the data that product agents receive from the Manufacturing
 * Service Bus (related to products).
 * 
 * NOTE: created as a copy of the AgentData class, and then refactored according to the Visio notes
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class RawProductData {
    /**
     * WP3 semantic model alignment.
     * Product instance id.
    */
    private String productId;
    
    /**
     * WP3 semantic model alignment.
     * Logical location of the product instance.
    */
    private LogicalLocation logicalLocation;
    
    /**
     * WP3 semantic model alignment.
     * List of recipes.
    */
    private List<Recipe> recipes;

    /**
     * WP3 semantic model alignment.
     * Timestamp of the object.
    */
    private Date registeredTimestamp;
    
    private static final int FIELDS_COUNT = 4;

    // reflection stuff
    public RawProductData() {
    }

    public RawProductData(String productId, LogicalLocation logicalLocation, List<Recipe> recipes, Date registeredTimestamp) {
        this.productId = productId;
        this.logicalLocation = logicalLocation;
        this.recipes = recipes;
        this.registeredTimestamp = registeredTimestamp;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public LogicalLocation getLogicalLocation() {
        return logicalLocation;
    }

    public void setLogicalLocation(LogicalLocation logicalLocation) {
        this.logicalLocation = logicalLocation;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
    
    public Date getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(Date registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }
    
        
    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * product instance id,
     * logical location,
     * list of recipes,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(productId);
        
        builder.append(SerializationConstants.TOKEN_RAW_PRODUCT_DATA);
        builder.append(logicalLocation.toString());
        
        builder.append(SerializationConstants.TOKEN_RAW_PRODUCT_DATA);
        builder.append(ListsToString.writeRecipes(recipes));
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_RAW_PRODUCT_DATA);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();        
    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * product instance id,
     * logical location,
     * list of recipes,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
    public static RawProductData fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_RAW_PRODUCT_DATA);

        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("RawProductData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new RawProductData(
                tokenizer.nextToken(),                                          // product instance id
                LogicalLocation.fromString(tokenizer.nextToken()),              // logical location
                StringToLists.readRecipes(tokenizer.nextToken()),               // list of recipes
                sdf.parse(tokenizer.nextToken())                                // registeredTimestamp
        );
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * product instance id,
     * logical location,
     * list of recipes,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> recipeIds = recipes.stream().map(recipe -> recipe.getUniqueId()).collect(Collectors.toList());
        
        doc.append("id", productId);
        doc.append("logicalLocation", logicalLocation);
        doc.append("recipes", recipeIds);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
                
        return doc;        
    }
}
