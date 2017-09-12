/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Vertx;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.StAXEventBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.SAXException;

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
  protected SubSystem subSystem;
  protected Vertx vert;

  public DeviceAdapter()
  {
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

  // ---------------------------------------------------------------------------------------------------------------- //
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

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @return
   */
  public List<Module> getListOfEquipments()
  {
    return this.subSystem.getInternalModules();
  }

  /**
   *
   * @param module
   */
  public void addEquipmentModule(Module module)
  {
    this.subSystem.getInternalModules().add(module);
  }

  /**
   *
   * @param deviceName
   * @return
   */
  public boolean removeEquipmentModule(String deviceName)
  {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @return
   */
  public List<Recipe> getListOfRecipes()
  {
    return this.subSystem.getRecipes();
  }

  /**
   *
   * @param newRecipe
   * @return
   */
  public List<Recipe> addNewRecipe(Recipe newRecipe)
  {
    this.subSystem.getRecipes().add(newRecipe);
    // TODO we should verify if the recipe was added correctly 
    return this.subSystem.getRecipes();
  }

  /**
   *
   * @param recipeId
   * @return
   */
  public List<Recipe> remvoeRecipe(String recipeId)
  {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @return
   */
  public List<Skill> getListOfSkills()
  {
    return this.subSystem.getSkills();
  }

  /**
   *
   * @param newSkill
   * @return
   */
  public List<Skill> addNewSkill(Skill newSkill)
  {
    this.subSystem.getSkills().add(newSkill);
    // TODO we should verify if the skill was added correctly 
    return this.subSystem.getSkills();
  }

  /**
   *
   * @param skillId
   * @return
   */
  public List<Skill> removeSkill(String skillId)
  {
    throw new java.lang.UnsupportedOperationException("Not supported yet.");
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @return
   */
  public ExecutionTable getExecutionTable()
  {
    return this.subSystem.getExecutionTable();
  }

  /**
   *
   * @param executionTable
   * @return
   */
  public ExecutionTable setExecutionTable(ExecutionTable executionTable)
  {
    this.subSystem.setExecutionTable(executionTable);
    return this.subSystem.getExecutionTable();
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @param deviceDescriptionNode
   * @param skillsDescriptionNode
   * @return
   */
  public boolean parseDNToObjects(Element deviceDescriptionNode, Element skillsDescriptionNode)
  {
    System.out.println("Starting DA Parser **********************");
    try
    {

      List<Skill> skills = new ArrayList<>();
      List<Module> internalModules = new ArrayList<>();
      List<Recipe> recipes = new ArrayList<>();
      List<ExecutionTableRow> eTRows = new ArrayList<>();

      // TEST ONE
      List<Element> list = deviceDescriptionNode.getChildren();
      System.out.println("Number of elements list " + list.size());


      
      
      // TODO GET ELEMENTS IN THE XML DOM USING XPATH AND JDOM2
      String query0 = "//DeviceAdapter/Transport1/manufacturer";
      String query1 = "//DeviceAdapter/*/manufacturer/Type[contains(@namespace, 'manufacturer')]";
      String query2 = "//*[contains(local-name(), 'Name')]";
      String query3 = "//manufacturer/Type[contains(namespace/text(),'manufacturer')]/node()";
      
      
      XPathFactory xpfac = XPathFactory.instance();
      XPathExpression<Element> xp = xpfac.compile(query3, Filters.element());
      List<Element> list2 = xp.evaluate(deviceDescriptionNode);
      System.out.println("Number of elements list2 " + list2.size());
      for (Element l : list2) {
        System.out.println("We have target " + l.getValue());
      }

    
//    ExecutionTableRow eTRow = new ExecutionTableRow(
//            uniqueId,
//            productId,
//            recipeId,
//            nextRecipeId,
//            possibleRecipeChoices
//    );
//
//    ExecutionTable executionTable = new ExecutionTable(
//            uniqueId,
//            name,
//            description,
//            eTRows,
//            registeredTimestamp
//    );
//
//    SubSystem ssNode = new SubSystem(
//            uniqueId,
//            name,
//            description,
//            executionTable,
//            true,
//            skills,
//            null, // list of PhysicalPort
//            recipes,
//            internalModules,
//            address,
//            status,
//            manufacturer,
//            null, // PhysicalLocation
//            null, // LogicalLocation
//            type,
//            registeredTimestamp
//    );
    } catch (Exception ex)
    {
      System.out.println("[ERROR] " + ex.getMessage());
    }
    return false;
  }

// ------------------------------------------------------------------------------------------------------------------ //
// ------------------------------------------------------------------------------------------------------------------ //
  /**
   *
   * @return
   */
  public abstract Object getClient();
}
