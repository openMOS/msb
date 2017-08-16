/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import eu.openmos.agentcloud.data.utilities.ListsToString;
import eu.openmos.agentcloud.data.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bson.Document;

/**
executionTable [tableId, Map(
tableEntryId,
productId,
recipeId, 
recipeValidity, 
activityStatus,
Frequency (? -> Task 4.4))
)]
* 
     * WP3 semantic model alignment.
 * @author valerio.gentile
 */
public class ExecutionTable {
    
    /**
     * WP3 semantic model alignment.
     * Execution table unique ID.
     */
    private String uniqueId;
    /**
     * WP3 semantic model alignment.
     * Execution table name.
     */
    private String name;
    /**
     * WP3 semantic model alignment.
     * Execution table description.
     */
    private String description;
    /**
     * WP3 semantic model alignment.
     * List of execution table rows.
    */
    private List<ExecutionTableRow> rows;    
    /**
     * WP3 semantic model alignment.
     * Execution table validity timestamp.
    */
    private Date registeredTimestamp;    

    private static final int FIELDS_COUNT = 5;
    
    // default constructor, for reflection stuff
    public ExecutionTable()
    {}
    
    public ExecutionTable(String uniqueId, String name, String description, 
            List<ExecutionTableRow> rows, Date registeredTimestamp) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.rows = rows;
        this.registeredTimestamp = registeredTimestamp;
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

    public Date getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(Date registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
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
     * 
     * uniqueId,
     * name,
     * description,
     * list of execution table rows,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uniqueId);
        
        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
        builder.append(name);
        
        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
        builder.append(description);

        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
        builder.append(ListsToString.writeExecutionTableRows(rows));

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_EXECUTION_TABLE);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();
    }
   
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
    public static ExecutionTable fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EXECUTION_TABLE);
        
        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("ExecutionTable - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        String uniqueId = tokenizer.nextToken();
        String name = tokenizer.nextToken();
        String description = tokenizer.nextToken();
        
        List<ExecutionTableRow> rows = StringToLists.readExecutionTableRows(tokenizer.nextToken());
                
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        Date registeredTimestamp = sdf.parse(tokenizer.nextToken());
        
        return new ExecutionTable(
                uniqueId, 
                name, 
                description, 
                rows, 
                registeredTimestamp
        );
    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * list of execution table rows,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
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
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;
    }
}
