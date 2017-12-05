/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
import eu.openmos.msb.datastructures.DACManager;
// import eu.openmos.agentcloud.data.recipe.KPI;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.Recipe;
//import eu.openmos.agentcloud.data.recipe.Skill;
import java.util.ArrayList;
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
     * Returns the skill object given its unique identifier. Fills the skill
     * view page (slide 17 of 34).
     *
     * @return detail of skill
     *
     * @param skillId the unique id of the skill
     * @return skill object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{skillId}")
    public Skill getDetail(@PathParam("skillId") String skillPath) {
        logger.debug("SkillController - getDetail - skillId = " + skillPath);
        
        PathHelper helper = new PathHelper(skillPath, logger);
        Equipment equipment;
        
        if (helper.hasSubModules()) {
            equipment = (new ModuleController()).getDetail(helper.getModulesPath());
        } else {
            equipment = (new SubSystemController()).getDetail(helper.getSubSystemId());
        }
        
        if (equipment != null && equipment.getSkills() != null){
            for (Skill skill : equipment.getSkills()) {
                if (skill.getUniqueId().equalsIgnoreCase(helper.getSkillId())) {
                    return skill;
                }
            }
        }
        
        return null;

        
        /*
        // moduleId can be in this form subsystem-module-module-module....
        String[] ids = skillId.split(Base.PARAMSEPARATOR);

        // ss-skill
        // ss-modulo-skill
        // ss-modulo-modulo-skill
        String subSystemId, realSkillId;

        if (ids != null) {
            subSystemId = ids[0].split(Base.PARAMVALUESEPARATOR)[1];
            realSkillId = ids[ids.length - 1].split(Base.PARAMVALUESEPARATOR)[1];
            logger.debug("SK ID: " + realSkillId);
            if (ids.length == 2) {
                for (SubSystem subsystem : (new SubSystemController()).getList()) {
                    if (subsystem.getUniqueId().equals(subSystemId)) {
                        logger.debug("subsystem - found " + subSystemId);
                        for (Skill sk : subsystem.getSkills()) {
                            if (sk.getUniqueId().equalsIgnoreCase(realSkillId)) {
                                logger.debug("skill found: " + sk);
                                return sk;
                            }
                        }
                    }
                }

            } else {
                String modulePath = skillId.substring(0, skillId.lastIndexOf(Base.SKILLMARKERPREFIX) - 1);
                for (Skill sk : new ModuleController().getSkillsList(modulePath)) {
                    if (sk.getUniqueId().equals(realSkillId)) {
                        return sk;
                    }
                }
            }
        }
        return null;
        */
    }

    /**
     * Returns the list of recipes associated to a skill. Fills the skills
     * recipe list (slide 22 of 34) This method is exposed via a
     * "/skills/{skillId}/recipes" service call.
     *
     * @param skillId skill id, i.e. the skill unique identifier.
     * @return list of recipe objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/recipes")
    public List<Recipe> getRecipesList(@PathParam("skillId") String skillPath) {
        logger.debug("SkillController - getRecipesList - skillId = " + skillPath);
              
        PathHelper helper = new PathHelper(skillPath, logger);
        
        Skill skill = this.getDetail(skillPath);
        
        if (skill != null ) {
            if (helper.hasSubModules()) {
                logger.debug("Go for module");
                Module module = (new ModuleController()).getDetail(helper.getModulesPath());
                return this.getRecipesFromSkill(module.getRecipes(), skill);
            } else {
                logger.debug("Go for subSystem");
                SubSystem subSystem = (new SubSystemController()).getDetail(helper.getSubSystemId());
                return this.getRecipesFromSkill(subSystem.getRecipes(), skill);
            }
        }
        return null;
        
       /* String[] ids = skillId.split(Base.PARAMSEPARATOR);

        // ss-recip
        // ss-modulo-recipe
        // ss-modulo-modulo-recipe
        String subSystemId, realRecipeId;

        if (ids != null) {
            subSystemId = ids[0].split(Base.PARAMVALUESEPARATOR)[1];
            realRecipeId = ids[ids.length - 1].split(Base.PARAMVALUESEPARATOR)[1];
            logger.debug("SK ID: " + realRecipeId);
            if (ids.length == 2) {
                for (SubSystem subsystem : (new SubSystemController()).getList()) {
                    if (subsystem.getUniqueId().equals(subSystemId)) {
                        logger.debug("subsystem - found " + subSystemId);
                        return subsystem.getRecipes();
                    }
                }
            } else {
                String modulePath = skillId.substring(0, skillId.lastIndexOf(Base.SKILLMARKERPREFIX) - 1);
                return new ModuleController().getDetail(modulePath).getRecipes();
            }
        }
        return null;*/   
    }
    
    
    private List<Recipe> getRecipesFromSkill(List<Recipe> recipes, Skill skill) {
        List<Recipe> recipeToReturn = new ArrayList<>();
        for (Recipe recipe : recipes){
            if (recipe.getSkill().getUniqueId().equalsIgnoreCase(skill.getUniqueId())) {
                recipeToReturn.add(recipe);
            }
        }
        logger.debug("Recipe from skill - Found " + recipeToReturn.size() + " for skill : " + skill.getUniqueId());
        return recipeToReturn.isEmpty() ? null : recipeToReturn;
    }

    /**
     * Returns the list of kpis associated to a skill. Fills the skill detail
     * page (slide 19 of 34) This method is exposed via a
     * "/skills/{skillId}/kpis" service call.
     *
     * @param skillId skill id, i.e. the skill unique identifier.
     * @return list of kpi objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/kpis")
    public List<KPI> getKPIsList(@PathParam("skillId") String skillId) {
        logger.debug("cpad - getKPIsList - skillId = " + skillId);
        logger.debug("cpad getKPIsList - of the skill = " + skillId);
              
      List<KPI> kpis = new LinkedList<>();
      
      DACManager DACinstance = DACManager.getInstance();
      List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
      for (int i = 0; i < deviceAdaptersNames.size(); i++)
      {
        ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
        for (int j = 0; j < recipesFromDeviceAdapter.size(); j++)
        {
          if (recipesFromDeviceAdapter.get(i).getSkill().getUniqueId().equals(skillId))
          {
            return recipesFromDeviceAdapter.get(i).getSkill().getKpis();
          }
        }
      }

      return kpis;
    }

    /**
     * Returns the list of parameters associated to a skill. Fills the skill
     * detail page (slide 18 of 34) This method is exposed via a
     * "/skills/{skillId}/parameters" service call.
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

    List<Parameter> parameters = new LinkedList<>();

    DACManager DACinstance = DACManager.getInstance();
    List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
    for (int i = 0; i < deviceAdaptersNames.size(); i++)
    {
      ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
      for (int j = 0; j < recipesFromDeviceAdapter.size(); j++)
      {
        if (recipesFromDeviceAdapter.get(i).getSkill().getUniqueId().equals(skillId))
        {
          return recipesFromDeviceAdapter.get(i).getSkill().getParameters();
        }
      }
    }

    return parameters;
  }

    /**
     * Returns the list of equipments associated to a skill. Fills the skill
     * detail page (missing slide should be next after 20 of 34) This method is
     * exposed via a "/skills/{skillId}/equipments" service call.
     *
     * @param skillId skill id, i.e. the skill unique identifier.
     * @return list of equipment objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/equipments")
    public List<Module> getModuleList(@PathParam("skillId") String skillId) {
        logger.debug("cpad - getModuleList - skillId = " + skillId);
        logger.debug("cpad getModuleList - of the skill = " + skillId);
        List<Module> modules = new LinkedList<>();
      
        return modules;
    }
}


////////////////////////////////////////////////////////////////////////////////


/*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{skillId}")
    public Skill getDetail_OLD(@PathParam("skillId") String skillId) {
        logger.debug("skill getDetail - skillId = " + skillId);

        // moduleId can be in this form subsystem-module-module-module....
        String[] n = skillId.split(SkillController.PARAMSEPARATOR);
        if (n.length < 2) {
            // TODO url has some problems 
        }

        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0; z < nn.length; z++) {
            logger.debug("nn di " + z + " = " + nn[z]);
        }

        String subSystemId = nn[1];

        String[] oo = n[1].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("oo length " + oo.length);
        for (int z = 0; z < nn.length; z++) {
            logger.debug("oo di " + z + " = " + oo[z]);
        }

        String realSkillId = oo[1];

        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Skill sk : subsystem.getSkills()) {
                    if (sk.getUniqueId().equalsIgnoreCase(realSkillId)) {
                        logger.debug("skill found: " + sk);
                        return sk;
                    }
                }
            }
        }
        return null;

    }
*/



