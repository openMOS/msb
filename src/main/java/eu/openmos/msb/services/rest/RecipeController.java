/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

// import eu.openmos.agentcloud.data.recipe.KPISetting;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.ParameterSetting;
// import eu.openmos.agentcloud.data.recipe.Recipe;
// import eu.openmos.agentcloud.data.recipe.SkillRequirement;
import eu.openmos.model.*;
import eu.openmos.model.testdata.KPISettingTest;
import eu.openmos.model.testdata.ParameterSettingTest;
import eu.openmos.model.testdata.ParameterTest;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillRequirementTest;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/recipes")
public class RecipeController extends Base {
    private final Logger logger = Logger.getLogger(RecipeController.class.getName());
    
    /**
     * Returns the recipe object given its unique identifier.
     * Fills the skill recipe view page (slide 24 of 34). 
     * 
     * @return detail of recipe
     * 
     * @param uniqueId the unique id of the recipe
     * @return recipe object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{recipeId}")
    public Recipe getDetail(@PathParam("recipeId") String recipeId) {
        logger.debug("recipe getDetail - recipeId = " + recipeId);
//        return RecipeTest.getTestObject();

        String[] n = recipeId.split(RecipeController.PARAMSEPARATOR);
        if (n.length < 2)
        {
            // TODO url has some problems 
        }
        
        String realSubSystemId = "-1";
        String realModuleId = "-1";
        String realSkillId = "-1";
        String realRecipeId = "-1";
        for (int w = 0; w < n.length; w++)
        {
            logger.debug("n di " + w + " = " + n[w]);
            String[] nn = n[w].split(RecipeController.PARAMVALUESEPARATOR);
            logger.debug("nn length " + nn.length);
            for (int z = 0;z < nn.length; z++)
                logger.debug("nn di " + z + " = " + nn[z]);
            
            if (nn[0].equalsIgnoreCase("ss"))
                realSubSystemId = nn[1];
            else if (nn[0].equalsIgnoreCase("m"))
                realModuleId = nn[1];
            else if (nn[0].equalsIgnoreCase("sk"))
                realSkillId = nn[1];
            else if (nn[0].equalsIgnoreCase("r"))
                realRecipeId = nn[1];
        }
        
/*        
        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("nn di " + z + " = " + nn[z]);
        
        String subSystemId = nn[1];

        String[] oo = n[1].split(RecipeController.PARAMVALUESEPARATOR);
        logger.debug("oo length " + oo.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("oo di " + z + " = " + oo[z]);
        
        String realRecipeId = oo[1];
*/
/*
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getName().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Recipe r : subsystem.getRecipes())
                {
                    if (r.getUniqueId().equalsIgnoreCase(realRecipeId))
                    {
                        logger.debug("recipe found: " + r);
                        return r;
                    }
                }
            }
        }
*/
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(realSubSystemId)) {
                logger.debug("subsystem - found " + realSubSystemId);

                if (!realRecipeId.equalsIgnoreCase("-1") && realSkillId.equalsIgnoreCase("-1") )
                {
                    for (Recipe r : subsystem.getRecipes())
                    {
                        if (r.getUniqueId().equalsIgnoreCase(realRecipeId))
                        {
                            logger.debug("recipe found case1: " + r);
                            return r;
                        }
                    }
                }
                if (!realRecipeId.equalsIgnoreCase("-1") && !realSkillId.equalsIgnoreCase("-1") )
                {
                    for (Recipe r : subsystem.getRecipes())
                    {
                        if (r.getUniqueId().equalsIgnoreCase(realRecipeId))
                        {
                            logger.debug("recipe found case2: " + r);
                            return r;
                        }
                    }
                }
            }
        }

        return null;
   }


    /**
     * Manages the recipe update operation.
     * Fills.... dont know which slide.
     * 
     * @return detail of recipe
     * 
     * @param recipe    the recipe object to update
     * @return recipe updated object, or null if not existing
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{recipeId}")
    public Recipe update(@PathParam("recipeId") String recipeId, Recipe recipe) {
        logger.debug("recipe update - recipeId = " + recipeId);
        logger.debug("recipe update - uniqueId to update = " + recipe.getUniqueId());
        logger.debug("recipe update - recipe to update = " + recipe.toString());
        return recipe;
   }      

    /**
     * Returns the list of parameter settings associated to a recipe.
     * Fills the recipe detail page (slide 24 and 25 of 34)
     * This method is exposed via a "/recipes/{recipeId}/parameterSettings" service call.
     * 
     * @param recipeId   recipe id, i.e. the recipe unique identifier.
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
     * Returns the list of skill requirements associated to a recipe.
     * Fills the recipe detail page (slide 24 and 25 of 34)
     * This method is exposed via a "/recipes/{recipeId}/skillRequirements" service call.
     * 
     * @param recipeId   recipe id, i.e. the recipe unique identifier.
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
     * Returns the list of kpi settings associated to a recipe.
     * Fills the recipe detail page (slide 24 and 25 of 34)
     * This method is exposed via a "/recipes/{recipeId}/kpiSettings" service call.
     * 
     * @param recipeId   recipe id, i.e. the recipe unique identifier.
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
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/insertNewRecipe/{subSystemId}")
    public Recipe startInsertNewRecipe(@PathParam("subSystemId") String subSystemId,
            Skill skill){
        Recipe recipe = new Recipe();
        SubSystem subSystem = (new SubSystemController()).getDetail(subSystemId);
        
        logger.debug("Selected Skill: " + skill.toString());
        
        recipe.setSkill(skill);
        recipe.setSkillRequirements(skill.getSkillRequirements());             
        
        // recipe.setEquipment(subSystem);
        recipe.setEquipmentId(subSystem.getUniqueId());
        
        return recipe;
    }
}