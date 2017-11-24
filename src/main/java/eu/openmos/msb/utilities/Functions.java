/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.utilities;

import java.util.concurrent.ExecutionException;
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
      System.out.println("DEU MERDA!");
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
}
