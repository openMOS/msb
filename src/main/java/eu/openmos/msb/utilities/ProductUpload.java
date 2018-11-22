package eu.openmos.msb.utilities;

import eu.openmos.model.Product;
import eu.openmos.model.SkillReqPrecedent;
import eu.openmos.model.SkillRequirement;
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
public class ProductUpload
{

  private static final Logger logger = Logger.getLogger(ProductUpload.class.getName());

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
    for (int w = 0; w < fieldAttributes.getLength(); w++)
    {
      Node fieldAttribute = fieldAttributes.item(w);
      logger.trace("----------------fieldAttribute detail " + w + "\n"
              + NodeToStringConverter.convert(fieldAttribute, true, true));

      if (fieldAttribute.getNodeType() == Node.ELEMENT_NODE
              && fieldAttribute.getNodeName().equalsIgnoreCase("Attribute"))
      {
        Element firstElementDetail = (Element) fieldAttribute;
        fieldValue = firstElementDetail.getAttribute("Name");
        logger.trace("--------------------Name :" + fieldValue);

        if (fieldValue.equalsIgnoreCase(attributeName))
        {

          NodeList fieldAttributes2 = fieldAttribute.getChildNodes();
          logger.trace("fieldAttributes2.getLength() -> " + fieldAttributes2.getLength());
          for (int ww = 0; ww < fieldAttributes2.getLength(); ww++)
          {
            Node fieldAttribute2 = fieldAttributes2.item(ww);
            logger.trace("----------------------fieldAttribute2 detail " + ww + "\n"
                    + NodeToStringConverter.convert(fieldAttribute2, true, true));
            logger.trace("-----------------------ci siamo node name " + fieldAttribute2.getNodeName());
            logger.trace("------------------------ci siamo node value " + fieldAttribute2.getNodeValue());
            if (fieldAttribute2.getNodeName().equalsIgnoreCase(innerAttributeName))
            {
              NodeList fieldAttributes3 = fieldAttribute2.getChildNodes();
              logger.trace("------------------fieldAttributes3.getLength() -> " + fieldAttributes3.getLength());
              for (int www = 0; www < fieldAttributes3.getLength(); www++)
              {
                Node fieldAttribute3 = fieldAttributes3.item(www);
                logger.trace("----------------fieldAttribute3 detail " + www + "\n"
                        + NodeToStringConverter.convert(fieldAttribute3, true, true));

                fieldValue = fieldAttribute3.getNodeValue();
                logger.trace("----------------fieldValue to return" + fieldValue + "\n");
                return fieldValue;
              }
            }
          }
        }
      }
    }
    logger.trace("----------------end of the loop, fieldValue beeing returned" + fieldValue + "\n");
    return fieldValue;
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

  private static SkillRequirement getAMLSkillRequirement(Node skillRequirementNode) throws Exception
  {
    SkillRequirement sr = new SkillRequirement();
    sr.setRegistered(new Date());

    sr.setUniqueId(getNodeAttributeValue(skillRequirementNode, "ID"));
    sr.setName(getNodeAttributeValue(skillRequirementNode, "Name"));
    sr.setDescription(getAMLAttributeValue(skillRequirementNode, "description"));

    return sr;
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
      Document doc = docBuilder.parse(new File(amlFile));

      // normalize text representation
      doc.getDocumentElement().normalize();
      logger.trace("Root element of the doc is " + doc.getDocumentElement().getNodeName());

      productsList = loadProducts(doc);
      if (productsList.isEmpty())
      {
        productsList = loadProducts2(doc);
      }
      logger.trace("FINAL PRODUCTS LIST\n" + productsList);
      logger.debug("Number of products found: " + productsList.size());
    }
    catch (SAXParseException err)
    {
      logger.trace("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
      logger.trace(" " + err.getMessage());
    }
    catch (SAXException e)
    {
      Exception x = e.getException();
      ((x == null) ? e : x).printStackTrace();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
    }

    return productsList;
  }

  private static List<Product> loadProducts(Document document) throws XPathExpressionException, Exception
  {
    XPath xpath = XPathFactory.newInstance().newXPath();
    XPathExpression expr;

    expr = xpath.compile(
            "/CAEXFile/"
            + "InstanceHierarchy/"
            + "InternalElement/"
            + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Part']"
    );
    NodeList products = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
    int productsCount = products.getLength();
    //logger.trace("Total no of products -> " + productsCount);
    List<Product> productsList = new LinkedList<>();
    for (int l = 0; l < productsCount; l++)
    {
      Node internalelElement = products.item(l);
      //Node internalelElement = subsystems.item(l).getParentNode();            
      //String xmlInString = NodeToStringConverter.convert(internalelElement, true, true);
      //logger.trace("PRODUCT\n" + xmlInString);
      Product p = getAMLProduct(internalelElement);
      logger.trace("FINAL PRODUCT " + l + "\n" + p);
      productsList.add(p);
    }
    logger.trace("FINAL PRODUCTS LIST\n" + productsList);

    return productsList;
  }

