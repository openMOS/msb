/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.utilities;

import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Introsys
 */
public class Functions
{
  /**
   * 
   * @param toConvert
   * @return NodeId class
   */
  public static NodeId convertStringToNodeId(String toConvert)
  {
    try
    {
      int ns = Integer.parseInt(toConvert.split(":")[0]);
      String identifier = toConvert.substring(toConvert.indexOf(":") + 1);
      return new NodeId(ns, identifier);
    } catch (Exception ex)
    {
      return null;
    }
  }
  
  public static String readOPCNodeToString(OpcUaClient client, NodeId node)
  {
    try
    {
      return client.readValue(0, TimestampsToReturn.Neither, node).get().getValue().getValue().toString();
    } catch (InterruptedException | ExecutionException ex)
    {
      System.out.println("ERROR reading node: " + ex);
      return "";
    }
  }
  
  public static void writeNode(OpcUaClient client, NodeId node, String value)
    {
        Variant v = new Variant(value);
        DataValue dv = new DataValue(v, null, null);
        client.writeValue(node, dv);
    }
  
  public static String XMLtoString(String path)
   {
       try
       {
           File fXmlFile = new File(path);
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = null;
           dBuilder = dbFactory.newDocumentBuilder();
           org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

           StringWriter sw = new StringWriter();
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
           transformer.setOutputProperty(OutputKeys.METHOD, "xml");
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
           transformer.transform(new DOMSource((org.w3c.dom.Node) doc), new StreamResult(sw));
           return sw.toString();
       } catch (Exception ex)
       {
           throw new RuntimeException("Error converting to String", ex);
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
  
}
