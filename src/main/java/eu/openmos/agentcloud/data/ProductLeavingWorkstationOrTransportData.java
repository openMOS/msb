/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.utilities.ListsToString;
import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import eu.openmos.agentcloud.data.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 * Object that represents the data that resource or transport agents receive via socket 
 * from the Manufacturing Service Bus as soon as the product leaves the workstation or the transport
 * and that then are passed on to the optimizer.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class ProductLeavingWorkstationOrTransportData {
    private String agentId;
    private String productId;
    private List<String> recipeIds;
    private Date registeredTimestamp;

    private static final int FIELDS_COUNT = 4;
    
    // reflection stuff
    public ProductLeavingWorkstationOrTransportData() {
    }

    public ProductLeavingWorkstationOrTransportData(
            String agentId, 
            String productId, 
            List<String> recipeIds, 
            Date registeredTimestamp
    ) {
        this.agentId = agentId;
        this.productId = productId;
        this.recipeIds = recipeIds;
        this.registeredTimestamp = registeredTimestamp;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<String> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<String> recipeIds) {
        this.recipeIds = recipeIds;
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
     * agentId
     * productId
     * recipeIds
     * registeredTimestamp
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(agentId);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA);
        builder.append(productId);
        
        builder.append(SerializationConstants.TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA);
        builder.append(ListsToString.writeRecipeIds(recipeIds));

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();
        
    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * agentId
     * productId
     * recipeIds
     * registeredTimestamp
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
    public static ProductLeavingWorkstationOrTransportData fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA);

        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("ProductLeavingWorkstationOrTransportData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new ProductLeavingWorkstationOrTransportData(
                tokenizer.nextToken(),                                          // workstation or transport id
                tokenizer.nextToken(),                                          // product id           
                StringToLists.readRecipeIds(tokenizer.nextToken()),             // list of recipe ids
                sdf.parse(tokenizer.nextToken())                                // registeredTimestamp
        );
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * agentId
     * productId
     * recipeIds
     * registeredTimestamp
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append("agentId", agentId);
        doc.append("productId", productId);
        doc.append("recipeIds", recipeIds);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;        
    }
}
