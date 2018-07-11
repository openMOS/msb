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
public class ProcessAssessmentRow extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ProcessAssessmentRow.class.getName());    
    private static final long serialVersionUID = 6529685098267757922L;
    
    /**
     * Observation row ID.
     */
    private String uniqueId;
    /**
     * Assessment id.
     */
    private String processAssessmentId;
    /**
     * Assessment row type.
     * Can be "functionality", "quality", "performance".
     * @see ProcessAssessmentType 
     * Refactoring from type to parType (process assessment row type)
     */
    private String parType;    
        
    /**
     * Rating value.
     */
    private Double rating;
    
    /**
     * Default constructor.
     */
    public ProcessAssessmentRow() {super();}

    public ProcessAssessmentRow(String uniqueId, 
            String processAssessmentId, 
            String type, 
            Double rating, 
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.processAssessmentId = processAssessmentId;
        this.parType = type;
        this.rating = rating;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getParType() {
        return parType;
    }

    public void setParType(String parType) {
        this.parType = parType;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getProcessAssessmentId() {
        return processAssessmentId;
    }

    public void setProcessAssessmentId(String processAssessmentId) {
        this.processAssessmentId = processAssessmentId;
    }

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.PROCESS_ASSESSMENT_ID, processAssessmentId);
        
        doc.append(DatabaseConstants.TYPE, parType);

        doc.append(DatabaseConstants.RATING, rating);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("PROCESS ASSESSMENT ROW TOBSON: " + doc.toString());
        
        return doc;
    }

}
