package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import java.util.Date;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes an actual setting of a Physical Adjustment Parameter, 
 * i.e. the new current value.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class PhysicalAdjustmentParameterSetting extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(PhysicalAdjustmentParameterSetting.class.getName());
    private static final long serialVersionUID = 6529685098267757717L;

    /**
     * Physical Adjustment Parameter Setting ID.
     */
    private String uniqueId;
    /**
     * Physical Adjustment Parameter Setting name.
     */
    private String name;
    /**
     * Physical Adjustment Parameter Setting description.
     */
    private String description;
    /**
     * Physical Adjustment Parameter Setting value.
     */
    private String value;
    /**
     * Pointer to the Physical Adjustment Parameter.
     */
    private PhysicalAdjustmentParameter physicalAdjustmentParameter;    

    /**
     * Default constructor.
     */
    public PhysicalAdjustmentParameterSetting() {super();}
    
    /**
     * Parameterized constructor.
     * 
     * @param description - Physical Adjustment Parameter Setting's description.
     * @param id - Physical Adjustment Parameter Setting's ID.
     * @param name - Physical Adjustment Parameter Setting's name.
     * @param value - Physical Adjustment Parameter Setting's value.
     * @param parameter - pointer to the Physical Adjustment Parameter
     * @param registeredTimestamp - registration timestamp
     */
    public PhysicalAdjustmentParameterSetting(String description, 
            String id, 
            String name, 
            String value,
            PhysicalAdjustmentParameter physicalAdjustmentParameter,
            Date registeredTimestamp
    ) {
        super(registeredTimestamp);
        
        this.description = description;
        this.uniqueId = id;
        this.name = name;
        this.value = value;
        
        this.physicalAdjustmentParameter = physicalAdjustmentParameter;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PhysicalAdjustmentParameter getPhysicalAdjustmentParameter() {
        return physicalAdjustmentParameter;
    }

    public void setPhysicalAdjustmentParameter(PhysicalAdjustmentParameter physicalAdjustmentParameter) {
        this.physicalAdjustmentParameter = physicalAdjustmentParameter;
    }
    
     /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);
        
        String physicalAdjustmentParameterId = null;
        if (physicalAdjustmentParameter != null)
            physicalAdjustmentParameterId = physicalAdjustmentParameter.getUniqueId();
        
        return new Document(DatabaseConstants.DESCRIPTION, description)
                .append(DatabaseConstants.UNIQUE_ID, uniqueId)
                .append(DatabaseConstants.NAME, name)
                .append(DatabaseConstants.VALUE, value)
                .append(DatabaseConstants.PHYSICAL_ADJUSTMENT_PARAMETER_ID, physicalAdjustmentParameterId)
                .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
    }
}
