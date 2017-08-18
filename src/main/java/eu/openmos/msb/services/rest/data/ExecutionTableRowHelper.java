/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest.data;

import eu.openmos.model.ExecutionTableRow;

/**
 *
 * @author valerio.gentile
 */
public class ExecutionTableRowHelper
{

  private String executionTableId;
  private int rowPosition;
  private ExecutionTableRow row;

  public String getExecutionTableId()
  {
    return executionTableId;
  }

  public void setExecutionTableId(String executionTableId)
  {
    this.executionTableId = executionTableId;
  }

  public int getRowPosition()
  {
    return rowPosition;
  }

  public void setRowPosition(int rowPosition)
  {
    this.rowPosition = rowPosition;
  }

  public ExecutionTableRow getRow()
  {
    return row;
  }

  public void setRow(ExecutionTableRow row)
  {
    this.row = row;
  }

}