  private static List<Product> loadProducts2(Document document) throws XPathExpressionException, Exception
  {
    XPath xpath = XPathFactory.newInstance().newXPath();
    XPathExpression expr;

    expr = xpath.compile(
            "/CAEXFile/"
            + "InstanceHierarchy/"
            + "InternalElement/"
            + "InternalElement[@RefBaseSystemUnitPath='openMOSSystemUnitClassLib/Part/Assembly']"
    );
    NodeList products = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
    int productsCount = products.getLength();
    logger.trace("Total no of products -> " + productsCount);
    List<Product> productsList = new LinkedList<>();
    for (int l = 0; l < productsCount; l++)
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

  public static Product getAMLProduct(Node productNode) throws Exception
  {
    Product p = new Product();
    p.setRegistered(new Date());

    String xmlInString = NodeToStringConverter.convert(productNode, true, true);
    //logger.debug("PRODUCT\n" + xmlInString);

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
    //logger.trace("Total no of skillreq -> " + totalSkillreqLines);

    List<SkillRequirement> lsr = new LinkedList<>();
    for (int z = 0; z < totalSkillreqLines; z++)
    {
      Node skillreqLine = skillreqLines.item(z);
      //logger.trace("skillreq line " + z + "\n" + NodeToStringConverter.convert(skillreqLine, true, true));
      SkillRequirement sr = getAMLSkillRequirement(skillreqLine);

      XPathExpression exprInternalLink = xpath.compile(".//InternalLink");
      NodeList internalLinkLines = (NodeList) exprInternalLink.evaluate(skillreqLine, XPathConstants.NODESET);

      List<String> recipesIDs = new LinkedList<>();
      for (int i = 0; i < internalLinkLines.getLength(); i++)
      {
        Node internalLinkLine = internalLinkLines.item(i);
        String refA = getLinkID_RefA(internalLinkLine);
        String refB = getLinkID_RefB(internalLinkLine);
        if (refA.equals(sr.getUniqueId()))
        {
          recipesIDs.add(refB);
        }
        else
        {
          recipesIDs.add(refA);
        }
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
    //logger.debug("Total no of links -> " + totalLinksLines);

    for (int z = 0; z < totalLinksLines; z++)
    {
      Node linksLine = linksLines.item(z);
      //logger.trace("links line " + z + "\n" + NodeToStringConverter.convert(linksLine, true, true));
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
      //logger.debug("gotit B= " + gotIt);

      if (gotIt != null)
      {
        // TODO precedence given the sr id -> b comes before a
        SkillReqPrecedent srpGotIt = new SkillReqPrecedent(gotIt.getUniqueId(), gotIt.getName());

        int iPos = -1;
        List<SkillRequirement> productSRList = p.getSkillRequirements();
        //logger.debug("productSRList a t0 " + productSRList);

        for (SkillRequirement sk : productSRList)
        {
          // find b
          iPos++;

          if (sk.getUniqueId().equalsIgnoreCase(srl.getA()))
          {
            //logger.debug("trovato A, iPos = " + iPos);
//                        List<SkillRequirement> precedents = sk.getPrecedents();
            List<SkillReqPrecedent> precedents = sk.getPrecedents();
            //logger.debug("precedents 1 " + precedents);

            if (precedents == null)
            {
              precedents = new LinkedList<>();
            }
            //logger.debug("precedents 2 " + precedents);

            if (!precedents.contains(srpGotIt))
            {
              precedents.add(srpGotIt);
            }
            //logger.debug("precedents 3 " + precedents);
            sk.setPrecedents(precedents);
            //logger.debug("sk dopo 4 " + sk);

            productSRList.set(iPos, sk);
          }
        }
        //logger.debug("productSRList a t1 " + productSRList);

        p.setSkillRequirements(productSRList);
      }
    }

    return p;
  }

  private static SRLink getAMLSkillRequirementLink(Node linksLine) throws Exception
  {
    SRLink srl = new SRLink();

    String xmlInString = NodeToStringConverter.convert(linksLine, true, true);
    //logger.debug("SRLINK\n" + xmlInString);

    srl.setName(getNodeAttributeValue(linksLine, "Name"));

    String a = getNodeAttributeValue(linksLine, "RefPartnerSideA");
    String b = getNodeAttributeValue(linksLine, "RefPartnerSideB");

    int posa = a.indexOf(":");
    int posb = b.indexOf(":");

    srl.setA(a.substring(0, posa));
    srl.setB(b.substring(0, posb));

    //logger.debug(srl);

    return srl;
  }
}

class SRLink
{

  String name;
  String a;
  String b;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getA()
  {
    return a;
  }

  public void setA(String a)
  {
    this.a = a;
  }

  public String getB()
  {
    return b;
  }

  public void setB(String b)
  {
    this.b = b;
  }

  @Override
  public String toString()
  {
    return "SRLink{" + "name=" + name + ", a=" + a + ", b=" + b + '}';
  }

}
