package eu.openmos.msb.utilities;

import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.ControlPort;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.InformationPort;
import eu.openmos.model.KPI;
import eu.openmos.model.Module;
import eu.openmos.model.PhysicalPort;
import eu.openmos.model.Product;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author valerio.gentile
 */
public class ProductUpload {
    private static final Logger logger = Logger.getLogger(ProductUpload.class.getName());

    public static void main(String[] args) throws Exception    
{
    /*
        List<SubSystem> subsystemsList = getMasmecSubSystems();
        logger.trace("FINAL SUBSYSTEMS LIST\n" + subsystemsList);
    */
        List<Product> productsList = getMasmecProducts();
        logger.info("FINAL PRODUCTS LIST\n" + productsList);
}
    
public static void main_OLD(String[] args) throws Exception    
{
    try 
    {

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse (new File("C:\\Users\\valerio.gentile\\Downloads\\VER4.aml"));

        // normalize text representation
        doc.getDocumentElement().normalize();
        logger.trace ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

        List<SubSystem> subsystemsList = loadSubSystems(doc);
        logger.trace("FINAL SUBSYSTEMS LIST\n" + subsystemsList);
        
    } catch (SAXParseException err) {
        logger.trace ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
        logger.trace(" " + err.getMessage ());
    } catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();
    } catch (Throwable t) {
        t.printStackTrace ();
    }                    
}

    public static List<SubSystem> getMasmecSubSystems()
    {
        List<SubSystem> subsystemsList = new LinkedList<>();
        
        try 
        {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // Document doc = docBuilder.parse (new File("C:\\Users\\valerio.gentile\\Downloads\\VER4.aml"));
            String amlFile = ConfigurationLoader.getMandatoryProperty("amlfile");
            logger.debug("amlFile = [" + amlFile + "]");                    
            Document doc = docBuilder.parse (new File(amlFile));

            // normalize text representation
            doc.getDocumentElement().normalize();
            logger.trace ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            subsystemsList = loadSubSystems(doc);
            logger.trace("FINAL SUBSYSTEMS LIST\n" + subsystemsList);

        } catch (SAXParseException err) {
            logger.trace ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            logger.trace(" " + err.getMessage ());
        } catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }                    
        
        return subsystemsList;
    }
    
    private static String getAMLFieldValue(Node field) throws Exception
    {
        String fieldValue = null;
        
        NodeList fieldAttributes = field.getChildNodes();
        for(int w=0; w<fieldAttributes.getLength() ; w++) 
        {
            Node fieldAttribute = fieldAttributes.item(w);            
            logger.trace("fieldAttribute detail " + w + "\n" + 
                    NodeToStringConverter.convert(fieldAttribute, true, true));

            if(fieldAttribute.getNodeType() == Node.ELEMENT_NODE &&
                    fieldAttribute.getNodeName().equalsIgnoreCase("InternalElement")) {
                
                Element firstElementDetail = (Element)fieldAttribute;
                fieldValue = firstElementDetail.getAttribute("Name");
                logger.trace("Name :"+ fieldValue);
                
                break;
            }
        }
        return fieldValue;        
    }

    public static String getAMLAttributeValue(Node field, String attributeName) throws Exception
    {
        return getAMLAttributeValue(field, attributeName, "Value");
    }
    
    public static String getAMLAttributeValue(Node field, String attributeName, String innerAttributeName) throws Exception
    {
        String fieldValue = null;
        logger.trace("---------------attributeName to search for -> " + attributeName);
        
        NodeList fieldAttributes = field.getChildNodes();
        logger.trace("---------------fieldAttributes.getLength() -> " + fieldAttributes.getLength());
        for(int w=0; w<fieldAttributes.getLength() ; w++) 
        {
            Node fieldAttribute = fieldAttributes.item(w);            
            logger.trace("----------------fieldAttribute detail " + w + "\n" + 
                    NodeToStringConverter.convert(fieldAttribute, true, true));

            if(fieldAttribute.getNodeType() == Node.ELEMENT_NODE &&
                    fieldAttribute.getNodeName().equalsIgnoreCase("Attribute")) 
            {
                Element firstElementDetail = (Element)fieldAttribute;
                fieldValue = firstElementDetail.getAttribute("Name");
                logger.trace("--------------------Name :"+ fieldValue);
                
                if (fieldValue.equalsIgnoreCase(attributeName))
                {

                    NodeList fieldAttributes2 = fieldAttribute.getChildNodes();
                    logger.trace("fieldAttributes2.getLength() -> " + fieldAttributes2.getLength());
                    for(int ww=0; ww<fieldAttributes2.getLength() ; ww++) 
                    {
                        Node fieldAttribute2 = fieldAttributes2.item(ww);            
                        logger.trace("----------------------fieldAttribute2 detail " + ww + "\n" + 
                            NodeToStringConverter.convert(fieldAttribute2, true, true));
                        logger.trace("-----------------------ci siamo node name " + fieldAttribute2.getNodeName());
                        logger.trace("------------------------ci siamo node value " + fieldAttribute2.getNodeValue());
                        if (fieldAttribute2.getNodeName().equalsIgnoreCase(innerAttributeName))
                        {
                    NodeList fieldAttributes3 = fieldAttribute2.getChildNodes();
                    logger.trace("------------------fieldAttributes3.getLength() -> " + fieldAttributes3.getLength());
                    for(int www=0; www<fieldAttributes3.getLength() ; www++) 
                    {
                        Node fieldAttribute3 = fieldAttributes3.item(www);            
                        logger.trace("----------------fieldAttribute3 detail " + www + "\n" + 
                            NodeToStringConverter.convert(fieldAttribute3, true, true));
                        
                        
                            fieldValue = fieldAttribute3.getNodeValue();
                            logger.trace("----------------fieldValue to return" + fieldValue + "\n");
                            return fieldValue;
                        }
                    }                    
                }
                }
                    
//                Element firstElementDetail = (Element)fieldAttribute;
//                fieldValue = firstElementDetail.getAttribute("Name");
//                logger.trace("Name :"+ fieldValue);
//                
//                if (fieldValue.equalsIgnoreCase(attributeName))
//                {
////                    return field.getNodeValue();
//                        String retFieldValue = firstElementDetail.getAttribute("Value");
//                        return retFieldValue;
//                    
//                }
//                
//                break;
            }
        }
        logger.trace("----------------end of the loop, fieldValue beeing returned" + fieldValue + "\n");
        return fieldValue;        
    }


    private static ExecutionTable getAMLExecutionTable(Node executionTableNode) throws Exception
    {
        String executionTableInString = NodeToStringConverter.convert(executionTableNode, true, true);
        logger.trace("EXECUTION TABLE\n" + executionTableInString);

        ExecutionTable executionTable = new ExecutionTable();        
        executionTable.setRegistered(new Date());
        // List<ExecutionTableRow> executionTableRows = executionTable.getRows();
        List<ExecutionTableRow> executionTableRows = new LinkedList<>();
        
        // execution table attributes print
        // printNodeAttributes(executionTableNode);

        Element executionTableElement = (Element)executionTableNode;
        
        String executionTableName = executionTableElement.getAttribute("Name");
        logger.trace("Execution table name :"+ executionTableName);
        
        executionTable.setUniqueId(executionTableName);
        executionTable.setName(executionTableName);
        executionTable.setDescription(executionTableName);
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;
/*        
        // works from the top of the file        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name='ExecutionTable']/"
                        + "InternalElement[starts-with(@Name,'Line')]"
        );
        // better, but works on the complete document
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name='ExecutionTable']/"
                        + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/ExecutionTable/Line']"
        );
*/        
        // the best so far, search from the current node down
        expr = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/ExecutionTable/Line']"
        );

        NodeList etLines = (NodeList) expr.evaluate(executionTableNode, XPathConstants.NODESET);            
        int totalLines = etLines.getLength();
        logger.trace("Total no of execution table lines -> " + totalLines);

        for(int z=0; z<totalLines ; z++) 
        {
            Node etLine = etLines.item(z);            
            logger.trace("et line " + z + "\n" + NodeToStringConverter.convert(etLine, true, true));
            
            ExecutionTableRow executionTableRow = getAMLExecutionTableRow(etLine);
            logger.trace("executionTableRow:\n" + executionTableRow);
            
            executionTableRows.add(executionTableRow);            
        }
        executionTable.setRows(executionTableRows);
        
        return executionTable;
    }
    
    private static void printNodeAttributes(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        attributes.getNamedItem("RefBaseSystemUnitPath");
        logger.trace("attributes\n" + attributes);
        int attrlen = attributes.getLength();
        logger.trace("attributes length\n" + attrlen);
        for (int k = 0; k < attrlen; k++)
        {
            Node item = attributes.item(k);                
            logger.trace("NODE ATTRIBUTE " + k + "\n" + item.getNodeName() + " - " + item.getNodeValue());
        }                
    }

    public static String getNodeAttributeValue(Node node, String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        // attributes.getNamedItem("RefBaseSystemUnitPath");
        logger.trace("attributes\n" + attributes);
        int attrlen = attributes.getLength();
        logger.trace("attributes length\n" + attrlen);
        return attributes.getNamedItem(attributeName).getNodeValue();
/*        
        for (int k = 0; k < attrlen; k++)
        {
            Node item = attributes.item(k);                
            logger.trace("NODE ATTRIBUTE " + k + "\n" + item.getNodeName() + " - " + item.getNodeValue());
        }  
*/        
    }

    private static ExecutionTableRow getAMLExecutionTableRow(Node etLine) throws Exception 
    {
        // execution table row
        ExecutionTableRow executionTableRow = new ExecutionTableRow();
        executionTableRow.setRegistered(new Date());       
        executionTableRow.setUniqueId(getAMLFieldValue(etLine.getParentNode()));
            
        NodeList etLineDetails = etLine.getChildNodes();
        for(int y=0; y<etLineDetails.getLength() ; y++) 
        {
            Node etLineDetail = etLineDetails.item(y);            
            logger.trace("etLine detail " + y + "\n" + NodeToStringConverter.convert(etLineDetail, true, true));

            if(etLineDetail.getNodeType() == Node.ELEMENT_NODE) {
                Element firstElement = (Element)etLineDetail;
                String attributeType = firstElement.getAttribute("RefBaseSystemUnitPath");
                logger.trace("RefBaseSystemUnitPath :"+ attributeType);

                // product -> RefBaseSystemUnitPath="openMOSSystemUnitClassLib/ExecutionTable/Line/ProductColumn"
                // recipe -> RefBaseSystemUnitPath="openMOSSystemUnitClassLib/ExecutionTable/Line/RecipeColumn"
                // next recipe -> RefBaseSystemUnitPath="openMOSSystemUnitClassLib/ExecutionTable/Line/NextRecipeToExecuteColumn"
                // possible choices -> RefBaseSystemUnitPath="openMOSSystemUnitClassLib/ExecutionTable/Line/ListOfPossibleRecipeChoicesColumn"           
                if (attributeType.equalsIgnoreCase("openMOSSystemUnitClassLib/ExecutionTable/Line/ProductColumn"))
                {
                    String product = getAMLFieldValue(etLineDetail);
                    logger.trace("PRODUCT  :"+ product);
                    executionTableRow.setProductId(product);
                }
                else if (attributeType.equalsIgnoreCase("openMOSSystemUnitClassLib/ExecutionTable/Line/RecipeColumn"))
                {
                    String recipe = getAMLFieldValue(etLineDetail);
                    logger.trace("RECIPE  :"+ recipe);
                    executionTableRow.setRecipeId(recipe);
                }
                else if (attributeType.equalsIgnoreCase("openMOSSystemUnitClassLib/ExecutionTable/Line/NextRecipeToExecuteColumn"))
                {
                    String nextRecipe = getAMLFieldValue(etLineDetail);
                    logger.trace("NEXT RECIPE  :"+ nextRecipe);
                    executionTableRow.setNextRecipeId(nextRecipe);
                }
                else if (attributeType.equalsIgnoreCase("openMOSSystemUnitClassLib/ExecutionTable/Line/ListOfPossibleRecipeChoicesColumn"))
                {
                    /*
                    String possibleRecipeChoices = getAMLFieldValue(etLineDetail);
                    logger.trace("POSSIBLE CHOICES  :"+ possibleRecipeChoices);
                    executionTableRow.setPossibleRecipeChoices(possibleRecipeChoices);
                    */ 
                    // da capire cosa arriva nll'aml, per ora non ho questo caso
                }

            }
        }
        return executionTableRow;
    }

    private static PhysicalPort getAMLPhysicalPort(Node physicalPortNode) throws Exception
    {
        String physicalPortInString = NodeToStringConverter.convert(physicalPortNode, true, true);
        logger.trace("PHYSICAL PORT\n" + physicalPortInString);

        PhysicalPort physicalPort = new PhysicalPort();        
        physicalPort.setRegistered(new Date());
        
        // physical port attributes print
        printNodeAttributes(physicalPortNode);
        
//        physicalPort.setUniqueId(getNodeAttributeValue(physicalPortNode, "ID"));
        physicalPort.setUniqueId(getNodeAttributeValue(physicalPortNode, "Name"));
        physicalPort.setName(getNodeAttributeValue(physicalPortNode, "Name"));
        physicalPort.setDescription(getNodeAttributeValue(physicalPortNode, "Name"));
        
        physicalPort.setDirection(getAMLAttributeValue(physicalPortNode, "Direction"));
        
        return physicalPort;
    }

    private static Recipe getAMLRecipe(Node recipeNode) throws XPathExpressionException, Exception {
        Recipe r = new Recipe();
        r.setRegistered(new Date());

        // recipe data
//        r.setUniqueId(getNodeAttributeValue(recipeNode, "ID"));
        r.setUniqueId(getNodeAttributeValue(recipeNode, "Name"));
        r.setName(getNodeAttributeValue(recipeNode, "Name"));        
        r.setDescription(getAMLAttributeValue(recipeNode, "description"));
        
        XPath xpath = XPathFactory.newInstance().newXPath();

/*        
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Port/InformationPort"
        XPathExpression exprInformationPort;
        exprInformationPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/InformationPort']"
        );

        NodeList informationPortLines = (NodeList) exprInformationPort.evaluate(recipeNode, XPathConstants.NODESET);            
        int totalInformationPortLines = informationPortLines.getLength();
        logger.trace("Total no of information ports -> " + totalInformationPortLines);

        List<InformationPort> lip = new LinkedList<>();
        for(int z=0; z<totalInformationPortLines ; z++) 
        {
            Node informationPortLine = informationPortLines.item(z);            
            logger.trace("informationPort line " + z + "\n" + NodeToStringConverter.convert(informationPortLine, true, true));
            
            InformationPort informationPort = getAMLInformationPort(informationPortLine);
            lip.add(informationPort);
        }
*/
        XPathExpression exprControlPort;
        exprControlPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/ControlPort']"
        );

        NodeList controlPortLines = (NodeList) exprControlPort.evaluate(recipeNode, XPathConstants.NODESET);            
        int totalControlPortLines = controlPortLines.getLength();
        logger.trace("Total no of control ports -> " + totalControlPortLines);

        for(int z=0; z<totalControlPortLines ; z++) 
        {
            Node controlPortLine = controlPortLines.item(z);            
            logger.trace("controlPort line " + z + "\n" + NodeToStringConverter.convert(controlPortLine, true, true));
            
            ControlPort controlPort = getAMLControlPort(controlPortLine);
            if (controlPort.getName().equalsIgnoreCase("outputcontrolport"))
            {
                r.setExecutedBySkillControlPort(controlPort);
                break;
            }
        }

        // skill requirements
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Requirement/SkillRequirement">
        XPathExpression exprSkillRequirement;
        exprSkillRequirement = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Requirement/SkillRequirement']"
        );

        NodeList skillreqLines = (NodeList) exprSkillRequirement.evaluate(recipeNode, XPathConstants.NODESET);            
        int totalSkillreqLines = skillreqLines.getLength();
        logger.trace("Total no of skillreq -> " + totalSkillreqLines);

        List<SkillRequirement> lsr = new LinkedList<>();
        for(int z=0; z<totalSkillreqLines ; z++) 
        {
            Node skillreqLine = skillreqLines.item(z);            
            logger.trace("skillreq line " + z + "\n" + NodeToStringConverter.convert(skillreqLine, true, true));
            SkillRequirement sr = getAMLSkillRequirement(skillreqLine);
            lsr.add(sr);
        }
        r.setSkillRequirements(lsr);
                    
        return r;
    }

    private static InformationPort getAMLInformationPort(Node informationPortNode) throws Exception {
        String physicalPortInString = NodeToStringConverter.convert(informationPortNode, true, true);
        logger.trace("PHYSICAL PORT\n" + physicalPortInString);

        InformationPort informationPort = new InformationPort();        
        informationPort.setRegistered(new Date());
        
        // physical port attributes print
        printNodeAttributes(informationPortNode);
        
//        informationPort.setUniqueId(getNodeAttributeValue(informationPortNode, "ID"));
        informationPort.setUniqueId(getNodeAttributeValue(informationPortNode, "Name"));
        informationPort.setName(getNodeAttributeValue(informationPortNode, "Name"));
        informationPort.setDescription(getNodeAttributeValue(informationPortNode, "Name"));
        
        informationPort.setDirection(getAMLAttributeValue(informationPortNode, "Direction"));
        
        // KPI
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/KPI"
        XPath xpath = XPathFactory.newInstance().newXPath();        
        XPathExpression exprKPI;
        exprKPI = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/KPI']"
        );

        NodeList kpiLines = (NodeList) exprKPI.evaluate(informationPortNode, XPathConstants.NODESET);            
        int kpiLinesCount = kpiLines.getLength();
        logger.trace("Total no of kpis -> " + kpiLinesCount);

        List<KPI> kpis = new LinkedList<>();
        for(int z=0; z<kpiLinesCount ; z++) 
        {
            Node kpiLine = kpiLines.item(z);            
            logger.trace("kpi line " + z + "\n" + NodeToStringConverter.convert(kpiLine, true, true));
            
            KPI kpi = getAMLKPI(kpiLine);

            kpis.add(kpi);
        }
        
        informationPort.setKpis(kpis);
        return informationPort;
    }

    private static ControlPort getAMLControlPort(Node controlPortNode) throws Exception {
        String controlPortInString = NodeToStringConverter.convert(controlPortNode, true, true);
        logger.trace("CONTROL PORT\n" + controlPortInString);

        ControlPort controlPort = new ControlPort();        
        controlPort.setRegistered(new Date());
        
        // control port attributes print
        printNodeAttributes(controlPortNode);
        
//        informationPort.setUniqueId(getNodeAttributeValue(informationPortNode, "ID"));
        controlPort.setUniqueId(getNodeAttributeValue(controlPortNode, "Name"));
        controlPort.setName(getNodeAttributeValue(controlPortNode, "Name"));
        controlPort.setDescription(getNodeAttributeValue(controlPortNode, "Name"));
        
        controlPort.setDirection(getAMLAttributeValue(controlPortNode, "Direction"));
        
        return controlPort;
    }

    private static KPI getAMLKPI(Node kpiNode) throws Exception {
        String kpiInString = NodeToStringConverter.convert(kpiNode, true, true);
        logger.trace("KPI\n" + kpiInString);

        KPI kpi = new KPI();        
        kpi.setRegistered(new Date());
        
        // physical port attributes print
        printNodeAttributes(kpiNode);
        
//        kpi.setUniqueId(getNodeAttributeValue(kpiNode, "ID"));
        kpi.setUniqueId(getNodeAttributeValue(kpiNode, "Name"));
        kpi.setName(getNodeAttributeValue(kpiNode, "Name"));
        kpi.setDescription(getNodeAttributeValue(kpiNode, "Name"));
        
//        kpi.setDirection(getAMLAttributeValue(kpiNode, "Direction"));

        return kpi;
    }

    private static Module getAMLModule(Node moduleNode) throws XPathExpressionException, Exception {
        Module m = new Module();
        m.setRegistered(new Date());

        // recipe data
//        m.setUniqueId(getNodeAttributeValue(moduleNode, "ID"));
        m.setUniqueId(getNodeAttributeValue(moduleNode, "Name"));
        m.setName(getNodeAttributeValue(moduleNode, "Name"));        
        
        m.setDescription(getAMLAttributeValue(moduleNode, "description", "Description"));        
        m.setManufacturer(getAMLAttributeValue(moduleNode, "manufacturer"));

        List<PhysicalPort> physicalPortsList = loadPhysicalPorts(moduleNode);
/*        
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exprPhysicalPorts;
        exprPhysicalPorts = xpath.compile("./InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/PhysicalPort']");
        NodeList physicalPorts = (NodeList) exprPhysicalPorts.evaluate(moduleNode, XPathConstants.NODESET);            
        int physicalPortsCount = physicalPorts.getLength();
        logger.trace("Total no of physical ports -> " + physicalPortsCount);
        List<PhysicalPort> physicalPortsList = new LinkedList<PhysicalPort>();
        for(int l=0; l<physicalPortsCount ; l++) 
        {
            Node internalelElement = physicalPorts.item(l);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("PHYSICAL PORT\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            PhysicalPort pp = getAMLPhysicalPort(internalelElement);
            logger.trace("FINAL PHYSICAL PORT " + l + "\n" + pp);
            physicalPortsList.add(pp);
        } 
        logger.trace("FINAL PHYSICAL PORTS LIST\n" + physicalPortsList);
*/

        m.setPhysicalPorts(physicalPortsList);
        
        return m;
    }

    private static SubSystem getAMLSubSystem(Node subsystemNode) throws Exception {
        SubSystem s = new SubSystem();
        s.setRegistered(new Date());
        
            String xmlInString = NodeToStringConverter.convert(subsystemNode, true, true);
            logger.trace("SUBSYSTEM\n" + xmlInString);
        
        NodeToStringConverter.convert(subsystemNode, true, true);

        s.setUniqueId(getNodeAttributeValue(subsystemNode, "Name"));
        s.setName(getNodeAttributeValue(subsystemNode, "Name"));    
        
        s.setExecutionTable(loadExecutionTable(subsystemNode));
        s.setInternalModules(loadModules(subsystemNode));
        
        s.setSkills(loadSkills(subsystemNode, s.getName()));
        
//        s.setRecipes(loadRecipes(subsystemNode));
        s.setRecipes(loadRecipes(subsystemNode, s.getName(), s.getSkills()));
        
        s.setPhysicalPorts(loadPhysicalPorts(subsystemNode));
        
        // not the best way
        if (s.getName().toLowerCase().startsWith("transport"))
                s.setType(Constants.DF_TRANSPORT);
        else
            s.setType(Constants.DF_RESOURCE);        
        
        return s;
    }

    private static ExecutionTable loadExecutionTable(Node parentNode) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

