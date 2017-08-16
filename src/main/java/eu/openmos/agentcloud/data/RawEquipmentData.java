/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.agentcloud.data.utilities.SerializationConstants;
import eu.openmos.agentcloud.data.utilities.ListsToString;
import eu.openmos.agentcloud.data.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;
import org.bson.Document;

/**
 * Object that represents the data that resource and transport agents receive from the Manufacturing
 * Service Bus (related to workstations, transport stations) and that then are passed on to the optimizer.
 * 
 * NOTE: created as a copy of the AgentData class, and then refactored according to the Visio notes
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class RawEquipmentData {
    /**
     * WP3 semantic model alignment
     * It matches with (it replaces) the former field "cloud agent name" 
     */
    // private String agentUniqueName;
    private String equipmentId;
    
    /**
     * Data value coming from the sensors.
     */
    private String[] sensorValues;

    // TODO to discuss with Pedro Lboro:
    // on the visio file we have datatype and measurement unit....
    // Are these two fields related to every element of the sensorValues array?
    // Or not?    
    /**
     * Value type of sensors data.
     */
    // private ValueType valueType;
    private String valueType;
    
    /**
     * Measurement unit for sensors data.
     */
    private String measurementUnit;

    /**
     * WP3 semantic model alignment.
     * Timestamp of sensors data.
    */
    private Date registeredTimestamp;

    private static final int FIELDS_COUNT = 5;
    
    // reflection stuff
    public RawEquipmentData() {
    }

    public RawEquipmentData(String equipmentId, String[] sensorValues, String valueType, String measurementUnit, Date registeredTimestamp) {
        this.equipmentId = equipmentId;
        this.sensorValues = sensorValues;
        this.valueType = valueType;
        this.measurementUnit = measurementUnit;
        this.registeredTimestamp = registeredTimestamp;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String[] getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(String[] sensorValues) {
        this.sensorValues = sensorValues;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Date getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public void setRegisteredTimestamp(Date registeredTimestamp) {
        this.registeredTimestamp = registeredTimestamp;
    }    
        
    /**
     * Method that serializes the object.
     * The returned string has the following format:
     * 
     * equipmentId,
     * array of sensor values,
     * value type TO CHECK WITH PEDRO LBORO,
     * measurement unit TO CHECK WITH PEDRO LBORO,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return Serialized form of the object. 
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(equipmentId);
        
        builder.append(SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA);
        builder.append(ListsToString.writeSensorValues(sensorValues));
        
        builder.append(SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA);
        builder.append(valueType);

        builder.append(SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA);
        builder.append(measurementUnit);

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
        builder.append(SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA);
        builder.append(stringRegisteredTimestamp);
        
        return builder.toString();
        
    }
    
    /**
    * Method that deserializes a String object.
     * The input string needs to have the following format:
     * 
     * equipmentId,
     * array of sensor values,
     * value type TO CHECK WITH PEDRO LBORO,
     * measurement unit TO CHECK WITH PEDRO LBORO,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
    * 
    * @param object - String to be deserialized.
    * @return Deserialized object.
     * @throws java.text.ParseException
     */
    public static RawEquipmentData fromString(String object) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_RAW_EQUIPMENT_DATA);

        if (tokenizer.countTokens() != FIELDS_COUNT)
            throw new ParseException("RawEquipmentData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);

        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        return new RawEquipmentData(
                tokenizer.nextToken(),                                          // equipment id
                StringToLists.readSensorValues(tokenizer.nextToken()),          // sensor values
                tokenizer.nextToken(),                                          // value type           
                tokenizer.nextToken(),                                          // measurement unit
                sdf.parse(tokenizer.nextToken())                                // registeredTimestamp
        );
    }
    
    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
     * 
     * equipmentId,
     * array of sensor values,
     * value type TO CHECK WITH PEDRO LBORO,
     * measurement unit TO CHECK WITH PEDRO LBORO,
     * registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        doc.append("id", equipmentId);
        doc.append("sensorValues", Arrays.asList(sensorValues));
        doc.append("valueType", valueType);
        doc.append("measurementUnit", measurementUnit);
        doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));
        
        return doc;        
    }
}
