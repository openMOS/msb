/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.ExecutionTable;
import eu.openmos.model.ExecutionTableRow;
import eu.openmos.model.SubSystem;
import eu.openmos.model.testdata.ExecutionTableTest;
import eu.openmos.msb.services.rest.data.ExecutionTableRowHelper;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
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
public class ExecutionTableController {
    private final Logger logger = Logger.getLogger(ExecutionTableController.class.getName());
    
    /**
     * Returns the full execution table given its unique identifier.
     * Fills the execution table view page (slide 8 of 34). 
     * 
     * @return detail of execution table
     * 
     * @param uniqueId the unique id of the execution table
     * @return executiontable object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{executionTableId}")
    public ExecutionTable getDetail(@PathParam("executionTableId") String executionTableId) {
        logger.debug("execution table getDetail - executionTableId = " + executionTableId);
        return ExecutionTableTest.getTestObject(executionTableId, ThreadLocalRandom.current().nextInt(1, 10 + 1));
   }


    /**
     * Updates the whole execution table.
     * Matches with the execution table update pages (slide 9 to 12 of 34). 
     * 
     * return updated execution table
     * 
     * @param subSystemId   the execution table to update
     * @param executionTable 
     * @return executiontable updated object, or null if not existing
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{subSystemId}")
    public ExecutionTable update(@PathParam("subSystemId") String subSystemId, 
            ExecutionTable executionTable) {        
        
        logger.debug("execution table update - Update ExecutionTable from SubSystem: " + subSystemId);
        SubSystem subSystem = getSubSystemById(subSystemId);
        if(subSystem != null){
            subSystem.setExecutionTable(executionTable);       
        }        
        logger.debug(subSystem != null ? "execution update - ExecutionTable successfully updated" :
            "execution table update - can not find subSystem with Id: " + subSystemId);
        return subSystem != null ? subSystem.getExecutionTable() : null;
   }   

    /**
     * Insert the given row into the execution table.
     * Matches with the execution table update pages (slide 9 to 12 of 34). 
     * 
     * return updated execution table
     * 
     * @param subSystemId   unique identifier of the execution table to update
     * @param rowToInsert   the execution table row to insert in which position in which execution table
     * @return executiontable updated object, or null if not existing
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{subSystemId}/newRow")
    public ExecutionTable insertRow(@PathParam("subSystemId") String subSystemId, 
            ExecutionTableRowHelper rowToInsert) {
        
        logger.debug("execution table insert - Insert new row in ExecutionTable from SubSystem: " + subSystemId);
        SubSystem subSystem = getSubSystemById(subSystemId);
        if(subSystem != null) {
            subSystem.getExecutionTable().getRows()
                    .add(rowToInsert.getRowPosition(), rowToInsert.getRow());
        }        
        logger.debug(subSystem != null ? 
                "execution table insert - new row insert successfully" :
                "execution table insert - can not find subSystem with Id: " + subSystemId
            );
        return subSystem != null ? subSystem.getExecutionTable() : null;
   }   

    /**
     * Returns list of rows of the given execution table.
     * 
     * @return list of execution table rows
     * 
     * @param uniqueId the unique id of the execution table
     * @return list of executiontable rows object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{executionTableId}/rows")
    public List<ExecutionTableRow> getRows(@PathParam("executionTableId") String executionTableId) {
        logger.debug("execution table getRows - executionTableId = " + executionTableId);
        return ExecutionTableTest.getTestObject(executionTableId, ThreadLocalRandom.current().nextInt(1, 10 + 1)).getRows();
   }

    /**
     * Returns selected row of the given execution table.
     * 
     * @return one execution table row
     * 
     * @param uniqueId the unique id of the execution table
     * @param rowId the unique id of the execution table row
     * @return selected executiontable row object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{executionTableId}/rows/{executionTableRowId}")
    public ExecutionTableRow getRow(
            @PathParam("executionTableId") String executionTableId,
            @PathParam("executionTableRowId") String executionTableRowId) {
        logger.debug("execution table getRow - executionTableId = " + executionTableId);
        logger.debug("execution table getRow - executionTableRowId = " + executionTableRowId);
        
        List<ExecutionTableRow> rows = ExecutionTableTest.getTestObject(executionTableId, ThreadLocalRandom.current().nextInt(1, 10 + 1)).getRows();
        for (ExecutionTableRow row : rows)
            if (row.getUniqueId().equalsIgnoreCase(executionTableRowId))
                    return row;

        return null;
   }

    /**
     * Deletes selected row of the given execution table.
     * 
     * return updated execution table
     * 
     * param uniqueId  unique id of the execution table
     * @param executionTableRowId  unique id of the execution table row to be deleted
     * @return updated executiontable, or null if not existing
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{subSystemId}/rows/{executionTableRowId}")
    public ExecutionTable deleteRow(@PathParam("subSystemId") String subSystemId,
            @PathParam("executionTableRowId") String executionTableRowId) {
        
        logger.debug("execution table delete row - delete row with Id: " 
                + executionTableRowId + " , from executionTable of subSystem: " + subSystemId);
        SubSystem subSystem = getSubSystemById(subSystemId);
        if(subSystem != null) {
            List<ExecutionTableRow> toRemove = new ArrayList<>();
            for(ExecutionTableRow row : subSystem.getExecutionTable().getRows()){
                if(row.getUniqueId().equalsIgnoreCase(executionTableRowId)){
                    toRemove.add(row);
                }
            }
            subSystem.getExecutionTable().getRows().removeAll(toRemove);
        }
        logger.debug(subSystem != null ? 
                "execution table delete row - Row successfully deleted from execution table" : 
                "execution table delete row - can not find subSystem: " + subSystemId);
        return subSystem != null ? subSystem.getExecutionTable() : null;
   }

    /**
     * Updates selected row of the given execution table.
     * 
     * @return updated execution table
     * 
     * @param uniqueId  unique id of the execution table
     * @param rowId  unique id of the execution table row to be updated
     * @return updated executiontable, or null if not existing
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{executionTableId}/rows/{executionTableRowId}")
    public ExecutionTable updateRow(
            @PathParam("executionTableId") String executionTableId,
            @PathParam("executionTableRowId") String executionTableRowId,
            ExecutionTableRow rowToUpdate) {
        logger.debug("execution table updateRow - executionTableId = " + executionTableId);
        logger.debug("execution table updateRow - executionTableRowId = " + executionTableRowId);
        logger.debug("execution table updateRow - rowToUpdate = " + rowToUpdate);
        
        return ExecutionTableTest.getTestObject(executionTableId, 6);
    }
    
    private SubSystem getSubSystemById(String subSystemId) {
        for(SubSystem ss : (new SubSystemController()).getList()) {
            if(ss.getName().equalsIgnoreCase(subSystemId)){
                return ss;
            }
        }
        return null;
    }
}