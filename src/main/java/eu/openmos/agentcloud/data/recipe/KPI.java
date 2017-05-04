/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.StringTokenizer;

/**
 * Object that describes a Key Performance Indicator of the demonstrators.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 */
public class KPI {
    /**
     * KPI's Description.
     */
    private String description;
    /**
     * KPI's ID.
     */
    private String uniqueId;
    /**
     * KPI's name.
     */
    private String name;
    /**
     * KPI's upper bound.
     */
    private String defaultUpperBound;
    /**
     * KPI's lower bound.
     */
    private String defaultLowerBound;
    /**
     * KPI's current value.
     */
    private String currentValue;
    /**
     * KPI's unit.
     */
    private String unit;

    /**
     * Default constructor.
     */   
    public KPI() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - KPI's Description.
     * @param uniqueId - KPI's ID.
     * @param name - KPI's name.
     * @param defaultUpperBound - KPI's upper bound.
     * @param defaultLowerBound - KPI's lower bound.
     * @param currentValue - KPI's current value.
     * @param unit - KPI's unit.
     */
    public KPI(String description, String uniqueId, String name, String defaultUpperBound, String defaultLowerBound, String currentValue, String unit) {
        this.description = description;
        this.uniqueId = uniqueId;
        this.name = name;
        this.defaultUpperBound = defaultUpperBound;
        this.defaultLowerBound = defaultLowerBound;
        this.currentValue = currentValue;
        this.unit = unit;
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

    public String getDefaultUpperBound() {
        return defaultUpperBound;
    }

    public void setDefaultUpperBound(String defaultUpperBound) {
        this.defaultUpperBound = defaultUpperBound;
    }

    public String getDefaultLowerBound() {
        return defaultLowerBound;
    }

    public void setDefaultLowerBound(String defaultLowerBound) {
        this.defaultLowerBound = defaultLowerBound;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
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
        return description + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + uniqueId + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + name + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + defaultUpperBound + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + defaultLowerBound + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + currentValue + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI + unit;
    }

    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static KPI fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI);
        return new KPI(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
    }   
    
}
