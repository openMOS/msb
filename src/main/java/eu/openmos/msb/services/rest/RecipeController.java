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
import eu.openmos.msb.database.interaction.DatabaseInteraction;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.utilities.Functions;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/recipes")
public class RecipeController extends Base
{

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
    public Recipe getDetail(@PathParam("recipeId") String recipePath) {
        logger.debug("recipe getDetail - recipeId = " + recipePath);
        
        PathHelper helper = new PathHelper(recipePath, logger);        
        if (helper.hasSubModules()) {
            Module module = (new ModuleController()).getDetail(helper.getModulesPath());
            return this.getRecipeFromList(module.getRecipes(), helper.getRecipeId());
        } else {
            SubSystem subSystem = (new SubSystemController()).getDetail(helper.getSubSystemId());
            return this.getRecipeFromList(subSystem.getRecipes(), helper.getRecipeId());
        }
       
        /*
        String[] ids = recipeId.split(Base.PARAMSEPARATOR);
        String subSystemId = "";
        String realRecipeId = "";

        if (ids != null) {
            realRecipeId = ids[ids.length - 1].split(Base.PARAMVALUESEPARATOR)[1];
            subSystemId = ids[0].split(Base.PARAMVALUESEPARATOR)[1];
            logger.debug("REAL ID : " + realRecipeId);
            
            if (ids.length == 2) {
                for (SubSystem ss : new SubSystemController().getList()) {
                    if (ss.getUniqueId().equals(subSystemId)) {
                        for (Recipe rec : ss.getRecipes()) {
                            if (rec.getUniqueId().equalsIgnoreCase(realRecipeId)) {
                                return rec;
                            }
                        }
                    }
                }
            } else {
                String modulePath = recipeId.substring(0, recipeId.lastIndexOf(Base.PARAMSEPARATOR + Base.RECIPEMARKERPREFIX));
                if (modulePath.contains(Base.SKILLMARKERPREFIX)) {
                    modulePath = modulePath.substring(0, modulePath.indexOf(Base.PARAMSEPARATOR + Base.SKILLMARKERPREFIX));
                }
                logger.debug("Recipe Controller GET MODULE: " + modulePath);
                
                for (Recipe rec : new ModuleController().getModuleRecipes(modulePath)) {
                    if (rec.getUniqueId().equals(realRecipeId)) {
                        return rec;
                    }
                }
            }
        }

        logger.debug("Error getting recipe");
        return null;*/
    }
    
