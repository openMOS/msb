/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.StringTokenizer;

/**
 * Object that describes an actual setting of a KPI, i.e., a possible value for 
 * it.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 */
public class KPISetting {
    /**
     * KPI Setting's description.
     */
    private String description;
    /**
     * KPI Setting's ID.
     */
    private String id;
    /**
     * KPI Setting's name.
     */
    private String name;
    /**
     * KPI Setting's value.
     */
    private String value;

    /**
     * Default constructor.
     */
    public KPISetting() {}
    
    /** 
     * Parameterized constuctor.
     * 
     * @param description - KPI Setting's description.
     * @param id - KPI Setting's ID.
     * @param name - KPI Setting's name.
     * @param value - KPI Setting's value.
     */
    public KPISetting(String description, String id, String name, String value) {
        this.description = description;
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Method that serializes the object.
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        return description + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING + id + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING + name + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING + value;
    }
    
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static KPISetting fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_SETTING);
        return new KPISetting(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
    }
}
