/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.fakemsb.cloudinterface.test.EquipmentTest;
import eu.openmos.fakemsb.cloudinterface.test.SkillTest;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/equipments")
public class EquipmentController {
    private final Logger logger = Logger.getLogger(EquipmentController.class.getName());
    
    /**
     * Returns the equipment object given its unique identifier.
     * Fills the equipment view page (slide 15 of 34). 
     * 
     * @return detail of equipment
     * 
     * @param equipmentId the unique id of the equipment
     * @return equipment object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{equipmentId}")
    public Equipment getDetail(@PathParam("equipmentId") String equipmentId) {
        logger.debug("equipment getDetail - equipmentId = " + equipmentId);
        return EquipmentTest.getTestObject(equipmentId, -1);
   }

   /**
     * Returns the list of skills associated to the given equipment.
     * Fills the skills list view page (slide 16 of 34).
     * This method is exposed via a "/equipments/{equipmentId}/skills" service call.
     * 
     * @param equipmentId   equipmentId id, i.e. the equipment unique identifier.
     * @return list of skills objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{equipmentId}/skills")    
    public List<Skill> getSkillsList(@PathParam("equipmentId") String equipmentId)
    {
        logger.debug("equipment - getSkillsList - cpadId = " + equipmentId);
        logger.debug("equipment getSkillsList - of the cpad = " + equipmentId);
        return SkillTest.getTestList();
    }
 
}