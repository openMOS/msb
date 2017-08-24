package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.ListsToString;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
executionTable [tableId, Map(
tableEntryId,
productId,
recipeId, 
recipeValidity, 
activityStatus,
Frequency (? -> Task 4.4))
)]
* 
 * @author valerio.gentile
 */
public class ExecutionTable extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ExecutionTable.class.getName());
    private static final long serialVersionUID = 6529685098267757685L;  
    
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

//    private static final int FIELDS_COUNT = 5;
    
    // default constructor, for reflection stuff
    public ExecutionTable()    {super();}
    
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
    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 uniqueId,
 name,
 description,
 list of execution table rows,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    public String toString_OLD() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
//        builder.append(description);
//
//        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
//        builder.append(ListsToString.writeExecutionTableRows(rows));
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
//        builder.append(stringRegisteredTimestamp);
//        
//        return builder.toString();
//    }
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * list of execution table rows,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static ExecutionTable fromString_OLD(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EXECUTION_TABLE);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("ExecutionTable - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        String uniqueId = tokenizer.nextToken();
//        String name = tokenizer.nextToken();
//        String description = tokenizer.nextToken();
//        
//        List<ExecutionTableRow> rows = StringToLists.readExecutionTableRows(tokenizer.nextToken());
//                
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        Date registered = sdf.parse(tokenizer.nextToken());
//        
//        return new ExecutionTable(
//                uniqueId, 
//                name, 
//                description, 
//                rows, 
//                registered
//        );
//    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 uniqueId,
 name,
 description,
 list of execution table rows,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> executionTableRowIds = rows.stream().map(row -> row.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", uniqueId);
        doc.append("rows", executionTableRowIds);           
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }

//    public static ExecutionTable deserialize(String object) 
//    {        
//        ExecutionTable objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (ExecutionTable) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }

  public ExecutionTable updateRow(String executionTableRowId, ExecutionTableRow rowToUpdate)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
