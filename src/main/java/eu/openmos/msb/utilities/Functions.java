/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
import javax.xml.transform.TransformerException;
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
    } catch (NumberFormatException ex)
    {
        System.out.println("ERROR convertStringToNodeId: " + ex.getMessage());
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
      System.out.println("ERROR reading node: " + ex.getMessage());
      return "";
    }
  }
  
    public static void writeNode(OpcUaClient client, NodeId node, String value)
    {
        Variant v = new Variant(value);
        DataValue dv = new DataValue(v, null, null);
        client.writeValue(node, dv);
    }
  
  public static String ClassToString(Object classToParse)
  {
    try
    {
      StringWriter sw = new StringWriter();
      javax.xml.bind.JAXBContext jc = JAXBContext.newInstance(classToParse.getClass());
      Marshaller jaxbMArshaller = jc.createMarshaller();
      jaxbMArshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMArshaller.marshal(classToParse, sw); //print to String
      String classString = sw.toString();
      return classString;
    } catch (JAXBException ex)
    {
      System.out.println("Error ClassToString: " + ex.getMessage());
    }
    return null;
  }

  public static Object StringToClass(String stringClass, Object classToParse)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;

    try
    {
      db = dbf.newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream(new StringReader(stringClass));

      org.w3c.dom.Document doc = db.parse(is);
      JAXBContext jc = JAXBContext.newInstance(classToParse.getClass());
      Unmarshaller unmar = jc.createUnmarshaller();
      classToParse = (Object) unmar.unmarshal(doc);

      return classToParse;
    } catch (IOException | JAXBException | ParserConfigurationException | SAXException ex)
    {
      System.out.println("Error parsing stringToClass: " + ex.getMessage());
    }
    return null;
  }
  
}
