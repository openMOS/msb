/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.model.utilities.ListsToString;
import eu.openmos.model.utilities.StringToLists;
import eu.openmos.model.utilities.SerializationConstants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 * Object that represents data regarding recipe execution and that then are passed on to the optimizer.
 *
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class RecipeExecutionData
{

  private String productId;
  private String recipeId;
  private List<String> kpiIds;
  private List<String> parameterIds;
  private Date registeredTimestamp;

  private static final int FIELDS_COUNT = 5;

  // reflection stuff
  public RecipeExecutionData()
  {
  }

  public RecipeExecutionData(
          String productId,
          String recipeId,
          List<String> kpiIds,
          List<String> parameterIds,
          Date registeredTimestamp)
  {
    this.productId = productId;
    this.recipeId = recipeId;
    this.kpiIds = kpiIds;
    this.parameterIds = parameterIds;
    this.registeredTimestamp = registeredTimestamp;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId(String productId)
  {
    this.productId = productId;
  }

  public String getRecipeId()
  {
    return recipeId;
  }

  public void setRecipeId(String recipeId)
  {
    this.recipeId = recipeId;
  }

  public List<String> getKpiIds()
  {
    return kpiIds;
  }

  public void setKpiIds(List<String> kpiIds)
  {
    this.kpiIds = kpiIds;
  }

  public List<String> getParameterIds()
  {
    return parameterIds;
  }

  public void setParameterIds(List<String> parameterIds)
  {
    this.parameterIds = parameterIds;
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
   * productId recipeId list of kpi ids list of parameter ids registeredTimestamp
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append(productId);

    builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
    builder.append(recipeId);

    builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
    builder.append(ListsToString.writeKPIIds(kpiIds));

    builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
    builder.append(ListsToString.writeParameterIds(parameterIds));

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    builder.append(SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);
    builder.append(stringRegisteredTimestamp);

    return builder.toString();

  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * productId recipeId list of kpi ids list of parameter ids registeredTimestamp
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   * @throws java.text.ParseException
   */
  public static RecipeExecutionData fromString(String object) throws ParseException
  {
    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_RECIPE_EXECUTION_DATA);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("RecipeExecutionData - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    return new RecipeExecutionData(
            tokenizer.nextToken(), // product id
            tokenizer.nextToken(), // recipe id
            StringToLists.readKPIIds(tokenizer.nextToken()), // list of kpi ids
            StringToLists.readParameterIds(tokenizer.nextToken()), // list of parameter ids
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * productId recipeId list of kpi ids list of parameter ids registeredTimestamp
   *
   * @return BSON form of the object.
   */
  public Document toBSON()
  {
    Document doc = new Document();

    doc.append("productId", productId);
    doc.append("recipeId", recipeId);
    doc.append("kpiIds", kpiIds);
    doc.append("parameterIds", parameterIds);
    doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));

    return doc;
  }
}
