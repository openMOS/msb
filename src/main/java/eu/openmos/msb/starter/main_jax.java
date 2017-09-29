/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Filipe
 */
public class main_jax
{

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      FileInputStream fileIS = new FileInputStream("aml.xml");
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document xmlDocument = builder.parse(fileIS);
      
      FileInputStream fileIS1 = new FileInputStream("C:\\Users\\Introsys\\Desktop\\XMLTest\\file2.xml");
      DocumentBuilderFactory builderFactory1 = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder1 = builderFactory1.newDocumentBuilder();
      Document xmlDocument1 = builder1.parse(fileIS1);
      
      SubSystem auxSystem = new SubSystem();  
      //auxSystem.setExecutionTable(ReadExecutionTable(xmlDocument));
      auxSystem.setRecipes(ReadRecipes(xmlDocument));
      //auxSystem.setInternaleModules(ReadModules(xmlDocument));
      //auxSystem.setSkills(ReadSkill(xmlDocument1));
      int i = 0;
    } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException ex)
    {
      System.out.println("ERROR " + ex.getMessage());
    }
  }

  private static ExecutionTable ReadExecutionTable(Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/ExecutionTable/*[not(self::Type)][not(self::TaskExecutionTable)]"; //isto é o IDdo subsystem -> ExecutionTable ExecutionTable row 
    XPath xPath = XPathFactory.newInstance().newXPath();
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
                execRow.setRecipeId(temp[temp.length - 1].substring(0, temp[temp.length - 1].indexOf("\n")));
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
                execRow.setProductId(temp[temp.length - 1].substring(0, temp[temp.length - 1].indexOf("\n")));
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
                execRow.setNextRecipeId(temp[temp.length - 1].substring(0, temp[temp.length - 1].indexOf("\n")));
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
                PossibleRC.add(temp[temp.length - 1].substring(0, temp[temp.length - 1].indexOf("\n")));
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

  private static List<Recipe> ReadRecipes(Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*[contains(name(),'Recipe')]";
    XPath xPath = XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Recipe> recipeList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Recipe recipe = new Recipe();
      List<SkillRequirement> SRs = new ArrayList<>();
      List<KPISetting> KPIs = new ArrayList<>();
      Node n = nodeList.item(i);
      NodeList recipeChilds = n.getChildNodes();

      for (int j = 0; j < recipeChilds.getLength(); j++)
      {
        Node n2 = recipeChilds.item(j);
        //System.out.println("***NODE NAME: " + n2.getNodeName());
        if ("Path".equals(n2.getNodeName()))
        {
          String[] temp = n2.getTextContent().split("/");
          recipe.setName(temp[temp.length - 1]);
          System.out.println("recipeName " + recipe.getName());
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
                String[] temp = n2.getTextContent().split("/");
                auxKPISetting.setName(temp[temp.length - 1].substring(0, temp[temp.length - 1].indexOf("\n")));
                System.out.println("KPI Setting NAME: " + auxKPISetting.getName());
              } else if (auxNode.getNodeName().equals("Value"))
              {
                auxKPISetting.setValue(auxNode.getTextContent());
                System.out.println("KPI Setting VALUE: " + auxKPISetting.getValue());
              }
            }
            KPIs.add(auxKPISetting);
          }
          else if (n2.getNodeName().equals("InvokeSkill"))
          {
            NodeList auxNodeList = n2.getChildNodes();
            for (int z = 0; z < auxNodeList.getLength(); z++)
            {
              Node auxNode = auxNodeList.item(z);
              if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path"))
                recipe.setMsbProtocolEndpoint(auxNode.getTextContent());
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

  private static List<Module> ReadModules(Document xmlDocument) throws XPathExpressionException
  {
    String query = "//DeviceAdapter/*/*[contains(name(),'Conveyor') or contains(name(),'Junction')]";
    XPath xPath = XPathFactory.newInstance().newXPath();
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
  
   private static List<Skill> ReadSkill(Document xmlDocument) throws XPathExpressionException
  {
    String query = "//Skills/*[contains(name(),'AtomicSkill') or contains(name(),'CompositeSkill')]";
    XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
    NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

    System.out.println("Elements " + nodeList.getLength());
    List<Skill> skillList = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Skill auxSkill = new Skill();
      Node n = nodeList.item(i);
      NodeList skillChilds = n.getChildNodes();

      for (int j = 0; j < skillChilds.getLength(); j++)
      {
        Node n2 = skillChilds.item(j);
        //System.out.println("***NODE NAME: " + n2.getNodeName());

        if (n2.getNodeType() == Node.ELEMENT_NODE && !n2.getNodeName().equals("Type") && !n2.getNodeName().equals("Path"))
        {
          System.out.println("***SKILL NAME: " + n2.getNodeName());
          auxSkill.setName(n2.getNodeName());
          auxSkill.setSkillRequirements(new ArrayList<>());
          //auxSkill.set

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
                auxSkill.getSkillRequirements().add(auxSR);

              }
              NodeList auxChilds2 = auxData.getChildNodes();
              for (int z = 0; z < 1; z++)
              {

              }
            }
          }
        }
      }
      skillList.add(auxSkill);
    }
    return skillList;
  }
}
