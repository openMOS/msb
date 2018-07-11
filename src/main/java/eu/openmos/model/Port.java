package eu.openmos.model;

import java.util.Date;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * Abstract base class for port concept.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public abstract class Port extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757022L;
    
    /**
     * Port ID.
     */
    protected String uniqueId;
    /**
     * Port name.
     */
    protected String name;
    /**
     * Port description.
     */
    protected String description;
    /**
     * Port type. It's a logical way of grouping different ports.
     * Refactoring from type to portType
     */
    protected String portType;    
    /**
     * Port direction.
     */
    protected String direction;    

    /**
     * Default constructor.
     */
    public Port() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param uniqueId - Port ID.
     * @param name - Port name.
     * @param description - Port description.
     * @param type - Port type
     * @param direction - Port direction
     * @param registeredTimestamp
     */
    public Port(String uniqueId, String name, String description, 
            String type, String direction, Date registeredTimestamp) {
        super(registeredTimestamp);

        this.uniqueId = uniqueId;
        this.name = name;
        this.description = description;        
        this.portType = type;
        this.direction = direction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
