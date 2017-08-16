/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data.recipe;

import eu.openmos.agentcloud.data.utilities.ListsToString;
import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import eu.openmos.agentcloud.data.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 * WP3 semantic model alignment.
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Equipment {
    
    /**
     * WP3 semantic model alignment.
     * Equipment's ID.
     */
    private String uniqueId;
    /**
     * WP3 semantic model alignment.
     * Equipment's name.
     */
    private String name;
    /**
     * WP3 semantic model alignment.
     * Equipment's description.
     */
    private String description;
    /**
     * WP3 semantic model alignment.
     * Equipment execution table.
     * 
     * MSB alignment.
     * We will not have an execution table on equipments.
    */
    private ExecutionTable executionTable;
    
    /*
     * MSB alignment.
    */
    private boolean connected = false;    
    /**
     * MSB alignment
     * WP4 semantic model alignment.
     * List of skills!
     */
    private List<Skill> skills;
    
    /**
     * WP3 semantic model alignment.
     * Equipment timestamp.
    */
    private Date registeredTimestamp;
    
    private static final int FIELDS_COUNT = 7;

    // for reflection purpose
    public Equipment() {}

    public Equipment(
            String uniqueId, 
            String name, 
            String description, 
            ExecutionTable executionTable, 
            boolean connected,
            List<Skill> skills,
            Date registeredTimestamp
    ) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.registeredTimestamp = registeredTimestamp;
        this.executionTable = executionTable;
        this.connected = connected;
        this.skills = skills;
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

    public ExecutionTable getExecutionTable() {
        return executionTable;
    }

    public void setExecutionTable(ExecutionTable executionTable) {
        this.executionTable = executionTable;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }        

     /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * execution table,
     * connected,
     * list of skills,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uniqueId);
        
        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
        builder.append(name);
        
        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
        builder.append(description);

        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
        builder.append(executionTable.toString());

        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
        builder.append(connected);

        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
        builder.append(ListsToString.writeSkills(skills));

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
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
     * execution table,
     * connected,
     * list of skills,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
    public static Equipment fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EQUIPMENT);
        
        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("Equipment - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new Equipment(
                tokenizer.nextToken(),      // uniqueId 
                tokenizer.nextToken(),       // name 
                tokenizer.nextToken(),      // description 
                ExecutionTable.fromString(tokenizer.nextToken()),    // execution table
                Boolean.valueOf(tokenizer.nextToken()),      // connected 
                StringToLists.readSkills(tokenizer.nextToken()),        // list of skills
                sdf.parse(tokenizer.nextToken())             // registeredTimestamp
        );
    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * uniqueId,
     * name,
     * description,
     * execution table,
     * connected,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("executionTable", executionTable.getUniqueId());
        doc.append("connected", connected);
        doc.append("skillIds", skillIds);        
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;
    }
}
