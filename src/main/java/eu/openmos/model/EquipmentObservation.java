package eu.openmos.model;

import java.util.Date;
import java.io.Serializable;
import java.text.ParseException;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes user obervations concerning a physical equipment.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class EquipmentObservation extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(EquipmentObservation.class.getName());    
    private static final long serialVersionUID = 6529685098267757901L;
    
    /**
     * Observation ID.
     */
    private String uniqueId;
    /**
     * Observation name.
     */
    private String name;
    /**
     * Observation description.
     */
    private String description;
    
    /**
     * Pointer to the physical equipment (subsystem or module) whom the observation is related to.
     */
    private String equipmentId;
    /**
     * Can be "system", "subSystem" or "module".
     */
    private String equipmentType;
    /**
     * Observation main type.
     * Can be "functionality", "quality", "performance".
     * @see EquipmentObservationType 
     * Refactoring from type to eoType (equipment observation type)
     */
    private String eoType;    
    /**
     * Observation sub type.
     * Depends on the type. 
     * @see EquipmentObservationTypeFunctionality
     * @see EquipmentObservationTypePerformance
     * @see EquipmentObservationTypeQuality
     */
    private String subtype;    
        
    /**
     * Measurement unit.
     */
    private String unit;
    /**
     * Measured value.
     */
    private Double value;
    
    private String userText;

    /**
     * Default constructor.
     */
    public EquipmentObservation() {super();}

    public EquipmentObservation(String uniqueId, String name, String description, 
            String equipmentId, String equipmentType,
            String type, String subtype, String unit, Double value, String userText,
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.equipmentId = equipmentId;
        this.equipmentType = equipmentType;
        this.eoType = type;
        this.subtype = subtype;
        this.unit = unit;
        this.value = value;
        this.userText = userText;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEoType() {
        return eoType;
    }

    public void setEoType(String eoType) {
        this.eoType = eoType;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
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
     * @param bsonParameter - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static EquipmentObservation fromBSON(Document bsonParameter)
    throws ParseException 
    {
            return (EquipmentObservation)fromBSON2(bsonParameter, EquipmentObservation.class);
    }    

    public void setMessage(String content) {
        // questo metodo non va qui per nessun motivo
        logger.debug("mesg = " + content);
    }
}
