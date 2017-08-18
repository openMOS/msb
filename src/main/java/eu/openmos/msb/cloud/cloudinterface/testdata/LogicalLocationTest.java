/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

import eu.openmos.agentcloud.data.LogicalLocation;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class LogicalLocationTest
{

  public static LogicalLocation getTestObject()
  {
    Date registeredTimestamp = new Date();

    return new LogicalLocation("LogicalLocationLocation", "LogicalLocationProductId", registeredTimestamp);
  }

  public static List<LogicalLocation> getTestList()
  {
    return new LinkedList<>(Arrays.asList(getTestObject()));
  }
}
