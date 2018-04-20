package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Execution table master class.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 * @author Renato Martins <renato.martins@introsys.eu.eu>
 */
@XmlRootElement(name = "executionTable")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecutionTable_DA extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ExecutionTable_DA.class.getName());
    private static final long serialVersionUID = 6529685098267757004L;  
    
    /**
     * Execution table unique ID.
     */
    @XmlElement(name = "amlId")
    private String uniqueId;
    /**
     * Execution table name.
     */
    @XmlElement(name = "name")
    private String name;
    /**
     * Execution table description.
     */
    @XmlElement(name = "description")
    private String description;
    /**
     * List of execution table rows.
     */
    @XmlElement(name = "rows")
    private List<ExecutionTableRow_DA> rows;    

    @XmlElement(name = "changeExecutionTableObjectID")
    private String changeExecutionTableObjectID;
    
    @XmlElement(name = "changeExecutionTableMethodID")
    private String changeExecutionTableMethodID;
    
    // default constructor, for reflection stuff
    public ExecutionTable_DA()    {super();}

    /**
     * Constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param rows
     * @param registeredTimestamp 
     */    
    public ExecutionTable_DA(String uniqueId, String name, String description, 
            List<ExecutionTableRow_DA> rows, Date registeredTimestamp) {
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

    public List<ExecutionTableRow_DA> getRows() {
        return rows;
    }

    public void setRows(List<ExecutionTableRow_DA> rows) {
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
    
    public static ExecutionTable_DA createExecutionTable_DA(ExecutionTable execTable)
    {
      List<ExecutionTableRow_DA> rows_da = new ArrayList<>();
      for(ExecutionTableRow row : execTable.getRows())
      {
        rows_da.add(ExecutionTableRow_DA.createExecutionTableRow_DA(row));
      }
      
      ExecutionTable_DA execTable_DA = new ExecutionTable_DA(execTable.getUniqueId(), execTable.getName(), 
              execTable.getDescription(), rows_da, execTable.getRegistered());
      
      execTable_DA.setChangeExecutionTableMethodID(execTable.getChangeExecutionTableMethodID());
      execTable_DA.setChangeExecutionTableObjectID(execTable.getChangeExecutionTableObjectID());
      
      return execTable_DA;
    }
    
}
