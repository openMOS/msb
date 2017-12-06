package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import java.util.ArrayList;
import java.util.List;
import io.vertx.core.Vertx;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
import eu.openmos.model.ParameterSetting;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import io.vertx.core.VertxOptions;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.DOMOutputter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author andre
 */
public abstract class DeviceAdapter
{

  protected int bd_id;

  protected boolean hasAgent;

  /*
   * device name || devices in the workstation and its data
   */
  protected SubSystem subSystem;
  protected Vertx vert;

  public DeviceAdapter()
  {
    subSystem = new SubSystem(); // will be depreceated
    hasAgent = false;

    //initVertx();
    
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return bd_id;
  }

  public void setId(int ID)
  {
    this.bd_id = ID;
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

  public boolean getHasAgent()
  {
    return this.hasAgent;
  }

  public void setHasAgent(boolean hasAgent)
  {
    this.hasAgent = hasAgent;
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

      subSystem.setUniqueId(ReadDeviceAdapterID(deviceDescriptionDoc)); //aml_id
      subSystem.setConnected(true);
      subSystem.setSkills(ReadSkill(skillDescriptionDoc));
      subSystem.setManufacturer(ReadManufacturer(deviceDescriptionDoc));
      subSystem.setExecutionTable(ReadExecutionTable(deviceDescriptionDoc));
      subSystem.setInternaleModules(ReadModules(deviceDescriptionDoc));
      subSystem.setRecipes(ReadRecipes(deviceDescriptionDoc));
      
      for (Module module : subSystem.getInternalModules())
      {
          subSystem.getRecipes().addAll(module.getRecipes());
      }
      
      
      //Introsys DEMO: associate DAid to the recipe
      List<Recipe> recipes = subSystem.getRecipes();
      List<String> equipmentIds = new LinkedList<>();
      equipmentIds.add(subSystem.getUniqueId());
      for(int i=0; i<recipes.size(); i++){
        recipes.get(i).setEquipmentIds(equipmentIds);
      }
      
      /*for (Recipe recipe : subSystem.getRecipes())
      {
        recipe.getEquipmentIds().add(subSystem.getUniqueId());
      }*/

      subSystem.setType(ReadDeviceAdapterType(deviceDescriptionDoc));

      List<String> ReadDeviceAdapterState = ReadDeviceAdapterState(deviceDescriptionDoc);
      if (ReadDeviceAdapterState.size() == 2)
      {
        subSystem.setState(ReadDeviceAdapterState.get(1));
        subSystem.setStatePath(ReadDeviceAdapterState.get(0));
      }

      PECManager.getInstance().getExecutionMap().put(subSystem.getUniqueId(), new Semaphore(1));
      System.out.println("[SEMAPHORE] CREATED for " + subSystem.getName());

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

  private static ExecutionTable ReadExecutionTable(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*/ExecutionTable/*[not(self::Type)][not(self::TaskExecutionTable)][not(self::ExecutionTable)]"; //isto é o IDdo subsystem -> ExecutionTable ExecutionTable row 

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
      } else if ("ID".equals(n.getNodeName()))
      {
        for (int j = 0; j < n.getChildNodes().getLength(); j++)
        {
          Node IdChildNodes = n.getChildNodes().item(j);
          if (IdChildNodes.getNodeName().equals("Value"))
          {
            execTable.setUniqueId(IdChildNodes.getTextContent());
          }
        }
      } else
      {
        NodeList execTableAux = n.getChildNodes();
        ExecutionTableRow execRow = new ExecutionTableRow();

        for (int j = 0; j < execTableAux.getLength(); j++)
        {
          Node n2 = execTableAux.item(j);
          if (n2.getNodeName().equals("ID"))
          {
            for (int k = 0; k < n2.getChildNodes().getLength(); k++)
            {
              Node IdChildNodes = n2.getChildNodes().item(k);
              if (IdChildNodes.getNodeName().equals("Value"))
              {
                if (execRow.getUniqueId() == null)
                {
                  execRow.setUniqueId(IdChildNodes.getTextContent());
                  break;
                }
              }
            }
          } else if (n2.getNodeName().equals("Recipe"))
          {
            NodeList recipes = n2.getChildNodes();
            for (int k = 0; k < recipes.getLength(); k++)
            {
              if (recipes.item(k).getNodeType() == Node.ELEMENT_NODE && recipes.item(k).getNodeName().endsWith("_Recipe"))
              {
                Node nID = recipes.item(k);
                NodeList nIDChild = nID.getChildNodes();
                for (int x = 0; x < nIDChild.getLength(); x++)
                {
                  Node IdChildNodes = nIDChild.item(x);
                  if (IdChildNodes.getNodeName().equals("ID"))
                  {
                    NodeList realIDchildNodes = IdChildNodes.getChildNodes();
                    for (int wot = 0; wot < realIDchildNodes.getLength(); wot++)
                    {
                      if (realIDchildNodes.item(wot).getNodeName().equals("Value"))
                      {
                        execRow.setRecipeId(realIDchildNodes.item(wot).getTextContent());
                      }
                    }
                  }
                }
              }
            }
          } else if (n2.getNodeName().equals("Product"))
          {
            NodeList products = n2.getChildNodes();
            for (int k = 0; k < products.getLength(); k++)
            {
              if (products.item(k).getNodeType() == Node.ELEMENT_NODE && products.item(k).getNodeName().equals("ID") /*&& !products.item(k).getNodeName().contains("Column")
                                    && !products.item(k).getNodeName().equals("Path") && !products.item(k).getNodeName().equals("Type")*/)
              {
                Node nID = products.item(k);
                NodeList nIDChild = nID.getChildNodes();
                for (int x = 0; x < nIDChild.getLength(); x++)
                {
                  Node IdChildNodes = nIDChild.item(x);
                  if (IdChildNodes.getNodeName().equals("Value"))
                  {
                    execRow.setProductId(IdChildNodes.getTextContent());
                  }
                }
              }
            }
          } else if (n2.getNodeName().equals("NextRecipeToExecute"))
          {
            NodeList nRtE = n2.getChildNodes();
            for (int k = 0; k < nRtE.getLength(); k++)
            {
              if (nRtE.item(k).getNodeType() == Node.ELEMENT_NODE /*&& nRtE.item(k).getNodeName().equals("ID")*/ /*&& !nRtE.item(k).getNodeName().contains("Column")
                                    && !nRtE.item(k).getNodeName().equals("Path") && !nRtE.item(k).getNodeName().equals("Type")*/)
              {
                Node nID = nRtE.item(k);
                if (nID.getNodeName().endsWith("Recipe"))
                { //MASMEC
                  NodeList NextRecipeChildNodes = nID.getChildNodes();
                  for (int z = 0; z < NextRecipeChildNodes.getLength(); z++)
                  {
                    String NExtRecipenode = NextRecipeChildNodes.item(z).getNodeName();

                    if (NExtRecipenode.equals("ID"))
                    {
                      NodeList IDchildNodes = NextRecipeChildNodes.item(z).getChildNodes();
                      for (int id = 0; id < IDchildNodes.getLength(); id++)
                      {
                        if (IDchildNodes.item(id).getNodeName().equals("Value"))
                        {
                          String recipeid = IDchildNodes.item(id).getTextContent();
                          System.out.println("next recipe id: " + recipeid);
                          execRow.setNextRecipeId(IDchildNodes.item(id).getTextContent());
                        } else if (IDchildNodes.item(id).getNodeName().equals("Path"))
                        {
                          String ns = IDchildNodes.item(id).getAttributes().getNamedItem("ns").getNodeValue();
                          execRow.setNextRecipeIdPath(ns + ":" + IDchildNodes.item(id).getTextContent()); //CHECK THIS
                        }
                      }
                    }

                  }
                }
              }
            }
          } else if (n2.getNodeName().equals("ListOfPossibleRecipeChoices"))
          { //TODO WHEN THERE IS SOMETHING WITH DATA HERE
            NodeList ListOfpossRchoices = n2.getChildNodes();
            List<String> PossibleRC = new ArrayList<>();
            for (int k = 0; k < ListOfpossRchoices.getLength(); k++)
            {
              if (ListOfpossRchoices.item(k).getNodeType() == Node.ELEMENT_NODE && !ListOfpossRchoices.item(k).getNodeName().contains("Column")
                      && !ListOfpossRchoices.item(k).getNodeName().equals("Path") && !ListOfpossRchoices.item(k).getNodeName().equals("Type")
                      && !ListOfpossRchoices.item(k).getNodeName().equals("ID"))
              {
                Node nID = ListOfpossRchoices.item(k);
                NodeList nIDChild = nID.getChildNodes();
                for (int x = 0; x < nIDChild.getLength(); x++)
                {
                  if ("ID".equals(nIDChild.item(x).getNodeName()))
                  {
                    for (int z = 0; z < nIDChild.item(x).getChildNodes().getLength(); z++)
                    {
                      Node IdChildNodes = nIDChild.item(x).getChildNodes().item(z);
                      if (IdChildNodes.getNodeName().equals("Value"))
                      {
                        PossibleRC.add(IdChildNodes.getTextContent());
                      }
                    }
                  }
                }
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

  private List<Recipe> ReadRecipes(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*/*[contains(name(),'Recipe')]";
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
      List<ParameterSetting> PARAMETERS = new ArrayList<>();
      Node n = nodeList.item(i);
      NodeList recipeChilds = n.getChildNodes();

      for (int j = 0; j < recipeChilds.getLength(); j++)
      {
        Node n2 = recipeChilds.item(j);
        if ("Path".equals(n2.getNodeName()))
        {
          String auxTest = n2.getAttributes().getNamedItem("ns").getNodeValue();
          recipe.setInvokeObjectID(auxTest + ":" + n2.getTextContent());

          String[] temp = n2.getTextContent().split("/");
          recipe.setName(temp[temp.length - 1]);
          System.out.println("recipeName " + recipe.getName());
        } else if ("ID".equals(n2.getNodeName()))
        {
          for (int t = 0; t < n2.getChildNodes().getLength(); t++)
          {
            Node IdChildNodes = n2.getChildNodes().item(t);

            if (IdChildNodes.getNodeName().equals("Value"))
            {
              recipe.setUniqueId(IdChildNodes.getTextContent());
            }
          }
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
            auxSkillReq.setDescription(n2.getTextContent());
            for (int z = 0; z < SkillReqs.getLength(); z++)
            {
              Node skillReq = SkillReqs.item(z);
              if (skillReq.getNodeName().equals("ID"))
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
            //KPIs
          } else if (n2.getNodeName().endsWith("InformationPort"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node kpiNode = auxNodeList.item(z);
              if (kpiNode.getNodeName().toLowerCase().contains("kpi"))
              {
                System.out.println("KPI NAME: " + kpiNode.getNodeName());
                KPISetting auxKPISetting = new KPISetting();
                auxKPISetting.setName(kpiNode.getNodeName()); //MASMEC

                NodeList auxNodeList12 = kpiNode.getChildNodes();
                for (int h = 0; h < auxNodeList12.getLength(); h++)
                {
                  Node auxNode = auxNodeList12.item(h);

                  if (auxNode.getNodeName().equals("ID"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxKPISetting.setUniqueId(auxNode1.getTextContent());
                        System.out.println("KPI ID: " + auxKPISetting.getUniqueId());
                      }
                    }
                  } else if (auxNode.getNodeName().equals("value"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Path"))
                      {
                        int ns = Integer.parseInt(auxNode1.getAttributes().getNamedItem("ns").getNodeValue());
                        auxKPISetting.setPath(ns + ":" + auxNode1.getTextContent()); //CHECK THIS!
                        System.out.println("KPI path: " + auxKPISetting.getPath());
                      }
                    }
                  } else if (auxNode.getNodeName().equals("Unit"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxKPISetting.setUnit(auxNode1.getTextContent());
                        System.out.println("KPI Unit: " + auxKPISetting.getUnit());
                      }
                    }
                  }
                }
                KPIs.add(auxKPISetting);
              }
            }
          } else if (n2.getNodeName().endsWith("ParameterPort"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node parameterNode = auxNodeList.item(z);
              if (parameterNode.getNodeName().toLowerCase().endsWith("_parameter"))
              {
                System.out.println("PARAMETER NAME: " + parameterNode.getNodeName());
                ParameterSetting auxParameterSetting = new ParameterSetting();

                NodeList auxNodeList12 = parameterNode.getChildNodes();
                for (int h = 0; h < auxNodeList12.getLength(); h++)
                {
                  Node auxNode = auxNodeList12.item(h);
                  if (auxNode.getNodeName().equals("ID"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxParameterSetting.setUniqueId(auxNode1.getTextContent());
                        System.out.println("PARAMETER ID: " + auxParameterSetting.getUniqueId());
                      }
                    }
                  } else if (!auxNode.getNodeName().equals("Path") && !auxNode.getNodeName().equals("Type")
                          && !auxNode.getNodeName().toLowerCase().endsWith("parameter") && !auxNode.getNodeName().toLowerCase().endsWith("unit"))
                  {
                    auxParameterSetting.setName(auxNode.getNodeName());
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxParameterSetting.setValue(auxNode1.getTextContent());
                        System.out.println("PARAMETER value: " + auxParameterSetting.getValue());
                        break;
                      }
                    }
                  }
                }
                PARAMETERS.add(auxParameterSetting);
              }
            }
          } else if (n2.getNodeName().equals("InvokeSkill"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
              {
                String auxTest = auxNode.getAttributes().getNamedItem("ns").getNodeValue();
                recipe.setInvokeMethodID(auxTest + ":" + auxNode.getTextContent());
              }
            }
          } else if (n2.getNodeName().equals("SkillState"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
              {
                int ns = Integer.parseInt(auxNode.getAttributes().getNamedItem("ns").getNodeValue());
                recipe.setStatePath(ns + ":" + auxNode.getTextContent()); //CHECK THIS!
              } else if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Value"))
              {
                recipe.setState(auxNode.getTextContent());
              }
            }
          } else
          {
            if (gogogo)
            {
              //get skill - first node with SR inside
              if (n2.getNodeType() == Node.ELEMENT_NODE)
              {
                for (Skill auxSkill : subSystem.getSkills())
                {
                  if (n2.getNodeName().equals(auxSkill.getName()))
                  {
                    recipe.setSkill(auxSkill);
                    gogogo = false;
                    break;
                  }
                }
              }
            }
          }
        }
      }
      recipe.setParameterSettings(PARAMETERS);
      recipe.setKpiSettings(KPIs);
      recipe.setSkillRequirements(SRs);
      
      recipeList.add(recipe);
    }
    return recipeList;
  }

  private List<Module> ReadModules(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*/*[Module][Equipment]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Module> moduleList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Module module = new Module();
      List<Recipe> recipeList = new ArrayList<>();
      Node n = nodeList.item(i);
      NodeList moduleChilds = n.getChildNodes();

      for (int j = 0; j < moduleChilds.getLength(); j++)
      {
        Node n2 = moduleChilds.item(j);
        if ("Path".equals(n2.getNodeName()))
        {
          String[] temp = n2.getTextContent().split("/");
          module.setName(temp[temp.length - 1]);
          System.out.println("moduleName " + module.getName());
        } else if ("ID".equals(n2.getNodeName()))
        {
          for (int t = 0; t < n2.getChildNodes().getLength(); t++)
          {
            Node IdChildNodes = n2.getChildNodes().item(t);

            if (IdChildNodes.getNodeName().equals("Value"))
            {
              module.setUniqueId(IdChildNodes.getTextContent());
            }
          }
        } else if ("description".equals(n2.getNodeName()))
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
        else if(n2.getNodeName().toLowerCase().endsWith("_recipe"))
        {
            Recipe recipe = new Recipe();
      boolean gogogo = true;
      List<SkillRequirement> SRs = new ArrayList<>();
      List<KPISetting> KPIs = new ArrayList<>();
      List<ParameterSetting> PARAMETERS = new ArrayList<>();
      NodeList recipeChilds = n2.getChildNodes();

      for (int q = 0; q < recipeChilds.getLength(); q++)
      {
             Node nRecipe = recipeChilds.item(q);
        if ("Path".equals(nRecipe.getNodeName()))
        {
          String auxTest = nRecipe.getAttributes().getNamedItem("ns").getNodeValue();
          recipe.setInvokeObjectID(auxTest + ":" + nRecipe.getTextContent());

          String[] temp = nRecipe.getTextContent().split("/");
          recipe.setName(temp[temp.length - 1]);
          System.out.println("recipeName " + recipe.getName());
        } else if ("ID".equals(nRecipe.getNodeName()))
        {
          for (int t = 0; t < nRecipe.getChildNodes().getLength(); t++)
          {
            Node IdChildNodes = nRecipe.getChildNodes().item(t);

            if (IdChildNodes.getNodeName().equals("Value"))
            {
              recipe.setUniqueId(IdChildNodes.getTextContent());
            }
          }
        } else
        {
          //description
          if ("description".equals(nRecipe.getNodeName()))
          {
            NodeList descChilds = nRecipe.getChildNodes();
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
          } else if (nRecipe.getNodeName().matches(("SR(\\d).*")))//SR+um digito pelo menos
          {
            System.out.println("isto é um SR: " + nRecipe.getNodeName());
            NodeList SkillReqs = nRecipe.getChildNodes();
            SkillRequirement auxSkillReq = new SkillRequirement();
            auxSkillReq.setDescription(nRecipe.getTextContent());
            for (int z = 0; z < SkillReqs.getLength(); z++)
            {
              Node skillReq = SkillReqs.item(z);
              if (skillReq.getNodeName().equals("ID"))
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
            //KPIs
          } else if (nRecipe.getNodeName().endsWith("InformationPort"))
          {
            NodeList auxNodeList = nRecipe.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node kpiNode = auxNodeList.item(z);
              if (kpiNode.getNodeName().toLowerCase().contains("kpi"))
              {
                System.out.println("KPI NAME: " + kpiNode.getNodeName());
                KPISetting auxKPISetting = new KPISetting();
                auxKPISetting.setName(kpiNode.getNodeName()); //MASMEC

                NodeList auxNodeList12 = kpiNode.getChildNodes();
                for (int h = 0; h < auxNodeList12.getLength(); h++)
                {
                  Node auxNode = auxNodeList12.item(h);

                  if (auxNode.getNodeName().equals("ID"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxKPISetting.setUniqueId(auxNode1.getTextContent());
                        System.out.println("KPI ID: " + auxKPISetting.getUniqueId());
                      }
                    }
                  } else if (auxNode.getNodeName().toLowerCase().equals("value"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Path"))
                      {
                        int ns = Integer.parseInt(auxNode1.getAttributes().getNamedItem("ns").getNodeValue());
                        auxKPISetting.setPath(ns + ":" + auxNode1.getTextContent()); //CHECK THIS!
                        System.out.println("KPI path: " + auxKPISetting.getPath());
                      }
                    }
                  } else if (auxNode.getNodeName().equals("Unit"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxKPISetting.setUnit(auxNode1.getTextContent());
                        System.out.println("KPI Unit: " + auxKPISetting.getUnit());
                      }
                    }
                  }
                }
                KPIs.add(auxKPISetting);
              }
            }
          } else if (nRecipe.getNodeName().endsWith("ParameterPort"))
          {
            NodeList auxNodeList = nRecipe.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node parameterNode = auxNodeList.item(z);
              if (parameterNode.getNodeName().toLowerCase().endsWith("_parameter"))
              {
                System.out.println("PARAMETER NAME: " + parameterNode.getNodeName());
                ParameterSetting auxParameterSetting = new ParameterSetting();

                NodeList auxNodeList12 = parameterNode.getChildNodes();
                for (int h = 0; h < auxNodeList12.getLength(); h++)
                {
                  Node auxNode = auxNodeList12.item(h);

                  if (auxNode.getNodeName().equals("ID"))
                  {
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxParameterSetting.setUniqueId(auxNode1.getTextContent());
                        System.out.println("PARAMETER ID: " + auxParameterSetting.getUniqueId());
                      }
                    }
                  } else if (!auxNode.getNodeName().equals("Path") && !auxNode.getNodeName().equals("Type")
                          && !auxNode.getNodeName().toLowerCase().endsWith("parameter") && !auxNode.getNodeName().toLowerCase().endsWith("unit"))
                  {
                    auxParameterSetting.setName(auxNode.getNodeName());
                    NodeList auxNodeList1 = auxNode.getChildNodes();
                    for (int index = 0; index < auxNodeList1.getLength(); index++)
                    {
                      Node auxNode1 = auxNodeList1.item(index);
                      if (auxNode1.getNodeName().equals("Value"))
                      {
                        auxParameterSetting.setValue(auxNode1.getTextContent());
                        System.out.println("PARAMETER value: " + auxParameterSetting.getValue());
                      }
                    }
                  }
                }
                PARAMETERS.add(auxParameterSetting);
              }
            }
          } else if (nRecipe.getNodeName().equals("InvokeSkill"))
          {
            NodeList auxNodeList = nRecipe.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
              {
                String auxTest = auxNode.getAttributes().getNamedItem("ns").getNodeValue();
                recipe.setInvokeMethodID(auxTest + ":" + auxNode.getTextContent());
              }
            }
          } else if (nRecipe.getNodeName().equals("SkillState"))
          {
            NodeList auxNodeList = nRecipe.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
              {
                int ns = Integer.parseInt(auxNode.getAttributes().getNamedItem("ns").getNodeValue());
                recipe.setStatePath(ns + ":" + auxNode.getTextContent()); //CHECK THIS!
              } else if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Value"))
              {
                recipe.setState(auxNode.getTextContent());
              }
            }
          } else
          {
            if (gogogo)
            {
              //get skill - first node with SR inside
              if (nRecipe.getNodeType() == Node.ELEMENT_NODE)
              {
                for (Skill auxSkill : subSystem.getSkills())
                {
                  if (nRecipe.getNodeName().equals(auxSkill.getName()))
                  {
                    recipe.setSkill(auxSkill);
                    gogogo = false;
                    break;
                  }
                }
              }
            }
          }
        }
        }
      recipe.setParameterSettings(PARAMETERS);
      recipe.setKpiSettings(KPIs);
      recipe.setSkillRequirements(SRs);
      
      /*List<String> equipmentIds = new LinkedList<>();
      equipmentIds.add(module.getUniqueId());
      recipe.setEquipmentIds(equipmentIds); //Introsys DEMO*/
      
      recipeList.add(recipe);
        }
      }
      module.setRecipes(recipeList);
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

        if (n2.getNodeType() == Node.ELEMENT_NODE && !n2.getNodeName().equals("Type") && !n2.getNodeName().equals("Path"))
        {
          Skill auxSkill = new Skill();
          List<SkillRequirement> auxReq = new ArrayList<>();
          System.out.println("***SKILL NAME: " + n2.getNodeName());
          auxSkill.setName(n2.getNodeName());

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
              } else if (auxData.getNodeName().matches("ID"))
              {
                NodeList IDchilds = auxData.getChildNodes();
                for (int z = 0; z < IDchilds.getLength(); z++)
                {
                  if (IDchilds.item(z).getNodeName().matches("Value"))
                  {
                    auxSkill.setUniqueId(IDchilds.item(z).getTextContent());
                    System.out.println("Skill TEM ID! :O " + IDchilds.item(z).getTextContent());
                  }
                }
              }
            }
          }
          auxSkill.setSkillRequirements(auxReq);
          if (auxSkill.getUniqueId() == null)
          {
            auxSkill.setUniqueId(UUID.randomUUID().toString());
          }

          skillList.add(auxSkill);
        }
      }
    }

    List<Skill> indexToRemove = new ArrayList<>();

    for (int i = 0; i < skillList.size(); i++)
    {
      for (int j = i; j < skillList.size(); j++)
      {
        if (i == j)
        {
          continue;
        } else
        {
          if (skillList.get(i).getName().equals(skillList.get(j).getName()))
          {
            if (!indexToRemove.contains(skillList.get(j)))
            {
              indexToRemove.add(skillList.get(j));
            }
          }
        }
      }
    }
    skillList.removeAll(indexToRemove);

    return skillList;
  }

  private static String ReadManufacturer(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*/manufacturer/Type[contains(@namespace, 'manufacturer')]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());

    System.out.println("Elements " + nodeList.getLength());

    if (nodeList.getLength() > 0)
    {
      NodeList nodeChilds = nodeList.item(0).getChildNodes();

      for (int i = 0; i < nodeChilds.getLength(); i++)
      {
        Node n = nodeChilds.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName() == "Value")
        {
          return n.getTextContent();
        }
      }
    }

    return "";
  }

  private static String ReadDeviceAdapterID(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*/ID/Value";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());

    if (nodeList.getLength() > 0)
    {
      NodeList nodeChilds = nodeList.item(0).getChildNodes();
      if (nodeChilds.getLength() == 1)
      {
        return nodeChilds.item(0).getTextContent();
      } else
      {
        return "NOAMLID";
      }
    } else
    {
      return "NOAMLID";
    }

  }

  private static String ReadDeviceAdapterType(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query1 = "//DeviceAdapter/*/*/TransportSystem";
    String query2 = "//DeviceAdapter/*/*/WorkStation";

    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query1).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Type elements Transport: " + nodeList.getLength());

    NodeList nodeList2 = (NodeList) xPath.compile(query2).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Type elements Workstation: " + nodeList2.getLength());

    if (nodeList.getLength() > 0)
    {
      return "TransportSystem";
    } else if (nodeList2.getLength() > 0)
    {
      return "WorkStation";
    } else
    {
      return "UnknownType";
    }

  }

  private static List<String> ReadDeviceAdapterState(org.w3c.dom.Document xmlDocument) throws XPathExpressionException
  {
    String query1 = "//DeviceAdapter/*/*/DeviceAdapterState";

    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query1).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("State elements num: " + nodeList.getLength());
    NodeList childNodeList = nodeList.item(0).getChildNodes();

    List<String> results = new ArrayList<>();
    for (int i = 0; i < childNodeList.getLength(); i++)
    {
      if (childNodeList.item(i).getNodeName().equals("Value"))
      {
        results.add(childNodeList.item(i).getTextContent());
      } else if (childNodeList.item(i).getNodeName().equals("Path"))
      {
        String auxTest = childNodeList.item(i).getAttributes().getNamedItem("ns").getNodeValue();
        results.add(auxTest + ":" + childNodeList.item(i).getTextContent());
      }

    }
    return results;
  }
  
  public void initVertx()
  {
    try
    {
      String myIP = ConfigurationLoader.getMandatoryProperty("openmos.msb.ipaddress");
      VertxOptions options = new VertxOptions();
      options.setClustered(true).setClusterHost(myIP);
      Vertx.clusteredVertx(options, res ->
      {
        if (res.succeeded())
        {
          vert = res.result();
        } else
        {
          System.out.println("[DEVICE ADAPTER] vertx creation not succedeed");
        }
      });
    } catch (Exception ex)
    {
      System.out.println("Error trying to init vertx: " + ex.getMessage());
    }
  }
// ------------------------------------------------------------------------------------------------------------------ //
// ------------------------------------------------------------------------------------------------------------------ //

  /**
   *
   * @return
   */
  public abstract Object getClient();
}
