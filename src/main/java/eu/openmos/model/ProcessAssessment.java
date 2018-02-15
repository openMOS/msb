package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.util.Date;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes user assessment concerning a recipe.
 * Holds a list of rows (ProcessAssessmentRow).
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ProcessAssessment extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ProcessAssessment.class.getName());    
    private static final long serialVersionUID = 6529685098267757931L;
    
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
     * Pointer to the recipe whom the observation is related to.
     */
    private String recipeId;
    
    /**
     * List of details.
     */
    List<ProcessAssessmentRow> rows;
    
    private String userText;

    // name of the user that register the assessment
    private String userName;

    // current status of the system while inserting the assessment
    private String systemStatus;

    /**
     * Default constructor.
     */
    public ProcessAssessment() {super();}

    public ProcessAssessment(String uniqueId, String name, String description, 
            String recipeId, 
            List<ProcessAssessmentRow> rows,
            String userText,
            String userName,
            String systemStatus,
            Date registered) {
        super(registered);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.recipeId = recipeId;
        
        this.rows = rows;
        
        this.userText = userText;
        
        this.userName = userName;
        this.systemStatus = systemStatus;
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

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public List<ProcessAssessmentRow> getRows() {
        return rows;
    }

    public void setRows(List<ProcessAssessmentRow> rows) {
        this.rows = rows;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(String systemStatus) {
        this.systemStatus = systemStatus;
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> rowIds = null;
        if (rows != null)
            rowIds = rows.stream().map(row -> row.getUniqueId()).collect(Collectors.toList());
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        
        doc.append(DatabaseConstants.RECIPE_ID, recipeId);
        
        doc.append(DatabaseConstants.PROCESS_ASSESSMENT_ROW_IDS, rowIds);
        
        doc.append(DatabaseConstants.USER_TEXT, userText);
        
        doc.append(DatabaseConstants.USER_NAME, userName);
        doc.append(DatabaseConstants.SYSTEM_STATUS, systemStatus);
        
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        logger.debug("PROCESS ASSESSMENT TOBSON: " + doc.toString());
        
        return doc;
    }
}