/*
@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{skillId}/recipes")
    public List<Recipe> getRecipesList_OLD(@PathParam("skillId") String skillId) {
        logger.debug("SkillController - getRecipesList - skillId = " + skillId);
        
        // moduleId can be in this form subsystem-module-module-module....
        String[] n = skillId.split(SkillController.PARAMSEPARATOR);
        if (n.length < 2) {
            // TODO url has some problems 
        }

        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0; z < nn.length; z++) {
            logger.debug("nn di " + z + " = " + nn[z]);
        }

        String subSystemId = nn[1];

        String[] oo = n[1].split(SkillController.PARAMVALUESEPARATOR);
        logger.debug("oo length " + oo.length);
        for (int z = 0; z < nn.length; z++) {
            logger.debug("oo di " + z + " = " + oo[z]);
        }

        String realSkillId = oo[1];
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
        List<Recipe> lrToReturn = new LinkedList<>();
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                for (Recipe r : subsystem.getRecipes()) {
                    if (r.getSkill() != null && r.getSkill().getUniqueId().equalsIgnoreCase(realSkillId)) {
                        logger.debug("recipe found: " + r);
                        lrToReturn.add(r);
                    }
                }
            }
        }

        if (lrToReturn.size() != 0) {
            return lrToReturn;
        } else {
            return null;
        }
    }
*/

    

    