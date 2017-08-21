/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.model.Equipment;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;
import io.vertx.core.Vertx;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author andre
 */
public abstract class DeviceAdapter
{

  protected int bd_id;

  /*
   * device name || devices in the workstation and its data
   */
  protected List<Equipment> listOfDevices;
  protected SubSystem subSystem;
  protected Vertx vert;

  public DeviceAdapter()
  {
    listOfDevices = new ArrayList<Equipment>();
    subSystem = new SubSystem(); // will be depreceated
    vert = Vertx.vertx();
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return bd_id;
  }

  /**
   * @param name
   * @return the ServerTableMaps
   */
  public String getEquipmentStatusByName(String name)
  {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  /**
   * @param uniqueID
   * @return the ServerTableMaps
   */
  public String getEquipmentStatusByID(String uniqueID)
  {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @return
   */
  public List<Equipment> getListOfEquipments()
  {
    // TODO
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param module
   */
  public void addEquipmentModule(Equipment module)
  {
    // TODO
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param deviceName
   * @return
   */
  public boolean removeEquipmentModule(String deviceName)
  {
    // TODO
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  /**
   *
   * @param system
   */
  public void setSubSystem(SubSystem system)
  {
    this.subSystem = system;
  }

  /**
   *
   * @return SubSystem
   */
  public SubSystem getSubSystem()
  {
    return this.subSystem;
  }

  public List<Recipe> getListOfRecipes()
  {

    Recipe r = new Recipe();
    return null;

  }

  /**
   *
   * @return
   */
  public List<Skill> getListOfSkills()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @return
   */
  public abstract Object getClient();

  public ExecutionTable getExecutionTable()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public List<Skill> addNewSkill(Skill newSkill)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public List<Recipe> addNewRecipe(Recipe newRecipe)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  public ExecutionTable setExecutionTable(ExecutionTable executionTable)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
