/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.data;

import eu.openmos.model.ProductInstance;
import eu.openmos.model.utilities.SerializationConstants;
import eu.openmos.model.utilities.ListsToString;
import eu.openmos.model.utilities.StringToLists;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Luis Ribeiro <luis.ribeiro@liu.se>
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Order
{

  /**
   * Order ID.
   */
  private String uniqueId;
  /**
   * Order name.
   */
  private String name;
  /**
   * Order description.
   */
  private String description;
  /**
   * WP3 semantic model alignment. Order priority.
   */
  private int priority;
  /**
   * Order lines.
   */
  private List<ProductInstance> productDescriptions;
  /**
   * WP3 semantic model alignment. Order timestamp.
   */
  private Date registeredTimestamp;

  private static final int FIELDS_COUNT = 6;

  private static final Logger logger = Logger.getLogger(Order.class.getName());

  // for reflection purpose
  public Order()
  {
  }

  public Order(String uniqueId, String name, String description, int priority,
          List<ProductInstance> productDescriptions, Date registeredTimestamp)
  {
    this.productDescriptions = productDescriptions;
    this.uniqueId = uniqueId;
    this.name = name;
    this.description = description;
    this.priority = priority;
    this.registeredTimestamp = registeredTimestamp;
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

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public int getPriority()
  {
    return priority;
  }

  public void setPriority(int priority)
  {
    this.priority = priority;
  }

  public Date getRegisteredTimestamp()
  {
    return registeredTimestamp;
  }

  public void setRegisteredTimestamp(Date registeredTimestamp)
  {
    this.registeredTimestamp = registeredTimestamp;
  }

  public List<ProductInstance> getProductDescriptions()
  {
    return productDescriptions;
  }

  public void setProductDescriptions(List<ProductInstance> productDescriptions)
  {
    this.productDescriptions = productDescriptions;
  }

  /**
   * Method that serializes the object. The returned string has the following format:
   *
   * uniqueId, name, description, priority, list of product descriptions registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return Serialized form of the object.
   */
  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append(uniqueId);

    builder.append(SerializationConstants.TOKEN_ORDER);
    builder.append(name);

    builder.append(SerializationConstants.TOKEN_ORDER);
    builder.append(description);

    builder.append(SerializationConstants.TOKEN_ORDER);
    builder.append(priority);

    builder.append(SerializationConstants.TOKEN_ORDER);
    builder.append(ListsToString.writeProductDescriptions(productDescriptions));

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
    String stringRegisteredTimestamp = sdf.format(this.registeredTimestamp);
    builder.append(SerializationConstants.TOKEN_ORDER);
    builder.append(stringRegisteredTimestamp);

    return builder.toString();
  }

  /**
   * Method that deserializes a String object. The input string needs to have the following format:
   *
   * uniqueId, name, description, priority, list of product descriptions registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @param object - String to be deserialized.
   * @return Deserialized object.
   * @throws java.text.ParseException
   */
  public static Order fromString(String object) throws ParseException
  {
    StringTokenizer tokenizer = new StringTokenizer(object, SerializationConstants.TOKEN_ORDER);

    if (tokenizer.countTokens() != FIELDS_COUNT)
    {
      throw new ParseException("Order - " + SerializationConstants.INVALID_FORMAT_FIELD_COUNT_ERROR + FIELDS_COUNT, 0);
    }

    SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);

    return new Order(
            tokenizer.nextToken(), // uniqueId 
            tokenizer.nextToken(), // name 
            tokenizer.nextToken(), // description 
            Integer.parseInt(tokenizer.nextToken()), // priority 
            StringToLists.readProductDescriptions(tokenizer.nextToken()), // list of product descriptions
            sdf.parse(tokenizer.nextToken()) // registeredTimestamp
    );
  }

  /**
   * Method that serializes the object into a BSON document. The returned BSON document has the following format:
   *
   * uniqueId, name, description, priority, list of product descriptions registeredTimestamp ("yyyy-MM-dd HH:mm:ss.SSS")
   *
   * @return BSON form of the object.
   */
  public Document toBSON()
  {
    Document doc = new Document();

    // WRONG
//        Document pDescs = new Document();
//        for (ProductInstance p : productDescriptions )
//        {
//            logger.debug("ORDER TOBSON LINE: " + p.getUniqueId());
//                pDescs.append("id", p.getUniqueId());
//        }
    // WRONG -> Document supports List, not array
//        String[] productDescriptionIds = new String[productDescriptions.size()];
//        int i = 0;
//        for (ProductInstance p : productDescriptions )
//            productDescriptionIds[i++] = p.getUniqueId();
    List<String> productDescriptionIds = productDescriptions.stream().map(pd -> pd.getUniqueId()).collect(Collectors.toList());

    doc.append("id", uniqueId);
    doc.append("name", name);
    doc.append("description", description);
    doc.append("priority", priority);
    // doc.append("productDescriptions", pDescs); 
    doc.append("productDescriptions", productDescriptionIds);
    doc.append("registered", new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registeredTimestamp));

    logger.debug("ORDER TOBSON: " + doc.toString());

    return doc;
  }
}
