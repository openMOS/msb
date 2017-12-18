/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import _masmec.NodeToStringConverter;
import static _masmec.aml5.getAMLProduct;
import static _masmec.aml5.getNodeAttributeValue;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.KPISetting;
import eu.openmos.model.Module;
import eu.openmos.model.Product;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DecisionTree;
import eu.openmos.msb.datastructures.DeviceAdapter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Filipe
 */
public class main_jax
{
    static DecisionTree newTree;
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      String marsh = createString();      
      Recipe recipe = (Recipe) StringToClass(marsh, new Recipe());
      int i = 0;
    } catch (JAXBException ex)
    {
      System.out.println("ERROR " + ex.getMessage());
    }
  }

    private static String createString() throws JAXBException {
        SkillRequirement SR = new SkillRequirement();
        SR.setUniqueId("SR_ID");
        SR.setName("SR_NAME");
        List<SkillRequirement> SR_List = new ArrayList<>();
        SR_List.add(SR);
        Skill skill = new Skill();
        skill.setUniqueId("skill_ID");
        skill.setName("skill_NAME");
        Recipe recipe = new Recipe();
        recipe.setUniqueId("recipe_ID");
        recipe.setName("recipe_NAME");
        recipe.setSkill(skill);
        recipe.setSkillRequirements(SR_List);

        StringWriter sw = new StringWriter();
        javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(Recipe.class);
        Marshaller jaxbMArshaller = jc.createMarshaller();
        jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMArshaller.marshal(recipe, sw); //print to String
        //String excTablesString = XMLtoString("updateExecTables.xml"); //TODO: use outputStream instead of file!
        String recipeString = sw.toString();
        return recipeString;
    }

    private static Object StringToClass(String stringClass, Object classToParse) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;

        try {
            db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(stringClass));

            org.w3c.dom.Document doc = db.parse(is);
            JAXBContext jc = JAXBContext.newInstance(classToParse.getClass());
            Unmarshaller unmar = jc.createUnmarshaller();
            classToParse = (Object) unmar.unmarshal(doc);
            
            return classToParse;
        } catch (IOException | JAXBException | ParserConfigurationException | SAXException ex) {
            // handle ParserConfigurationException
            System.out.println("Error parsing stringToClass: " + ex);
        }
        return null;
    }

  private static List<Product> loadProducts(Document document) throws XPathExpressionException, Exception 
  {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Part']"
        );        
        NodeList products = (NodeList) expr.evaluate(document, XPathConstants.NODESET);            
        int productsCount = products.getLength();
        //logger.trace("Total no of products -> " + productsCount);
        List<Product> productsList = new LinkedList<>();
        for(int l=0; l<productsCount ; l++) 
        {
            Node internalelElement = products.item(l);            
//            Node internalelElement = subsystems.item(l).getParentNode();            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            //logger.trace("PRODUCT\n" + xmlInString);

            Product p = getAMLProduct(internalelElement);
            //logger.trace("FINAL PRODUCT " + l + "\n" + p);
            productsList.add(p);
        } 
        //logger.trace("FINAL PRODUCTS LIST\n" + productsList);
        
        
        return productsList;        
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
   
   public static void CreateTree(Product product){
       // Create instance of class DecisionTree

        newTree = new DecisionTree();

        // Generate tree

        generateTree(product);

        // Output tree

        System.out.println("\nOUTPUT DECISION TREE");
        System.out.println("====================");
        newTree.outputBinTree();

        // Query tree

        //queryTree();
   } 
   
      /* GENERATE TREE */

    static void generateTree(Product product) {
        System.out.println("\nGENERATE DECISION TREE");
        System.out.println("======================");
        List<Integer> rootIndex = new ArrayList<>();
        String currentNodeID = "";

        //check which one has no precedence ->rootnode       
        for (int i = 0; i < product.getSkillRequirements().size(); i++) {
            if (product.getSkillRequirements().get(i).getPrecedents() == null) {
                newTree.createRoot(product.getSkillRequirements().get(i).getUniqueId(), product.getSkillRequirements().get(i).getName());
                rootIndex.add(i);
                currentNodeID = product.getSkillRequirements().get(i).getUniqueId();
                break;
            }
        }
        //**********************************

        for (int i = 0; i < product.getSkillRequirements().size(); i++) {
            if (rootIndex.contains(i)) {
                continue;
            } else /*if(product.getSkillRequirements().get(i).getPrecedents()==null)*/ {
                for (int j = 0; j < product.getSkillRequirements().get(i).getPrecedents().size(); j++) {
                    if (product.getSkillRequirements().get(i).getPrecedents().get(j).getUniqueId() == currentNodeID) 
                    {
                        for (int z=0; z < product.getSkillRequirements().get(i).getRecipeIDs().size(); z++){
                        newTree.addYesNode(currentNodeID, product.getSkillRequirements().get(i).getUniqueId(),
                                product.getSkillRequirements().get(i).getName());
                        }
                    }
                }
                rootIndex.add(i);
            }
        }

    }

    /* QUERY TREE */
	
    static void queryTree() throws IOException {
        System.out.println("\nQUERY DECISION TREE");
        System.out.println("===================");
        newTree.queryBinTree();
    }
    
     private Boolean checkNextValidation(String nextRecipeID) {
        String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(nextRecipeID);
        if (Daid != null) {
            String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
            DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
            for (int i = 0; i < da.getExecutionTable().getRows().size(); i++) {
                if (da.getExecutionTable().getRows().get(i).getRecipeId() == nextRecipeID) {
                    String auxNextLKT1 = da.getExecutionTable().getRows().get(i).getNextRecipeId();
                    boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(auxNextLKT1);
                    return valid;
                }
            }
        }
        return false;
    }
    
    private String doStuff2(String recipeID) {

        //get execTable
        String Daid = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(recipeID);

        if (Daid != null) {
            String DA_name = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid);
            DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(DA_name);
            for (int i = 0; i < da.getExecutionTable().getRows().size(); i++) {
                if (da.getExecutionTable().getRows().get(i).getRecipeId() == recipeID) {
                    String NextRecipe = da.getExecutionTable().getRows().get(i).getNextRecipeId();
                    List<String> PossibleRecipeChoices = da.getExecutionTable().getRows().get(i).getPossibleRecipeChoices();
                    if (!PossibleRecipeChoices.isEmpty()) {
                        boolean valid = DatabaseInteraction.getInstance().getRecipeIdIsValid(NextRecipe);
                        if (valid) {
                            //yes
                            if (checkNextValidation(NextRecipe)) {
                                return NextRecipe;
                            }
                        } else {
                            for (int j = 0; j < PossibleRecipeChoices.size(); j++) {
                                String choice = PossibleRecipeChoices.get(j);
                                String Daid1 = DatabaseInteraction.getInstance().getDA_DB_IDbyRecipeID(choice);
                                if (Daid1 != null) {
                                    String DA_name1 = DatabaseInteraction.getInstance().getDeviceAdapterNameByDB_ID(Daid1);
                                    DeviceAdapter da1 = DACManager.getInstance().getDeviceAdapterbyName(DA_name1);
                                    for (int l = 0; l < da1.getExecutionTable().getRows().size(); l++) {
                                        if (da1.getExecutionTable().getRows().get(l).getRecipeId() == choice) {
                                            String NextRecipe1 = da1.getExecutionTable().getRows().get(l).getNextRecipeId();
                                            boolean valid1 = DatabaseInteraction.getInstance().getRecipeIdIsValid(NextRecipe1);
                                            if (valid1) {
                                                //yes
                                                if (checkNextValidation(NextRecipe1)) {
                                                    return NextRecipe1;
                                                } else {
                                                    return "";
                                                }
                                            } else {
                                                return "";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        return "";
    }

}
