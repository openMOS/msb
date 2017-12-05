package eu.openmos.msb.services.rest;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public final class PathHelper extends Base {
    private String subSystemId;
   
    private List<String> modulesIds;
    
    private String skillId;
    
    private String recipeId;
    
    private String completePath;
    
    private Logger logger;
    
    public PathHelper(String path, Logger logger) {
        this.completePath = path;
        this.modulesIds = new ArrayList<>();
        this.logger = logger;
        this.startComponents();
    }    
    
    public String getSubSystemId(){
        return this.subSystemId;
    }
    
    public List<String> getModulesIds(){
        return this.modulesIds;
    }
    
    public String getSkillId(){
        return this.skillId;
    }
    
    public String getRecipeId(){
        return this.recipeId;
    }
    
    public String getCompletePath(){
        return this.completePath;
    }
    
    public void startComponents(){
        this.subSystemId = this.calculateSubSystemId(completePath);
        this.modulesIds = this.calculateModulesIds(completePath);
        this.skillId = this.calculateSkillId(completePath);
        this.recipeId = this.calculateRecipeId(completePath);
    }
    
    private String[] splitParam(String completePath) {
        return completePath.split(Base.PARAMSEPARATOR);
    }
    
    private String getComponentValue(String component){
        return component.split(Base.PARAMVALUESEPARATOR)[1];
    }
    
    public String calculateSubSystemId(String completePath){
        String[] pathParts = this.splitParam(completePath);
        
        if (pathParts != null && pathParts.length > 0) {
            return pathParts[0] != null && (this.getComponentValue(pathParts[0]) != null) ?
                    this.getComponentValue(pathParts[0])
                    : null;
        }     
        return null;
    }
    
    public List<String> calculateModulesIds(String completePath){
        String[] pathParts = this.splitParam(completePath);
        List<String> ids = new ArrayList<>();
        
        for (String part : pathParts){
            logger.debug("PART : " + part);
            if (part.contains(Base.MODULEMARKERPREFIX + Base.PARAMVALUESEPARATOR)) {
                logger.debug("ADD PART " + part);
                ids.add(this.getComponentValue(part));
            }
        }
        return ids;
    }
    
    public String calculateSkillId(String completePath){
        String[] pathParts = this.splitParam(completePath);
        
        for (String part : pathParts) {
            if (part.contains(Base.SKILLMARKERPREFIX + Base.PARAMVALUESEPARATOR)) {
                return this.getComponentValue(part);
            }
        }        
        return null;
    }
    
    public String calculateRecipeId(String completePath) {
        String[] pathParts = this.splitParam(completePath);
        
        for (String part : pathParts) {
            if (part.contains(Base.RECIPEMARKERPREFIX + Base.PARAMVALUESEPARATOR)) {
                return this.getComponentValue(part);
            }
        }
        return null;
    }
    
    public String getModulesPath() {
        String path = Base.SKILLMARKERPREFIX 
                + Base.PARAMVALUESEPARATOR 
                + this.subSystemId;
        
        for(int i = 0; i < this.modulesIds.size(); i++) {
            path += Base.PARAMSEPARATOR + Base.MODULEMARKERPREFIX 
                    + Base.PARAMVALUESEPARATOR
                    + this.modulesIds.get(i);
        }
        return path;
    }
    
    public boolean hasSubModules(){
        return !(this.modulesIds.isEmpty());
    }
    
    public String resultToString(){
        return "SS ID : " + this.subSystemId + '\n'
                + "Modules : " + this.modulesIds.toString() + '\n'
                + "Skill : " + this.skillId + '\n'
                + "Recipe : " + this.recipeId + '\n'
                + "Modules parts: " + this.getModulesPath() + '\n'
                + "Has subModules: " + this.hasSubModules();
    }
}
