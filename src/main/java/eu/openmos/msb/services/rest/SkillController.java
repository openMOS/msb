/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.model.Equipment;
import eu.openmos.model.KPI;
import eu.openmos.model.Module;
import eu.openmos.model.Parameter;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.testdata.KPITest;
import eu.openmos.model.testdata.ModuleTest;
import eu.openmos.model.testdata.ParameterTest;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillTest;
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
@Path("/api/v1/skills")
public class SkillController
{

  private final Logger logger = Logger.getLogger(SkillController.class.getName());

  /**
   * Returns the skill object given its unique identifier. Fills the skill view page (slide 17 of 34).
   *
   * @return detail of skill
   *
   * @param skillId the unique id of the skill
   * @return skill object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{skillId}")
  public Skill getDetail(@PathParam("skillId") String skillId)
  {
    logger.debug("skill getDetail - skillId = " + skillId);
    return SkillTest.getTestObject();
  }

  /**
   * Returns the list of recipes associated to a skill. Fills the skills recipe list (slide 22 of 34) This method is
   * exposed via a "/skills/{skillId}/recipes" service call.
   *
   * @param skillId skill id, i.e. the skill unique identifier.
   * @return list of recipe objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/recipes")
  public List<Recipe> getRecipesList(@PathParam("skillId") String skillId)
  {
    logger.debug("cpad - getRecipesList - skillId = " + skillId);
    logger.debug("cpad getRecipesList - of the skill = " + skillId);
    return RecipeTest.getTestList();
  }

  /**
   * Returns the list of kpis associated to a skill. Fills the skill detail page (slide 19 of 34) This method is exposed
   * via a "/skills/{skillId}/kpis" service call.
   *
   * @param skillId skill id, i.e. the skill unique identifier.
   * @return list of kpi objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/kpis")
  public List<KPI> getKPIsList(@PathParam("skillId") String skillId)
  {
    logger.debug("cpad - getKPIsList - skillId = " + skillId);
    logger.debug("cpad getKPIsList - of the skill = " + skillId);
    return KPITest.getTestList();
  }

  /**
   * Returns the list of parameters associated to a skill. Fills the skill detail page (slide 18 of 34) This method is
   * exposed via a "/skills/{skillId}/parameters" service call.
   *
   * @param skillId skill id, i.e. the skill unique identifier.
   * @return list of parameter objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/parameters")
  public List<Parameter> getParametersList(@PathParam("skillId") String skillId)
  {
    logger.debug("cpad - getParametersList - skillId = " + skillId);
    logger.debug("cpad getParametersList - of the skill = " + skillId);
    return ParameterTest.getTestList();
  }

  /**
   * Returns the list of equipments associated to a skill. Fills the skill detail page (missing slide should be next
   * after 20 of 34) This method is exposed via a "/skills/{skillId}/equipments" service call.
   *
   * @param skillId skill id, i.e. the skill unique identifier.
   * @return list of equipment objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/equipments")
  public List<Module> getEquipmentList(@PathParam("skillId") String skillId)
  {
    logger.debug("cpad - getEquipmentList - skillId = " + skillId);
    logger.debug("cpad getEquipmentList - of the skill = " + skillId);
    return ModuleTest.getTestList("TO FIX");
  }
}
