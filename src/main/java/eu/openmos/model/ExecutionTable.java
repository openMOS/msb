package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Execution table master class.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ExecutionTable extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ExecutionTable.class.getName());
    private static final long serialVersionUID = 6529685098267757004L;  
    
    /**
     * Execution table unique ID.
     */
    private String uniqueId;
    /**
     * Execution table name.
     */
    private String name;
    /**
     * Execution table description.
     */
    private String description;
    /**
     * List of execution table rows.
     */
    private List<ExecutionTableRow> rows;    

    private String changeExecutionTableObjectID;
    
    private String changeExecutionTableMethodID;
    
    // default constructor, for reflection stuff
    public ExecutionTable()    {super();}

    /**
     * Constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param rows
     * @param registeredTimestamp 
     */    
    public ExecutionTable(String uniqueId, String name, String description, 
            List<ExecutionTableRow> rows, Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.rows = rows;
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

    public List<ExecutionTableRow> getRows() {
        return rows;
    }

    public void setRows(List<ExecutionTableRow> rows) {
        this.rows = rows;
    }
    
    public String getChangeExecutionTableObjectID() {
        return changeExecutionTableObjectID;
    }
    
    public String getChangeExecutionTableMethodID() {
        return changeExecutionTableMethodID;
    }
    
    public void setChangeExecutionTableObjectID(String changeExecutionTableObjectID) {
        this.changeExecutionTableObjectID = changeExecutionTableObjectID;
    }
    
    public void setChangeExecutionTableMethodID(String changeExecutionTableMethodID) {
        this.changeExecutionTableMethodID = changeExecutionTableMethodID;
    }
    
     /**
     * Method that serializes the execution table master data into a BSON document in order to be stored into the database.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
                
        List<String> executionTableRowIds = null;
        if (rows != null)
            executionTableRowIds = rows.stream().map(row -> row.getUniqueId()).collect(Collectors.toList());        
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        doc.append(DatabaseConstants.EXECUTION_TABLE_ROW_IDS, executionTableRowIds);           
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }

    @Override
    public String toString() {
        return "ExecutionTable{" + "uniqueId=" + uniqueId + ", name=" + name + ", description=" + description + ", rows=" + rows + ", changeExecutionTableObjectID=" + changeExecutionTableObjectID + ", changeExecutionTableMethodID=" + changeExecutionTableMethodID + '}';
    }
    
    
}
