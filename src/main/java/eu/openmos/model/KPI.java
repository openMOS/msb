package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes a Key Performance Indicator of the demonstrators.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class KPI extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(KPI.class.getName());
    private static final long serialVersionUID = 6529685098267757007L;
    
    /**
     * KPI unique ID.
     */
    private String uniqueId;

    /**
     * KPI name.
     */
    private String name;

    /**
     * KPI Description.
     */
    private String description;

    /**
     * KPI type.
     */
    private String type;
    /**
     * KPI unit.
     */
    private String unit;
    /**
     * KPI current value type
     */
    private String valueType;
    /**
     * KPI current value.
     */
    private String value;
    
    /**
     * KPI lower bound.
     */
    private String defaultLowerBound;
    /**
     * KPI upper bound.
     */
    private String defaultUpperBound;
    
    /**
     * Default constructor, for reflection.
     */   
    public KPI() {super();}
    
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
    public KPI(String description, String uniqueId, String name, 
            String defaultUpperBound, String defaultLowerBound, 
            String currentValue, String unit, String type, String valueType,
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;

        this.type = type;
        this.unit = unit;
        this.value = currentValue;
        this.valueType = valueType;

        this.defaultLowerBound = defaultLowerBound;
        this.defaultUpperBound = defaultUpperBound;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
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
    public static KPI fromBSON(Document bsonKPI) {   
        return (KPI)fromBSON2(bsonKPI, KPI.class);
    }
}
