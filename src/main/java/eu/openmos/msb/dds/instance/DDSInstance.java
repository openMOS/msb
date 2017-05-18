/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;


/**
 *
 * @author andre
 */
public class DDSInstance
{

  private static DDSInstance instance = null;


  /**
   * 
   */
  protected DDSInstance()
  {
    // EMPTY
  }


  /**
   * 
   * @return A DDSInstance that is unique to the project
   */
  public static DDSInstance getInstance()
  {
    if (instance == null)
    {
      synchronized (DDSInstance.class)
      {
        if (instance == null) // doublce check
        {
          instance = new DDSInstance();
        }
      }
    }
    return instance;
  }

}
// EOF
