/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.msb.services.rest.data.ExecutionTableRowHelper;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.msb.datastructures.DACManager;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/executiontables")
public class ExecutionTableController
{

  private final Logger logger = Logger.getLogger(ExecutionTableController.class.getName());

  /**
   * Returns the full execution table given its unique identifier. Fills the execution table view page (slide 8 of 34).
   *
   * @return detail of execution table
   *
   * @param uniqueId the unique id of the execution table
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}")
  public ExecutionTable getDetail(@PathParam("uniqueId") String uniqueId)
  {
    logger.debug("execution table getDetail - uniqueId = " + uniqueId);
    return DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable();
  }

  /**
   * Updates the whole execution table. Matches with the execution table update pages (slide 9 to 12 of 34).
   *
   * @param uniqueId
   * @param executionTable
   * @return updated execution table
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}")
  public ExecutionTable update(@PathParam("uniqueId") String uniqueId, ExecutionTable executionTable)
  {
    logger.debug("execution table update - uniqueId = " + uniqueId);
    logger.debug("execution table update - uniqueId to update = " + executionTable.getUniqueId());
    logger.debug("execution table update - full table to update = " + executionTable.toString());
    return DACManager.getInstance().getDeviceAdapter(uniqueId).setExecutionTable(executionTable);
  }

  /**
   * Insert the given row into the execution table. Matches with the execution table update pages (slide 9 to 12 of 34).
   *
   * @param uniqueId
   * @return updated execution table
   * @param rowToInsert the execution table row to insert in which position in which execution table
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}/newRow")
  public ExecutionTable insertRow(@PathParam("uniqueId") String uniqueId, ExecutionTableRowHelper rowToInsert)
  {
    logger.debug("execution table insertRow - uniqueId = " + uniqueId);
    logger.debug("execution table insertRow - uniqueId to update = " + rowToInsert.getExecutionTableId());
    logger.debug("execution table insertRow - position for insert = " + rowToInsert.getRowPosition());
    logger.debug("execution table insertRow - row to insert = " + rowToInsert.getRow());

    // TODO insert new line
    logger.debug("execution table insertRow - table updated - line inserted");
    DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable().getRows().add(rowToInsert.getRow());
    return DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable();
  }

  /**
   * Returns list of rows of the given execution table.
   *
   * @param uniqueId
   * @return list of execution table rows
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}/rows")
  public List<ExecutionTableRow> getRows(@PathParam("uniqueId") String uniqueId)
  {
    logger.debug("execution table getRows - uniqueId = " + uniqueId);
    return DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable().getRows();
  }

  /**
   * Returns selected row of the given execution table.
   *
   * @param executionTableRowId
   * @return one execution table row
   *
   * @param uniqueId the unique id of the execution table
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}/rows/{executionTableRowId}")
  public ExecutionTableRow getRow(
          @PathParam("uniqueId") String uniqueId,
          @PathParam("executionTableRowId") String executionTableRowId)
  {
    logger.debug("execution table getRow - uniqueId = " + uniqueId);
    logger.debug("execution table getRow - executionTableRowId = " + executionTableRowId);

    List<ExecutionTableRow> rows = DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable().getRows();
    for (ExecutionTableRow row : rows)
    {
      if (row.getUniqueId().equalsIgnoreCase(executionTableRowId))
      {
        return row;
      }
    }

    return null;
  }

  /**
   * Deletes selected row of the given execution table.
   *
   * @param executionTableRowId
   * @return updated execution table
   *
   * @param uniqueId unique id of the execution table
   */
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}/rows/{executionTableRowId}")
  public ExecutionTable deleteRow(
          @PathParam("uniqueId") String uniqueId,
          @PathParam("executionTableRowId") String executionTableRowId)
  {
    logger.debug("execution table deleteRow - executionTableId = " + uniqueId);
    logger.debug("execution table deleteRow - executionTableRowId = " + executionTableRowId);

    List<ExecutionTableRow> rows = DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable().getRows();
    for (ExecutionTableRow row : rows)
    {
      if (row.getUniqueId().equalsIgnoreCase(executionTableRowId))
      {
        rows.remove(row);
      }
    }

    return DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable();
  }

  /**
   * Updates selected row of the given execution table.
   *
   * @param executionTableRowId
   * @param rowToUpdate
   * @return updated execution table
   *
   * @param uniqueId unique id of the execution table
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{uniqueId}/rows/{executionTableRowId}")
  public ExecutionTable updateRow(
          @PathParam("executionTableId") String uniqueId,
          @PathParam("executionTableRowId") String executionTableRowId,
          ExecutionTableRow rowToUpdate)
  {
    logger.debug("execution table updateRow - executionTableId = " + uniqueId);
    logger.debug("execution table updateRow - executionTableRowId = " + executionTableRowId);
    logger.debug("execution table updateRow - rowToUpdate = " + rowToUpdate);

    return DACManager.getInstance().getDeviceAdapter(uniqueId).getExecutionTable().updateRow(executionTableRowId, rowToUpdate);
  }
}
