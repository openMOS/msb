package eu.openmos.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Modules that compose subsystems.
 * Can have internal modules inside as well.
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class Module extends Equipment implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());
    private static final long serialVersionUID = 6529685098267757689L;
    
    protected List<Module> internalModules;

    public List<Module> getInternalModules() {
        return internalModules;
    }

    public void setInternaleModules(List<Module> internalModules) {
        this.internalModules = internalModules;
    }

    // for reflection purpose
    public Module() {super();}

    public Module(
            String uniqueId, 
            String name, 
            String description, 
            boolean connected,
            List<Skill> skills,
            List<Module> internalModules,
            String address,
            String status,
            String manufacturer,
            Date registeredTimestamp
    ) {
        super(uniqueId, name, description, connected, skills, address, status, manufacturer, registeredTimestamp);
        this.setInternaleModules(internalModules);
        }
    
//    public static Module deserialize(String object) 
//    {        
//        Module objectToReturn = null;
//        try 
//        {
//            ByteArrayInputStream bIn = new ByteArrayInputStream(object.getBytes());
//            ObjectInputStream in = new ObjectInputStream(bIn);
//            objectToReturn = (Module) in.readObject();
//            in.close();
//            bIn.close();
//        }
//        catch (IOException i) 
//        {
//            logger.error(i);
//        }
//        catch (ClassNotFoundException c) 
//        {
//            logger.error(c);
//        }
//        return objectToReturn;
//    }
}
