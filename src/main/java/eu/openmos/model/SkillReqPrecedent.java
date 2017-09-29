package eu.openmos.model;

import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class SkillReqPrecedent extends Base {
    private static final Logger logger = Logger.getLogger(SkillReqPrecedent.class.getName());
    private static final long serialVersionUID = 6529685098267757027L;
    
    String uniqueId;
    String name;

    public SkillReqPrecedent() {
        super();
    }

    public SkillReqPrecedent(String uniqueId, String name) {
        super();
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
     
}
