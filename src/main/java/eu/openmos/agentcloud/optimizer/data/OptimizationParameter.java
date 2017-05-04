
package eu.openmos.agentcloud.optimizer.data;

import java.util.StringTokenizer;

/**
 * The OptimizationParameter class defines a setup parameter for an optimization algorithm.
 * This class should be used when reparametrizing a custom optimization algorithm through the optimizer agent {@link eu.openmos.agentcloud.optimizer.OptimizerAgent}
 * 
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 */
public class OptimizationParameter {

    private String name;
    private String type;
    private String unit;
    private String value;
    private String defaultValue;
    
    public OptimizationParameter() {}
    
    /**
     * The constructor
     * 
     * @param name is the user defined name of the parameter
     * @param type is the user defined type of the parameter
     * @param unit is the user defined unit of the parameter
     * @param value is the user defined value of the parameter
     * @param defaultValue is the user defined default value of the parameter
     */
    public OptimizationParameter(String name, String type, String unit, String value, String defaultValue) {
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    /**
     * Getter for name
     * 
     * @return the name of the parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     * 
     * @param name the new name of the parameter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Type
     *  
     * @return the type of the parameter
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type
     * 
     * @param type the new type of the parameter
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for unit
     * 
     * @return the unit of the parameter
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter for unit
     * 
     * @param unit the new unit of the parameter
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Getter for value
     * 
     * @return the value of the parameter
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter for value
     * 
     * @param value the new value for value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Getter for default value
     * 
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setter for default value
     * 
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return name + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_OPTIMIZATION_PARAMETER + type + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_OPTIMIZATION_PARAMETER + unit + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_OPTIMIZATION_PARAMETER + value + eu.openmos.agentcloud.data.utilities.Constants.TOKEN_OPTIMIZATION_PARAMETER + defaultValue;
    }
    
    /**
     * The fromString method takes a String description, of an OptimizationParameter, created with the toString method on this class and creates the corresponding object
     * 
     * @param object is the String description of the object
     * @return the new object created from the String
     */
    public static OptimizationParameter fromString(String object) {
        StringTokenizer tokenizer = new StringTokenizer(object, eu.openmos.agentcloud.data.utilities.Constants.TOKEN_OPTIMIZATION_PARAMETER);
        return new OptimizationParameter(tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken(), tokenizer.nextToken());
    }
}
