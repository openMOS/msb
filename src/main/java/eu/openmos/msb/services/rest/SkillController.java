/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
// import eu.openmos.agentcloud.data.recipe.KPI;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.Recipe;
//import eu.openmos.agentcloud.data.recipe.Skill;
import eu.openmos.model.testdata.ModuleTest;
import eu.openmos.model.testdata.KPITest;
import eu.openmos.model.testdata.ParameterTest;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillTest;
import java.util.LinkedList;
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
public class SkillController extends Base {
    private final Logger logger = Logger.getLogger(SkillController.class.getName());
    
    /**
     * Returns the skill object given its unique identifier.
     * Fills the skill view page (slide 17 of 34). 
     * 
     * @return detail of skill
     * 
     * @param skillId the unique id of the skill
     * @return skill object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{skillId}")
    public Skill getDetail(@PathParam("skillId") String skillId) {
        logger.debug("skill getDetail - skillId = " + skillId);
//        return SkillTest.getTestObject();

        
        Module moduleToReturn = null;
        
        // moduleId can be in this form subsystem-module-module-module....
        String[] n = skillId.split(SkillController.PARAMSEPARATOR);
        if (n.length < 2)
        {
            // TODO url has some problems 
        }
        
        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("nn di " + z + " = " + nn[z]);
        
        String subSystemId = nn[1];

        String[] oo = n[1].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("oo length " + oo.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("oo di " + z + " = " + oo[z]);
        
        String realSkillId = oo[1];

        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
//            if (subsystem.getName().equals(subSystemId)) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Skill sk : subsystem.getSkills())
                {
                    if (sk.getUniqueId().equalsIgnoreCase(realSkillId))
                    {
                        logger.debug("skill found: " + sk);
                        return sk;
                    }
                }
            }
        }
        return null;

   }

    /**
     * Returns the list of recipes associated to a skill.
     * Fills the skills recipe list (slide 22 of 34)
     * This method is exposed via a "/skills/{skillId}/recipes" service call.
     * 
     * @param skillId   skill id, i.e. the skill unique identifier.
     * @return list of recipe objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/recipes")    
    public List<Recipe> getRecipesList(@PathParam("skillId") String skillId)
    {
        logger.debug("cpad - getRecipesList - skillId = " + skillId);
//        return RecipeTest.getTestList();

        // moduleId can be in this form subsystem-module-module-module....
        String[] n = skillId.split(SkillController.PARAMSEPARATOR);
        if (n.length < 2)
        {
            // TODO url has some problems 
        }
        
        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("nn di " + z + " = " + nn[z]);
        
        String subSystemId = nn[1];

        String[] oo = n[1].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("oo length " + oo.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("oo di " + z + " = " + oo[z]);
        
        String realSkillId = oo[1];
/*
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getName().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Skill sk : subsystem.getSkills())
                {
                    if (sk.getUniqueId().equalsIgnoreCase(realSkillId))
                    {
                        logger.debug("skill found: " + sk);
                        return sk.getRecipeIds();
                    }
                }
            }
        }
*/
    List<Recipe> lrToReturn = new LinkedList<>();
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Recipe r : subsystem.getRecipes())
                {
                    if (r.getSkill() != null && r.getSkill().getUniqueId().equalsIgnoreCase(realSkillId))
                    {
                        logger.debug("recipe found: " + r);
                        lrToReturn.add(r);
                    }
                }
            }
        }

        if (lrToReturn.size() != 0)
        return lrToReturn;
        else return null;
    }

    /**
     * Returns the list of kpis associated to a skill.
     * Fills the skill detail page (slide 19 of 34)
     * This method is exposed via a "/skills/{skillId}/kpis" service call.
     * 
     * @param skillId   skill id, i.e. the skill unique identifier.
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
     * Returns the list of parameters associated to a skill.
     * Fills the skill detail page (slide 18 of 34)
     * This method is exposed via a "/skills/{skillId}/parameters" service call.
     * 
     * @param skillId   skill id, i.e. the skill unique identifier.
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
     * Returns the list of equipments associated to a skill.
     * Fills the skill detail page (missing slide should be next after 20 of 34)
     * This method is exposed via a "/skills/{skillId}/equipments" service call.
     * 
     * @param skillId   skill id, i.e. the skill unique identifier.
     * @return list of equipment objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/equipments")    
    public List<Module> getModuleList(@PathParam("skillId") String skillId)
    {
        logger.debug("cpad - getModuleList - skillId = " + skillId);
        logger.debug("cpad getModuleList - of the skill = " + skillId);
        return ModuleTest.getTestList("TO FIX");
    }
}