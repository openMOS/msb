/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.testdata;

import eu.openmos.model.Part;
import eu.openmos.model.Product;
//import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.model.SkillRequirement;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class ProductTest {
    
    public static Product getTestObject()
    {
        Date registeredTimestamp = new Date();
        
        String modelId = "modelId";
        String name = "modelName";
        String description = "description";
        List<Part> lc = new LinkedList<>();
        List<SkillRequirement> lsr = new LinkedList<>();
        
        Product product = new Product(
                modelId, 
                name, 
                description, 
                lc, 
                lsr, 
                registeredTimestamp
        );
        
        return product;
    }
    
    public static List<Product> getTestList()
    {
        return new LinkedList<>(Arrays.asList(getTestObject()));        
    }
}
