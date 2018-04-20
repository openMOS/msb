package eu.openmos.msb.services.rest;

import eu.openmos.model.*;
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
@Path("/api/v1/modules")
public class ModuleController extends Base {            
    private final Logger logger = Logger.getLogger(ModuleController.class.getName());
    
    /**
     * Returns the module object given its unique identifier.
     * Fills the module view page (slide 15 of 34). 
     * 
     * @param modulePath
     * @return detail of module
     * 
     * @param moduleId the unique id of the module
     * @return module object, or null if not existing
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{moduleId}")
    public Module getDetail(@PathParam("moduleId") String modulePath) {
        logger.debug("moduleController - getDetail - moduleId = " + modulePath);            
        
        Module moduleToReturn = null;       
        PathHelper helper = new PathHelper(modulePath, logger);
        String subSystemId = helper.getSubSystemId();
        
        SubSystem subsystem = (new SubSystemController()).getDetail(subSystemId);
        if (subsystem != null) {
            logger.debug("subsystem - found " + subSystemId);
            moduleToReturn = searchForModule(subsystem.getModules(), 
                    helper.getModulesIds().toArray(new String[helper.getModulesIds().size()]), 
                    0);
        }
        logger.debug("module - " + modulePath + " - returning " + moduleToReturn);
        return moduleToReturn;
    }
    
   /**
     * Returns the list of sub-modules associated to a module.
     * Fills the sub system view page (slide 14 of 34).
     * This method is exposed via a "/modules/{moduleId}/modules" service call.
     * 
     * @param moduleId   module id, i.e. the module unique identifier.
     * @return list of modules objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{moduleId}/modules")    
    public List<Module> getModulesList(@PathParam("moduleId") String modulePath){
        logger.debug("Moduler Controller - getModulesList ");
        Module module = this.getDetail(modulePath);
        return module != null ? module.getInternalModules() : null;
    }

    private Module searchForModule(List<Module> modulesList, String[] moduleNames, int currentModuleNamesIndex) {
        if (modulesList == null || modulesList.isEmpty()) {
            return null;
        }

        String moduleId = moduleNames[currentModuleNamesIndex];
        for (Module mod : modulesList) {
            if (mod.getUniqueId().equals(moduleId)) {
                logger.debug("module - found " + moduleId);
                if (currentModuleNamesIndex == (moduleNames.length - 1)){
                    logger.debug("ModuleController - SearchForModule - returning: " + mod);
                    return mod;
                } else {
                    return searchForModule(mod.getInternalModules(), moduleNames, ++currentModuleNamesIndex);
                }
            }
        }        
        return null;
    }
    
   /**
     * Returns the list of skills associated to the given module.
     * Fills the skills list view page (slide 16 of 34).
     * This method is exposed via a "/modules/{moduleId}/skills" service call.
     * 
     * @param moduleId   moduleId id, i.e. the module unique identifier.
     * @return list of skills objects. List can be empty, cannot be null.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{moduleId}/skills")    
    public List<Skill> getSkillsList(@PathParam("moduleId") String modulePath) {
        logger.debug("Module Controller - getSkillsList - path = " + modulePath);
        Module module = this.getDetail(modulePath);
        return module != null ? module.getSkills() : null;
    }
        
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path(value = "/{moduleId}/recipes")
    public List<Recipe> getModuleRecipes(@PathParam("moduleId") String modulePath) {        
        logger.debug("Module Controller - getRecipeList - path = " + modulePath);        
        Module module = this.getDetail(modulePath);
        logger.debug("Get Module detail: " + module);        
        return module != null ? module.getRecipes() : null;
    }
 
    
    
    ////////////////////////////////////////////////////////////////////////////////
    
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{moduleId}/modules")    
    public List<Module> getModulesList_OLD(@PathParam("moduleId") String moduleId)
    {
        List<Module> listToReturn = null;
        
        logger.debug("modules - getModulesList - moduleId = " + moduleId);
        
        // moduleId can be in this form subsystem-module-module-module....
        String[] n = moduleId.split(ModuleController.PARAMSEPARATOR);
        if (n.length < 2)
        {
            // TODO url has some problems 
        }
        
        String[] nn = n[0].split(ModuleController.PARAMVALUESEPARATOR);
        String subSystemId = nn[1];
        
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

//                listToReturn = searchForModule(subsystem.getInternalModules(), n, 1);
                listToReturn = searchForModule(subsystem.getInternalModules(), n, 1).getInternalModules();
            }
        }
        logger.debug("module - " + moduleId + " - returning " + listToReturn);
        
        if (listToReturn == null)
            listToReturn = new LinkedList<Module>();
        
        return listToReturn;
    }
    */
    
    
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{moduleId}/skills")    
    public List<Skill> getSkillsList_OLD(@PathParam("moduleId") String moduleId)
    {      
        String[] ids = moduleId.split(Base.PARAMSEPARATOR);
        
        String subSystemId = "";
        String moduleRealId = "";
        
        if (ids != null && ids.length > 0) {
            subSystemId = ids[0].split(Base.PARAMVALUESEPARATOR)[1];
            logger.debug(subSystemId);
            moduleRealId = ids[1].split(Base.PARAMVALUESEPARATOR)[1];
            logger.debug(moduleRealId);
            
            
            SubSystemController ss = new SubSystemController();
            
            List<Module> subSystemModules = ss.getModulesList(subSystemId);
            
            for (Module mod : subSystemModules) {
                if (mod.getUniqueId().equalsIgnoreCase(moduleRealId)) {
                    logger.debug("Found module: " + moduleRealId);
                    return mod.getSkills();
                }
            }
        }
        
        logger.debug("Some error return empty list");
        return new ArrayList<>();
        /// TO REMOVE
        //return SkillTest.getTestList();
    }    
    */
    
    
    /*
    private Module searchForModule(List<Module> modulesList, String[] moduleNames, int currentModuleNamesIndex)
    {
        if (modulesList == null || modulesList.size() == 0)
            return null;

        // String[] kk = moduleNames[currentModuleNamesIndex].split(ModuleController.PARAMVALUESEPARATOR);
        // String moduleId = kk[1];
        String moduleId = moduleNames[currentModuleNamesIndex];
        for (Module mod : modulesList)
        {
//            if (mod.getName().equals(moduleNames[currentModuleNamesIndex]))
            if (mod.getName().equals(moduleId))
            {
//                logger.debug("module - found " + moduleNames[currentModuleNamesIndex]);
                logger.debug("module - found " + moduleId);
                // found 
                if (currentModuleNamesIndex == (moduleNames.length - 1)){
                    logger.debug("ModuleController - SearchForModule - returning: " + mod);
                    return mod; // mod.getInternalModules();
                } else {
                    return searchForModule(mod.getInternalModules(), moduleNames, ++currentModuleNamesIndex);
                }
            }
        }
        
        return null;
    }
    */
    
    
    /*
    public Module getDetail_OLD(@PathParam("moduleId") String modulePath) {
        logger.debug("module - getDetail - moduleId = " + modulePath);            
        Module moduleToReturn = null;
       
        // moduleId can be in this form subsystem-module-module-module....
        String[] n = moduleId.split(ModuleController.PARAMSEPARATOR);
        if (n.length < 2)
        {
            // TODO url has some problems 
        }
        
        logger.debug("n di 0 = " + n[0]);
        logger.debug("n di 1 = " + n[1]);
        String[] nn = n[0].split(ModuleController.PARAMVALUESEPARATOR);
        logger.debug("nn length " + nn.length);
        for (int z = 0;z < nn.length; z++)
            logger.debug("nn di " + z + " = " + nn[z]);
            
        
        String subSystemId = nn[1];
        
        PathHelper helper = new PathHelper(moduleId, logger);
        String subSystemId = helper.getSubSystemId();
        
        for (SubSystem subsystem : (new SubSystemController()).getList()) {
//            if (subsystem.getName().equals(n[0])) {
            if (subsystem.getUniqueId().equals(subSystemId)) {
                logger.debug("subsystem - found " + subSystemId);

                // listToReturn = searchForModule(subsystem.getInternalModules(), n, 1);
                moduleToReturn = searchForModule(subsystem.getInternalModules(), n, 1);
            }
        }
        logger.debug("module - " + moduleId + " - returning " + moduleToReturn);
        
        if (listToReturn == null)
            listToReturn = new LinkedList<Module>();
        return moduleToReturn;
    }*/
    
    
    
    /*
    private List<Module> searchForModule_ORIGINAL(List<Module> modulesList, String[] moduleNames, int currentModuleNamesIndex)
    {
        if (modulesList == null || modulesList.size() == 0)
            return null;

        String[] kk = moduleNames[currentModuleNamesIndex].split(ModuleController.PARAMVALUESEPARATOR);
        String moduleId = kk[1];
        for (Module mod : modulesList)
        {
//            if (mod.getName().equals(moduleNames[currentModuleNamesIndex]))
            if (mod.getName().equals(moduleId))
            {
//                logger.debug("module - found " + moduleNames[currentModuleNamesIndex]);
                logger.debug("module - found " + moduleId);
                // found 
                if (currentModuleNamesIndex == (moduleNames.length - 1))
                    return mod.getInternalModules();
                else
                {
                    return searchForModule_ORIGINAL(mod.getInternalModules(), moduleNames, ++currentModuleNamesIndex);
                }
            }
        }
        
        return null;
    }*/
}