//        expr = xpath.compile("/CAEXFile/InstanceHierarchy/InternalElement[@Name='Transport1']/InternalElement[@Name='ExecutionTable']");
        expr = xpath.compile("./InternalElement[@Name='ExecutionTable']");
        NodeList nodelistInternalElements = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
        int totalInternalElements = nodelistInternalElements.getLength();

        logger.trace("Total no of executionTables found -> " + totalInternalElements);
        assert(totalInternalElements == 1);        

            Node internalelElement = nodelistInternalElements.item(0);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("EXECUTION TABLE\n" + xmlInString);

            ExecutionTable et = getAMLExecutionTable(internalelElement);
            logger.trace("FINAL EXECUTION TABLE\n" + et);        
        
            return et;
    }

    private static List<SubSystem> loadSubSystems(Document document) throws XPathExpressionException, Exception
    {
        // internal elements di transport1
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // SUBSYSTEMS
// RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Equipment/AssemblySystem"        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Equipment/AssemblySystem']/"
                        + "InternalElement"
        );        
        NodeList subsystems = (NodeList) expr.evaluate(document, XPathConstants.NODESET);            
        int subsystemsCount = subsystems.getLength();
        logger.trace("Total no of modules -> " + subsystemsCount);
        List<SubSystem> subsystemsList = new LinkedList<SubSystem>();
        for(int l=0; l<subsystemsCount ; l++) 
        {
            Node internalelElement = subsystems.item(l);            
//            Node internalelElement = subsystems.item(l).getParentNode();            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("SUBSYSTEM\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            SubSystem s = getAMLSubSystem(internalelElement);
            logger.trace("FINAL SUBSYSTEM " + l + "\n" + s);
            subsystemsList.add(s);
        } 
        logger.trace("FINAL SUBSYSTEMS LIST\n" + subsystemsList);
        
        return subsystemsList;        
    }

    private static List<Module> loadModules(Node parentNode) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // MODULES
