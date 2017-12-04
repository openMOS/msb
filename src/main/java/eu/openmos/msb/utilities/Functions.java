/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.utilities;

import java.io.File;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
}
