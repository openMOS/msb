/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.utilities.Constants;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Object that describes an action a device can perform.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 */
public class Skill {
    
    public static final int TYPE_ATOMIC = 0;
    public static final int TYPE_COMPOSITE = 1;
    
    /**
     * Skill's description.
     */
    private String description;
    /**
     * Skill's ID.
     */
    private String uniqueId;
    /**
     * KPIs the skill needs to respect.
     */
    private List<KPI> kpis;
    /**
     * Skill's name.
     */
    private String name;
    /**
     * Parameters the skill needs to meet.
     */
    private List<Parameter> parameters;
    /**
     * Skills' type.
     */
    private int type;

    /**
     * Default constructor.
     */
    public Skill() {}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Skill's description.
     * @param uniqueId - Skill's ID.
     * @param kpis - KPIs the skill needs to respect.
     * @param name - Skill's name.
     * @param paremeters - Parameters the skill needs to meet.
     * @param type - Skills' type.
     */
    public Skill(String description, String uniqueId, List<KPI> kpis, String name, List<Parameter> paremeters, int type) {
        this.description = description;
        this.uniqueId = uniqueId;
        this.kpis = kpis;
        this.name = name;
        this.parameters = paremeters;
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

    public List<KPI> getKpis() {
        return kpis;
    }

    public void setKpis(List<KPI> kpis) {
        this.kpis = kpis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
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
        StringBuilder builder = new StringBuilder();
        builder.append(description);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        builder.append(uniqueId);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        if(kpis == null || kpis.isEmpty())
            builder.append(" ");
        else {
            for(KPI kpi : kpis) {
                builder.append(kpi.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_LIST_ITEM);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        builder.append(name);
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        if(parameters == null || parameters.isEmpty())
            builder.append(" ");
        else {
            for(Parameter parameter : parameters) {
                builder.append(parameter.toString());
                builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER_LIST_ITEM);
            }
        }
        builder.append(eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        builder.append(type);
        
        return builder.toString();
    }
   
    /**
    * Method that deserializes a String object.
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
    public static Skill fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_SKILL);
        String description = tokenizer.nextToken();
        String uniqueId = tokenizer.nextToken();
        
        StringTokenizer kpisTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_KPI_LIST_ITEM);
        List<KPI> kpis = new LinkedList<>();
        while(kpisTokenizer.hasMoreTokens()) {
            String token = kpisTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                kpis.add(KPI.fromString(token));
        }
        String name = tokenizer.nextToken();
        
        StringTokenizer parametersTokenizer = new StringTokenizer(tokenizer.nextToken(), eu.openmos.agentcloud.data.utilities.Constants.TOKEN_PARAMETER_LIST_ITEM);
        List<Parameter> parameters = new LinkedList<>();
        while(parametersTokenizer.hasMoreTokens()) {
            String token = parametersTokenizer.nextToken();
            if(!token.isEmpty() && !token.equals(" "))
                parameters.add(Parameter.fromString(token));
        }
        
        String type = tokenizer.nextToken();
        return new Skill(description, uniqueId, kpis, name, parameters, Integer.parseInt(type));
    }
}
