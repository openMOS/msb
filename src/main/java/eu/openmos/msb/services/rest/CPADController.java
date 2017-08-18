/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.agentcloud.data.CyberPhysicalAgentDescription;
// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
// import eu.openmos.agentcloud.data.recipe.Recipe;
// import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.msb.cloud.cloudinterface.testdata.CyberPhysicalAgentDescriptionTest;
import eu.openmos.msb.cloud.cloudinterface.testdata.EquipmentTest;
import eu.openmos.msb.cloud.cloudinterface.testdata.RecipeTest;
import eu.openmos.msb.cloud.cloudinterface.testdata.SkillTest;
import eu.openmos.agentcloud.utilities.Constants;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/api/v1/cpads")
public class CPADController {
    private final Logger logger = Logger.getLogger(CPADController.class.getName());
    
    /**
     * Returns the list of workstations and transports, e.g. resource and transport agents.
     * Fills the system overview page (slide 5 of 34)
     * 
     * @return list of cyberphysicalagentdescription objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CyberPhysicalAgentDescription> getList()
    {
        logger.debug("cpads getList");
        List<CyberPhysicalAgentDescription> ls = new LinkedList<>();
        
        for (int i = 0; i < 5; i++)
        {
            CyberPhysicalAgentDescription cpad1 = CyberPhysicalAgentDescriptionTest.getTestObject();
            if (i%2 == 0)
            {
                cpad1.setEquipmentId("WORKSTATION_" + i);
                cpad1.setType(Constants.DF_RESOURCE);
            }
            else
            {
                cpad1.setEquipmentId("TRANSPORT_" + i);
                cpad1.setType(Constants.DF_TRANSPORT);
            }
                
            ls.add(cpad1);
        }
        
        return ls;
    }

    /**
     * Returns the detail of a given workstation or transport, e.g. of a resource or transport agent.
     * Fills the workstation view page (slide 6 of 34) or the transport view (slide 7 of 34)
     * according to the type field of the cpad.
     * 
     * @return detail of cyberphysical agent
     * 
     * @param cpadId the unique id of the workstation or transport
     * @return cyberphysicalagentdescription object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}")    
    public CyberPhysicalAgentDescription getDetail(@PathParam("cpadId") String cpadId) {
        logger.debug("cpad - getDetail - cpadId = " + cpadId);
        for (CyberPhysicalAgentDescription cpad : getList()) {
            if (cpad.getEquipmentId().equals(cpadId)) {
                logger.debug("cpad - found " + cpadId + " - returning " + cpad.toString());
                return cpad;
            }
        }
        logger.debug("cpad - not found " + cpadId + " - returning null");
        return null; // TBV
    }

    /**
     * Returns the list of recipes associated to a workstation or a transport.
     * Fills the skills recipe list (slide 22 of 34)
     * This method is exposed via a "/cpads/{cpadId}/recipes" service call.
     * 
     * @param cpadId   cpad id, i.e. the agent unique identifier.
     * @return list of recipe objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}/recipes")    
    public List<Recipe> getRecipesList(@PathParam("cpadId") String cpadId)
    {
        logger.debug("cpad - getRecipesList - cpadId = " + cpadId);
        logger.debug("cpad getRecipesList - of the cpad = " + cpadId);
        return RecipeTest.getTestList();
    }

    /**
     * Allows to insert a new recipe associated to a workstation or a transport.
     * Returns the updated list of recipes associated to the same workstation or transport.
     * Fills the skills recipe creation view (slide 23 of 34)
     * This method is exposed via a POST to "/cpads/{cpadId}/recipes" service call.
     * 
     * @param cpadId   the agent unique identifier.
     * @param newRecipe   the recipe to be inserted.
     * @return list of recipe objects associated to the same equipment (workstation or transport). 
     * List can be empty, cannot be null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}/recipes")    
    public List<Recipe> newRecipe(@PathParam("cpadId") String cpadId, Recipe newRecipe)
    {
        logger.debug("cpad - newRecipe - cpadId = " + cpadId);
        logger.debug("cpad newRecipe - recipe to insert = " + newRecipe.toString());
        // return RecipeTest.getTestList();
        return getRecipesList(cpadId);
    }

    
   /**
     * Returns the list of sub-equipments associated to a workstation or a transport.
     * Fills the sub system view page (slide 14 of 34) because there's no sub-system object into openmos project,
     * and the chain implemented is workstation->equipments, 
     * is not workstation-subsystems-equipments.
     * This method is exposed via a "/cpads/{cpadId}/equipments" service call.
     * 
     * @param cpadId   cpad id, i.e. the agent unique identifier.
     * @return list of equipments objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}/equipments")    
    public List<Equipment> getEquipmentsList(@PathParam("cpadId") String cpadId)
    {
        logger.debug("cpad - getEquipmentsList - cpadId = " + cpadId);
        logger.debug("cpad getEquipmentsList - of the cpad = " + cpadId);
        return EquipmentTest.getTestList(cpadId);
    }

   /**
     * Returns the list of skills associated to a workstation or a transport.
     * Fills the skills list view page (slide 16 of 34).
     * This method is exposed via a "/cpads/{cpadId}/skills" service call.
     * 
     * @param cpadId   cpad id, i.e. the agent unique identifier.
     * @return list of skills objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}/skills")    
    public List<Skill> getSkillsList(@PathParam("cpadId") String cpadId)
    {
        logger.debug("cpad - getSkillsList - cpadId = " + cpadId);
        logger.debug("cpad getSkillsList - of the cpad = " + cpadId);
        return SkillTest.getTestList();
    }
 
    /**
     * Allows to insert a new skill associated to a workstation or a transport.
     * Returns the updated list of skills associated to the same workstation or transport.
     * Fills the composite skill creation view (slide 21 of 34)
     * This method is exposed via a POST to "/cpads/{cpadId}/skills" service call.
     * 
     * @param cpadId   the agent unique identifier.
     * @param newSkill   the skill to be inserted.
     * @return list of skill objects associated to the same equipment (workstation or transport). 
     * List can be empty, cannot be null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{cpadId}/skills")    
    public List<Skill> newCompositeSkill(@PathParam("cpadId") String cpadId, Skill newSkill)
    {
        logger.debug("cpad - newCompositeSkill - cpadId = " + cpadId);
        logger.debug("cpad newCompositeSkill - skill to insert = " + newSkill.toString());
        return getSkillsList(cpadId);
    }
}