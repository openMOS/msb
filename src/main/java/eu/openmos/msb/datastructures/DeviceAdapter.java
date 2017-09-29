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
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

  /**
   *
   * @return SubSystem
   */
  public Vertx getVertx()
  {
    return this.vert;
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
  public List<Recipe> removeRecipe(String recipeId)
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
    try
    {
      org.w3c.dom.Document deviceDescriptionDoc = new DOMOutputter().output(new org.jdom2.Document(deviceDescriptionNode));
      org.w3c.dom.Document skillDescriptionDoc = new DOMOutputter().output(new org.jdom2.Document(skillsDescriptionNode));

      subSystem.setExecutionTable(ReadExecutionTable(deviceDescriptionDoc));
      subSystem.setInternaleModules(ReadModules(deviceDescriptionDoc));
      subSystem.setRecipes(ReadRecipes(deviceDescriptionDoc));
      subSystem.setSkills(ReadSkill(skillDescriptionDoc));

      verifyRecipeSkill(subSystem.getRecipes(), subSystem.getSkills());

      return true;
    } catch (XPathExpressionException ex)
    {
      System.out.println("[ERROR] " + ex.getMessage());
    } catch (JDOMException ex)
    {
      Logger.getLogger(DeviceAdapter.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
  }

  private static void verifyRecipeSkill(List<Recipe> auxRecipes, List<Skill> auxSkills)
  {
    for (Recipe auxRecipe : auxRecipes)
    {
      for (Skill auxSkill : auxSkills)
      {
        if (auxRecipe.getSkill().getName().equals(auxSkill.getName()))
        {
          auxRecipe.setSkill(auxSkill);
          break;
        }
      }
    }
  }

  private static ExecutionTable ReadExecutionTable(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/ExecutionTable/*[not(self::Type)][not(self::TaskExecutionTable)]"; //isto é o IDdo subsystem -> ExecutionTable ExecutionTable row 
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());

    ExecutionTable execTable = new ExecutionTable();
    List<ExecutionTableRow> auxRowList = new ArrayList<>();
    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node n = nodeList.item(i);

      if ("Path".equals(n.getNodeName()))
      {
        execTable.setName(n.getTextContent());
      } else
      {
        NodeList execTableAux = n.getChildNodes();
        ExecutionTableRow execRow = new ExecutionTableRow();
        execRow.setUniqueId(n.getNodeName());
        System.out.println("uniqueID " + execRow.getUniqueId());

        for (int j = 0; j < execTableAux.getLength(); j++)
        {
          Node n2 = execTableAux.item(j);
          if (n2.getNodeName().equals("Recipe"))
          {
            NodeList recipes = n2.getChildNodes();
            for (int k = 0; k < recipes.getLength(); k++)
            {
              if (recipes.item(k).getNodeType() == Node.ELEMENT_NODE && !recipes.item(k).getNodeName().contains("Column") && !recipes.item(k).getNodeName().equals("Path") && !recipes.item(k).getNodeName().equals("Type"))
              {
                String[] temp = recipes.item(k).getTextContent().split("/");
                execRow.setRecipeId(temp[temp.length - 1]);//.substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("recipeID " + execRow.getRecipeId());
              }
            }
          } else if (n2.getNodeName().equals("Product"))
          {
            NodeList products = n2.getChildNodes();
            for (int k = 0; k < products.getLength(); k++)
            {
              if (products.item(k).getNodeType() == Node.ELEMENT_NODE && !products.item(k).getNodeName().contains("Column") && !products.item(k).getNodeName().equals("Path") && !products.item(k).getNodeName().equals("Type"))
              {
                String[] temp = products.item(k).getTextContent().split("/");
                execRow.setProductId(temp[temp.length - 1]);//.substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("productID " + execRow.getProductId());
              }
            }
          } else if (n2.getNodeName().equals("NextRecipeToExecute"))
          {
            NodeList nRtE = n2.getChildNodes();
            for (int k = 0; k < nRtE.getLength(); k++)
            {
              if (nRtE.item(k).getNodeType() == Node.ELEMENT_NODE && !nRtE.item(k).getNodeName().contains("Column") && !nRtE.item(k).getNodeName().equals("Path") && !nRtE.item(k).getNodeName().equals("Type"))
              {
                String[] temp = nRtE.item(k).getTextContent().split("/");
                execRow.setNextRecipeId(temp[temp.length - 1]);//.substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("nrte " + execRow.getNextRecipeId());
              }
            }
          } else if (n2.getNodeName().equals("ListOfPossibleRecipeChoices"))
          {
            NodeList nRtE = n2.getChildNodes();
            List<String> PossibleRC = new ArrayList<>();
            for (int k = 0; k < nRtE.getLength(); k++)
            {
              if (nRtE.item(k).getNodeType() == Node.ELEMENT_NODE && !nRtE.item(k).getNodeName().contains("Column") && !nRtE.item(k).getNodeName().equals("Path") && !nRtE.item(k).getNodeName().equals("Type"))
              {

                String[] temp = nRtE.item(k).getTextContent().split("/");
                PossibleRC.add(temp[temp.length - 1]);//.substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("PossibleRC " + temp[temp.length - 1]);
              }
            }
            execRow.setPossibleRecipeChoices(PossibleRC);
          }
        }
        auxRowList.add(execRow);
      }
    }
    execTable.setRows(auxRowList);
    return execTable;
  }

  private static List<Recipe> ReadRecipes(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*[contains(name(),'Recipe')]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Recipe> recipeList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Recipe recipe = new Recipe();
      boolean gogogo = true;
      List<SkillRequirement> SRs = new ArrayList<>();
      List<KPISetting> KPIs = new ArrayList<>();
      Node n = nodeList.item(i);
      NodeList recipeChilds = n.getChildNodes();

      for (int j = 0; j < recipeChilds.getLength(); j++)
      {
        Node n2 = recipeChilds.item(j);
        if ("Path".equals(n2.getNodeName()))
        {
          String[] temp = n2.getTextContent().split("/");
          recipe.setName(temp[temp.length - 1]);
          System.out.println("recipeName " + recipe.getName());
          recipe.setUniqueId(UUID.randomUUID().toString());
        } else
        {
          //description
          if ("description".equals(n2.getNodeName()))
          {
            NodeList descChilds = n2.getChildNodes();
            for (int k = 0; k < descChilds.getLength(); k++)
            {
              Node descChild = descChilds.item(k);
              if (descChild.getNodeName().equals("Value"))
              {
                recipe.setDescription(descChild.getTextContent());
                System.out.println("recipeDescription " + recipe.getDescription());
                recipe.setValid(true); //let's supose all recipes on the adapter are valid upon registration?
              }
            }
            //SKRequirements
          } else if (n2.getNodeName().matches(("SR(\\d).*")))//SR+um digito pelo menos
          {
            System.out.println("isto é um SR: " + n2.getNodeName());
            NodeList SkillReqs = n2.getChildNodes();
            SkillRequirement auxSkillReq = new SkillRequirement();

            for (int z = 0; z < SkillReqs.getLength(); z++)
            {
              Node skillReq = SkillReqs.item(z);
              if (skillReq.getNodeName().equals("id"))
              {
                NodeList auxNodeList = skillReq.getChildNodes();
                for (int index = 0; index < auxNodeList.getLength(); index++)
                {
                  Node auxNode = auxNodeList.item(index);
                  if (auxNode.getNodeName().equals("Value"))
                  {
                    auxSkillReq.setUniqueId(auxNode.getTextContent());
                    System.out.println("SR ID: " + auxSkillReq.getUniqueId());
                  }
                }
              } else if (skillReq.getNodeName().equals("name"))
              {
                NodeList auxNodeList = skillReq.getChildNodes();
                for (int index = 0; index < auxNodeList.getLength(); index++)
                {
                  Node auxNode = auxNodeList.item(index);
                  if (auxNode.getNodeName().equals("Value"))
                  {
                    auxSkillReq.setName(auxNode.getTextContent());
                    System.out.println("SR Name: " + auxSkillReq.getName());
                  }
                }
              } else if (skillReq.getNodeName().equals("InvokeSkill"))
              {
                NodeList auxNodeList = skillReq.getChildNodes();
                for (int index = 0; index < auxNodeList.getLength(); index++)
                {
                  Node auxNode = auxNodeList.item(index);
                  if (auxNode.getNodeName().equals("Path"))
                  {
                    //INVOKESKILL FOR SR IS HERE!
                    //auxSkillReq.set
                    //System.out.println("SR Name: " + auxSkillReq.getName());
                  }
                }
              }
            }
            SRs.add(auxSkillReq);
          } else if (n2.getNodeName().equals("processTime") || n2.getNodeName().equals("meanProcessEnergyConsumption"))
          {
            System.out.println("KPI NAME: " + n2.getNodeName());
            KPISetting auxKPISetting = new KPISetting();

            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeName().equals("Path"))
              {
                String[] temp = auxNode.getTextContent().split("/");
                auxKPISetting.setName(temp[temp.length - 1]);//.substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("KPI Setting NAME: " + auxKPISetting.getName());
              } else if (auxNode.getNodeName().equals("Value"))
              {
                auxKPISetting.setValue(auxNode.getTextContent());
                System.out.println("KPI Setting VALUE: " + auxKPISetting.getValue());
              }
            }
            KPIs.add(auxKPISetting);
          } else if (n2.getNodeName().equals("InvokeSkill"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
              {
                recipe.setMsbProtocolEndpoint(auxNode.getTextContent());
              }
            }
          } else
          {
            if (gogogo)
            {
              NodeList auxNodeList = n2.getChildNodes();
              for (int z = 0; z < auxNodeList.getLength(); z++)
              {
                Node auxNode = auxNodeList.item(z);
                if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().matches("SR(\\d).*"))
                {
                  Skill auxSkill = new Skill();
                  auxSkill.setName(n2.getNodeName());
                  recipe.setSkill(auxSkill);
                  gogogo = false;
                  break;
                }
              }
            }
          }
        }
      }
      recipe.setKpiSettings(KPIs);
      recipe.setSkillRequirements(SRs);
      recipeList.add(recipe);
    }
    return recipeList;
  }

  private static List<Module> ReadModules(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*[contains(name(),'Conveyor') or contains(name(),'Junction')]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Module> moduleList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Module module = new Module();
      Node n = nodeList.item(i);

      NodeList moduleChilds = n.getChildNodes();

      for (int j = 0; j < moduleChilds.getLength(); j++)
      {
        Node n2 = moduleChilds.item(j);
        //System.out.println("***NODE NAME: " + n2.getNodeName());
        if ("Path".equals(n2.getNodeName()))
        {
          String[] temp = n2.getTextContent().split("/");
          module.setName(temp[temp.length - 1]);
          System.out.println("moduleName " + module.getName());
        } else
        {
          //description
          if ("description".equals(n2.getNodeName()))
          {
            NodeList descChilds = n2.getChildNodes();
            for (int k = 0; k < descChilds.getLength(); k++)
            {
              Node descChild = descChilds.item(k);
              if (descChild.getNodeName().equals("Value"))
              {
                module.setDescription(descChild.getTextContent());
                System.out.println("moduleDescription " + module.getDescription());
              }
            }
          }
        }
      }
      moduleList.add(module);
    }
    return moduleList;
  }

  private static List<Skill> ReadSkill(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//Skills/*[contains(name(),'AtomicSkill') or contains(name(),'CompositeSkill')]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Skill> skillList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node n = nodeList.item(i);
      NodeList skillChilds = n.getChildNodes();

      for (int j = 0; j < skillChilds.getLength(); j++)
      {
        Node n2 = skillChilds.item(j);
        //System.out.println("***NODE NAME: " + n2.getNodeName());

        if (n2.getNodeType() == Node.ELEMENT_NODE && !n2.getNodeName().equals("Type") && !n2.getNodeName().equals("Path"))
        {
          Skill auxSkill = new Skill();
          List<SkillRequirement> auxReq = new ArrayList<>();
          System.out.println("***SKILL NAME: " + n2.getNodeName());
          auxSkill.setName(n2.getNodeName());
          auxSkill.setUniqueId(UUID.randomUUID().toString());

          NodeList auxChilds = n2.getChildNodes();
          for (int k = 0; k < auxChilds.getLength(); k++)
          {
            Node auxData = auxChilds.item(k);
            if (auxData.getNodeType() == Node.ELEMENT_NODE)
            {
              if (auxData.getNodeName().matches("SR(\\d).*")) //SR+um digito pelo menos
              {
                //SR
                System.out.println("***SR NAME: " + auxData.getNodeName());
                SkillRequirement auxSR = new SkillRequirement();
                auxSR.setName(auxData.getNodeName());
                auxReq.add(auxSR);
              }
              /*
              NodeList auxChilds2 = auxData.getChildNodes();
              for (int z = 0; z < 1; z++)
              {

              }
               */
            }
          }
          auxSkill.setSkillRequirements(auxReq);
          skillList.add(auxSkill);
        }
      }
    }

    return skillList;
  }

// ------------------------------------------------------------------------------------------------------------------ //
// ------------------------------------------------------------------------------------------------------------------ //
  /**
   *
   * @return
   */
  public abstract Object getClient();
}
