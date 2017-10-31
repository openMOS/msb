package eu.openmos.msb.services.rest;

//import _masmec.aml5;
//import _masmec.MasmecModel;
import eu.openmos.model.*;
import eu.openmos.model.testdata.RecipeTest;
import eu.openmos.model.testdata.SkillTest;
import eu.openmos.agentcloud.utilities.Constants;
import eu.openmos.model.testdata.ExecutionTableTest;
import eu.openmos.model.testdata.ModuleTest;
import eu.openmos.model.testdata.SubSystemTest;
import eu.openmos.msb.datastructures.DACManager;
import eu.openmos.msb.datastructures.DeviceAdapter;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import eu.openmos.msb.services.rest.data.ExecutionTableRowHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio Gatto <antonio.gatto@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
@Path("/api/v1/subsystems")
public class SubSystemController extends Base {

    private final Logger logger = Logger.getLogger(SubSystemController.class.getName());
    private final StopWatch HMIsubsystemUpdateWatch = new StopWatch();

    /**
     * Returns the list of workstations and transports, e.g. resource and
     * transport agents. Fills the system overview page (slide 5 of 34)
     *
     * @return list of cyberphysicalagentdescription objects. List can be empty,
     * cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubSystem> getList() {
      
      HMIsubsystemUpdateWatch.start();
      
        logger.debug("subsystems getList");
        List<SubSystem> ls = new LinkedList<>();
        logger.debug("subsystems getList 1");

//        ls = aml5.getMasmecSubSystems();
        //ls = MasmecModel.getInstance().getSubSystems();
        DACManager aux = DACManager.getInstance();
        logger.debug("subsystems getList 2");
        
        List<String> deviceAdapters = aux.getDeviceAdapters();
        logger.debug("subsystems getList 2.5: " + deviceAdapters );
        
        for (String adapterName : deviceAdapters)
        {
            if (adapterName.contains("MSB") || adapterName.contains("fortiss"))
                continue;
          logger.debug("subsystems getList 3: " + adapterName);
          DeviceAdapter adapter = aux.getDeviceAdapter(adapterName);
            logger.debug("subsystems getList 4 ");
          // VaG - 28/09/2017
          // begin
          if (adapter != null)
          {
            logger.debug("subsystems getList 5 - adapter is not null ");
            SubSystem ss = adapter.getSubSystem();
            if (ss != null)
            {
                logger.debug("subsystems getList 6 - subsystem is not null ");
                logger.debug("subsystems getList 7 - subsystem name: " + ss.getName());
                // assume the name is populated
                if (ss.getUniqueId() == null || ss.getUniqueId().length() == 0)
                  ss.setUniqueId(ss.getName());
                logger.debug("subsystems getList 8");
                if (ss.getDescription() == null || ss.getDescription().length() == 0)
                  ss.setDescription(ss.getName());
                logger.debug("subsystems getList 9");
                ss.setRegistered(new Date());
                logger.debug("subsystems getList 10");
                ls.add(ss);
                logger.debug("subsystems getList 11");
                  // end
            }
          }
        }
//        for (int i = 0; i < 5; i++)
//        {
//            SubSystem subsystem1 = SubSystemTest.getTestObject();
//            if (i%2 == 0)
//            {
//                subsystem1.setName("WORKSTATION_" + i);
//                subsystem1.setUniqueId("WORKSTATION_" + i);
//                subsystem1.setType(Constants.DF_RESOURCE);
//            }
//            else
//            {
//                subsystem1.setName("TRANSPORT_" + i);
//                subsystem1.setUniqueId("TRANSPORT_" + i);
//                subsystem1.setType(Constants.DF_TRANSPORT);
//            }
//                
//            ls.add(subsystem1);
//        }
        logger.debug("susbsytem list from MSB: ");
        if (ls == null)
            logger.debug("susbsytem list from MSB: ls is null");
        if (ls.size() == 0)
            logger.debug("susbsytem list from MSB: ls size is 0");            
        if (ls.size() != 0)
            logger.debug("susbsytem list from MSB: ls size is " + ls.size());            
        // logger.debug("susbsytem list from MSB: " + ls);
         
        PerformanceMasurement perfMeasure = PerformanceMasurement.getInstance();
        perfMeasure.getHMISubsystemUpdateTimers().add(HMIsubsystemUpdateWatch.getTime());
        HMIsubsystemUpdateWatch.stop();
                
        return ls;
    }

    /**
     * Returns the detail of a given workstation or transport, e.g. of a
     * resource or transport agent. Fills the workstation view page (slide 6 of
     * 34) or the transport view (slide 7 of 34) according to the type field of
     * the subsystem.
     *
     * @return detail of cyberphysical agent
     *
     * @param subsystemId the unique id of the workstation or transport
     * @return cyberphysicalagentdescription object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}")
    public SubSystem getDetail(@PathParam("subsystemId") String subsystemId) {
        logger.debug("subsystem - getDetail - subsystemId = " + subsystemId);
        for (SubSystem subsystem : getList()) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subsystemId)) {
                logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
                return subsystem;
            }
        }
        logger.debug("subsystem - not found " + subsystemId + " - returning null");
        return null; // TBV
    }

    /**
     * Returns the list of recipes associated to a workstation or a transport.
     * Fills the skills recipe list (slide 22 of 34) This method is exposed via
     * a "/subsystems/{subsystemId}/recipes" service call.
     *
     * @param subsystemId subsystem id, i.e. the agent unique identifier.
     * @return list of recipe objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/recipes")
    public List<Recipe> getRecipesList(@PathParam("subsystemId") String subsystemId) {
        logger.debug("subsystem - getRecipesList - subsystemId = " + subsystemId);
        logger.debug("subsystem getRecipesList - of the subsystem = " + subsystemId);
//        return RecipeTest.getTestList();
        for (SubSystem subsystem : getList()) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subsystemId)) {
                logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
                return subsystem.getRecipes();
            }
        }
        logger.debug("subsystem - not found " + subsystemId + " - returning null");
        return null; // TBV
    }

    /**
     * Allows to insert a new recipe associated to a workstation or a transport.
     * Returns the updated list of recipes associated to the same workstation or
     * transport. Fills the skills recipe creation view (slide 23 of 34) This
     * method is exposed via a POST to "/subsystems/{subsystemId}/recipes"
     * service call.
     *
     * @param subsystemId the agent unique identifier.
     * @param newRecipe the recipe to be inserted.
     * @return list of recipe objects associated to the same subsystem
     * (workstation or transport). List can be empty, cannot be null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/recipes")
    public List<Recipe> newRecipe(@PathParam("subsystemId") String subsystemId, 
            Recipe newRecipe) {   
        getDetail(subsystemId).getRecipes().add(newRecipe);
        
        return getRecipesList(subsystemId);
    }

    /**
     * Returns the list of sub-modules associated to a workstation or a
     * transport. Fills the sub system view page (slide 14 of 34). This method
     * is exposed via a "/subsystems/{subsystemId}/modules" service call.
     *
     * @param subsystemId subsystem id, i.e. the agent unique identifier.
     * @return list of modules objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/modules")
    public List<Module> getModulesList(@PathParam("subsystemId") String subsystemId) {
        logger.debug("subsystem - getEquipmentsList - subsystemId = " + subsystemId);
        logger.debug("subsystem getEquipmentsList - of the subsystem = " + subsystemId);
//        return ModuleTest.getTestList(subsystemId);
        for (SubSystem subsystem : getList()) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subsystemId)) {
                logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
                return subsystem.getInternalModules();
            }
        }
        logger.debug("subsystem - not found " + subsystemId + " - returning null");
        return null; // TBV
    }

    /**
     * Returns the list of skills associated to a workstation or a transport.
     * Fills the skills list view page (slide 16 of 34). This method is exposed
     * via a "/subsystems/{subsystemId}/skills" service call.
     *
     * @param subsystemId subsystem id, i.e. the agent unique identifier.
     * @return list of skills objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/skills")
    public List<Skill> getSkillsList(@PathParam("subsystemId") String subsystemId) {
        logger.debug("subsystem - getSkillsList - subsystemId = " + subsystemId);
        logger.debug("subsystem getSkillsList - of the subsystem = " + subsystemId);
//        return SkillTest.getTestList();
        for (SubSystem subsystem : getList()) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subsystemId)) {
                logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
                return subsystem.getSkills();
            }
        }
        logger.debug("subsystem - not found " + subsystemId + " - returning null");
        return null; // TBV
    }

    /**
     * Allows to insert a new skill associated to a workstation or a transport.
     * Returns the updated list of skills associated to the same workstation or
     * transport. Fills the composite skill creation view (slide 21 of 34) This
     * method is exposed via a POST to "/subsystems/{subsystemId}/skills"
     * service call.
     *
     * @param subsystemId the agent unique identifier.
     * @param newSkill the skill to be inserted.
     * @return list of skill objects associated to the same subsystem
     * (workstation or transport). List can be empty, cannot be null.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/skills")
    public List<Skill> newCompositeSkill(@PathParam("subsystemId") String subsystemId, Skill newSkill) {
        logger.debug("subsystem - newCompositeSkill - subsystemId = " + subsystemId);
        logger.debug("subsystem newCompositeSkill - skill to insert = " + newSkill.toString());
        return getSkillsList(subsystemId);
    }

    /**
     * Returns the full execution table given its unique identifier. Fills the
     * execution table view page (slide 8 of 34).
     *
     * @return detail of execution table
     *
     * @param uniqueId the unique id of the execution table
     * @return executiontable object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{subsystemId}/executionTable")
    public ExecutionTable getExecutionTable(@PathParam("subsystemId") String subsystemId) {
        logger.debug("execution table getDetail of subsystem  = " + subsystemId);
        for (SubSystem subsystem : getList()) {
            // VaG - 19/10/2017
            // Since we have ids....
//            if (subsystem.getName().equals(subsystemId)) {
            if (subsystem.getUniqueId().equals(subsystemId)) {
                logger.debug("subsystem - found " + subsystemId + " - returning " + subsystem.toString());
                return subsystem.getExecutionTable();
            }
        }
        logger.debug("subsystem - not found " + subsystemId + " - returning null");
        return null; // TBV
    }
}
