package eu.openmos.model;

import java.util.Date;
import java.util.StringTokenizer;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes a functional parameter of a device.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Parameter extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757690L;
    
    /**
     * Parameter ID.
     */
    private String uniqueId;
    /**
     * Parameter name.
     */
    private String name;
    /**
     * Parameter description.
     */
    private String description;
    /**
     * Parameter unit.
     */
    private String unit;
    /**
     * Parameter lower bound.
     */
    private String lowerBound;
    /**
     * Parameter upper bound.
     */
    private String upperBound;
    /**
     * Parameter default value.
     */
    private String defaultValue;    
    /**
     * Parameter type.
     */
    private String type;    

//    private static final int FIELDS_COUNT = 9;
    
    /**
     * Default constructor.
     */
    public Parameter() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param defaultValue - Parameter's default value.
     * @param description - Parameter's description.
     * @param uniqueId - Parameter's ID.
     * @param lowerBound - Parameter's lower bound.
     * @param upperBound - Parameter's upper bound.
     * @param name - Parameter's name.
     * @param unit - Parameter's unit.
     * @param type
     * @param registeredTimestamp
     */
    public Parameter(String defaultValue, String description, String uniqueId, 
            String lowerBound, String upperBound, String name, String unit,
            String type, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.defaultValue = defaultValue;
        this.description = description;
        this.uniqueId = uniqueId;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.name = name;
        this.unit = unit;
        
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public String getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(String lowerBound) {
        this.lowerBound = lowerBound;
    }

    public String getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(String upperBound) {
        this.upperBound = upperBound;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
 
 default value,
 description,
 unique id,
 lower bound,
 upper bound,
 name,
 unit,
 type,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString_OLD() {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        
//        return defaultValue + SerializationConstants.TOKEN_PARAMETER + 
//                description + SerializationConstants.TOKEN_PARAMETER + 
//                uniqueId + SerializationConstants.TOKEN_PARAMETER + 
//                lowerBound + SerializationConstants.TOKEN_PARAMETER + 
//                upperBound + SerializationConstants.TOKEN_PARAMETER + 
//                name + SerializationConstants.TOKEN_PARAMETER + 
//                unit + SerializationConstants.TOKEN_PARAMETER + 
//                type + SerializationConstants.TOKEN_PARAMETER + 
//                stringRegisteredTimestamp;
//    }
    
//    @Override
//    public String toString() {
//        String outString = null;
//    
//        try {
//         ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
//         ObjectOutputStream out = new ObjectOutputStream(bOut);
//         out.writeObject(this);
//         out.close();
//         bOut.close();
//         outString = bOut.toString();
//      }catch(IOException i) {
//         i.printStackTrace();
//      }     
//        return outString;
//    }
    
   
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
 
 default value,
 description,
 unique id,
 lower bound,
 upper bound,
 name,
 unit,
 type,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static Parameter fromString_OLD(String object) 
//    throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PARAMETER);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("Parameter - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new Parameter(
//                tokenizer.nextToken(),  // default value
//                tokenizer.nextToken(),  // description
//                tokenizer.nextToken(),  // unique id
//                tokenizer.nextToken(),  // lower bound
//                tokenizer.nextToken(),  // upper bound
//                tokenizer.nextToken(),  // name 
//                tokenizer.nextToken(),  // unit
//                tokenizer.nextToken(),  // type
//                sdf.parse(tokenizer.nextToken())    // registered
////                tokenizer.nextToken()    // registered
//        );
//    }

//    public static Parameter fromString(String object) throws ParseException {        
//        Parameter p = null;
//      try {
//         ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//         ObjectInputStream in = new ObjectInputStream(bIn);
//         p = (Parameter) in.readObject();
//         in.close();
//         bIn.close();
//         return p;
//      }catch(IOException i) {
//         i.printStackTrace();
//         return null;
//      }catch(ClassNotFoundException c) {
//         System.out.println("SubSystem class not found");
//         c.printStackTrace();
//         return null;
//      }
//    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
  "defaultValue": defaultValue,
  "description": description,
  "id": unique id,
  "lowerBound": lowerBound,
  "upperBound": upperBound,
  "name": name,
  "unit": unit,
  "type": type,
  "registered": registered
 } 
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        return toBSON2();
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        return new Document("defaultValue", defaultValue)
//                .append("description", description)
//                .append("id", uniqueId)
//                .append("lowerBound", lowerBound)
//                .append("upperBound", upperBound)
//                .append("name", name)
//                .append("unit", unit)
//                .append("type", type)
//                .append("registered", stringRegisteredTimestamp);
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
    public static Parameter fromBSON(Document bsonParameter)
    throws ParseException 
    {
            return (Parameter)fromBSON2(bsonParameter, Parameter.class);
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        
//        return new Parameter(
//            bsonParameter.get("defaultValue").toString(),
//            bsonParameter.get("description").toString(),
//            bsonParameter.get("id").toString(),
//            bsonParameter.get("lowerBound").toString(),
//            bsonParameter.get("upperBound").toString(),
//            bsonParameter.get("name").toString(),
//            bsonParameter.get("unit").toString(),
//            bsonParameter.get("type").toString(),
//            sdf.parse(bsonParameter.get("registered").toString())                
////            bsonParameter.get("registered").toString()
//        );
    }
    
//    public static void main(String[] args)
//    {
//        Parameter pStart = Parameter.getTestObject();
//        String myParameterSerialized = pStart.toString();
//        System.out.println("start = " + myParameterSerialized);
//            Parameter pDest = Parameter.deserialize(myParameterSerialized);
//            assert pStart.getDefaultValue().equals(pDest.getDefaultValue());
//            System.out.println(pStart.getDefaultValue());
//            assert pStart.getDescription().equals(pDest.getDescription());
//            System.out.println(pStart.getDescription());
//            assert pStart.getLowerBound().equals(pDest.getLowerBound());
//            System.out.println(pStart.getLowerBound());
//            assert pStart.getName().equals(pDest.getName());
//            System.out.println(pStart.getName());
//            assert pStart.getRegistered().equals(pDest.getRegistered());
//            System.out.println(pStart.getRegistered());
//            assert pStart.getType().equals(pDest.getType());
//            System.out.println(pStart.getType());
//            assert pStart.getUniqueId().equals(pDest.getUniqueId());
//            System.out.println(pStart.getUniqueId());
//            assert pStart.getUnit().equals(pDest.getUnit());
//            System.out.println(pStart.getUnit());
//            assert pStart.getUpperBound().equals(pDest.getUpperBound());
//            System.out.println(pStart.getUpperBound());
//    }

//    public static Parameter getTestObject()
//    {
//        Date registeredTimestamp = new Date();
//        // String registered = new Date().toString();
//        
//        Parameter parameter;
//        
//        String parameterDefaultValue = "parameterDefaultValue";
//        String parameterDescription = "parameterDescription";
//        String parameterUniqueId = "parameterUniqueId";
//        String parameterLowerBound = "parameterLowerBound";
//        String parameterUpperBound = "parameterUpperBound";
//        String parameterName = "parameterName";
//        String parameterUnit = "parameterUnit";
//        String parameterType = "parameterType";
//        
//        parameter = new Parameter(
//                parameterDefaultValue, 
//                parameterDescription, 
//                parameterUniqueId, 
//                parameterLowerBound, 
//                parameterUpperBound, 
//                parameterName, 
//                parameterUnit,
//                parameterType,
//                registeredTimestamp
//        );
//        
//        return parameter;
//    }
//    
//    
//    public static Parameter deserialize(String object) 
//    {        
//        Parameter objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (Parameter) in.readObject();
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