package eu.openmos.model;

import java.io.Serializable;

/**
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class SubSystemStage implements Serializable {
    private static final long serialVersionUID = 6529685098267757497L;    

    private String subSystemId;
    private String stage;

    // reflection stuff
    public SubSystemStage() { super();    }

    public SubSystemStage(
            String subSystemId,
            String stage) {
        
        this.subSystemId = subSystemId;                
        this.stage = stage;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }    
    
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
