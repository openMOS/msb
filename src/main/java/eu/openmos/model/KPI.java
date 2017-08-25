package eu.openmos.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes a Key Performance Indicator of the demonstrators.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class KPI extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(KPI.class.getName());
    private static final long serialVersionUID = 6529685098267757687L;
    
    /**
     * KPI unique ID.
     */
    private String uniqueId;

    /**
     * KPI name.
     */
    private String name;

    /**
     * KPI Description.
     */
    private String description;

    /**
     * KPI upper bound.
     */
    private String defaultUpperBound;

    /**
     * KPI lower bound.
     */
    private String defaultLowerBound;
    
    /**
     * KPI current value.
     */
    private String currentValue;
    
    /**
     * KPI unit.
     */
    private String unit;
   
    /**
     * KPI type.
     */
    private String type;
    
//    private static final int FIELDS_COUNT = 8;

    /**
     * Default constructor, for reflection.
     */   
    public KPI() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - KPI's Description.
     * @param uniqueId - KPI's ID.
     * @param name - KPI's name.
     * @param defaultUpperBound - KPI's upper bound.
     * @param defaultLowerBound - KPI's lower bound.
     * @param currentValue - KPI's current value.
     * @param unit - KPI's unit.
     */
    public KPI(String description, String uniqueId, String name, 
            String defaultUpperBound, String defaultLowerBound, 
            String currentValue, String unit, String type) {
//        super(registeredTimestamp);
        super();
        
        this.description = description;
        this.uniqueId = uniqueId;
        this.name = name;
        this.defaultUpperBound = defaultUpperBound;
        this.defaultLowerBound = defaultLowerBound;
        this.currentValue = currentValue;
        this.unit = unit;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDefaultUpperBound() {
        return defaultUpperBound;
    }

    public void setDefaultUpperBound(String defaultUpperBound) {
        this.defaultUpperBound = defaultUpperBound;
    }

    public String getDefaultLowerBound() {
        return defaultLowerBound;
    }

    public void setDefaultLowerBound(String defaultLowerBound) {
        this.defaultLowerBound = defaultLowerBound;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * description,
     * unique id,
     * name,
     * upper bound,
     * lower bound,
     * value,
     * unit
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        return description + SerializationConstants.TOKEN_KPI + 
//                uniqueId + SerializationConstants.TOKEN_KPI + 
//                name + SerializationConstants.TOKEN_KPI + 
//                defaultUpperBound + SerializationConstants.TOKEN_KPI + 
//                defaultLowerBound + SerializationConstants.TOKEN_KPI + 
//                currentValue + SerializationConstants.TOKEN_KPI + 
//                unit + SerializationConstants.TOKEN_KPI + 
//                type;
//    }

    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
    *
     * description,
     * unique id,
     * name,
     * upper bound,
     * lower bound,
     * value,
     * unit
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static KPI fromString(String object) throws ParseException {
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_KPI);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("KPI - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new KPI(
//                tokenizer.nextToken(),      // description
//                tokenizer.nextToken(),      // unique id
//                tokenizer.nextToken(),      // name
//                tokenizer.nextToken(),      // upper bound
//                tokenizer.nextToken(),      // lower bound
//                tokenizer.nextToken(),      // value
//                tokenizer.nextToken(),      // unit
//                tokenizer.nextToken()       // type
//        );
//    }   
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * description,
     * unique id,
     * name,
     * upper bound,
     * lower bound,
     * value,
     * unit
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        Document doc = new Document();
//        
//        doc.append("description", description);
//        doc.append("uniqueId", uniqueId);
//        doc.append("name", name);
//        doc.append("defaultUpperBound", defaultUpperBound);
//        doc.append("defaultLowerBound", defaultLowerBound);
//        doc.append("currentValue", currentValue);
//        doc.append("unit", unit);
//        doc.append("type", type);
//        return doc;
    }   
    
    /**
     * Method that deserializes a BSON object.
     * The input BSON needs to have the following format:
     * 
     * description,
     * unique id,
     * name,
     * upper bound,
     * lower bound,
     * value,
     * unit
     * 
     * @param bsonKPI - BSON to be deserialized.
    * @return Deserialized object.
     */
    public static KPI fromBSON(Document bsonKPI) {   
        return (KPI)fromBSON2(bsonKPI, KPI.class);
//        return new KPI(
//            bsonKPI.get("description").toString(),
//            bsonKPI.get("id").toString(),       // it was "uniqueid"
//            bsonKPI.get("name").toString(),
//            bsonKPI.get("defaultUpperBound").toString(),
//            bsonKPI.get("defaultLowerBound").toString(),
//            bsonKPI.get("currentValue").toString(),
//            bsonKPI.get("unit").toString(),
//            bsonKPI.get("type").toString()                                
//        );
    }

//    public static KPI deserialize(String object) 
//    {        
//        KPI objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (KPI) in.readObject();
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
}
