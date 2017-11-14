package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

public class SubSystemRecipes extends Base implements Serializable {    
    private static final Logger logger = Logger.getLogger(SubSystemRecipes.class.getName());
    private static final long serialVersionUID = 6529685098267757032L;

    private String uniqueId;
    
    /**
     * The recipes the agent can apply (Resource/Transport Agent).
     */
//    @XmlElement(name = "recipes")
    private List<Recipe> recipes;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    

    /**
     * Default constructor, for reflection purpose.
     */
    public SubSystemRecipes() { super(); }

    public SubSystemRecipes(
            String uniqueId, 
            List<Recipe> recipes, 
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);

        this.uniqueId = uniqueId;
        this.recipes = recipes;
        
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
