package eu.openmos.model;

import java.util.Date;
import java.io.Serializable;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes a functional parameter of a device.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Parameter extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757015L;
    
    /**
     * Parameter ID.
     */
    private String uniqueId;
    /**
     * Parameter name.
     */
    private String name;
    /**
     * Parameter description.
     */
    private String description;
    
    /**
     * Parameter default value.
     */
    private String defaultValue;    
    /**
     * Parameter lower bound.
     */
    private String lowerBound;
    /**
     * Parameter type.
     */
    private String type;    
    /**
     * Parameter unit.
     */
    private String unit;
    /**
     * Parameter upper bound.
     */
    private String upperBound;
    /**
     * Parameter value type
     */
    private String valueType;

    /**
     * Default constructor.
     */
    public Parameter() {super();}
    
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
     * @param type
     * @param registeredTimestamp
     */
    public Parameter(String defaultValue, String description, String uniqueId, 
            String lowerBound, String upperBound, String name, String unit,
            String type, String valueType, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.defaultValue = defaultValue;
        this.description = description;
        this.uniqueId = uniqueId;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.name = name;
        this.unit = unit;
        
        this.type = type;
        this.valueType = valueType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        return toBSON2();
    }
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonKPI - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static Parameter fromBSON(Document bsonParameter)
    throws ParseException 
    {
            return (Parameter)Parameter.fromBSON2(bsonParameter, Parameter.class);
    }    
}