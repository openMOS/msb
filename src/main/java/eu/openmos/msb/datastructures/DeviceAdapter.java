/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.msb.messages.ExecuteData;
import eu.openmos.msb.messages.ServerStatus;
import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 * @author andre
 */
public class DeviceAdapter
{

  private String id;
  private String name;
  private String longDescription;
  private String shortDescription;

  // [TODO - af-silva - ver qual a melhor solucao para ter mais do que um cliente para diferents protocolos]
  // Client
  private MSB_MiloClientSubscription opcClientIDMaps = new MSB_MiloClientSubscription();
  private List<ExecuteData> executionTableMaps; //workstation name || executetabledata
  private List<ServerStatus> serverStatusMaps; // device name || devices in the workstation and its data


  public DeviceAdapter()
  {

  }


  /**
   * @return the id
   */
  public String getId()
  {
    return id;
  }


  /**
   * @param id the id to set
   */
  public void setId(String id)
  {
    this.id = id;
  }


  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }


  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }


  /**
   * @return the long_description
   */
  public String getLongDescription()
  {
    return longDescription;
  }


  /**
   * @param long_description the long_description to set
   */
  public void setLongDescription(String long_description)
  {
    this.longDescription = long_description;
  }


  /**
   * @return the short_description
   */
  public String getShortDescription()
  {
    return shortDescription;
  }


  /**
   * @param short_description the short_description to set
   */
  public void setShort_description(String short_description)
  {
    this.shortDescription = short_description;
  }


  /**
   * @return the opcClientIDMaps
   */
  public MSB_MiloClientSubscription getOpcClientIDMaps()
  {
    return opcClientIDMaps;
  }


  /**
   * @param opcClientIDMaps the opcClientIDMaps to set
   */
  public void setOpcClientIDMaps(MSB_MiloClientSubscription opcClientIDMaps)
  {
    this.opcClientIDMaps = opcClientIDMaps;
  }


  // TABLES ***********************************************************************************************************/
  // ***************************************************************************************************************** /
  /**
   * @return the executionTableMaps
   */
  public List<ExecuteData> getExecutionTableMaps()
  {
    return executionTableMaps;
  }


  /**
   *
   * @param data
   */
  public void addToExecutionData(ExecuteData data)
  {
    this.executionTableMaps.add(data);
  }


  /**
   *
   * @param productID
   * @return
   */
  public ExecuteData getExecuteDataByProductID(String productID)
  {
    Iterator<ExecuteData> it = this.executionTableMaps.iterator();
    while (it.hasNext())
    {
      ExecuteData temp = it.next();
      if (temp.getProductID().toUpperCase().equals(productID.toUpperCase()))
      {
        return temp;
      }
    }
    return null;
  }


  /**
   *
   * @param recipeID
   * @return
   */
  public ExecuteData getExecuteDataByRecipeID(String recipeID)
  {
    Iterator<ExecuteData> it = this.executionTableMaps.iterator();
    while (it.hasNext())
    {
      ExecuteData temp = it.next();
      if (temp.getRecipeID().toUpperCase().equals(recipeID.toUpperCase()))
      {
        return temp;
      }
    }
    return null;
  }


  /**
   *
   * @param recipeID
   * @return
   */
  public boolean removeExecuteDataByRecipeID(String recipeID)
  {
    ExecuteData data = getExecuteDataByRecipeID(recipeID);
    return this.executionTableMaps.remove(data);
  }


  // ***************************************************************************************************************** /
  /**
   * @param name
   * @return the ServerTableMaps
   */
  public ServerStatus getServerStatus(String name)
  {
    Iterator<ServerStatus> it = this.serverStatusMaps.iterator();
    while (it.hasNext())
    {
      ServerStatus temp = it.next();
      if (temp.getName().toUpperCase().equals(name.toUpperCase()))
      {
        return temp;
      }
    }
    return null;
  }


  /**
   *
   * @param server
   */
  public void addToServerStatusMaps(ServerStatus server)
  {
    this.serverStatusMaps.add(server);
  }


  /**
   *
   * @return List of ServerStatus
   */
  public List<ServerStatus> getServerStatusMaps()
  {
    return this.serverStatusMaps;
  }

}
