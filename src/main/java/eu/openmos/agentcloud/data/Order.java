/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author luiri60
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class Order {
    private List<ProductDescription> productDescriptions;

    public Order() {}
    
    public Order(List<ProductDescription> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }

    public List<ProductDescription> getProductDescriptions() {
        return productDescriptions;
    }

    public void setProductDescriptions(List<ProductDescription> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }    

    /**
     * Method that deserializes a String object.
     * 
     * @param object - String to be deserialized.
     * @return Deserialized object.
     */
    public static Order fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_ORDER);
        StringTokenizer productDescriptionsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM);
        List<ProductDescription> productDescriptions = new LinkedList<>();
        while(productDescriptionsTokenizer.hasMoreTokens()) {
            String token = productDescriptionsTokenizer.nextToken();
            if(!token.isEmpty())
                productDescriptions.add(ProductDescription.fromString(token));
        }
        return new Order(productDescriptions);
    }
    
    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(ProductDescription p : productDescriptions) {
            builder.append(p.toString());
            builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM);
        }
        return builder.toString();
    }
    
}