/*        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@RefBaseSystemUnitPath='Masmec_SystemUnitClassLib/Conveyor' "
                        + "or RefBaseSystemUnitPath='Masmec_SystemUnitClassLib/Junction']"
        );        
*/
        expr = xpath.compile(
                "./InternalElement[@RefBaseSystemUnitPath='Masmec_SystemUnitClassLib/Conveyor' "
                        + "or @RefBaseSystemUnitPath='Masmec_SystemUnitClassLib/Junction']"
        );        

        NodeList modules = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
        int modulesCount = modules.getLength();
        logger.trace("Total no of modules -> " + modulesCount);
        List<Module> modulesList = new LinkedList<Module>();
        for(int l=0; l<modulesCount ; l++) 
        {
            Node internalelElement = modules.item(l);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("MODULE\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            Module m = getAMLModule(internalelElement);
            logger.trace("FINAL MODULE " + l + "\n" + m);
            modulesList.add(m);
        } 
        logger.trace("FINAL MODULES LIST\n" + modulesList);
        
        return modulesList;
    }
    
    private static List<Recipe> loadRecipes(Node parentNode, String subSystemId, List<Skill> skills) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // RECIPE
        // 'consent' = substring(., string-length(.) - string-length('consent') +1)
        // [@*[substring(., string-length() -8) = 'Copyright']]
        // non so perchè Recipe sia lungo 6 e io debba cercare "-5".... boh cmq funza