  private Recipe getRecipeFromList(List<Recipe> recipes, String recipeId)
  {
    if (recipes != null && !recipes.isEmpty())
    {
      for (Recipe recipe : recipes)
      {
        if (recipe.getUniqueId().equalsIgnoreCase(recipeId))
        {
          return recipe;
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
  public Recipe update(@PathParam("recipeId") String recipeId, Recipe recipe)
  {
    Recipe recipeToUpdate = this.getDetail(recipeId);
    logger.debug("To UPDATE ID: " + recipeToUpdate.getUniqueId());
    logger.debug("New RECIPE ID: " + recipe.getUniqueId());

    recipeToUpdate.setSkillRequirements(recipe.getSkillRequirements());
    recipeToUpdate.setKpiSettings(recipe.getKpiSettings());

    if (recipe.getParameterSettings() != null
            && !recipe.getParameterSettings().isEmpty())
    {
      recipeToUpdate.setParameterSettings(recipe.getParameterSettings());
    }

    //send the updated recipe to DA
    String recipeUniqueId = recipe.getUniqueId();
    List<String> deviceAdaptersNames = DACManager.getInstance().getDeviceAdaptersNames();
    Boolean ret = null;

    for (int i = 0; i < deviceAdaptersNames.size(); i++)
    {
      DeviceAdapter deviceAdapter = DACManager.getInstance().getDeviceAdapterbyName(deviceAdaptersNames.get(i));
      if (deviceAdapter != null)
      {
        List<Recipe> recipes = deviceAdapter.getSubSystem().getRecipes();
        for (int j = 0; j < recipes.size(); j++)
        {
          if (recipes.get(j).getUniqueId().equals(recipe.getUniqueId()))
          {
            recipes.get(j).setDescription(recipe.getDescription());
            recipes.get(j).setEquipmentIds(recipe.getEquipmentIds());
            recipes.get(j).setExecutedBySkillControlPort(recipe.getExecutedBySkillControlPort());
            recipes.get(j).setInvokeMethodID(recipe.getInvokeMethodID());
            recipes.get(j).setInvokeObjectID(recipe.getInvokeObjectID());
            recipes.get(j).setKpiSettings(recipe.getKpiSettings());
            recipes.get(j).setLastOptimizationTime(recipe.getLastOptimizationTime());
            recipes.get(j).setMsbProtocolEndpoint(recipe.getMsbProtocolEndpoint());
            recipes.get(j).setName(recipe.getName());
            recipes.get(j).setOptimized(recipe.isOptimized());
            recipes.get(j).setParameterSettings(recipe.getParameterSettings());
            recipes.get(j).setRegistered(recipe.getRegistered());
            recipes.get(j).setSkill(recipe.getSkill());
            recipes.get(j).setSkillRequirements(recipe.getSkillRequirements());
            recipes.get(j).setState(recipe.getState());
            recipes.get(j).setStatePath(recipe.getStatePath());
            recipes.get(j).setUniqueAgentName(recipe.getUniqueAgentName());
            recipes.get(j).setUniqueId(recipe.getUniqueId());
            recipes.get(j).setValid(recipe.isValid());
            
            DeviceAdapterOPC client = (DeviceAdapterOPC) deviceAdapter.getClient();
            //client.getClient().InvokeExecTableUpdate(client, NodeId.NULL_GUID, NodeId.NULL_GUID, excTablesString); //TO be done by DA
            ret = true;
            logger.info("Sending new execution table to DA: " + deviceAdaptersNames.get(i));
          } else
          {

          }
        }

        if (ret)
        {
          Recipe_DA recipe_DA = Recipe_DA.createRecipe_DA(recipe);
          MSBClientSubscription client = (MSBClientSubscription) deviceAdapter.getClient();
          String RecipeSerialized = Functions.ClassToString(recipe_DA);
          NodeId objectID = Functions.convertStringToNodeId(recipe_DA.getChangeRecipeObjectID());
          NodeId methodID = Functions.convertStringToNodeId(recipe_DA.getChangeRecipeMethodID());
          boolean updateRecipe = client.updateRecipe(client.getClientObject(), objectID, methodID, RecipeSerialized);

          if (updateRecipe)
          {
            return recipe;
          } else
          {
            return null;
          }
        }

      }
    }

    return null;
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

        logger.debug("Insert Skill for : " + subSystemId);
        
        Equipment equipment;
        
        // Creating new Recipe Object
        Recipe recipe = new Recipe();
       
        PathHelper helper = new PathHelper(subSystemId, logger);
        
        equipment = (new SubSystemController()).getDetail(helper.getSubSystemId());
        /*
        Not used at the moment, need only subSystemId
        if (helper.hasSubModules()) {
            equipment = (new ModuleController()).getDetail(helper.getModulesPath());
        } else {
            equipment = (new SubSystemController()).getDetail(helper.getSubSystemId());
        }*/
        
        // Getting SubSystem Detail 
        // SubSystem subSystem = (new SubSystemController()).getDetail(subSystemId);

        // Setting Recipe registered Date
        recipe.setRegistered(new Date());

        // Setting Recipe uniqueID
        recipe.setUniqueId(this.generateId(recipe.getRegistered()));

        // Setting recipe skill
        recipe.setSkill(skill);

        // Setting Recipe Skill Requirements with empty list that 
        // that will be filled using HMI
        recipe.setSkillRequirements(new ArrayList<>());
        
        recipe.setOptimized(true);
        recipe.setValid(true);

        // Setting Recipe subSystemId
//        recipe.setEquipmentId(subSystem.getUniqueId());
        List<String> equipmentIds = new LinkedList<>();
        equipmentIds.add(equipment.getUniqueId());
        recipe.setEquipmentIds(equipmentIds);

        recipe.setKpiSettings(getKPISettingFromSkill(skill));

        recipe.setParameterSettings(getParameterSettingsFromSkill(skill));
        //recipe.setParameterSettings(new ArrayList());
        //recipe.getParameterSettings().add(ParameterSettingTest.getTestObject());
        //recipe.getParameterSettings().add(ParameterSettingTest.getTestObject());
        //recipe.getParameterSettings().add(ParameterSettingTest.getTestObject());
        
 
        return recipe;
    }

  public List<KPISetting> getKPISettingFromSkill(Skill skill)
  {
    List<KPISetting> kpiSettings = new ArrayList<>();
    logger.debug("getting KPI from skill");
    if (skill != null && skill.getInformationPorts() != null)
    {

      for (InformationPort infoPort : skill.getInformationPorts())
      {
        for (KPI kpi : infoPort.getKpis())
        {
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

  private List<ParameterSetting> getParameterSettingsFromSkill(Skill skill)
  {
    List<ParameterSetting> parameterSettings = new ArrayList<>();
    logger.debug("getting Parameter from Skill");
    if (skill != null && skill.getParameterPorts() != null)
    {
      logger.debug("Found " + skill.getParameterPorts().size() + " ParamPort");
      for (ParameterPort paramPort : skill.getParameterPorts())
      {
        for (Parameter parameter : paramPort.getParameters())
        {
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

  private String generateId(Date registeredDate)
  {
    return registeredDate.getTime()
            + "_"
            + new Random().nextInt(10000);
  }
  
    /**
     * Service for triggering a specific Recipe. Returns a status message
     * depending on the outcome.
     *
     * @return status
     *
     * @param uniqueId the unique id of the recipe
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
      @Path("/{recipeId}/trigger")
    public String recipeTriggering(@PathParam("recipeId") String recipeId) {

        DACManager DACinstance = DACManager.getInstance();
        List<String> deviceAdaptersNames = DACinstance.getDeviceAdaptersNames();
        for (int i = 0; i < deviceAdaptersNames.size(); i++) {
            ArrayList<Recipe> recipesFromDeviceAdapter = DACManager.getInstance().getRecipesFromDeviceAdapter(deviceAdaptersNames.get(i));
            for (int j = 0; j < recipesFromDeviceAdapter.size(); j++) {
                if (recipesFromDeviceAdapter.get(i).getUniqueId().equals(recipeId)) {
                    
                    DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyName(deviceAdaptersNames.get(i));
                    
                    //CHECK IF THE DA is on rampup?
                    if (da.getSubSystem().getStage().equals(MSBConstants.SYSTEM_STAGE_RAMP_UP)) {

                        String invokeObjectID = recipesFromDeviceAdapter.get(i).getInvokeObjectID();
                        String invokeMethodID = recipesFromDeviceAdapter.get(i).getInvokeMethodID();
                        DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;
                        boolean result = false;

                        //EXECUTE THE RECIPE
                        logger.debug("[EXECUTE] recipeID: " + recipeId);
                        NodeId objectID = Functions.convertStringToNodeId(invokeObjectID);
                        NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);

                        result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objectID, methodID, "HMItest", "HMItest");

                        if (result) { //status code of the call
                            return "Success";
                        } else {
                            return "Couldn't Execute";
                        }
                    }else{
                        return "Adapter is not on RampUp stage!";
                    }
                }
            }
        }
        return "Recipe not found";
    }
    
}
