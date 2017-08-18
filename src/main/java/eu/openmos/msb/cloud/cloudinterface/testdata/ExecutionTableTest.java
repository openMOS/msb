/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.cloud.cloudinterface.testdata;

import eu.openmos.model.ExecutionTable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author valerio.gentile
 */
public class ExecutionTableTest
{

  public static List<ExecutionTable> getTestList(String parent, int itemsCount)
  {
    List<ExecutionTable> let = new LinkedList<ExecutionTable>();
    for (int i = 0; i < itemsCount; i++)
    {
      ExecutionTable et = getTestObject(parent, i);
      let.add(et);
    }
    return let;
  }

  public static ExecutionTable getTestObject(String parent, int rowsCount)
  {
    Date registeredTimestamp = new Date();

    ExecutionTable executionTable = new ExecutionTable(
            parent + " - ExecutionTableUniqueId",
            parent + " - ExecutionTableName",
            parent + " - ExecutionTableDescription",
            ExecutionTableRowTest.getTestList(parent, rowsCount),
            registeredTimestamp);

    return executionTable;
  }
}
