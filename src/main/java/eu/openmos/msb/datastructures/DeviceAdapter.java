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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
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
public abstract class DeviceAdapter {

    protected int bd_id;
    
    protected boolean hasAgent;
    
    /*
   * device name || devices in the workstation and its data
     */
    protected SubSystem subSystem;
    protected Vertx vert;

    public DeviceAdapter() {
        subSystem = new SubSystem(); // will be depreceated
        vert = Vertx.vertx();
        hasAgent=false;
    }

    /**
     * @return the id
     */
    public int getId() {
        return bd_id;
    }
    
    public void setId(int ID) {
        this.bd_id=ID;
    }
    


    /**
     * @param name
     * @return the ServerTableMaps
     */
    public String getEquipmentStatusByName(String name) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param uniqueID
     * @return the ServerTableMaps
     */
    public String getEquipmentStatusByID(String uniqueID) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // ---------------------------------------------------------------------------------------------------------------- //
    /**
     *
     * @param system
     */
    public void setSubSystem(SubSystem system) {
        this.subSystem = system;
    }

    /**
     *
     * @return SubSystem
     */
    public SubSystem getSubSystem() {
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
    public Vertx getVertx() {
        return this.vert;
    }

    // ---------------------------------------------------------------------------------------------------------------- //
    /**
     *
     * @return
     */
    public List<Module> getListOfEquipments() {
        return this.subSystem.getInternalModules();
    }

    /**
     *
     * @param module
     */
    public void addEquipmentModule(Module module) {
        this.subSystem.getInternalModules().add(module);
    }

    /**
     *
     * @param deviceName
     * @return
     */
    public boolean removeEquipmentModule(String deviceName) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // ---------------------------------------------------------------------------------------------------------------- //
    /**
     *
     * @return
     */
    public List<Recipe> getListOfRecipes() {
        return this.subSystem.getRecipes();
    }

    /**
     *
     * @param newRecipe
     * @return
     */
    public List<Recipe> addNewRecipe(Recipe newRecipe) {
        this.subSystem.getRecipes().add(newRecipe);
        // TODO we should verify if the recipe was added correctly 
        return this.subSystem.getRecipes();
    }

    /**
     *
     * @param recipeId
     * @return
     */
    public List<Recipe> removeRecipe(String recipeId) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // ---------------------------------------------------------------------------------------------------------------- //
    /**
     *
     * @return
     */
    public List<Skill> getListOfSkills() {
        return this.subSystem.getSkills();
    }

    /**
     *
     * @param newSkill
     * @return
     */
    public List<Skill> addNewSkill(Skill newSkill) {
        this.subSystem.getSkills().add(newSkill);
        // TODO we should verify if the skill was added correctly 
        return this.subSystem.getSkills();
    }

    /**
     *
     * @param skillId
     * @return
     */
    public List<Skill> removeSkill(String skillId) {
        throw new java.lang.UnsupportedOperationException("Not supported yet.");
    }

    // ---------------------------------------------------------------------------------------------------------------- //
    /**
     *
     * @return
     */
    public ExecutionTable getExecutionTable() {
        return this.subSystem.getExecutionTable();
    }

    /**
     *
     * @param executionTable
     * @return
     */
    public ExecutionTable setExecutionTable(ExecutionTable executionTable) {
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
    public boolean parseDNToObjects(Element deviceDescriptionNode, Element skillsDescriptionNode) {
        try {
            org.w3c.dom.Document deviceDescriptionDoc = new DOMOutputter().output(new org.jdom2.Document(deviceDescriptionNode));
            org.w3c.dom.Document skillDescriptionDoc = new DOMOutputter().output(new org.jdom2.Document(skillsDescriptionNode));

            //TEST
            subSystem.setUniqueId(ReadDeviceAdapterID(deviceDescriptionDoc)); //aml_id
            subSystem.setConnected(true);
            //
            subSystem.setSkills(ReadSkill(skillDescriptionDoc));
            
            subSystem.setManufacturer(ReadManufacturer(deviceDescriptionDoc));
            subSystem.setExecutionTable(ReadExecutionTable(deviceDescriptionDoc));
            subSystem.setInternaleModules(ReadModules(deviceDescriptionDoc));
            subSystem.setRecipes(ReadRecipes(deviceDescriptionDoc));
            
            subSystem.setType(ReadDeviceAdapterType(deviceDescriptionDoc));
                        
            subSystem.setState(MSBConstants.ADAPTER_STATE_READY);
            //verifyRecipeSkill(subSystem.getRecipes(), subSystem.getSkills());

            //Logger.getLogger(SubSystem.class.getName()).log(Level.SEVERE, null, "testing");

            return true;
        } catch (XPathExpressionException ex) {
            System.out.println("[ERROR] " + ex.getMessage());
        } catch (JDOMException ex) {
            Logger.getLogger(DeviceAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static void verifyRecipeSkill(List<Recipe> auxRecipes, List<Skill> auxSkills) {
        for (Recipe auxRecipe : auxRecipes) {
            for (Skill auxSkill : auxSkills) {
                if (auxRecipe.getSkill().getName().equals(auxSkill.getName())) {
                    auxRecipe.setSkill(auxSkill);
                    break;
                }
            }
        }
    }

    private static ExecutionTable ReadExecutionTable(org.w3c.dom.Document xmlDocument) throws XPathExpressionException {
        String query = "//DeviceAdapter/*/*/ExecutionTable/*[not(self::Type)][not(self::TaskExecutionTable)]"; //isto é o IDdo subsystem -> ExecutionTable ExecutionTable row 
        XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

        System.out.println("Elements " + nodeList.getLength());

        ExecutionTable execTable = new ExecutionTable();

        List<ExecutionTableRow> auxRowList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);

            if ("Path".equals(n.getNodeName())) {
                execTable.setName(n.getTextContent());
            } else if ("ID".equals(n.getNodeName())) {
                for (int j = 0; j < n.getChildNodes().getLength(); j++) {
                    Node IdChildNodes = n.getChildNodes().item(j);

                    if (IdChildNodes.getNodeName().equals("Value")) {
                        execTable.setUniqueId(IdChildNodes.getTextContent());
                    }
                }
            } else {
              String name1=n.getNodeName();
                NodeList execTableAux = n.getChildNodes();
                ExecutionTableRow execRow = new ExecutionTableRow();

                for (int j = 0; j < execTableAux.getLength(); j++) {
                    Node n2 = execTableAux.item(j);
                    String name=n2.getNodeName();
                    if (n2.getNodeName().equals("ID")) {
                        for (int k = 0; k < n2.getChildNodes().getLength(); k++) {
                            Node IdChildNodes = n2.getChildNodes().item(k);
                            if (IdChildNodes.getNodeName().equals("Value")) {
                              if (execRow.getUniqueId()== null){
                                execRow.setUniqueId(IdChildNodes.getTextContent());
                                break;
                              }
                            }
                        }
                    } else if (n2.getNodeName().equals("Recipe")) {
                      NodeList recipes = n2.getChildNodes();
                      for (int k = 0; k < recipes.getLength(); k++)
                      {
                        /*if (recipes.item(k).getNodeType() == Node.ELEMENT_NODE && !recipes.item(k).getNodeName().contains("Column")
                                    && !recipes.item(k).getNodeName().equals("Path") && !recipes.item(k).getNodeName().equals("Type")
                                    && !recipes.item(k).getNodeName().equals("ID")) {*/
                        if (recipes.item(k).getNodeType() == Node.ELEMENT_NODE && recipes.item(k).getNodeName().equals("ID"))
                        {
                          Node nID = recipes.item(k);
                          NodeList nIDChild = nID.getChildNodes();
                          for (int x = 0; x < nIDChild.getLength(); x++)
                          {
                            Node IdChildNodes = nIDChild.item(x);
                            if (IdChildNodes.getNodeName().equals("Value"))
                            {
                              execRow.setRecipeId(IdChildNodes.getTextContent());
                            }
                          }
                        }
                      }
                    } else if (n2.getNodeName().equals("Product"))
                    {
                      NodeList products = n2.getChildNodes();
                      for (int k = 0; k < products.getLength(); k++)
                      {
                        if (products.item(k).getNodeType() == Node.ELEMENT_NODE && products.item(k).getNodeName().equals("ID")
                                    /*&& !products.item(k).getNodeName().contains("Column")
                                    && !products.item(k).getNodeName().equals("Path") && !products.item(k).getNodeName().equals("Type")*/) {
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
                    } else if (n2.getNodeName().equals("NextRecipeToExecute")) {
                        NodeList nRtE = n2.getChildNodes();
                        for (int k = 0; k < nRtE.getLength(); k++) {
                            if (nRtE.item(k).getNodeType() == Node.ELEMENT_NODE && nRtE.item(k).getNodeName().equals("ID")
                                    /*&& !nRtE.item(k).getNodeName().contains("Column")
                                    && !nRtE.item(k).getNodeName().equals("Path") && !nRtE.item(k).getNodeName().equals("Type")*/) {

                                Node nID = nRtE.item(k);
                          NodeList nIDChild = nID.getChildNodes();
                          for (int x = 0; x < nIDChild.getLength(); x++)
                          {

                            Node IdChildNodes = nIDChild.item(x);
                            if (IdChildNodes.getNodeName().equals("Value"))
                            {
                              execRow.setNextRecipeId(IdChildNodes.getTextContent());
                            } else if (IdChildNodes.getNodeName().equals("Path"))
                            {
                              String ns = IdChildNodes.getAttributes().getNamedItem("ns").getNodeValue();
                              execRow.setNextRecipeIdPath(ns + ":" + IdChildNodes.getTextContent()); //CHECK THIS
                            }

                          }
                        }
                      }
                    } else if (n2.getNodeName().equals("ListOfPossibleRecipeChoices")) { //TODO WHEN THERE IS SOMETHING WITH DATA HERE
                        NodeList ListOfpossRchoices = n2.getChildNodes();
                        List<String> PossibleRC = new ArrayList<>();
                        for (int k = 0; k < ListOfpossRchoices.getLength(); k++) {
                            if (ListOfpossRchoices.item(k).getNodeType() == Node.ELEMENT_NODE && !ListOfpossRchoices.item(k).getNodeName().contains("Column")
                                    && !ListOfpossRchoices.item(k).getNodeName().equals("Path") && !ListOfpossRchoices.item(k).getNodeName().equals("Type")
                                    && !ListOfpossRchoices.item(k).getNodeName().equals("ID")) {

                                Node nID = ListOfpossRchoices.item(k);
                                NodeList nIDChild = nID.getChildNodes();
                                for (int x = 0; x < nIDChild.getLength(); x++) {
                                    if ("ID".equals(nIDChild.item(x).getNodeName())) {
                                        for (int z = 0; z < nIDChild.item(x).getChildNodes().getLength(); z++) {
                                            Node IdChildNodes = nIDChild.item(x).getChildNodes().item(z);
                                            if (IdChildNodes.getNodeName().equals("Value")) {
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

    private List<Recipe> ReadRecipes(org.w3c.dom.Document xmlDocument) throws XPathExpressionException {
        String query = "//DeviceAdapter/*/*/*[contains(name(),'Recipe')]";
        XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

        System.out.println("Elements " + nodeList.getLength());
        List<Recipe> recipeList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Recipe recipe = new Recipe();
            boolean gogogo = true;
            List<SkillRequirement> SRs = new ArrayList<>();
            List<KPISetting> KPIs = new ArrayList<>();
            Node n = nodeList.item(i);
            NodeList recipeChilds = n.getChildNodes();

            for (int j = 0; j < recipeChilds.getLength(); j++) {
                Node n2 = recipeChilds.item(j);
                if ("Path".equals(n2.getNodeName())) {
                    
                    String auxTest = n2.getAttributes().getNamedItem("ns").getNodeValue();
                    recipe.setInvokeObjectID(auxTest + ":" + n2.getTextContent());
                    
                    String[] temp = n2.getTextContent().split("/");
                    recipe.setName(temp[temp.length - 1]);
                    System.out.println("recipeName " + recipe.getName());
                } else if ("ID".equals(n2.getNodeName())) {
                    for (int t = 0; t < n2.getChildNodes().getLength(); t++) {
                        Node IdChildNodes = n2.getChildNodes().item(t);

                        if (IdChildNodes.getNodeName().equals("Value")) {
                            recipe.setUniqueId(IdChildNodes.getTextContent());
                        }
                    }
                } else {
                    //description
                    if ("description".equals(n2.getNodeName())) {
                        NodeList descChilds = n2.getChildNodes();
                        for (int k = 0; k < descChilds.getLength(); k++) {
                            Node descChild = descChilds.item(k);
                            if (descChild.getNodeName().equals("Value")) {
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
                        for (int z = 0; z < SkillReqs.getLength(); z++) {
                            Node skillReq = SkillReqs.item(z);
                            if (skillReq.getNodeName().equals("ID")) {
                                NodeList auxNodeList = skillReq.getChildNodes();
                                for (int index = 0; index < auxNodeList.getLength(); index++) {
                                    Node auxNode = auxNodeList.item(index);
                                    if (auxNode.getNodeName().equals("Value")) {
                                        auxSkillReq.setUniqueId(auxNode.getTextContent());
                                        System.out.println("SR ID: " + auxSkillReq.getUniqueId());
                                    }
                                }
                            } else if (skillReq.getNodeName().equals("name")) {
                                NodeList auxNodeList = skillReq.getChildNodes();
                                for (int index = 0; index < auxNodeList.getLength(); index++) {
                                    Node auxNode = auxNodeList.item(index);
                                    if (auxNode.getNodeName().equals("Value")) {
                                        auxSkillReq.setName(auxNode.getTextContent());
                                        System.out.println("SR Name: " + auxSkillReq.getName());
                                    }
                                }
                            } else if (skillReq.getNodeName().equals("InvokeSkill")) {
                                NodeList auxNodeList = skillReq.getChildNodes();
                                for (int index = 0; index < auxNodeList.getLength(); index++) {
                                    Node auxNode = auxNodeList.item(index);
                                    if (auxNode.getNodeName().equals("Path")) {
                                        //INVOKESKILL FOR SR IS HERE!
                                        //auxSkillReq.set
                                        //System.out.println("SR Name: " + auxSkillReq.getName());
                                    }
                                }
                            }
                        }
                        SRs.add(auxSkillReq);
                    } else if (n2.getNodeName().equals("Path_InformationPort")) {

                        NodeList auxNodeList = n2.getChildNodes();
                        for (int z = 0; z < auxNodeList.getLength(); z++) {
                            Node kpiNode = auxNodeList.item(z);
                            if (kpiNode.getNodeName().toLowerCase().contains("kpi")) {
                                System.out.println("KPI NAME: " + kpiNode.getNodeName());
                                KPISetting auxKPISetting = new KPISetting();

                                NodeList auxNodeList12 = kpiNode.getChildNodes();
                                for (int h = 0; h < auxNodeList12.getLength(); h++) {
                                    Node auxNode = auxNodeList12.item(h);

                                    if (auxNode.getNodeName().equals("ID")) {
                                        NodeList auxNodeList1 = auxNode.getChildNodes();
                                        for (int index = 0; index < auxNodeList1.getLength(); index++) {
                                            Node auxNode1 = auxNodeList1.item(index);
                                            if (auxNode1.getNodeName().equals("Value")) {
                                                auxKPISetting.setUniqueId(auxNode1.getTextContent());
                                                System.out.println("KPI ID: " + auxKPISetting.getUniqueId());
                                            }
                                        }
                                    } else if (auxNode.getNodeName().equals("name")) {
                                        NodeList auxNodeList1 = auxNode.getChildNodes();
                                        for (int index = 0; index < auxNodeList1.getLength(); index++) {
                                            Node auxNode1 = auxNodeList1.item(index);
                                            if (auxNode1.getNodeName().equals("Value")) {
                                                auxKPISetting.setName(auxNode1.getTextContent());
                                                System.out.println("KPI Name: " + auxKPISetting.getName());
                                            }
                                        }
                                    } else if (auxNode.getNodeName().equals("value")) {
                                        NodeList auxNodeList1 = auxNode.getChildNodes();
                                        for (int index = 0; index < auxNodeList1.getLength(); index++) {
                                            Node auxNode1 = auxNodeList1.item(index);
                                            if (auxNode1.getNodeName().equals("Path")) {
                                                int ns = Integer.parseInt(auxNode1.getAttributes().getNamedItem("ns").getNodeValue());
                                                //auxKPISetting.setPath(new NodeId(ns,auxNode1.getTextContent())); //CHANGE IT TO STRING NODE ID KPI SETTINGS
                                                auxKPISetting.setPath(auxNode1.getAttributes().getNamedItem("ns").getNodeValue().toString() + ":" + auxNode1.getTextContent()); //CHECK THIS!
                                                System.out.println("KPI path: " + auxKPISetting.getDescription());
                                            }
                                        }
                                    }
                                }
                                KPIs.add(auxKPISetting);
                            }
                        }
                    } else if (n2.getNodeName().equals("InvokeSkill")) {
                        NodeList auxNodeList = n2.getChildNodes();
                        for (int z = 0; z < auxNodeList.getLength(); z++) {
                            Node auxNode = auxNodeList.item(z);
                            if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path")) {
                                String auxTest = auxNode.getAttributes().getNamedItem("ns").getNodeValue();
                                recipe.setInvokeMethodID(auxTest + ":" + auxNode.getTextContent());
                            }
                        }
                    } else if(n2.getNodeName().equals("SkillState")){
                        NodeList auxNodeList = n2.getChildNodes();
                        for (int z = 0; z < auxNodeList.getLength(); z++) {
                            Node auxNode = auxNodeList.item(z);
                            if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Path")) {
                                int ns = Integer.parseInt(auxNode.getAttributes().getNamedItem("ns").getNodeValue());
                                recipe.setStatePath(auxNode.getAttributes().getNamedItem("ns").getNodeValue().toString() + ":" + auxNode.getTextContent()); //CHECK THIS!
                            }else if (auxNode.getNodeType() == Node.ELEMENT_NODE && auxNode.getNodeName().equals("Value")){
                                recipe.setState(auxNode.getTextContent());
                            }
                        }
                    }
                    else
              {
                if (gogogo)
                {
                  //get skill - first node with SR inside                                  
                    if (n2.getNodeType() == Node.ELEMENT_NODE)
                    {
                      for (Skill auxSkill : subSystem.getSkills())
                      {
                        String auxSkillName= auxSkill.getName();
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
            recipe.setKpiSettings(KPIs);
            recipe.setSkillRequirements(SRs);
            recipeList.add(recipe);
        }
        return recipeList;
    }

    private static List<Module> ReadModules(org.w3c.dom.Document xmlDocument) throws XPathExpressionException {
        String query = "//DeviceAdapter/*/*/*[Module][Equipment]";
        XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);

        System.out.println("Elements " + nodeList.getLength());
        List<Module> moduleList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Module module = new Module();
            Node n = nodeList.item(i);
            NodeList moduleChilds = n.getChildNodes();

            for (int j = 0; j < moduleChilds.getLength(); j++) {
                Node n2 = moduleChilds.item(j);
                if ("Path".equals(n2.getNodeName())) {
                    String[] temp = n2.getTextContent().split("/");
                    module.setName(temp[temp.length - 1]);
                    System.out.println("moduleName " + module.getName());
                } else if ("ID".equals(n2.getNodeName())) {
                    for (int t = 0; t < n2.getChildNodes().getLength(); t++) {
                        Node IdChildNodes = n2.getChildNodes().item(t);

                        if (IdChildNodes.getNodeName().equals("Value")) {
                            module.setUniqueId(IdChildNodes.getTextContent());
                        }
                    }
                } else {
                    //description
                    if ("description".equals(n2.getNodeName())) {
                        NodeList descChilds = n2.getChildNodes();
                        for (int k = 0; k < descChilds.getLength(); k++) {
                            Node descChild = descChilds.item(k);
                            if (descChild.getNodeName().equals("Value")) {
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

    private static List<Skill> ReadSkill(org.w3c.dom.Document xmlDocument) throws XPathExpressionException {
        //String query = "//Skills/*[contains(name(),'AtomicSkill') or contains(name(),'CompositeSkill')]/Type[not(contains(@namespace, 'openMOSSystemUnitClassLib'))]";
        String query = "//Skills/*[contains(name(),'AtomicSkill') or contains(name(),'CompositeSkill')]";
        XPath xPath = javax.xml.xpath.XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile(query).evaluate(xmlDocument, XPathConstants.NODESET);
        
        

        System.out.println("Elements " + nodeList.getLength());
        List<Skill> skillList = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            NodeList skillChilds = n.getChildNodes();

            for (int j = 0; j < skillChilds.getLength(); j++) {
                Node n2 = skillChilds.item(j);
                //System.out.println("***NODE NAME: " + n2.getNodeName());

                if (n2.getNodeType() == Node.ELEMENT_NODE && !n2.getNodeName().equals("Type") && !n2.getNodeName().equals("Path")) {
                    Skill auxSkill = new Skill();
                    List<SkillRequirement> auxReq = new ArrayList<>();
                    System.out.println("***SKILL NAME: " + n2.getNodeName());
                    auxSkill.setName(n2.getNodeName());
                    

                    NodeList auxChilds = n2.getChildNodes();
                    for (int k = 0; k < auxChilds.getLength(); k++) {
                        Node auxData = auxChilds.item(k);
                        String name=auxData.getNodeName();
                        if (auxData.getNodeType() == Node.ELEMENT_NODE) {
                            if (auxData.getNodeName().matches("SR(\\d).*")) //SR+um digito pelo menos
                            {
                                //SR
                                System.out.println("***SR NAME: " + auxData.getNodeName());
                                SkillRequirement auxSR = new SkillRequirement();
                                auxSR.setName(auxData.getNodeName());
                                auxReq.add(auxSR);
                            }else if(auxData.getNodeName().matches("ID")){
                              NodeList IDchilds = auxData.getChildNodes();
                              for(int z=0;z<IDchilds.getLength();z++){
                                String name2=IDchilds.item(z).getNodeName();
                                String value=IDchilds.item(z).getNodeValue();
                                String txtcntn=IDchilds.item(z).getTextContent();
                                
                                if(IDchilds.item(z).getNodeName().matches("Value")){
                                  auxSkill.setUniqueId(IDchilds.item(z).getTextContent());
                                  System.out.println("Skill TEM ID! :O "+IDchilds.item(z).getTextContent());
                                }
                              }
                              //System.out.println("Skill TEM ID! ");
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
                    if(auxSkill.getUniqueId()==null)
                      auxSkill.setUniqueId(UUID.randomUUID().toString());
                    
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

        NodeList nodeChilds = nodeList.item(0).getChildNodes();
            
        for (int i = 0; i < nodeChilds.getLength(); i++) {
            Node n = nodeChilds.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName() == "Value")
                return n.getTextContent();
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
    //String query1 = "//DeviceAdapter/*/*/*/Path[contains(@ns, 'TransportSystem')]";
    //String query2 = "//DeviceAdapter/*/*/*/Path[contains(@ns, 'WorkStation')]";
    
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
// ------------------------------------------------------------------------------------------------------------------ //
// ------------------------------------------------------------------------------------------------------------------ //
    /**
     *
     * @return
     */
    public abstract Object getClient();
}
