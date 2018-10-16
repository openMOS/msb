/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.rest;

// import eu.openmos.agentcloud.data.recipe.Equipment;
import eu.openmos.model.*;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.MSBConstants;
import eu.openmos.msb.datastructures.QueuedAction;
import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;
import eu.openmos.msb.utilities.Functions;
// import eu.openmos.agentcloud.data.recipe.KPI;
// import eu.openmos.agentcloud.data.recipe.Parameter;
// import eu.openmos.agentcloud.data.recipe.Recipe;
//import eu.openmos.agentcloud.data.recipe.Skill;
import java.util.ArrayList;
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
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/skills")
public class SkillController extends Base
{

  private final Logger logger = Logger.getLogger(SkillController.class.getName());

  /**
   * Returns the skill object given its unique identifier. Fills the skill view page (slide 17 of 34).
   *
   * @param skillPath
   * @return detail of skill
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path(value = "/{skillId}")
  public Skill getDetail(@PathParam("skillId") String skillPath)
  {
    logger.debug("SkillController - getDetail - skillId = " + skillPath);

    PathHelper helper = new PathHelper(skillPath, logger);
    Equipment equipment;

    if (helper.hasSubModules())
    {
      equipment = (new ModuleController()).getDetail(helper.getModulesPath());
    }
    else
    {
      equipment = (new SubSystemController()).getDetail(helper.getSubSystemId());
    }

    if (equipment != null && equipment.getSkills() != null)
    {
      for (Skill skill : equipment.getSkills())
      {
        if (skill.getUniqueId().equalsIgnoreCase(helper.getSkillId()))
        {
          return skill;
        }
      }
    }
    return null;
  }

  /**
   * Allows to trigger a skill via insertion of a temporary recipe.
   *
   * @param skillPath
   * @param productInstanceId
   * @param tmpRecipe the temporary recipe to be inserted only for skill triggering.
   * @return status.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/trigger/{productInstanceId}")
  public String skillTriggering(@PathParam("skillId") String skillPath, @PathParam("productInstanceId") String productInstanceId,
          Recipe tmpRecipe)
  {
    logger.debug("skillTriggering: " + skillPath + " - " + productInstanceId + " - " + tmpRecipe);

    PathHelper helper = new PathHelper(skillPath, logger);

    Boolean ret = null;
    List<String> deviceAdaptersID = DACManager.getInstance().getDeviceAdapters_AML_IDs();

    if (helper.hasSubModules())
    {
      Module module = (new ModuleController()).getDetail(helper.getModulesPath());
      if (module != null)
      {
        for (String da_id : deviceAdaptersID)
        {
          DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
          if (da != null)
          {
            for (Module auxModule : da.getSubSystem().getInternalModules())
            {
              if (auxModule.getUniqueId().equals(module.getUniqueId()))
              {
                String string_recipe = Functions.ClassToString(tmpRecipe);

                DeviceAdapterOPC client = (DeviceAdapterOPC) da;
                OpcUaClient opcua_client = client.getClient().getClientObject();

                NodeId update_object_id = Functions.convertStringToNodeId(auxModule.getChangeRecipeObjectID());
                NodeId update_method_id = Functions.convertStringToNodeId(auxModule.getChangeRecipeMethodID());

                ret = client.getClient().InvokeUpdate(opcua_client, update_object_id, update_method_id, string_recipe);

                logger.info("Sending new temp recipe to DA: " + da.getSubSystem().getName());

                QueuedAction qa = new QueuedAction();
                qa.setDa_id(da_id);
                qa.setActionType(MSBConstants.QUEUE_TYPE_EXECUTE);
                qa.setRecipe_id(tmpRecipe.getUniqueId());
                qa.setProduct_instance_id(productInstanceId);
                qa.setProduct_type_id("");
                DACManager.getInstance().QueuedActionMap.put(da_id, qa);

                QueuedAction qa_remove = new QueuedAction();
                qa_remove.setDa_id(da_id);
                qa_remove.setActionType(MSBConstants.QUEUE_TYPE_REMOVE);
                qa_remove.setRecipe_id(tmpRecipe.getUniqueId());
                DACManager.getInstance().QueuedActionMap.put(tmpRecipe.getUniqueId(), qa_remove);
              }
            }
          }
        }
      }
    }
    else
    {
      SubSystem subSystem = (new SubSystemController()).getDetail(helper.getSubSystemId());
      if (subSystem != null)
      {
        for (String da_id : deviceAdaptersID)
        {
          DeviceAdapter da = DACManager.getInstance().getDeviceAdapterbyAML_ID(da_id);
          if (da != null)
          {
            if (da.getSubSystem().getUniqueId().equals(subSystem.getUniqueId()))
            {
              String string_recipe = Functions.ClassToString(tmpRecipe);

              DeviceAdapterOPC client = (DeviceAdapterOPC) da;
              OpcUaClient opcua_client = client.getClient().getClientObject();

              NodeId object_id = Functions.convertStringToNodeId(da.getSubSystem().getChangeRecipeObjectID());
              NodeId method_id = Functions.convertStringToNodeId(da.getSubSystem().getChangeRecipeMethodID());

              ret = client.getClient().InvokeUpdate(opcua_client, object_id, method_id, string_recipe);

              logger.info("Sending new temp recipe to DA: " + da.getSubSystem().getName());

              QueuedAction qa = new QueuedAction();
              qa.setDa_id(da_id);
              qa.setActionType(MSBConstants.QUEUE_TYPE_EXECUTE);
              qa.setRecipe_id(tmpRecipe.getUniqueId());
              qa.setProduct_instance_id(productInstanceId);
              qa.setProduct_type_id("");
              DACManager.getInstance().QueuedActionMap.put(da_id, qa);

              QueuedAction qa_remove = new QueuedAction();
              qa_remove.setDa_id(da_id);
              qa_remove.setActionType(MSBConstants.QUEUE_TYPE_REMOVE);
              qa_remove.setRecipe_id(tmpRecipe.getUniqueId());
              DACManager.getInstance().QueuedActionMap.put(tmpRecipe.getUniqueId(), qa_remove);
            }
          }
        }
      }
    }
    return "Success";
  }

  /**
   * Returns the list of recipes associated to a skill. Fills the skills recipe list (slide 22 of 34) This method is exposed via a "/skills/{skillId}/recipes"
   * service call.
   *
   * @param skillPath
   * @return list of recipe objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/recipes")
  public List<Recipe> getRecipesList(@PathParam("skillId") String skillPath)
  {
    logger.debug("SkillController - getRecipesList - skillId = " + skillPath);

    PathHelper helper = new PathHelper(skillPath, logger);

    Skill skill = this.getDetail(skillPath);

    if (skill != null)
    {
      if (helper.hasSubModules())
      {
        logger.debug("Go for module");
        Module module = (new ModuleController()).getDetail(helper.getModulesPath());
        return this.getRecipesFromSkill(module.getRecipes(), skill);
      }
      else
      {
        logger.debug("Go for subSystem");
        SubSystem subSystem = (new SubSystemController()).getDetail(helper.getSubSystemId());
        return this.getRecipesFromSkill(subSystem.getRecipes(), skill);
      }
    }
    return null;
  }

  private List<Recipe> getRecipesFromSkill(List<Recipe> recipes, Skill skill)
  {
    List<Recipe> recipeToReturn = new ArrayList<>();
    for (Recipe recipe : recipes)
    {
      if (recipe.getSkill().getUniqueId().equalsIgnoreCase(skill.getUniqueId()))
      {
        recipeToReturn.add(recipe);
      }
    }
    logger.debug("Recipe from skill - Found " + recipeToReturn.size() + " for skill : " + skill.getUniqueId());
    return recipeToReturn.isEmpty() ? null : recipeToReturn;
  }

  /**
   * Returns the list of kpis associated to a skill. Fills the skill detail page (slide 19 of 34) This method is exposed via a "/skills/{skillId}/kpis" service
   * call.
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

    List<KPI> kpis = new LinkedList<>();

    DACManager DACinstance = DACManager.getInstance();
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();

    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACinstance.getDeviceAdapterbyAML_ID(da_id);
      List<Skill> recipesFromDeviceAdapter = da.getSubSystem().getSkills();
      for (Module module : da.getSubSystem().getInternalModules())
      {
        recipesFromDeviceAdapter.addAll(module.getSkills());
      }

      for (Skill skill : recipesFromDeviceAdapter)
      {
        if (skill.getUniqueId().equals(skillId))
        {
          return skill.getKpis();
        }
      }
    }

    return kpis;
  }

  /**
   * Returns the list of parameters associated to a skill. Fills the skill detail page (slide 18 of 34) This method is exposed via a
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
    List<String> deviceAdaptersID = DACinstance.getDeviceAdapters_AML_IDs();

    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = DACinstance.getDeviceAdapterbyAML_ID(da_id);
      List<Skill> recipesFromDeviceAdapter = da.getSubSystem().getSkills();
      for (Module module : da.getSubSystem().getInternalModules())
      {
        recipesFromDeviceAdapter.addAll(module.getSkills());
      }

      for (Skill skill : recipesFromDeviceAdapter)
      {
        if (skill.getUniqueId().equals(skillId))
        {
          return skill.getParameters();
        }
      }
    }
    return parameters;
  }

  /**
   * Returns the list of equipments associated to a skill. Fills the skill detail page (missing slide should be next after 20 of 34) This method is exposed via
   * a "/skills/{skillId}/equipments" service call.
   *
   * @param skillId skill id, i.e. the skill unique identifier.
   * @return list of equipment objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{skillId}/equipments")
  public List<Module> getModuleList(@PathParam("skillId") String skillId)
  {
    logger.debug("cpad - getModuleList - skillId = " + skillId);
    logger.debug("cpad getModuleList - of the skill = " + skillId);
    List<Module> modules = new LinkedList<>();

    return modules;
  }
}
