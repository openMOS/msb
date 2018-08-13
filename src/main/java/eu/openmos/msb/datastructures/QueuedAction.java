/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

/**
 *
 * @author renato.martins
 */
public class QueuedAction
{
  private String action_type;
  private String da_id;
  private String product_instance_id;
  private String product_type_id;
  private String recipe_id;

  /**
   * @return the type
   */
  public String getaActionType()
  {
    return action_type;
  }

  /**
   * @param type the type to set
   */
  public void setActionType(String type)
  {
    this.action_type = type;
  }

  /**
   * @return the da_id
   */
  public String getDa_id()
  {
    return da_id;
  }

  /**
   * @param da_id the da_id to set
   */
  public void setDa_id(String da_id)
  {
    this.da_id = da_id;
  }

  /**
   * @return the product_instance_id
   */
  public String getProduct_instance_id()
  {
    return product_instance_id;
  }

  /**
   * @param product_instance_id the product_instance_id to set
   */
  public void setProduct_instance_id(String product_instance_id)
  {
    this.product_instance_id = product_instance_id;
  }

  /**
   * @return the product_type_id
   */
  public String getProduct_type_id()
  {
    return product_type_id;
  }

  /**
   * @param product_type_id the product_type_id to set
   */
  public void setProduct_type_id(String product_type_id)
  {
    this.product_type_id = product_type_id;
  }

  /**
   * @return the recipe_id
   */
  public String getRecipe_id()
  {
    return recipe_id;
  }

  /**
   * @param recipe_id the recipe_id to set
   */
  public void setRecipe_id(String recipe_id)
  {
    this.recipe_id = recipe_id;
  }
  
}
