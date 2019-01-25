package eu.openmos.msb.services.rest;

//import _masmec.aml5;
//import _masmec.MasmecModel;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import eu.openmos.model.*;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.DeviceAdapterOPC;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.utilities.Functions;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.BindingProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/subsystems")
public class SubSystemController extends Base
{

  private final Logger logger = Logger.getLogger(SubSystemController.class.getName());
  private final StopWatch HMIsubsystemUpdateWatch = new StopWatch();
  private static final String CLOUD_ENDPOINT = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
//  private static final Boolean USING_CLOUD = Boolean.parseBoolean(ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud"));
  private static final Boolean USING_CLOUD = true;

  /**
   * Returns the list of workstations and transports, e.g. resource and transport agents. Fills the system overview page (slide 5 of 34)
   *
   * @return list of cyberphysicalagentdescription objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<SubSystem> getList()
  {

    HMIsubsystemUpdateWatch.start();

    List<SubSystem> ls = new LinkedList<>();

    DACManager dac = DACManager.getInstance();
    //logger.debug("subsystems getList 2");

    List<String> deviceAdaptersID = dac.getDeviceAdapters_AML_IDs();
    //logger.debug("subsystems getList 2.5: " + deviceAdapters );

    for (String da_id : deviceAdaptersID)
    {
      DeviceAdapter da = dac.getDeviceAdapterbyAML_ID(da_id);
      if (da != null)
      {
        if (da.getSubSystem().getName().toUpperCase().contains("MSB") || da.getSubSystem().getName().toUpperCase().contains("FORTISS"))
        {
          continue;
        }

        SubSystem ss = da.getSubSystem();
        if (ss != null)
        {
          // assume the name is populated
          if (ss.getUniqueId() == null || ss.getUniqueId().length() == 0)
          {
            ss.setUniqueId(ss.getName());
          }

          if (ss.getDescription() == null || ss.getDescription().length() == 0)
          {
            ss.setDescription(ss.getName());
          }

          ss.setRegistered(new Date());

          DeviceAdapterOPC test = (DeviceAdapterOPC) da;
          if (ss.getStatePath() != null && !ss.getStatePath().equals(""))
          {
            NodeId node = Functions.convertStringToNodeId(ss.getStatePath());
            ss.setState(Functions.readOPCNodeToString(test.getClient().getClientObject(), node));
          }
          ls.add(ss);
        }
      }
    }

    PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
    perfMeasure.getHMISubsystemUpdateTimers().add(HMIsubsystemUpdateWatch.getTime());
    HMIsubsystemUpdateWatch.stop();

    return ls;
  }

  /**
   * Returns the detail of a given workstation or transport, e.g. of a resource or transport agent. Fills the workstation view page (slide 6 of 34) or the
   * transport view (slide 7 of 34) according to the type field of the subsystem.
   *
   * @return detail of cyberphysical agent
   *
   * @param subsystemId the unique id of the workstation or transport
   * @return cyberphysicalagentdescription object, or null if not existing
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}")
  public SubSystem getDetail(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getDetail - subsystemId = " + subsystemId);
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return subsystem;
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  /**
   * Returns the list of recipes associated to a workstation or a transport. Fills the skills recipe list (slide 22 of 34) This method is exposed via a
   * "/subsystems/{subsystemId}/recipes" service call.
   *
   * @param subsystemId subsystem id, i.e. the agent unique identifier.
   * @return list of recipe objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/recipes")
  public List<Recipe> getRecipesList(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getRecipesList - subsystemId = " + subsystemId);
    //logger.debug("subsystem getRecipesList - of the subsystem = " + subsystemId);
//        return RecipeTest.getTestList();
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return subsystem.getRecipes();
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  /**
   * Returns the list of recipes associated to a workstation or a transport. Fills the skills recipe list (slide 22 of 34) This method is exposed via a
   * "/subsystems/{subsystemId}/recipes" service call.
   *
   * @param subsystemId subsystem id, i.e. the agent unique identifier.
   * @return list of recipe objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/fullRecipes")
  public List<Recipe> getFullRecipesList(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getRecipesList - subsystemId = " + subsystemId);
    //logger.debug("subsystem getRecipesList - of the subsystem = " + subsystemId);
//        return RecipeTest.getTestList();
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        List<Recipe> recipeList = new ArrayList<>(subsystem.getRecipes());
        for (Module module : subsystem.getInternalModules())
        {
          recipeList.addAll(new ArrayList<>(module.getRecipes()));
        }
        return recipeList;
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  /**
   * Allows to insert a new recipe associated to a workstation or a transport. Returns the updated list of recipes associated to the same workstation or
   * transport. Fills the skills recipe creation view (slide 23 of 34) This method is exposed via a POST to "/subsystems/{subsystemId}/recipes" service call.
   *
   * @param pathToInsert
   * @param newRecipe the recipe to be inserted.
   * @return list of recipe objects associated to the same subsystem (workstation or transport). List can be empty, cannot be null.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/recipes")
  public Boolean newRecipe(@PathParam("subsystemId") String pathToInsert, Recipe newRecipe)
  {
      //not dealing with product recipes or changes in product SR -- new field should be added in service call
    logger.debug("New Recipe for: " + pathToInsert);

    PathHelper helper = new PathHelper(pathToInsert, logger);

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
                  Recipe_DA recipe_DA = Recipe_DA.createRecipe_DA(newRecipe);
                String string_recipe = Functions.ClassToString(recipe_DA);

                DeviceAdapterOPC client = (DeviceAdapterOPC) da;
                OpcUaClient opcua_client = client.getClient().getClientObject();

                NodeId update_object_id = Functions.convertStringToNodeId(auxModule.getChangeRecipeObjectID());
                NodeId update_method_id = Functions.convertStringToNodeId(auxModule.getChangeRecipeMethodID());

                ret = client.getClient().InvokeUpdate(opcua_client, update_object_id, update_method_id, string_recipe, false);

                logger.info("Sending new recipe to DA: " + da.getSubSystem().getName() + " *** result: " + ret);

                return ret;
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
                Recipe_DA recipe_DA = Recipe_DA.createRecipe_DA(newRecipe);
              String string_recipe = Functions.ClassToString(recipe_DA);

              DeviceAdapterOPC client = (DeviceAdapterOPC) da;
              OpcUaClient opcua_client = client.getClient().getClientObject();

              NodeId object_id = Functions.convertStringToNodeId(da.getSubSystem().getChangeRecipeObjectID());
              NodeId method_id = Functions.convertStringToNodeId(da.getSubSystem().getChangeRecipeMethodID());

              ret = client.getClient().InvokeUpdate(opcua_client, object_id, method_id, string_recipe, false);

              logger.info("Sending new recipe to DA: " + da.getSubSystem().getName() + "*** result: " + ret);

              return ret;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Returns the list of sub-modules associated to a workstation or a transport. Fills the sub system view page (slide 14 of 34). This method is exposed via a
   * "/subsystems/{subsystemId}/modules" service call.
   *
   * @param subsystemId subsystem id, i.e. the agent unique identifier.
   * @return list of modules objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/modules")
  public List<Module> getModulesList(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getEquipmentsList - subsystemId = " + subsystemId);
    //logger.debug("subsystem getEquipmentsList - of the subsystem = " + subsystemId);
//        return ModuleTest.getTestList(subsystemId);
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return subsystem.getInternalModules();
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  /**
   * Returns the list of skills associated to a workstation or a transport. Fills the skills list view page (slide 16 of 34). This method is exposed via a
   * "/subsystems/{subsystemId}/skills" service call.
   *
   * @param subsystemId subsystem id, i.e. the agent unique identifier.
   * @return list of skills objects. List can be empty, cannot be null.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/skills")
  public List<Skill> getSkillsList(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getSkillsList - subsystemId = " + subsystemId);
    //logger.debug("subsystem getSkillsList - of the subsystem = " + subsystemId);
//        return SkillTest.getTestList();
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return subsystem.getSkills();
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  /**
   * Allows to insert a new skill associated to a workstation or a transport. Returns the updated list of skills associated to the same workstation or
   * transport. Fills the composite skill creation view (slide 21 of 34) This method is exposed via a POST to "/subsystems/{subsystemId}/skills" service call.
   *
   * @param subsystemId the agent unique identifier.
   * @param newSkill the skill to be inserted.
   * @return list of skill objects associated to the same subsystem (workstation or transport). List can be empty, cannot be null.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/skills")
  public List<Skill> newCompositeSkill(@PathParam("subsystemId") String subsystemId, Skill newSkill)
  {
    logger.debug("subsystem - newCompositeSkill - subsystemId = " + subsystemId);
    logger.debug("subsystem newCompositeSkill - skill to insert = " + newSkill.toString());
    return getSkillsList(subsystemId);
  }

  /**
   * Returns the full execution table given its unique identifier. Fills the execution table view page (slide 8 of 34).
   *
   * @param subsystemId
   * @return detail of execution table
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/executionTable")
  public ExecutionTable getExecutionTable(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("execution table getDetail of subsystem  = " + subsystemId);
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return subsystem.getExecutionTable();
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  @DELETE
  public void deleteSubSystem()
  {
    int pos = this.getList().size() - 1;
    if (pos >= 0)
    {
      this.getList().remove(pos);
      logger.debug("Deleted subSystem in position: " + pos);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/stage")
  public SubSystemStage getSubSystemStage(@PathParam("subsystemId") String subsystemId)
  {
    //logger.debug("subsystem - getSubSystemStage - subsystemId = " + subsystemId);
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
        return new SubSystemStage(subsystem.getUniqueId(), subsystem.getStage());
      }
    }
    //logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{subsystemId}/stage")
  public SubSystemStage updateSubSystemStage(@PathParam("subsystemId") String subsystemId,
          SubSystemStage newSubSystemStage)
  {
    //logger.debug("subsystem - updateSubSystemStage - subsystemId = " + subsystemId + " - newstage = " + newSubSystemStage.getStage());
    for (SubSystem subsystem : getList())
    {
      if (subsystem.getUniqueId().equals(subsystemId))
      {
        //logger.debug("subsystem - found " + subsystemId + " - " + subsystem.toString());
        subsystem.setStage(newSubSystemStage.getStage());

        try
        {
          if (USING_CLOUD)
          {
            try
            {
              SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
              SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
              BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
              bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUD_ENDPOINT);

              systemConfigurator.changeSubSystemStage(subsystemId, newSubSystemStage.getStage());
            }
            catch (Exception ex)
            {
              System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
            }
          }
        }
        catch (Exception ex)
        {

        }

        return new SubSystemStage(subsystem.getUniqueId(), subsystem.getStage());
      }
    }
    logger.debug("subsystem - not found " + subsystemId + " - returning null");
    return null; // TBV
  }

}