/*        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
*/
/*
        expr = xpath.compile(
                "./InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
*/
//RefBaseSystemUnitPath="MasmecSubSystem_SystemUnitClassLib/VirtualGluingStation/GluingSkill"
        List<Recipe> recipesList = new LinkedList<Recipe>();
        for (Skill skill : skills)
        {
            expr = xpath.compile(
                    "./InternalElement[@RefBaseSystemUnitPath='MasmecSubSystem_SystemUnitClassLib/" + subSystemId + "/" + skill.getUniqueId() + "']"
            );        
        
            NodeList recipes = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
            int recipesCount = recipes.getLength();
            logger.trace("Total no of recipes -> " + recipesCount);
            for(int l=0; l<recipesCount ; l++) 
            {
                Node internalelElement = recipes.item(l);            
                String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

                logger.trace("RECIPE\n" + xmlInString);

    //            ExecutionTable et = getAMLExecutionTable(internalelElement);
    //            logger.trace("FINAL EXECUTION TABLE\n" + et);        
                Recipe r = getAMLRecipe(internalelElement);
                r.setSkill(skill);
                logger.trace("FINAL RECIPE " + l + "\n" + r);
                recipesList.add(r);
            } 
        }

        logger.trace("FINAL RECIPES LIST\n" + recipesList);
        
        return recipesList;
    }

    private static List<Recipe> loadRecipes_OLD(Node parentNode) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // RECIPE
        // 'consent' = substring(., string-length(.) - string-length('consent') +1)
        // [@*[substring(., string-length() -8) = 'Copyright']]
        // non so perchè Recipe sia lungo 6 e io debba cercare "-5".... boh cmq funza
