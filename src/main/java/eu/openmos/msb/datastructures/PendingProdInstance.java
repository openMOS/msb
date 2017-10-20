/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

/**
 *
 * @author Introsys
 */
public class PendingProdInstance
{
  private String nextRecipeID;
  private String productInstanceID;
  private String daID;

  public PendingProdInstance(String nextRecipeID, String productInstanceID, String daID)
  {
    this.nextRecipeID = nextRecipeID;
    this.productInstanceID = productInstanceID;
    this.daID = daID;
  }

  public String getNextRecipeID()
  {
    return nextRecipeID;
  }

  public String getProductInstanceID()
  {
    return productInstanceID;
  }

  public String getDaID()
  {
    return daID;
  }

  public void setNextRecipeID(String nextRecipeID)
  {
    this.nextRecipeID = nextRecipeID;
  }

  public void setProductInstanceID(String productInstanceID)
  {
    this.productInstanceID = productInstanceID;
  }

  public void setDaID(String daID)
  {
    this.daID = daID;
  }
  
  
}
