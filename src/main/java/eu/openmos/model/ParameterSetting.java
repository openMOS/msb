package eu.openmos.model;

import java.util.Date;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes an actual setting of a Parameter, i.e., a possible value 
 * for it.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ParameterSetting extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(ParameterSetting.class.getName());
    private static final long serialVersionUID = 6529685098267757691L;

    /**
     * Parameter Setting ID.
     */
    private String uniqueId;
    /**
     * Parameter Setting name.
     */
    private String name;
    /**
     * Parameter Setting description.
     */
    private String description;
    /**
     * Paramenter Setting value.
     */
    private String value;
    /**
     * Pointer to the parameter.
     */
    private Parameter parameter;    

//    private static final int FIELDS_COUNT = 6;
    
    /**
     * Default constructor.
     */
    public ParameterSetting() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Parameter Setting's description.
     * @param id - Parameter Setting's ID.
     * @param name - Parameter Setting's name.
     * @param value - Paramenter Setting's value.
     * @param parameter - pointer to the Parameter
     * @param registeredTimestamp - registration timestamp
     */
    public ParameterSetting(String description, 
            String id, 
            String name, 
            String value,
            Parameter parameter,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.description = description;
        this.uniqueId = id;
        this.name = name;
        this.value = value;
        
        this.parameter = parameter;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    
     /**
     * Method that serializes the object.
     * The returned string has the following format:
 
 description,
 uniqueId,
 name,
 value,
 parameter,
 registered ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
//    @Override
//    public String toString_OLD() {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//        String stringRegisteredTimestamp = sdf.format(this.registered);
//        
//        return description + SerializationConstants.TOKEN_PARAMETER_SETTING 
//                + uniqueId + SerializationConstants.TOKEN_PARAMETER_SETTING 
//                + name + SerializationConstants.TOKEN_PARAMETER_SETTING 
//                + value + SerializationConstants.TOKEN_PARAMETER_SETTING 
//                + parameter.toString() + SerializationConstants.TOKEN_PARAMETER_SETTING 
//                + stringRegisteredTimestamp;
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
    
   
    
//    public static ParameterSetting fromString(String object) throws ParseException {        
//        ParameterSetting ps = null;
//      try {
//         ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//         ObjectInputStream in = new ObjectInputStream(bIn);
//         ps = (ParameterSetting) in.readObject();
//         in.close();
//         bIn.close();
//         return ps;
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
    * Method that deserializes a String object.
     * The input string needs to have the following format:
    * 
     * description,
     * uniqueId,
     * name,
     * value,
     * parameter,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
    */
//    public static ParameterSetting fromString_OLD(String object) 
//            throws ParseException
//    {
//        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
//
//        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PARAMETER_SETTING);
//
//        if (tokenizer.countTokens() != FIELDS_COUNT)
//            throw new ParseException("ParameterSetting - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
//        
//        return new ParameterSetting(
//                tokenizer.nextToken(),  // description
//                tokenizer.nextToken(),  // uniqueId
//                tokenizer.nextToken(),  // name
//                tokenizer.nextToken(),  // value
//                Parameter.fromString(tokenizer.nextToken()),    // parameter
////                sdf.parse(tokenizer.nextToken())    // registered
//                tokenizer.nextToken()    // registered
//        );
//    }
    
     /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
 
 {
  "description": description,
  "id": unique id,
  "name": name,
  "parameter": parameter,
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
                .append("parameterId", parameter.getUniqueId())
                .append("registered", stringRegisteredTimestamp);
    }

//    public static void main(String[] args)
//    {
//        ParameterSetting pStart = ParameterSetting.getTestObject();
//        Parameter pStart1 = pStart.getParameter();
//        String myParameterSettingSerialized = pStart.toString();
//        System.out.println("start = " + myParameterSettingSerialized);
//            ParameterSetting pDest = ParameterSetting.deserialize(myParameterSettingSerialized);
//            
//            assert pStart.getParameter().equals(pDest.getParameter());
//            System.out.println(pStart.getParameter());
//            assert pStart.getDescription().equals(pDest.getDescription());
//            System.out.println(pStart.getDescription());
//            assert pStart.getName().equals(pDest.getName());
//            System.out.println(pStart.getName());
//            assert pStart.getRegistered().equals(pDest.getRegistered());
//            System.out.println(pStart.getRegistered());
//            assert pStart.getUniqueId().equals(pDest.getUniqueId());
//            System.out.println(pStart.getUniqueId());
//            assert pStart.getValue().equals(pDest.getValue());
//            System.out.println(pStart.getValue());
//            
//            Parameter pDest1 = pDest.getParameter();
//            assert pStart1.getDefaultValue().equals(pDest1.getDefaultValue());
//            System.out.println(pStart1.getDefaultValue());
//            assert pStart1.getDescription().equals(pDest1.getDescription());
//            System.out.println(pStart1.getDescription());
//            assert pStart1.getLowerBound().equals(pDest1.getLowerBound());
//            System.out.println(pStart1.getLowerBound());
//            assert pStart1.getName().equals(pDest1.getName());
//            System.out.println(pStart1.getName());
//            assert pStart1.getRegistered().equals(pDest1.getRegistered());
//            System.out.println(pStart1.getRegistered());
//            assert pStart1.getType().equals(pDest1.getType());
//            System.out.println(pStart1.getType());
//            assert pStart1.getUniqueId().equals(pDest1.getUniqueId());
//            System.out.println(pStart1.getUniqueId());
//            assert pStart1.getUnit().equals(pDest1.getUnit());
//            System.out.println(pStart1.getUnit());
//            assert pStart1.getUpperBound().equals(pDest1.getUpperBound());
//            System.out.println(pStart1.getUpperBound());
//            
//        
//        
//    }
//
//    public static ParameterSetting getTestObject()
//    {
//        ParameterSetting parameterSetting;
//
//        Date registeredTimestamp = new Date();
//        
//        String parameterSettingDescription = "parameterSettingDescription";
//        String parameterSettingUniqueId = "parameterSettingUniqueId";
//        String parameterSettingName = "parameterSettingName";
//        String parameterSettingValue = "parameterSettingValue";
//        
//        Parameter parameter = Parameter.getTestObject();
//        
//        /*
//     * @param description - Parameter Setting's description.
//     * @param id - Parameter Setting's ID.
//     * @param name - Parameter Setting's name.
//     * @param value - Paramenter Setting's value.
//     * @param parameter - pointer to the Parameter
//     * @param registered - registration timestamp
//*/
//        parameterSetting = new ParameterSetting(parameterSettingDescription, parameterSettingUniqueId, 
//                parameterSettingName, parameterSettingValue, 
//                parameter, 
//                registeredTimestamp
////                registered.toString()
//        );
//        
//        return parameterSetting;        
//    }
//
//    public static ParameterSetting deserialize(String object) 
//    {        
//        ParameterSetting objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (ParameterSetting) in.readObject();
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