/*        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
*/
        expr = xpath.compile(
                "./InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
        NodeList recipes = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
        int recipesCount = recipes.getLength();
        logger.trace("Total no of recipes -> " + recipesCount);
        List<Recipe> recipesList = new LinkedList<Recipe>();
        for(int l=0; l<recipesCount ; l++) 
        {
            Node internalelElement = recipes.item(l);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("RECIPE\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            Recipe r = getAMLRecipe(internalelElement);
            logger.trace("FINAL RECIPE " + l + "\n" + r);
            recipesList.add(r);
        } 
        logger.trace("FINAL RECIPES LIST\n" + recipesList);
        
        return recipesList;
    }

    private static List<PhysicalPort> loadPhysicalPorts(Node parentNode) throws XPathExpressionException, Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exprPhysicalPorts;
        
        exprPhysicalPorts = xpath.compile("./InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/PhysicalPort']");
        NodeList physicalPorts = (NodeList) exprPhysicalPorts.evaluate(parentNode, XPathConstants.NODESET);            
        int physicalPortsCount = physicalPorts.getLength();
        logger.trace("Total no of physical ports -> " + physicalPortsCount);
        List<PhysicalPort> physicalPortsList = new LinkedList<PhysicalPort>();
        for(int l=0; l<physicalPortsCount ; l++) 
        {
            Node internalelElement = physicalPorts.item(l);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("PHYSICAL PORT\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            PhysicalPort pp = getAMLPhysicalPort(internalelElement);
            logger.trace("FINAL PHYSICAL PORT " + l + "\n" + pp);
            physicalPortsList.add(pp);
        } 
        logger.trace("FINAL PHYSICAL PORTS LIST\n" + physicalPortsList);
        
        return physicalPortsList;
    }

    private static List<Skill> loadSkills(Node parentNode, String subSystemName) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // SKILL
        // 'consent' = substring(., string-length(.) - string-length('consent') +1)
        // [@*[substring(., string-length() -8) = 'Copyright']]
        // non so perchè Recipe sia lungo 6 e io debba cercare "-5".... boh cmq funza
/*        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
*/
// SupportedRoleClass RefRoleClassPath="openMOSRoleClassLib/Skill"
/////////////                "./InternalElement/SupportedRoleClass[@RefRoleClassPath='openMOSRoleClassLib/Skill']"
//        expr = xpath.compile(
//                "./SystemUnit/[@RefBaseClassPath='openMOSSystemUnitClassLib/Skill/AtomicSkill']"
//                        + " | " +
//                        "./SystemUnit/[@RefBaseClassPath='openMOSSystemUnitClassLib/Skill/CompositeSkill']"
//        );        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "SystemUnitClassLib/"
                        + "SystemUnitClass[@Name='" + subSystemName + "']/"
                        + "SystemUnitClass[@RefBaseClassPath='openMOSSystemUnitClassLib/Skill/CompositeSkill']"
                        + "|"
                + "/CAEXFile/"
                        + "SystemUnitClassLib/"
                        + "SystemUnitClass[@Name='" + subSystemName + "']/"
                        + "SystemUnitClass[@RefBaseClassPath='openMOSSystemUnitClassLib/Skill/AtomicSkill']"
        );        

        NodeList skills = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
        int skillsCount = skills.getLength();
        logger.trace("Total no of skills -> " + skillsCount);
        List<Skill> skillsList = new LinkedList<Skill>();
        for(int l=0; l<skillsCount ; l++) 
        {
//             Node internalelElement = skills.item(l).getParentNode();            
            Node internalelElement = skills.item(l);            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("SKILL\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            Skill r = getAMLSkill(internalelElement);
            logger.trace("FINAL SKILL " + l + "\n" + r);
            skillsList.add(r);
        } 
        logger.trace("FINAL SKILLS LIST\n" + skillsList);
        
        return skillsList;
    }

    private static List<Skill> loadSkills_ORIGINAL(Node parentNode) throws XPathExpressionException, Exception
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        // SKILL
        // 'consent' = substring(., string-length(.) - string-length('consent') +1)
        // [@*[substring(., string-length() -8) = 'Copyright']]
        // non so perchè Recipe sia lungo 6 e io debba cercare "-5".... boh cmq funza
