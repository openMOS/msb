package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import java.util.Date;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes an actual setting of a Parameter, i.e., a possible value for it.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ParameterSetting extends Base implements Serializable
{

  private static final Logger logger = Logger.getLogger(ParameterSetting.class.getName());
  private static final long serialVersionUID = 6529685098267757017L;

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

  private transient String valuePath;

  /**
   * Default constructor.
   */
  public ParameterSetting()
  {
    super();
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
          Date registeredTimestamp
  )
  {
    super(registeredTimestamp);

    this.description = description;
    this.uniqueId = id;
    this.name = name;
    this.value = value;

    this.parameter = parameter;
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

  public String getValuePath()
  {
    return valuePath;
  }

  public void setValuePath(String parameterPath)
  {
    this.valuePath = parameterPath;
  }

  /**
   * Method that serializes the object into a BSON document.
   *
   * @return BSON Document format of the object.
   */
  public Document toBSON()
  {
    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registered);

    String parameterId = null;
    if (parameter != null)
    {
      parameterId = parameter.getUniqueId();
    }

    return new Document(DatabaseConstants.DESCRIPTION, description)
            .append(DatabaseConstants.UNIQUE_ID, uniqueId)
            .append(DatabaseConstants.NAME, name)
            .append(DatabaseConstants.VALUE, value)
            .append(DatabaseConstants.PARAMETER_ID, parameterId)
            .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
  }
}
