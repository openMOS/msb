/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.Equipment;
import eu.openmos.model.Module;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.msb.datastructures.DACManager;
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
@Path("/api/v1/equipments")
public class EquipmentController
{

  private final Logger logger = Logger.getLogger(EquipmentController.class.getName());

  /**
   * Returns the equipment object given its unique identifier. Fills the equipment view page (slide 15 of 34).
   *
   * @param equipmentId
   * @return detail of equipment
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{equipmentId}")
  public Equipment getDetail(@PathParam("equipmentId") String equipmentId)
  {
    logger.debug("equipment getDetail - equipmentId = " + equipmentId);
    //return DACManager.getInstance().getEquipment();
    return null;
  }

  /**
   * Returns the list of skills associated to the given equipment. Fills the skills list view page (slide 16 of 34).
   * This method is exposed via a "/equipments/{equipmentId}/skills" service call.
   *
   * @param equipmentId
   * @return list of skills objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{equipmentId}/skills")
  public List<Skill> getSkillsList(@PathParam("equipmentId") String equipmentId)
  {
    logger.debug("equipment - getSkillsList - equipmentId = " + equipmentId);
    return DACManager.getInstance().getDeviceAdapter(equipmentId).getListOfSkills();
  }

  /**
   * Returns the list of recipes associated to a workstation or a transport. Fills the skills recipe list (slide 22 of
   * 34) This method is exposed via a "/cpads/{cpadId}/recipes" service call.
   *
   * @param equipmentId
   * @return list of recipe objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{equipmentId}/recipes")
  public List<Recipe> getRecipesList(@PathParam("equipmentId") String equipmentId)
  {
    logger.debug("cpad - getRecipesList - equipmentId = " + equipmentId);

    return DACManager.getInstance().getDeviceAdapter(equipmentId).getListOfRecipes(); //TODO this needs to be tested
  }

  /**
   * Allows to insert a new recipe associated to a workstation or a transport. Returns the updated list of recipes
   * associated to the same workstation or transport. Fills the skills recipe creation view (slide 23 of 34) This method
   * is exposed via a POST to "/cpads/{cpadId}/recipes" service call.
   *
   * @param equipmentId
   * @param newRecipe the recipe to be inserted.
   * @return list of recipe objects associated to the same equipment (workstation or transport). List can be empty,
   * cannot be null.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{equipmentId}/recipes")
  public List<Recipe> newRecipe(@PathParam("equipmentId") String equipmentId, Recipe newRecipe)
  {
    logger.debug("cpad - newRecipe - equipmentId = " + equipmentId);
    return DACManager.getInstance().getDeviceAdapter(equipmentId).addNewRecipe(newRecipe);
  }

  /**
   * Returns the list of sub-equipments associated to a workstation or a transport. Fills the sub system view page
   * (slide 14 of 34) because there's no sub-system object into openmos project, and the chain implemented is
   * workstation->equipments, is not workstation-subsystems-equipments. This method is exposed via a
   * "/cpads/{cpadId}/equipments" service call.
   *
   * @param equipmentId
   * @return list of equipments objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{equipmentId}/equipments")
  public List<Module> getEquipmentsList(@PathParam("equipmentId") String equipmentId)
  {
    logger.debug("cpad - getEquipmentsList - equipmentId = " + equipmentId);
    return DACManager.getInstance().getDeviceAdapter(equipmentId).getListOfEquipments();
  }

  /**
   * Allows to insert a new skill associated to a workstation or a transport. Returns the updated list of skills
   * associated to the same workstation or transport. Fills the composite skill creation view (slide 21 of 34) This
   * method is exposed via a POST to "/cpads/{cpadId}/skills" service call.
   *
   * @param equipmentId
   * @param newSkill the skill to be inserted.
   * @return list of skill objects associated to the same equipment (workstation or transport). List can be empty,
   * cannot be null.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{equipmentId}/skills")
  public List<Skill> newCompositeSkill(@PathParam("equipmentId") String equipmentId, Skill newSkill)
  {
    logger.debug("cpad - newCompositeSkill - equipmentId = " + equipmentId);
    logger.debug("cpad newCompositeSkill - skill to insert = " + newSkill.toString());
    return DACManager.getInstance().getDeviceAdapter(equipmentId).addNewSkill(newSkill);
  }

}
