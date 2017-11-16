/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.utilities;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

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
}
