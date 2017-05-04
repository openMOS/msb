/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.StringTokenizer;

/**
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 */
public class SkillRequirement {
    
    public static final int TYPE_ATOMIC = 0;
    public static final int TYPE_COMPOSITE = 1;
    
    /**
     * Skill Requirement description.
     */
    private String description;
    /**
     * Skill Requirement ID.
     */
    private String uniqueId;
    /**
     * Skill Requirement name.
     */
    private String name;
    /**
     * Skill Requirement type.
     */
    private int type;

    /**
     * Default constructor.
     */
    public SkillRequirement() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Skill Requirement description.
     * @param uniqueId - Skill Requirement ID.
     * @param name - Skill Requirement name.
     * @param type - Skill Requirement type.
     */
    public SkillRequirement(String description, String uniqueId, String name, int type) {
        this.description = description;
        this.uniqueId = uniqueId;
        this.name = name;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

     /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        return description + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT + uniqueId + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT + name + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT + type;
    }
   
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static SkillRequirement fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL_REQUIREMENT);
        return new SkillRequirement(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), Integer.parseInt(tokenizer.nextToken()));
    }
}
