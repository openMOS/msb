/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.model.utilities.SerializationConstants;
import java.text.ParseException;
import java.util.StringTokenizer;
import eu.openmos.model.Recipe;

/**
 * Object that represents the data the agents receive from the Manufacturing Service Bus and that is then passed to the
 * optimizer.
 *
 * NOTE: it is not sure if we really need this class or not. For now, we are going to implement RawEquipmentData and
 * RawProductData that should cover what we need. TODO check if this is correct, if we need something else or what....
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 *
 */
@Deprecated
public class AgentData
{

  /**
   * Name of the agent where the data originated.
   */
  private String agentUniqueName;
  /**
   * Device's data creation timestamp.
   */
  private String deviceTime;
  /**
   * Manufacturing Service Bus data creation timestamp.
   */
  private String msbTime;
  /**
   * Data value.
   */
  private String value;
  /**
   * Recipe to which the value concerns.
   */
  private Recipe recipe;
  /**
   * The Java type of the value.
   */
  private ValueType valueType;
  /**
   * The type of data.
   */
  private DataType dataType;
  /**
   * Physical Location from where the data originated.
   */
  private PhysicalLocation physicalLocation;
  /**
   * Logical location from where the data originated.
   */
  private LogicalLocation logicalLocation;

  /**
   * Parameterized constructor.
   *
   * @param agentUniqueName - Agent's name.
   * @param deviceTime - Device's data creation timestamp.
   * @param msbTime - Manufacturing Service Bus data creation timestamp.
   * @param value - Data value.
   * @param recipe - Recipe to which the value concerns.
   * @param valueType - The Java type of the value.
   * @param dataType - The type of data.
   * @param physicalLocation - Physical Location from where the data originated.
   * @param logicalLocation - Logical location from where the data originated.
   */
  public AgentData(String agentUniqueName, String deviceTime, String msbTime, String value, Recipe recipe, ValueType valueType, DataType dataType, PhysicalLocation physicalLocation, LogicalLocation logicalLocation)
  {
    this.agentUniqueName = agentUniqueName;
    this.deviceTime = deviceTime;
    this.msbTime = msbTime;
    this.value = value;
    this.recipe = recipe;
    this.valueType = valueType;
    this.dataType = dataType;
    this.physicalLocation = physicalLocation;
    this.logicalLocation = logicalLocation;
  }

  public PhysicalLocation getPhysicalLocation()
  {
    return physicalLocation;
  }

  public void setPhysicalLocation(PhysicalLocation physicalLocation)
  {
    this.physicalLocation = physicalLocation;
  }

  public LogicalLocation getLogicalLocation()
  {
    return logicalLocation;
  }

  public void setLogicalLocation(LogicalLocation logicalLocation)
  {
    this.logicalLocation = logicalLocation;
  }

  public String getAgentUniqueName()
  {
    return agentUniqueName;
  }

  public void setAgentUniqueName(String agentUniqueName)
  {
    this.agentUniqueName = agentUniqueName;
  }

  public String getDeviceTime()
  {
    return deviceTime;
  }

  public void setDeviceTime(String deviceTime)
  {
    this.deviceTime = deviceTime;
  }

  public String getMsbTime()
  {
    return msbTime;
  }

  public void setMsbTime(String msbTime)
  {
    this.msbTime = msbTime;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Recipe getRecipe()
  {
    return recipe;
  }

  public void setRecipe(Recipe recipe)
  {
    this.recipe = recipe;
  }

  public ValueType getValueType()
  {
    return valueType;
  }

  public void setValueType(ValueType valueType)
  {
    this.valueType = valueType;
  }

  public DataType getDataType()
  {
    return dataType;
  }

  public void setDataType(DataType dataType)
  {
    this.dataType = dataType;
  }

  /**
   * Method that serializes the object.
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    return agentUniqueName + SerializationConstants.TOKEN_EXTRACTED_DATA
            + deviceTime + SerializationConstants.TOKEN_EXTRACTED_DATA
            + msbTime + SerializationConstants.TOKEN_EXTRACTED_DATA
            + value + SerializationConstants.TOKEN_EXTRACTED_DATA
            + recipe + SerializationConstants.TOKEN_EXTRACTED_DATA
            + valueType + SerializationConstants.TOKEN_EXTRACTED_DATA
            + dataType + SerializationConstants.TOKEN_EXTRACTED_DATA
            + physicalLocation + SerializationConstants.TOKEN_EXTRACTED_DATA
            + logicalLocation;
  }

  /**
   * Method that deserializes a String object.
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   */
  public static AgentData fromString(String object) throws ParseException
  {
    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_EXTRACTED_DATA);
    return new AgentData(
            tokenizer.nextToken(),
            tokenizer.nextToken(),
            tokenizer.nextToken(),
            tokenizer.nextToken(),
            Recipe.fromString(tokenizer.nextToken()),
            ValueType.valueOf(tokenizer.nextToken()),
            DataType.valueOf(tokenizer.nextToken()),
            PhysicalLocation.fromString(tokenizer.nextToken()),
            LogicalLocation.fromString(tokenizer.nextToken())
    );
  }
}
