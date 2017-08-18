/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;

import java.util.Date;
import java.util.StringTokenizer;
import eu.openmos.model.utilities.SerializationConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.bson.Document;

/**
 * Object that describes a functional parameter of a device.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Parameter
{

  /**
   * Parameter's ID.
   */
  private String uniqueId;
  /**
   * Parameter's name.
   */
  private String name;
  /**
   * Parameter's description.
   */
  private String description;
  /**
   * Parameter's unit.
   */
  private String unit;
  /**
   * Parameter's lower bound.
   */
  private String lowerBound;
  /**
   * Parameter's upper bound.
   */
  private String upperBound;
  /**
   * Parameter's default value.
   */
  private String defaultValue;

  /**
   * WP3 semantic model alignment. Parameter type.
   */
  private String type;
  /**
   * WP3 semantic model alignment.
   */
  private Date registeredTimestamp;

  private static final int FIELDS_COUNT = 9;

  /**
   * Default constructor.
   */
  public Parameter()
  {
  }

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
          String type, Date registeredTimestamp)
  {
    this.defaultValue = defaultValue;
    this.description = description;
    this.uniqueId = uniqueId;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.name = name;
    this.unit = unit;

    // WP3 semantic model alignment
    this.type = type;
    this.registeredTimestamp = registeredTimestamp;
  }

  public String getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId)
  {
    this.uniqueId = uniqueId;
  }

  public String getLowerBound()
  {
    return lowerBound;
  }

  public void setLowerBound(String lowerBound)
  {
    this.lowerBound = lowerBound;
  }

  public String getUpperBound()
  {
    return upperBound;
  }

  public void setUpperBound(String upperBound)
  {
    this.upperBound = upperBound;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getUnit()
  {
    return unit;
  }

  public void setUnit(String unit)
  {
    this.unit = unit;
  }

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public Date getRegisteredTimestamp()
  {
    return registeredTimestamp;
  }

  public void setRegisteredTimestamp(Date registeredTimestamp)
  {
    this.registeredTimestamp = registeredTimestamp;
  }

  /**
   * Method that serializes the object. The returned string has the following format:
   *
   * default value, description, unique id, lower bound, upper bound, name, unit, type, registeredTimestamp ("yyyy-MM-dd
   * HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);

    return defaultValue + SerializationConstants.TOKEN_PARAMETER
            + description + SerializationConstants.TOKEN_PARAMETER
            + uniqueId + SerializationConstants.TOKEN_PARAMETER
            + lowerBound + SerializationConstants.TOKEN_PARAMETER
            + upperBound + SerializationConstants.TOKEN_PARAMETER
            + name + SerializationConstants.TOKEN_PARAMETER
            + unit + SerializationConstants.TOKEN_PARAMETER
            + type + SerializationConstants.TOKEN_PARAMETER
            + stringRegisteredTimestamp;
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * default value, description, unique id, lower bound, upper bound, name, unit, type, registeredTimestamp ("yyyy-MM-dd
   * HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   */
  public static Parameter fromString(String object)
          throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PARAMETER);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("Parameter - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    return new Parameter(
            tokenizer.nextToken(), // default value
            tokenizer.nextToken(), // description
            tokenizer.nextToken(), // unique id
            tokenizer.nextToken(), // lower bound
            tokenizer.nextToken(), // upper bound
            tokenizer.nextToken(), // name 
            tokenizer.nextToken(), // unit
            tokenizer.nextToken(), // type
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * {
   * "defaultValue": defaultValue, "description": description, "id": unique id, "lowerBound": lowerBound, "upperBound":
   * upperBound, "name": name, "unit": unit, "type": type, "registered": registeredTimestamp }
   *
   * @return BSON Document format of the object.
   */
  public Document toBSON()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    return new Document("defaultValue", defaultValue)
            .append("description", description)
            .append("id", uniqueId)
            .append("lowerBound", lowerBound)
            .append("upperBound", upperBound)
            .append("name", name)
            .append("unit", unit)
            .append("type", type)
            .append("registered", stringRegisteredTimestamp);
  }

  /**
   * Method that deserializes a BSON object. The input BSON needs to have the following format:
   *
   * description, unique id, name, upper bound, lower bound, value, unit
   *
   * @param bsonKPI - BSON to be deserialized.
   * @return Deserialized object.
   */
  public static Parameter fromBSON(Document bsonParameter)
          throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    return new Parameter(
            bsonParameter.get("defaultValue").toString(),
            bsonParameter.get("description").toString(),
            bsonParameter.get("id").toString(),
            bsonParameter.get("lowerBound").toString(),
            bsonParameter.get("upperBound").toString(),
            bsonParameter.get("name").toString(),
            bsonParameter.get("unit").toString(),
            bsonParameter.get("type").toString(),
            sdf.parse(bsonParameter.get("registered").toString())
    );
  }
}
