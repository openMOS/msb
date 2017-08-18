/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model;

import java.util.StringTokenizer;
import eu.openmos.model.utilities.SerializationConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;

/**
 * Object that describes an actual setting of a KPI, i.e., a possible value for it.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class KPISetting
{

  /**
   * KPI Setting's description.
   */
  private String description;
  /**
   * KPI Setting's ID.
   */
  private String id;
  /**
   * KPI Setting's name.
   */
  private String name;
  /**
   * KPI Setting's value.
   */
  private String value;
  /**
   * WP3 semantic model alignment. Pointer to the kpi.
   */
  private KPI kpi;
  /**
   * WP3 semantic model alignment. KPI setting value at the given timestamp.
   */
  private Date registeredTimestamp;

  private static final int FIELDS_COUNT = 6;

  /**
   * Default constructor, for reflection.
   */
  public KPISetting()
  {
  }

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
          Date registeredTimestamp)
  {
    this.description = description;
    this.id = id;
    this.name = name;
    this.value = value;

    // WP3 semantic model alignment
    this.kpi = kpi;
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

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
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

  public KPI getKpi()
  {
    return kpi;
  }

  public void setKpi(KPI kpi)
  {
    this.kpi = kpi;
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
   * description, unique id, name, value, kpi, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(registeredTimestamp);

    return description + SerializationConstants.TOKEN_KPI_SETTING
            + id + SerializationConstants.TOKEN_KPI_SETTING
            + name + SerializationConstants.TOKEN_KPI_SETTING
            + value + SerializationConstants.TOKEN_KPI_SETTING
            + kpi.toString() + SerializationConstants.TOKEN_KPI_SETTING
            + stringRegisteredTimestamp;
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * description, unique id, name, value, kpi, registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   */
  public static KPISetting fromString(String object) throws ParseException
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_KPI_SETTING);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("KPISetting - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    return new KPISetting(
            tokenizer.nextToken(), // description
            tokenizer.nextToken(), // unique id
            tokenizer.nextToken(), // name
            tokenizer.nextToken(), // value
            KPI.fromString(tokenizer.nextToken()), // kpi
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * {
   * "description": description, "id": unique id, "name": name, "value": value, "kpi": kpi, "registered":
   * registeredTimestamp }
   *
   * @return BSON Document format of the object.
   */
  public Document toBSON()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    return new Document("description", description)
            .append("id", id)
            .append("name", name)
            .append("value", value)
            .append("kpiId", kpi.getUniqueId())
            .append("registered", stringRegisteredTimestamp);
  }

}
