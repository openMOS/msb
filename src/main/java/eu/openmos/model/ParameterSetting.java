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
 * Object that describes an actual setting of a Parameter, i.e., a possible value for it.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ParameterSetting
{

  /**
   * Parameter Setting's ID.
   */
  private String uniqueId;
  /**
   * Parameter Setting's name.
   */
  private String name;
  /**
   * Parameter Setting's description.
   */
  private String description;
  /**
   * Paramenter Setting's value.
   */
  private String value;
  /**
   * WP3 semantic model alignment. Pointer to the parameter.
   */
  private Parameter parameter;
  /**
   * WP3 semantic model alignment. Parameter setting value at the given timestamp.
   */
  private Date registeredTimestamp;

  private static final int FIELDS_COUNT = 6;

  /**
   * Default constructor.
   */
  public ParameterSetting()
  {
  }

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
          Date registeredTimestamp)
  {
    this.description = description;
    this.uniqueId = id;
    this.name = name;
    this.value = value;

    // WP3 semantic model alignment
    this.parameter = parameter;
    this.registeredTimestamp = registeredTimestamp;
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Parameter getParameter()
  {
    return parameter;
  }

  public void setParameter(Parameter parameter)
  {
    this.parameter = parameter;
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
   * description, uniqueId, name, value, parameter, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);

    return description + SerializationConstants.TOKEN_PARAMETER_SETTING
            + uniqueId + SerializationConstants.TOKEN_PARAMETER_SETTING
            + name + SerializationConstants.TOKEN_PARAMETER_SETTING
            + value + SerializationConstants.TOKEN_PARAMETER_SETTING
            + parameter.toString() + SerializationConstants.TOKEN_PARAMETER_SETTING
            + stringRegisteredTimestamp;
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * description, uniqueId, name, value, parameter, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   */
  public static ParameterSetting fromString(String object)
          throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_PARAMETER_SETTING);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("ParameterSetting - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    return new ParameterSetting(
            tokenizer.nextToken(), // description
            tokenizer.nextToken(), // uniqueId
            tokenizer.nextToken(), // name
            tokenizer.nextToken(), // value
            Parameter.fromString(tokenizer.nextToken()), // parameter
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * {
   * "description": description, "id": unique id, "name": name, "parameter": parameter, "registered":
   * registeredTimestamp }
   *
   * @return BSON Document format of the object.
   */
  public Document toBSON()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    return new Document("description", description)
            .append("id", uniqueId)
            .append("name", name)
            .append("value", value)
            .append("parameterId", parameter.getUniqueId())
            .append("registered", stringRegisteredTimestamp);
  }
}
