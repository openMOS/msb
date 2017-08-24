package eu.openmos.model;

import eu.openmos.model.utilities.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.bson.Document;

/**
 * Base abstract class for SubSytem and Module classes.
 * 
 * @see SubSystem
 * @see Module
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Equipment extends Base implements Serializable {    
        private static final long serialVersionUID = 6529685098267757684L;
    
    /**
     * Equipment ID.
     */
    @XmlElement(name = "amlId")    
    protected String uniqueId;
    
    /**
     * Equipment name.
     */
    @XmlElement(name = "name")
    protected String name;
    
    /**
     * Equipment description.
     */
    protected String description;
    
    /*
     * Is the equipment connected or not.
    */
    protected boolean connected = false;   
    
    /**
     * List of skills
     */
    protected List<Skill> skills;
    
    // VaG - 17/08/2017
    // Added after model revision 13.
    @XmlElement(name = "address")
    protected String address = "";

    @XmlElement(name = "status")
    protected String status = "";       // could be the "connected" field?

    protected String manufacturer = "";
    
//    private static final int FIELDS_COUNT = 10;

    // for reflection purpose
    public Equipment() {super();}

    public Equipment(
            String uniqueId, 
            String name, 
            String description, 
            boolean connected,
            List<Skill> skills,
            String address,
            String status,
            String manufacturer,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;
        this.connected = connected;
        this.skills = skills;
        
        this.address = address;
        this.status = status;
        this.manufacturer = manufacturer;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 uniqueId,
 name,
 description,
 execution table,
 connected,
 list of skills,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    public String toString_OLD() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(uniqueId);
//        
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(name);
//        
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(description);
//
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(connected);
//
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(ListsToString.writeSkills(skills));
//
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(address);
//
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(status);
//
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
//        builder.append(manufacturer);
//
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        builder.append(SerializationConstants.TOKEN_EQUIPMENT);
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
     * execution table,
     * connected,
     * list of skills,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
    */
//    public static Equipment fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EQUIPMENT);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("Equipment - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new Equipment(
//                tokenizer.nextToken(),      // uniqueId 
//                tokenizer.nextToken(),       // name 
//                tokenizer.nextToken(),      // description 
//                /* ExecutionTable.fromString(tokenizer.nextToken()),    // execution table */
//                Boolean.valueOf(tokenizer.nextToken()),      // connected 
//                StringToLists.readSkills(tokenizer.nextToken()),        // list of skills
//                StringToLists.readInternalModuleIds(tokenizer.nextToken()),        // list of internal modules
//                tokenizer.nextToken(),       // address 
//                tokenizer.nextToken(),       // status 
//                tokenizer.nextToken(),       // manufacturer 
//                sdf.parse(tokenizer.nextToken())             // registered
//        );
//    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 uniqueId,
 name,
 description,
 execution table,
 connected,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        List<String> moduleIds = skills.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());        
        
        doc.append("id", uniqueId);
        doc.append("name", name);
        doc.append("description", description);
        doc.append("connected", connected);
        doc.append("skillIds", skillIds);        
        doc.append("moduleIds", moduleIds);        
        doc.append("address", address);
        doc.append("status", status);
        doc.append("manifacturer", manufacturer);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }

}
