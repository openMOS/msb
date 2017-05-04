/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.StringTokenizer;

/**
 * Object that describes a functional parameter of a device.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 */
public class Parameter {
    /**
     * Parameter's default value.
     */
    private String defaultValue;
    /**
     * Parameter's description.
     */
    private String description;
    /**
     * Parameter's ID.
     */
    private String uniqueId;
    /**
     * Parameter's lower bound.
     */
    private String lowerBound;
    /**
     * Parameter's upper bound.
     */
    private String upperBound;
    /**
     * Parameter's name.
     */
    private String name;
    /**
     * Parameter's unit.
     */
    private String unit;
    
    /**
     * Default constructor.
     */
    public Parameter() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param defaultValue - Parameter's default value.
     * @param description - Parameter's description.
     * @param uniqueId - Parameter's ID.
     * @param lowerBound - Parameter's lower bound.
     * @param upperBound - Parameter's upper bound.
     * @param name - Parameter's name.
     * @param unit - Parameter's unit.
     */
    public Parameter(String defaultValue, String description, String uniqueId, String lowerBound, String upperBound, String name, String unit) {
        this.defaultValue = defaultValue;
        this.description = description;
        this.uniqueId = uniqueId;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.name = name;
        this.unit = unit;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(String lowerBound) {
        this.lowerBound = lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(String upperBound) {
        this.upperBound = upperBound;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

     /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        return defaultValue + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + description + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + uniqueId + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + lowerBound + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + upperBound + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + name + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER + unit;
    }
   
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static Parameter fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER);
        return new Parameter(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
    }
    
}
