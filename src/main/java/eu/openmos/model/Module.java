package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Modules that compose subsystems.
 * Can have internal modules inside as well.
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Module extends Equipment implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());
    private static final long serialVersionUID = 6529685098267757011L;
    
    /**
     * List of internal modules, can be empty.
     */
    protected List<Module> internalModules;
    
    /**
     * Pointer to the parent equipment, can be null.
     */
    protected String parentId;
    /**
     * Type of parent equipment, can be a subsystem or another module.
     */
    protected String parentType;

        private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public List<Module> getInternalModules() {
        return internalModules;
    }

    public void setInternaleModules(List<Module> internalModules) {
        this.internalModules = internalModules;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
    
    

    /**
     * Empty constructor, for reflection purpose.
     */
    public Module() {super();}

    /**
     * Parameterized constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param connected
     * @param skills
     * @param ports
     * @param internalModules
     * @param address
     * @param status
     * @param manufacturer
     * @param registeredTimestamp 
     */
    public Module(
            String uniqueId, 
            String name, 
            String description, 
            boolean connected,
            List<Skill> skills,
            List<PhysicalPort> ports,
            List<PhysicalAdjustmentParameter> physicalAdjustmentParameters,
            List<Module> internalModules,
            List<Recipe> recipes,
            String address,
            String status,
            String manufacturer,
            String parentId,
            String parentType,
            Date registeredTimestamp
    ) 
    {
        super(uniqueId, name, description, connected, skills, ports,
                physicalAdjustmentParameters,
                address, status, manufacturer, registeredTimestamp);

        this.internalModules = internalModules;
        this.recipes = recipes;
        this.parentId = parentId;
        this.parentType = parentType;
    }    

     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();

        List<String> skillIds = null;
        if (skills != null)
            skillIds = skills.stream().map(skill -> skill.getUniqueId()).collect(Collectors.toList());        
        
        List<String> physicalPortIds = null;
        if (physicalPorts != null)
            physicalPortIds = physicalPorts.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());        
        
        List<String> physicalAdjustmentParameterIds = null;
        if (physicalAdjustmentParameters != null)
            physicalAdjustmentParameterIds = physicalAdjustmentParameters.stream().map(port -> port.getUniqueId()).collect(Collectors.toList());
        
        List<String> moduleIds = null;
        if (internalModules != null)
            moduleIds = internalModules.stream().map(module -> module.getUniqueId()).collect(Collectors.toList());        
        
        List<String> recipeIds = null;
        if (recipes != null)
            recipeIds = recipes.stream().map(recipe -> recipe.getUniqueId()).collect(Collectors.toList());        
        
        doc.append(DatabaseConstants.UNIQUE_ID, uniqueId);
        doc.append(DatabaseConstants.NAME, name);
        doc.append(DatabaseConstants.DESCRIPTION, description);
        doc.append(DatabaseConstants.CONNECTED, connected);
        doc.append(DatabaseConstants.SKILL_IDS, skillIds);        
        doc.append(DatabaseConstants.PHYSICAL_PORT_IDS, physicalPortIds);        
        doc.append(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_IDS, physicalAdjustmentParameterIds); 
        doc.append(DatabaseConstants.INTERNAL_MODULE_IDS, moduleIds);        
        doc.append(DatabaseConstants.RECIPE_IDS, recipeIds);        
        doc.append(DatabaseConstants.ADDRESS, address);
        doc.append(DatabaseConstants.STATUS, status);
        doc.append(DatabaseConstants.MANUFACTURER, manufacturer);
        doc.append(DatabaseConstants.PARENT_ID, parentId);
        doc.append(DatabaseConstants.PARENT_TYPE, parentType);
        doc.append(DatabaseConstants.REGISTERED, new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION).format(this.registered));
        
        return doc;
    }
}
