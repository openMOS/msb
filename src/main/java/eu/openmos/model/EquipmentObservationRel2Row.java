package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object with the single detail of a user obervation.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class EquipmentObservationRel2Row extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(EquipmentObservationRel2Row.class.getName());    
    private static final long serialVersionUID = 6529685098267757912L;
    
    /**
     * Observation row ID.
     */
    private String uniqueId;
    /**
     * Observation id.
     */
    private String equipmentObservationId;
    /**
     * Observation row type.
     * Can be "functionality", "quality", "performance".
     * @see EquipmentObservationType 
     */
    private String type;    
    /**
     * Observation row sub type.
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
    
    /**
     * Default constructor.
     */
    public EquipmentObservationRel2Row() {super();}

    public EquipmentObservationRel2Row(String uniqueId, 
            String equipmentObservationId, 
            String type, 
            String subtype, 
            String unit, 
            Double value, 
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.equipmentObservationId = equipmentObservationId;
        this.type = type;
        this.subtype = subtype;
        this.unit = unit;
        this.value = value;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getEquipmentObservationId() {
        return equipmentObservationId;
    }

    public void setEquipmentObservationId(String equipmentObservationId) {
        this.equipmentObservationId = equipmentObservationId;
    }

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.EQUIPMENT_OBSERVATION_REL2_ID, equipmentObservationId);
        
        doc.append(DatabaseConstants.TYPE, type);
        doc.append(DatabaseConstants.SUBTYPE, subtype);
        
        doc.append(DatabaseConstants.UNIT, unit);
        doc.append(DatabaseConstants.VALUE, value);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("EQUIPMENT OBSERVATION REL2 ROW TOBSON: " + doc.toString());
        
        return doc;
    }

}