/*        
        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@Name='Transport1']/"
                        + "InternalElement[@Name[substring(., string-length() -5) = 'Recipe']]"
        );        
*/
// SupportedRoleClass RefRoleClassPath="openMOSRoleClassLib/Skill"
        expr = xpath.compile(
                "./InternalElement/SupportedRoleClass[@RefRoleClassPath='openMOSRoleClassLib/Skill']"
        );        
        NodeList skills = (NodeList) expr.evaluate(parentNode, XPathConstants.NODESET);            
        int skillsCount = skills.getLength();
        logger.trace("Total no of skills -> " + skillsCount);
        List<Skill> skillsList = new LinkedList<Skill>();
        for(int l=0; l<skillsCount ; l++) 
        {
            Node internalelElement = skills.item(l).getParentNode();            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("SKILL\n" + xmlInString);

//            ExecutionTable et = getAMLExecutionTable(internalelElement);
//            logger.trace("FINAL EXECUTION TABLE\n" + et);        
            Skill r = getAMLSkill(internalelElement);
            logger.trace("FINAL SKILL " + l + "\n" + r);
            skillsList.add(r);
        } 
        logger.trace("FINAL SKILLS LIST\n" + skillsList);
        
        return skillsList;
    }
    private static Skill getAMLSkill(Node skillNode) throws Exception {
        Skill s = new Skill();
        s.setRegistered(new Date());

        // skill data
//        r.setUniqueId(getNodeAttributeValue(recipeNode, "ID"));
        s.setUniqueId(getNodeAttributeValue(skillNode, "Name"));
        s.setName(getNodeAttributeValue(skillNode, "Name"));        
        s.setDescription(getAMLAttributeValue(skillNode, "description"));
        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Port/InformationPort"
        XPathExpression exprInformationPort;
        exprInformationPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/InformationPort']"
        );

        NodeList informationPortLines = (NodeList) exprInformationPort.evaluate(skillNode, XPathConstants.NODESET);            
        int totalInformationPortLines = informationPortLines.getLength();
        logger.trace("Total no of information ports -> " + totalInformationPortLines);

        List<InformationPort> lip = new LinkedList<>();
        for(int z=0; z<totalInformationPortLines ; z++) 
        {
            Node informationPortLine = informationPortLines.item(z);            
            logger.trace("informationPort line " + z + "\n" + NodeToStringConverter.convert(informationPortLine, true, true));
            
            InformationPort informationPort = getAMLInformationPort(informationPortLine);
            lip.add(informationPort);
        }
        s.setInformationPorts(lip);
        
        // skill requirements
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Requirement/SkillRequirement">
        XPathExpression exprSkillRequirement;
        exprSkillRequirement = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Requirement/SkillRequirement']"
        );

        NodeList skillreqLines = (NodeList) exprSkillRequirement.evaluate(skillNode, XPathConstants.NODESET);            
        int totalSkillreqLines = skillreqLines.getLength();
        logger.trace("Total no of skillreq -> " + totalSkillreqLines);

        List<SkillRequirement> lsr = new LinkedList<>();
        for(int z=0; z<totalSkillreqLines ; z++) 
        {
            Node skillreqLine = skillreqLines.item(z);            
            logger.trace("skillreq line " + z + "\n" + NodeToStringConverter.convert(skillreqLine, true, true));
            SkillRequirement sr = getAMLSkillRequirement(skillreqLine);
            lsr.add(sr);
        }
        s.setSkillRequirements(lsr);
                    
        XPathExpression exprControlPort;
        exprControlPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/ControlPort']"
        );

        NodeList controlPortLines = (NodeList) exprControlPort.evaluate(skillNode, XPathConstants.NODESET);            
        int totalControlPortLines = controlPortLines.getLength();
        logger.trace("Total no of control ports -> " + totalControlPortLines);

        List<ControlPort> lcp = new LinkedList<>();
        for(int z=0; z<totalControlPortLines ; z++) 
        {
            Node controlPortLine = controlPortLines.item(z);            
            logger.trace("controlPort line " + z + "\n" + NodeToStringConverter.convert(controlPortLine, true, true));
            
            ControlPort controlPort = getAMLControlPort(controlPortLine);
            lcp.add(controlPort);            
        }
        s.setControlPorts(lcp);
        
        return s;
    }

    private static SkillRequirement getAMLSkillRequirement(Node skillRequirementNode) throws Exception {
        SkillRequirement sr = new SkillRequirement();
        sr.setRegistered(new Date());

        // skill data
//        r.setUniqueId(getNodeAttributeValue(recipeNode, "ID"));
        // sr.setUniqueId(getNodeAttributeValue(skillRequirementNode, "Name"));
        sr.setUniqueId(getNodeAttributeValue(skillRequirementNode, "ID"));
        sr.setName(getNodeAttributeValue(skillRequirementNode, "Name"));        
        sr.setDescription(getAMLAttributeValue(skillRequirementNode, "description"));
        
/*        
        XPath xpath = XPathFactory.newInstance().newXPath();
        
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Port/InformationPort"
        XPathExpression exprInformationPort;
        exprInformationPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/InformationPort']"
        );

        NodeList informationPortLines = (NodeList) exprInformationPort.evaluate(skillNode, XPathConstants.NODESET);            
        int totalInformationPortLines = informationPortLines.getLength();
        logger.trace("Total no of information ports -> " + totalInformationPortLines);

        List<InformationPort> lip = new LinkedList<>();
        for(int z=0; z<totalInformationPortLines ; z++) 
        {
            Node informationPortLine = informationPortLines.item(z);            
            logger.trace("informationPort line " + z + "\n" + NodeToStringConverter.convert(informationPortLine, true, true));
            
            InformationPort informationPort = getAMLInformationPort(informationPortLine);
            lip.add(informationPort);
        }
        s.setInformationPorts(lip);
        
        // skill requirements
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Requirement/SkillRequirement">
        XPathExpression exprSkillRequirement;
        exprSkillRequirement = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Requirement/SkillRequirement']"
        );

        NodeList skillreqLines = (NodeList) exprSkillRequirement.evaluate(skillNode, XPathConstants.NODESET);            
        int totalSkillreqLines = skillreqLines.getLength();
        logger.trace("Total no of skillreq -> " + totalSkillreqLines);

        for(int z=0; z<totalSkillreqLines ; z++) 
        {
            SkillRequirement sr = new SkillRequirement();
            
            Node skillreqLine = skillreqLines.item(z);            
            logger.debug("skillreq line " + z + "\n" + NodeToStringConverter.convert(skillreqLine, true, true));
        }
                    
        XPathExpression exprControlPort;
        exprControlPort = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Port/ControlPort']"
        );

        NodeList controlPortLines = (NodeList) exprControlPort.evaluate(skillNode, XPathConstants.NODESET);            
        int totalControlPortLines = controlPortLines.getLength();
        logger.trace("Total no of control ports -> " + totalControlPortLines);

        List<ControlPort> lcp = new LinkedList<>();
        for(int z=0; z<totalControlPortLines ; z++) 
        {
            Node controlPortLine = controlPortLines.item(z);            
            logger.trace("controlPort line " + z + "\n" + NodeToStringConverter.convert(controlPortLine, true, true));
            
            ControlPort controlPort = getAMLControlPort(controlPortLine);
            lcp.add(controlPort);            
        }
        s.setControlPorts(lcp);
*/        
        return sr;
    }

    public static List<Product> getMasmecProducts()
    {
        return getProductsFromFile(ConfigurationLoader.getMandatoryProperty("amlfile"));
/*        
        List<Product> productsList = new LinkedList<>();
        
        try 
        {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            // Document doc = docBuilder.parse (new File("C:\\Users\\valerio.gentile\\Downloads\\VER4.aml"));
            String amlFile = ConfigurationLoader.getMandatoryProperty("amlfile");
            logger.debug("amlFile = [" + amlFile + "]");                    
            Document doc = docBuilder.parse (new File(amlFile));

            // normalize text representation
            doc.getDocumentElement().normalize();
            logger.trace ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            productsList = loadProducts(doc);
            logger.trace("FINAL PRODUCTS LIST\n" + productsList);

        } catch (SAXParseException err) {
            logger.trace ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            logger.trace(" " + err.getMessage ());
        } catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }                    
        
        return productsList;
*/
    }

    public static List<Product> getProductsFromFile(String fileFullName)
    {
        List<Product> productsList = new LinkedList<>();
        
        try 
        {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            
            // Document doc = docBuilder.parse (new File("C:\\Users\\valerio.gentile\\Downloads\\VER4.aml"));
            String amlFile = fileFullName;  // ConfigurationLoader.getMandatoryProperty("amlfile");
            logger.debug("amlFile = [" + amlFile + "]");                    
            Document doc = docBuilder.parse (new File(amlFile));

            // normalize text representation
            doc.getDocumentElement().normalize();
            logger.trace ("Root element of the doc is " + doc.getDocumentElement().getNodeName());

            productsList = loadProducts(doc);
            logger.trace("FINAL PRODUCTS LIST\n" + productsList);

        } catch (SAXParseException err) {
            logger.trace ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
            logger.trace(" " + err.getMessage ());
        } catch (SAXException e) {
            Exception x = e.getException ();
            ((x == null) ? e : x).printStackTrace ();
        } catch (Throwable t) {
            t.printStackTrace ();
        }                    
        
        return productsList;
    }

    private static List<Product> loadProducts(Document document) throws XPathExpressionException, Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr;

        expr = xpath.compile(
                "/CAEXFile/"
                        + "InstanceHierarchy/"
                        + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Part']"
        );        
        NodeList products = (NodeList) expr.evaluate(document, XPathConstants.NODESET);            
        int productsCount = products.getLength();
        logger.trace("Total no of products -> " + productsCount);
        List<Product> productsList = new LinkedList<>();
        for(int l=0; l<productsCount ; l++) 
        {
            Node internalelElement = products.item(l);            
//            Node internalelElement = subsystems.item(l).getParentNode();            
            String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);

            logger.trace("PRODUCT\n" + xmlInString);

            Product p = getAMLProduct(internalelElement);
            logger.trace("FINAL PRODUCT " + l + "\n" + p);
            productsList.add(p);
        } 
        logger.trace("FINAL PRODUCTS LIST\n" + productsList);
        
        return productsList;        
    }

    private static String getLinkID_RefA(Node internalLinkLine)
    {
        String recipeID = getNodeAttributeValue(internalLinkLine, "RefPartnerSideA").split(":")[0];
        return recipeID;
    }
    
    private static String getLinkID_RefB(Node internalLinkLine)
    {
        String recipeID = getNodeAttributeValue(internalLinkLine, "RefPartnerSideB").split(":")[0];
        return recipeID;
    }
    
    public static Product getAMLProduct(Node productNode) throws Exception {
        Product p = new Product();
        p.setRegistered(new Date());
        
            String xmlInString = NodeToStringConverter.convert(productNode, true, true);
            logger.debug("PRODUCT\n" + xmlInString);
        
        // NodeToStringConverter.convert(productNode, true, true);

        p.setUniqueId(getNodeAttributeValue(productNode, "ID"));
        p.setName(getNodeAttributeValue(productNode, "Name"));    
        // p.setDescription(getNodeAttributeValue(productNode, "description"));    
        p.setDescription(getAMLAttributeValue(productNode, "description", "Description"));

        
        // p.setSkillRequirements(loadSkillRequirements(productNode, p.getName()));
        // skill requirements
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Requirement/SkillRequirement">
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exprSkillRequirement;
        exprSkillRequirement = xpath.compile(
                        ".//InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Requirement/SkillRequirement']"
        );

        NodeList skillreqLines = (NodeList) exprSkillRequirement.evaluate(productNode, XPathConstants.NODESET);            
        int totalSkillreqLines = skillreqLines.getLength();
        logger.trace("Total no of skillreq -> " + totalSkillreqLines);

        List<SkillRequirement> lsr = new LinkedList<>();
        for(int z=0; z<totalSkillreqLines ; z++) 
        {
            Node skillreqLine = skillreqLines.item(z);            
            logger.trace("skillreq line " + z + "\n" + NodeToStringConverter.convert(skillreqLine, true, true));
            SkillRequirement sr = getAMLSkillRequirement(skillreqLine);
            
            XPathExpression exprInternalLink = xpath.compile(".//InternalLink");
            NodeList internalLinkLines = (NodeList) exprInternalLink.evaluate(skillreqLine, XPathConstants.NODESET);
            
            List<String> recipesIDs = new LinkedList<>();
            for(int i = 0; i < internalLinkLines.getLength(); i++)
            {
                Node internalLinkLine = internalLinkLines.item(i);
                String refA = getLinkID_RefA(internalLinkLine);
                String refB = getLinkID_RefB(internalLinkLine);
                if (refA.equals(sr.getUniqueId()))
                    recipesIDs.add(refB);
                else
                    recipesIDs.add(refA);
            }
            sr.setRecipeIDs(recipesIDs);
            lsr.add(sr);
        }
        p.setSkillRequirements(lsr);

        // precedences
        // RefBaseSystemUnitPath="openMOSSystemUnitClassLib/Requirement/SkillRequirement">
        // XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression exprLinks;
        exprLinks = xpath.compile(
                        ".//InternalLink"
        );

        NodeList linksLines = (NodeList) exprLinks.evaluate(productNode, XPathConstants.NODESET);            
        int totalLinksLines = linksLines.getLength();
        logger.debug("Total no of links -> " + totalLinksLines);

        for(int z=0; z<totalLinksLines ; z++) 
        {
            Node linksLine = linksLines.item(z);            
            logger.trace("links line " + z + "\n" + NodeToStringConverter.convert(linksLine, true, true));
            SRLink srl = getAMLSkillRequirementLink(linksLine);
            // logger.debug(srl);

            // find b
            SkillRequirement gotIt = null;
            for (SkillRequirement sk : p.getSkillRequirements())
            {
                if (sk.getUniqueId().equalsIgnoreCase(srl.getB()))
                {
                    gotIt = sk;
                    break;
                }
            }
            logger.debug("gotit B= " + gotIt);
            
            if (gotIt != null)
            {
                // TODO precedence given the sr id -> b comes before a
                SkillReqPrecedent srpGotIt = new SkillReqPrecedent(gotIt.getUniqueId(), gotIt.getName());
                        
                int iPos = -1;
                List<SkillRequirement> productSRList = p.getSkillRequirements();
                logger.debug("productSRList a t0 " + productSRList);

                for (SkillRequirement sk : productSRList)
                {
                    // find b
                    iPos++;

                    if (sk.getUniqueId().equalsIgnoreCase(srl.getA()))
                    {
                        logger.debug("trovato A, iPos = " + iPos);
//                        List<SkillRequirement> precedents = sk.getPrecedents();
                        List<SkillReqPrecedent> precedents = sk.getPrecedents();
                        logger.debug("precedents 1 " + precedents);
                        
                        if (precedents == null)
                            precedents = new LinkedList<>();
                        logger.debug("precedents 2 " + precedents);
                        
                        if (!precedents.contains(srpGotIt))
                            precedents.add(srpGotIt);
                        logger.debug("precedents 3 " + precedents);
                        sk.setPrecedents(precedents);
                        logger.debug("sk dopo 4 " + sk);

                        productSRList.set(iPos, sk);
                    }
                }
                logger.debug("productSRList a t1 " + productSRList);
                
                p.setSkillRequirements(productSRList);
            }
        }
        
        return p;
    }

    private static SRLink getAMLSkillRequirementLink(Node linksLine) throws Exception {
        SRLink srl = new SRLink();
        
            String xmlInString = NodeToStringConverter.convert(linksLine, true, true);
            logger.debug("SRLINK\n" + xmlInString);
        
        srl.setName(getNodeAttributeValue(linksLine, "Name"));
        
        String a = getNodeAttributeValue(linksLine, "RefPartnerSideA");
        String b = getNodeAttributeValue(linksLine, "RefPartnerSideB");
        
        int posa = a.indexOf(":");
        int posb = b.indexOf(":");
        
        srl.setA(a.substring(0, posa));
        srl.setB(b.substring(0, posb));
        
        logger.debug(srl);

        return srl;
    }
}

class SRLink 
{
    String name;
    String a;
    String b;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "SRLink{" + "name=" + name + ", a=" + a + ", b=" + b + '}';
    }
    
    
    
}