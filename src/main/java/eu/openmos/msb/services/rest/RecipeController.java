/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.*;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.datastructures.PECManager;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.starter.MSB_gui;
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
import javax.xml.ws.BindingProvider;
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
   * Returns the recipe object given its unique identifier. Fills the skill recipe view page (slide 24 of 34).
   *
   * @param recipePath
   * @return detail of recipe
   * @return recipe object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{recipeId}")
  public Recipe getDetail(@PathParam("recipeId") String recipePath)
  {
    logger.debug("recipe getDetail - recipeId = " + recipePath);

    PathHelper helper = new PathHelper(recipePath, logger);
    if (helper.hasSubModules())
    {
      Module module = (new ModuleController()).getDetail(helper.getModulesPath());
      Recipe recipe = this.getRecipeFromList(module.getRecipes(), helper.getRecipeId());
      
        if (recipe.getStatePath() != null) {
            DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterFromModuleID(module.getUniqueId());
            DeviceAdapterOPC da_opc = (DeviceAdapterOPC) CurrentDA;
            NodeId node = Functions.convertStringToNodeId(recipe.getStatePath());
            recipe.setState(Functions.readOPCNodeToString(da_opc.getClient().getClientObject(), node));
        }
      return recipe;
    } else
    {
      SubSystem subSystem = (new SubSystemController()).getDetail(helper.getSubSystemId());
      Recipe recipe = this.getRecipeFromList(subSystem.getRecipes(), helper.getRecipeId());
      
        if (recipe.getStatePath() != null) {
            DeviceAdapter CurrentDA = DACManager.getInstance().getDeviceAdapterbyAML_ID(subSystem.getUniqueId());
            NodeId node = Functions.convertStringToNodeId(recipe.getStatePath());
            DeviceAdapterOPC da_opc = (DeviceAdapterOPC) CurrentDA;
            recipe.setState(Functions.readOPCNodeToString(da_opc.getClient().getClientObject(), node));
        }
        return recipe;
    }

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
   * @param recipeId
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
    List<String> deviceAdaptersID = DACManager.getInstance().getDeviceAdapters_AML_IDs();
    Boolean ret = null;

    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
      if (da != null)
      {
        List<Recipe> recipes = da.getSubSystem().getRecipes();
        for (Recipe auxRecipe : recipes)
        {
          if (auxRecipe.getUniqueId().equals(recipe.getUniqueId()))
          {
            auxRecipe.setDescription(recipe.getDescription());
            auxRecipe.setEquipmentIds(recipe.getEquipmentIds());
            auxRecipe.setExecutedBySkillControlPort(recipe.getExecutedBySkillControlPort());
            auxRecipe.setInvokeMethodID(recipe.getInvokeMethodID());
            auxRecipe.setInvokeObjectID(recipe.getInvokeObjectID());
            auxRecipe.setKpiSettings(recipe.getKpiSettings());
            auxRecipe.setLastOptimizationTime(recipe.getLastOptimizationTime());
            auxRecipe.setMsbProtocolEndpoint(recipe.getMsbProtocolEndpoint());
            auxRecipe.setName(recipe.getName());
            auxRecipe.setOptimized(recipe.isOptimized());
            auxRecipe.setParameterSettings(recipe.getParameterSettings());
            auxRecipe.setRegistered(recipe.getRegistered());
            auxRecipe.setSkill(recipe.getSkill());
            auxRecipe.setSkillRequirements(recipe.getSkillRequirements());
            auxRecipe.setState(recipe.getState());
            auxRecipe.setStatePath(recipe.getStatePath());
            auxRecipe.setUniqueAgentName(recipe.getUniqueAgentName());
            auxRecipe.setUniqueId(recipe.getUniqueId());
            auxRecipe.setValid(recipe.isValid());

            DeviceAdapterOPC client = (DeviceAdapterOPC) da.getClient();
            
            //client.getClient().InvokeExecTableUpdate(client, NodeId.NULL_GUID, NodeId.NULL_GUID, excTablesString); //TO be done by DA
            ret = true;
            logger.info("Sending new execution table to DA: " + da.getSubSystem().getName());
          } else
          {

          }
        }

        if (ret)
        {
          Recipe_DA recipe_DA = Recipe_DA.createRecipe_DA(recipe);
          DeviceAdapterOPC da_opc = (DeviceAdapterOPC) da.getClient();
          MSBClientSubscription client = (MSBClientSubscription) da_opc.getClient();
          
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
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();
    
    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACinstance.getDeviceAdapterbyAML_ID(da_id);
      List<Recipe> recipesFromDeviceAdapter = da.getSubSystem().getRecipes();
      for (Module module : da.getSubSystem().getModules())
        recipesFromDeviceAdapter.addAll(module.getRecipes());
      
      for (Recipe recipe : recipesFromDeviceAdapter)
      {
        if (recipe.getUniqueId().equals(recipeId))
        {
          return recipe.getParameterSettings();
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
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();
    
    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACinstance.getDeviceAdapterbyAML_ID(da_id);
      List<Recipe> recipesFromDeviceAdapter = da.getSubSystem().getRecipes();
      for (Module module : da.getSubSystem().getModules())
        recipesFromDeviceAdapter.addAll(module.getRecipes());
      
      for (Recipe recipe : recipesFromDeviceAdapter)
      {
        if (recipe.getUniqueId().equals(recipeId))
        {
          return recipe.getSkillRequirements();
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
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();
    
    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACinstance.getDeviceAdapterbyAML_ID(da_id);
      List<Recipe> recipesFromDeviceAdapter = da.getSubSystem().getRecipes();
      for (Module module : da.getSubSystem().getModules())
        recipesFromDeviceAdapter.addAll(module.getRecipes());
      
      for (Recipe recipe : recipesFromDeviceAdapter)
      {
        if (recipe.getUniqueId().equals(recipeId))
        {
          return recipe.getKpiSettings();
        }
      }
    }
    return kpiSett;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/insertNewRecipe/{subSystemId}")
  public Recipe startInsertNewRecipe(@PathParam("subSystemId") String subSystemId/*, Skill skill*/)
  {

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
    //Skill skill = helper.getSkillId();
    
    Skill skill = null;
    for (Skill auxSkill : equipment.getSkills())
    {
      if (auxSkill.getUniqueId().equals(helper.getSkillId()))
      {
        skill = auxSkill;
        break;
      }
    }
    
    if (skill == null)
        return null;
    
    recipe.setSkill(skill);

    // Setting Recipe Skill Requirements with empty list that 
    // that will be filled using HMI
    recipe.setSkillRequirements(skill.getSkillRequirements());

    recipe.setOptimized(true);
    recipe.setValid(true);

    // Setting Recipe subSystemId
//        recipe.setEquipmentId(subSystem.getUniqueId());
    List<String> equipmentIds = new LinkedList<>();
    equipmentIds.add(equipment.getUniqueId());
    recipe.setEquipmentIds(equipmentIds);

    recipe.setKpiSettings(getKPISettingFromSkill2(skill));

    recipe.setParameterSettings(getParameterSettingsFromSkill2(skill));
    
    // fulfilled skill reqs
    recipe.setFulfilledSkillRequirements(new LinkedList<>());

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
                          kpi.getKpiType(),
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

  public List<KPISetting> getKPISettingFromSkill2(Skill skill)
  {
    List<KPISetting> kpiSettings = new ArrayList<>();
    logger.debug("getting KPI from skill");
    if (skill != null && skill.getKpis() != null)
    {

        for (KPI kpi : skill.getKpis())
        {
          KPISetting kpiSetting
                  = new KPISetting(
                          "KPISetting From KPI: " + kpi.getName(),
                          generateId(new Date()),
                          "KPISetting Name",
                          kpi,
                          kpi.getKpiType(),
                          kpi.getUnit(),
                          kpi.getValue(),
                          new Date()
                  );
          kpiSettings.add(kpiSetting);
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

  private List<ParameterSetting> getParameterSettingsFromSkill2(Skill skill)
  {
    List<ParameterSetting> parameterSettings = new ArrayList<>();
    logger.debug("getting Parameter from Skill");
    if (skill != null && skill.getParameters() != null)
    {
      //logger.debug("Found " + skill.getParameterPorts().size() + " ParamPort");

        for (Parameter parameter : skill.getParameters())
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
   * Service for triggering a specific Recipe. Returns a status message depending on the outcome.
   *
   * @param recipeId
   * @return status
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{recipeId}/trigger")
  public String recipeTriggering(@PathParam("recipeId") String recipeId)
  {
    logger.debug("start triggering!");
    DACManager DACinstance = DACManager.getInstance();
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();
    
    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
      if (da.getSubSystem().getName().toUpperCase().contains("MSB"))
        continue;
      
      List<Recipe> recipesFromDeviceAdapter = da.getSubSystem().getRecipes();
      for (Recipe recipe : recipesFromDeviceAdapter)
      {
        if (recipe.getUniqueId().equals(recipeId))
        {
          //CHECK IF THE DA is on rampup?
          if (da.getSubSystem().getStage().equals(MSBConstants.STAGE_RAMP_UP) || true)
          {
            String invokeObjectID = recipe.getInvokeObjectID();
            String invokeMethodID = recipe.getInvokeMethodID();
            DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;

            //EXECUTE THE RECIPE
            logger.debug("[EXECUTE] recipeID: " + recipeId);
            NodeId objectID = Functions.convertStringToNodeId(invokeObjectID);
            NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);

            boolean result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objectID, methodID, "HMItest", "HMItest", false);

            if (result)
            { //status code of the call
              return "Success";
            } else
            {
              return "Couldn't Execute";
            }
          } else
          {
            return "Adapter is not on RampUp stage!";
          }
        }
      }
    }
    return "Recipe not found";
  }

    /**
   * Service for triggering a specific Recipe. Returns a status message depending on the outcome.
   *
   * @param recipeId
     * @param productInstanceId
   * @return status
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{recipeId}/trigger/{productInstanceId}")
  public String recipeTriggering(@PathParam("recipeId") String recipeId, @PathParam("productInstanceId") String productInstanceId)
  {
    DACManager DACinstance = DACManager.getInstance();
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();
    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
      List<Recipe> recipesFromDeviceAdapter = da.getSubSystem().getRecipes();
      for (Recipe recipe : recipesFromDeviceAdapter)
      {
        if (recipe.getUniqueId().equals(recipeId))
        {
          //CHECK IF THE DA is on rampup?
          if (da.getSubSystem().getStage().equals(MSBConstants.STAGE_RAMP_UP))
          {
            String invokeObjectID = recipe.getInvokeObjectID();
            String invokeMethodID = recipe.getInvokeMethodID();
            DeviceAdapterOPC daOPC = (DeviceAdapterOPC) da;

              if (createSinglePI(productInstanceId)) {
                  //EXECUTE THE RECIPE
                  logger.debug("[EXECUTE] recipeID: " + recipeId);
                  NodeId objectID = Functions.convertStringToNodeId(invokeObjectID);
                  NodeId methodID = Functions.convertStringToNodeId(invokeMethodID);

                  boolean result = daOPC.getClient().InvokeDeviceSkill(daOPC.getClient().getClientObject(), objectID, methodID, productInstanceId, "HMItest", false);

                  if (result) { //status code of the call
                      return "Success";
                  } else {
                      return "Couldn't Execute";
                  }
              }
              else
                  return "Couldn't Execute";
          } else
          {
            return "Adapter is not on RampUp stage!";
          }
        }
      }
    }
    return "Recipe not found";
  }

  private Boolean createSinglePI(String productInstance_id)
  {
      if (!PECManager.getInstance().getProductsDoing().keySet().contains(productInstance_id))
    {
      OrderInstance oi = new OrderInstance();
      List<ProductInstance> piList = new ArrayList<>();
      oi.setUniqueId(productInstance_id);
      //create instance and agent
      ProductInstance pi = new ProductInstance(productInstance_id, "type", "name", "no_description",
              oi.getUniqueId(), null, false, null, ProductInstanceStatus.PRODUCING,
              new Date(), new Date());

      piList.add(pi);
      
      oi.setName(productInstance_id + "_name");
      oi.setDescription(productInstance_id + "_description");
      oi.setPriority(1);
      oi.setProductInstances(piList);
      oi.setRegistered(new Date());

      PECManager.getInstance().getProductsDoing().put(productInstance_id, pi);

      MSB_gui.addToTableCurrentOrders(oi.getUniqueId(), "type", productInstance_id);
      
      if (MSBConstants.USING_CLOUD)
      {
        try
        {
          //send oi to cloud
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          logger.info("Agent Cloud Cloudinterface address = [" + MSBConstants.CLOUD_ENDPOINT + "]");
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

          ServiceCallStatus orderStatus = systemConfigurator.acceptNewOrderInstance(oi);
          logger.info("Order Instance sent to the Agent Cloud with code: " + orderStatus.getCode());
          logger.info("Order Instance status: " + orderStatus.getDescription());
          //***
          //check order status
          if (orderStatus.getCode().equals("success.openmos.agentcloud.cloudinterface.systemconfigurator"))
          {
            ServiceCallStatus piStartStatus = systemConfigurator.startedProduct(pi);

          }
        } catch (Exception ex)
        {
          System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
      return true;
    }
    else
          return false;
  }
  
}
