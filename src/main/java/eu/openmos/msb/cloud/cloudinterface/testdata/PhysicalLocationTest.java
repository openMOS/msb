/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

import eu.openmos.agentcloud.data.PhysicalLocation;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class PhysicalLocationTest
{

  public static PhysicalLocation getTestObject()
  {
    Date registeredTimestamp = new Date();

    return new PhysicalLocation(
            "PhysicalLocationReferenceFrameName", 1, 2, 3, 4, 5, 6, "PhysicalLocationProductId", registeredTimestamp);
  }

  public static List<PhysicalLocation> getTestList()
  {
    return new LinkedList<>(Arrays.asList(getTestObject()));
  }

}