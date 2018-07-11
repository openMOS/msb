package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object with the single detail of a user assessment.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class EquipmentAssessmentRow extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(EquipmentAssessmentRow.class.getName());    
    private static final long serialVersionUID = 6529685098267757912L;
    
    /**
     * Observation row ID.
     */
    private String uniqueId;
    /**
     * Assessment id.
     */
    private String equipmentAssessmentId;
    /**
     * Assessment row type.
     * Can be "functionality", "quality", "performance".
     * @see EquipmentAssessmentType 
     * Refactoring from type to earType (equipment assessment row type)
     */
    private String earType;    
        
    /**
     * Rating value.
     */
    private Double rating;
    
    /**
     * Default constructor.
     */
    public EquipmentAssessmentRow() {super();}

    public EquipmentAssessmentRow(String uniqueId, 
            String equipmentAssessmentId, 
            String type, 
            Double rating, 
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.equipmentAssessmentId = equipmentAssessmentId;
        this.earType = type;
        this.rating = rating;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEarType() {
        return earType;
    }

    public void setEarType(String earType) {
        this.earType = earType;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getEquipmentAssessmentId() {
        return equipmentAssessmentId;
    }

    public void setEquipmentAssessmentId(String equipmentAssessmentId) {
        this.equipmentAssessmentId = equipmentAssessmentId;
    }

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.EQUIPMENT_ASSESSMENT_ID, equipmentAssessmentId);
        
        doc.append(DatabaseConstants.TYPE, earType);

        doc.append(DatabaseConstants.RATING, rating);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("EQUIPMENT ASSESSMENT ROW TOBSON: " + doc.toString());
        
        return doc;
    }

}
