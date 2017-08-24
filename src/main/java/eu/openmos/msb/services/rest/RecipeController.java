/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.KPISetting;
import eu.openmos.model.ParameterSetting;
import eu.openmos.model.Recipe;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.testdata.KPISettingTest;
import eu.openmos.model.testdata.ParameterSettingTest;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillRequirementTest;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/recipes")
public class RecipeController
{

  private final Logger logger = Logger.getLogger(RecipeController.class.getName());

  /**
   * Returns the recipe object given its unique identifier. Fills the skill recipe view page (slide 24 of 34).
   *
   * @return detail of recipe
   *
   * @param uniqueId the unique id of the recipe
   * @return recipe object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{recipeId}")
  public Recipe getDetail(@PathParam("recipeId") String recipeId)
  {
    logger.debug("recipe getDetail - recipeId = " + recipeId);
    return RecipeTest.getTestObject();
  }

  /**
   * Manages the recipe update operation. Fills.... dont know which slide.
   *
   * @return detail of recipe
   *
   * @param recipe the recipe object to update
   * @return recipe updated object, or null if not existing
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{recipeId}")
  public Recipe update(@PathParam("recipeId") String recipeId, Recipe recipe)
  {
    logger.debug("recipe update - recipeId = " + recipeId);
    logger.debug("recipe update - uniqueId to update = " + recipe.getUniqueId());
    logger.debug("recipe update - recipe to update = " + recipe.toString());
    return recipe;
  }

  /**
   * Returns the list of parameter settings associated to a recipe. Fills the recipe detail page (slide 24 and 25 of 34)
   * This method is exposed via a "/recipes/{recipeId}/parameterSettings" service call.
   *
   * @param recipeId recipe id, i.e. the recipe unique identifier.
   * @return list of parameter setting objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{recipeId}/parameterSettings")
  public List<ParameterSetting> getParameterSettingsList(@PathParam("recipeId") String recipeId)
  {
    logger.debug("cpad - getParameterSettingsList - skillId = " + recipeId);
    logger.debug("cpad getParameterSettingsList - of the skill = " + recipeId);
    return ParameterSettingTest.getTestList();
  }

  /**
   * Returns the list of skill requirements associated to a recipe. Fills the recipe detail page (slide 24 and 25 of 34)
   * This method is exposed via a "/recipes/{recipeId}/skillRequirements" service call.
   *
   * @param recipeId recipe id, i.e. the recipe unique identifier.
   * @return list of skill requirement objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{recipeId}/skillRequirements")
  public List<SkillRequirement> getSkillRequirementsList(@PathParam("recipeId") String recipeId)
  {
    logger.debug("cpad - getSkillRequirementsList - skillId = " + recipeId);
    logger.debug("cpad getSkillRequirementsList - of the skill = " + recipeId);
    return SkillRequirementTest.getTestList();
  }

  /**
   * Returns the list of kpi settings associated to a recipe. Fills the recipe detail page (slide 24 and 25 of 34) This
   * method is exposed via a "/recipes/{recipeId}/kpiSettings" service call.
   *
   * @param recipeId recipe id, i.e. the recipe unique identifier.
   * @return list of kpi setting objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{recipeId}/kpiSettings")
  public List<KPISetting> getKPISettingList(@PathParam("recipeId") String recipeId)
  {
    logger.debug("cpad - getKPISettingList - skillId = " + recipeId);
    logger.debug("cpad getKPISettingList - of the skill = " + recipeId);
    return KPISettingTest.getTestList();
  }
}
