package eu.openmos.model;

import static eu.openmos.model.Base.fromBSON2;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 *
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ParameterPort extends Port implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757016L;
    
    private List<Parameter> parameters;

    public ParameterPort() {super();}

    public ParameterPort(String uniqueId, String name, String description, 
            String type, String direction, List<Parameter> parameters, Date registeredTimestamp) {
        super(uniqueId, name, description, type, direction, registeredTimestamp);
        
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
    

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);
        
        List<String> parameterIds = null;
        if (parameters != null)
            parameterIds = parameters.stream().map(parameter -> parameter.getUniqueId()).collect(Collectors.toList());        

        return new Document()
                .append("description", description)
                .append("uniqueId", uniqueId)
                .append("name", name)
                .append("type", type)
                .append("direction", direction)
                .append("parameterIds", parameterIds)
                .append("registered", stringRegisteredTimestamp);
    }    
}
