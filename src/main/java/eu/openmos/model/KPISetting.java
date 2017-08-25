package eu.openmos.model;

import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes an actual setting of a KPI, i.e., a possible value for 
 * it.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class KPISetting extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(KPISetting.class.getName());
    private static final long serialVersionUID = 6529685098267757688L;
    
    /**
     * KPI Setting ID.
     */
    private String uniqueId;
    /**
     * KPI Setting name.
     */
    private String name;
    /**
     * KPI Setting description.
     */
    private String description;
    /**
     * KPI Setting value.
     */
    private String value;
    /**
     * Pointer to the kpi.
     */
    private KPI kpi; 
    
//    private static final int FIELDS_COUNT = 6;

    /**
     * Default constructor, for reflection.
     */
    public KPISetting() {super();}
    
    /** 
     * Parameterized constuctor.
     * 
     * @param description - KPI Setting's description.
     * @param id - KPI Setting's ID.
     * @param name - KPI Setting's name.
     * @param value - KPI Setting's value.
     * @param kpi - pointer to the KPI
     * @param registeredTimestamp - registration timestamp
     */
    public KPISetting(String description, 
            String id, 
            String name, 
            String value,
            KPI kpi,
            Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.description = description;
        this.uniqueId = id;
        this.name = name;
        this.value = value;
        
        this.kpi = kpi;
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

    public void setUniqueId(String id) {
        this.uniqueId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * description,
     * unique id,
     * name,
     * value,
     * kpi,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString() {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(registered);
//        
//        return description + SerializationConstants.TOKEN_KPI_SETTING + 
//                uniqueId + SerializationConstants.TOKEN_KPI_SETTING + 
//                name + SerializationConstants.TOKEN_KPI_SETTING + 
//                value + SerializationConstants.TOKEN_KPI_SETTING 
//                + kpi.toString() + SerializationConstants.TOKEN_KPI_SETTING 
//                + stringRegisteredTimestamp;
//    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
    * 
     * description,
     * unique id,
     * name,
     * value,
     * kpi,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static KPISetting fromString(String object) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_KPI_SETTING);
//        
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("KPISetting - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new KPISetting(
//                tokenizer.nextToken(),  // description
//                tokenizer.nextToken(),  // unique uniqueId
//                tokenizer.nextToken(),  // name
//                tokenizer.nextToken(),   // value
//                KPI.fromString(tokenizer.nextToken()),    // kpi
//                sdf.parse(tokenizer.nextToken())    // registered
//        );
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
  "description": description,
  "uniqueId": unique uniqueId,
  "name": name,
  "value": value,
  "kpi": kpi,
  "registered": registered
 }
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);
        return new Document("description", description)
                .append("id", uniqueId)
                .append("name", name)
                .append("value", value)
                .append("kpiId", kpi.getUniqueId())
                .append("registered", stringRegisteredTimestamp);
    }

//    public static KPISetting deserialize(String object) 
//    {        
//        KPISetting objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (KPISetting) in.readObject();
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
