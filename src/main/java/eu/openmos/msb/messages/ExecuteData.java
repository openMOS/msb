/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.messages;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 *
 * @author renato.martins
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExecuteData
{

  //
  @XmlElement(name = "productID")
  private String productID = "";

  //
  @XmlElement(name = "recipeID")
  private String recipeID = "";

  //
  @XmlElement(name = "status")
  private Boolean status = false;

  // OPC UA = folder in the server hierarchy
  // DDS = partion
  @XmlElement(name = "objectID")
  private String objectID = "";

  // OPC UA = method to call
  // DDS = topic to call
  @XmlElement(name = "methodID")
  private String methodID = "";


  public ExecuteData()
  {
    // EMPTY
  }


  /**
   * @return the productID
   */
  public String getProductID()
  {
    return productID;
  }


  /**
   * @param productID the productID to set
   */
  public void setProductID(String productID)
  {
    this.productID = productID;
  }


  /**
   * @return the recipeID
   */
  public String getRecipeID()
  {
    return recipeID;
  }


  /**
   * @param recipeID the recipeID to set
   */
  public void setRecipeID(String recipeID)
  {
    this.recipeID = recipeID;
  }


  /**
   * @return the status
   */
  public Boolean getStatus()
  {
    return status;
  }


  /**
   * @param status the status to set
   */
  public void setStatus(Boolean status)
  {
    this.status = status;
  }


  /**
   * @return the objectID
   */
  public String getObjectID()
  {
    return objectID;
  }


  /**
   * @param objectID the objectID to set
   */
  public void setObjectID(String objectID)
  {
    this.objectID = objectID;
  }


  /**
   * @return the methodID
   */
  public String getMethodID()
  {
    return methodID;
  }


  /**
   * @param methodID the methodID to set
   */
  public void setMethodID(String methodID)
  {
    this.methodID = methodID;
  }
}
