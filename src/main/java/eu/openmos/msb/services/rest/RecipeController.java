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
import eu.openmos.msb.datastructures.DACManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
     * Returns the recipe object given its unique identifier. Fills the skill
     * recipe view page (slide 24 of 34).
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
        if (n.length < 2) {
            // TODO url has some problems 
        }

        String realSubSystemId = "-1";
        String realModuleId = "-1";
        String realSkillId = "-1";
        String realRecipeId = "-1";
        for (int w = 0; w < n.length; w++) {
            logger.debug("n di " + w + " = " + n[w]);
            String[] nn = n[w].split(RecipeController.PARAMVALUESEPARATOR);
            logger.debug("nn length " + nn.length);
            for (int z = 0; z < nn.length; z++) {
                logger.debug("nn di " + z + " = " + nn[z]);
            }

            if (nn[0].equalsIgnoreCase("ss")) {
                realSubSystemId = nn[1];
            } else if (nn[0].equalsIgnoreCase("m")) {
                realModuleId = nn[1];
            } else if (nn[0].equalsIgnoreCase("sk")) {
                realSkillId = nn[1];
            } else if (nn[0].equalsIgnoreCase("r")) {
                realRecipeId = nn[1];
            }
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
            if (subsystem.getUniqueId().equals(realSubSystemId)) {
                logger.debug("subsystem - found " + realSubSystemId);

                if (!realRecipeId.equalsIgnoreCase("-1") && realSkillId.equalsIgnoreCase("-1")) {
                    for (Recipe r : subsystem.getRecipes()) {
                        if (r.getUniqueId().equalsIgnoreCase(realRecipeId)) {
                            logger.debug("recipe found case1: " + r);
                            return r;
                        }
                    }
                }
                if (!realRecipeId.equalsIgnoreCase("-1") && !realSkillId.equalsIgnoreCase("-1")) {
                    for (Recipe r : subsystem.getRecipes()) {
                        if (r.getUniqueId().equalsIgnoreCase(realRecipeId)) {
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
    public Recipe update(@PathParam("recipeId") String recipeId, Recipe recipe) {
        Recipe recipeToUpdate = this.getDetail(recipeId);
        logger.debug("To UPDATE ID: " + recipeToUpdate.getUniqueId());
        logger.debug("New RECIPE ID: " + recipe.getUniqueId());
        
        recipeToUpdate.setSkillRequirements(recipe.getSkillRequirements());       
        recipeToUpdate.setKpiSettings(recipe.getKpiSettings());
        
        if(recipe.getParameterSettings() != null 
                && !recipe.getParameterSettings().isEmpty()){
            recipeToUpdate.setParameterSettings(recipe.getParameterSettings());
        }
        
        return recipe;
    }

    /**
     * Returns the list of parameter settings associated to a recipe. Fills the
     * recipe detail page (slide 24 and 25 of 34) This method is exposed via a
     * "/recipes/{recipeId}/parameterSettings" service call.
     *
     * @param recipeId recipe id, i.e. the recipe unique identifier.
     * @return list of parameter setting objects. List can be empty, cannot be
     * null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}/parameterSettings")
    public List<ParameterSetting> getParameterSettingsList(@PathParam("recipeId") String recipeId) {
        logger.debug("cpad - getParameterSettingsList - skillId = " + recipeId);
        logger.debug("cpad getParameterSettingsList - of the skill = " + recipeId);
        
        List<ParameterSetting> parameterSett = new LinkedList<>();
        
       DACManager DACinstance = DACManager.getInstance();
      List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
      for (int i = 0; i < deviceAdaptersNames.size(); i++)
      {
        ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
        for (int j = 0; j < recipesFromDeviceAdapter.size(); j++)
        {
          if (recipesFromDeviceAdapter.get(i).getUniqueId().equals(recipeId))
          {
            return recipesFromDeviceAdapter.get(i).getParameterSettings();
          }
        }
      }
      
        return parameterSett;
    }

    /**
     * Returns the list of skill requirements associated to a recipe. Fills the
     * recipe detail page (slide 24 and 25 of 34) This method is exposed via a
     * "/recipes/{recipeId}/skillRequirements" service call.
     *
     * @param recipeId recipe id, i.e. the recipe unique identifier.
     * @return list of skill requirement objects. List can be empty, cannot be
     * null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}/skillRequirements")
    public List<SkillRequirement> getSkillRequirementsList(@PathParam("recipeId") String recipeId) {
        logger.debug("cpad - getSkillRequirementsList - skillId = " + recipeId);
        logger.debug("cpad getSkillRequirementsList - of the skill = " + recipeId);
        
      List<SkillRequirement> skillReq = new LinkedList<>();

      DACManager DACinstance = DACManager.getInstance();
      List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
      for (int i = 0; i < deviceAdaptersNames.size(); i++)
      {
        ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
        for (int j = 0; j < recipesFromDeviceAdapter.size(); j++)
        {
          if (recipesFromDeviceAdapter.get(i).getUniqueId().equals(recipeId))
          {
            return recipesFromDeviceAdapter.get(i).getSkillRequirements();
          }
        }
      }
        
        return skillReq;
    }

    /**
     * Returns the list of kpi settings associated to a recipe. Fills the recipe
     * detail page (slide 24 and 25 of 34) This method is exposed via a
     * "/recipes/{recipeId}/kpiSettings" service call.
     *
     * @param recipeId recipe id, i.e. the recipe unique identifier.
     * @return list of kpi setting objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{recipeId}/kpiSettings")
    public List<KPISetting> getKPISettingList(@PathParam("recipeId") String recipeId) {
        logger.debug("cpad - getKPISettingList - skillId = " + recipeId);
        logger.debug("cpad getKPISettingList - of the skill = " + recipeId);
        
        List<KPISetting> kpiSett = new LinkedList<>();

      DACManager DACinstance = DACManager.getInstance();
      List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
      for (int i = 0; i < deviceAdaptersNames.size(); i++)
      {
        ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
        for (int j = 0; j < recipesFromDeviceAdapter.size(); j++)
        {
          if (recipesFromDeviceAdapter.get(i).getUniqueId().equals(recipeId))
          {
            return recipesFromDeviceAdapter.get(i).getKpiSettings();
          }
        }
      }
      
        return kpiSett;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/insertNewRecipe/{subSystemId}")
    public Recipe startInsertNewRecipe(@PathParam("subSystemId") String subSystemId,
            Skill skill) {

        // Creating new Recipe Object
        Recipe recipe = new Recipe();
        // Getting SubSystem Detail 
        SubSystem subSystem = (new SubSystemController()).getDetail(subSystemId);

        // Setting Recipe registered Date
        recipe.setRegistered(new Date());

        // Setting Recipe uniqueID
        recipe.setUniqueId(this.generateId(recipe.getRegistered()));

        // Setting recipe skill
        recipe.setSkill(skill);

        // Setting Recipe Skill Requirements with empty list that 
        // that will be filled using HMI
        recipe.setSkillRequirements(new ArrayList<>());

        // Setting Recipe subSystemId
//        recipe.setEquipmentId(subSystem.getUniqueId());
        List<String> equipmentIds = new LinkedList<>();
        equipmentIds.add(subSystem.getUniqueId());
        recipe.setEquipmentIds(equipmentIds);

        recipe.setKpiSettings(getKPISettingFromSkill(skill));
        
        recipe.setParameterSettings(getParameterSettingsFromSkill(skill));
        // recipe.getParameterSettings().add(ParameterSettingTest.getTestObject());

        return recipe;
    }

    public List<KPISetting> getKPISettingFromSkill(Skill skill) {
        List<KPISetting> kpiSettings = new ArrayList<>();
        logger.debug("getting KPI from skill");
        if (skill != null && skill.getInformationPorts() != null) {

            for (InformationPort infoPort : skill.getInformationPorts()) {
                for (KPI kpi : infoPort.getKpis()) {
                    KPISetting kpiSetting
                            = new KPISetting(
                                    "KPISetting From KPI: " + kpi.getName(),
                                    generateId(new Date()),
                                    "KPISetting Name",
                                    kpi,
                                    kpi.getType(),
                                    kpi.getUnit(),
                                    kpi.getValue(),
                                    new Date()
                            );
                    kpiSettings.add(kpiSetting);
                }
            }
        }
        logger.debug("Return " + kpiSettings.size() + " KPIs Settings");
        return kpiSettings;
    }

    private List<ParameterSetting> getParameterSettingsFromSkill(Skill skill) {
        List<ParameterSetting> parameterSettings = new ArrayList<>();
        logger.debug("getting Parameter from Skill");
        if (skill != null && skill.getParameterPorts() != null) {
            logger.debug("Found " + skill.getParameterPorts().size() + " ParamPort");
            for (ParameterPort paramPort : skill.getParameterPorts()) {
                for (Parameter parameter : paramPort.getParameters()) {
                    ParameterSetting paramSett
                            = new ParameterSetting(
                                    "ParamSetting from Param: " + parameter.getName(),
                                    generateId(new Date()),
                                    "ParamSetting NAME",
                                    "ParamSetting Value",
                                    parameter,
                                    new Date()
                            );
                    parameterSettings.add(paramSett);
                }
            }
        }
        logger.debug("Returning " + parameterSettings.size() + " PARAM SETTING");
        return parameterSettings;
    }

    private String generateId(Date registeredDate) {
        return registeredDate.getTime()
                + "_"
                + new Random().nextInt(10000);
    }
}
