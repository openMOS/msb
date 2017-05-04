/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Object used to describe a Product Agent.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * 
 */
public class ProductDescription {
    /**
     * Agent's name.
     */
    private String name;
    /**
     * Skills that need to be executed.
     */
    private List<SkillRequirement> skillRequirements;

    /**
     * Default constructor.
     */
    public ProductDescription() {} 
    
    /**
     * Parameterized constructor.
     * 
     * @param name - Agent's name.
     * @param skillRequirements - Skills that need to be executed.
     */
    public ProductDescription(String name, List<SkillRequirement> skillRequirements) {
        this.name = name;
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

    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PRODUCT_DESCRIPTION);
        for(SkillRequirement skillReq : skillRequirements) {
            builder.append(skillReq.toString());
            builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
        }
        return builder.toString();
    }
        
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static ProductDescription fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PRODUCT_DESCRIPTION);
        String name = tokenizer.nextToken();
        
        StringTokenizer skillRequirementsTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT_LIST_ITEM);
        List<SkillRequirement> skillRequirements = new LinkedList<>();
        while(skillRequirementsTokenizer.hasMoreTokens()) {
            String token = skillRequirementsTokenizer.nextToken();
            if(!token.isEmpty())
                skillRequirements.add(SkillRequirement.fromString(token));
        }
        
        return new ProductDescription(name, skillRequirements);
    }
}
