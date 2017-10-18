package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.bson.Document;

/**
 * Object that describes an actual setting of a KPI, i.e., a possible value for 
 * it.
 * Into the semantic model diagrams this class is known as "KPIForecast".
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Luis Ribeiro
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class KPISetting extends Base implements Serializable {
    private static final Logger logger = Logger.getLogger(KPISetting.class.getName());
    private static final long serialVersionUID = 6529685098267757008L;
    
    /**
     * KPI Setting ID.
     */
    private String uniqueId;
    /**
     * KPI Setting name.
     */
    private String name;
    /**
     * KPI Setting description.
     */
    private String description;
    /**
     * Pointer to the kpi.
     */
    private KPI kpi; 
    /**
     * KPI Setting value type.
     */
    private String type;
    /**
     * KPI Setting value unit.
     */
    private String unit;
    /**
     * KPI Setting value.
     */
    private String value;
    
    //private transient NodeId path;
    
    private transient String path;

    /**
     * Default constructor, for reflection.
     */
    public KPISetting() {super();}
    
    /** 
     * Parameterized constuctor.
     * 
     * @param description - KPI Setting's description.
     * @param id - KPI Setting's ID.
     * @param name - KPI Setting's name.
     * @param value - KPI Setting's value.
     * @param kpi - pointer to the KPI
     * @param registeredTimestamp - registration timestamp
     */
    public KPISetting(String description, 
            String id, 
            String name, 
            KPI kpi,
            String type,
            String unit,
            String value,
            Date registeredTimestamp) {
        super(registeredTimestamp);
        
        this.description = description;
        this.uniqueId = id;
        this.name = name;
        
        this.type = type;
        this.unit = unit;
        this.value = value;
        
        this.kpi = kpi;
        
        this.path = null;
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

    public void setUniqueId(String id) {
        this.uniqueId = id;
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

    public KPI getKpi() {
        return kpi;
    }

    public void setKpi(KPI kpi) {
        this.kpi = kpi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    
    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);
        
        String kpiId = null;
        if (kpi != null)
            kpiId = kpi.getUniqueId();
        
        return new Document()
                .append(DatabaseConstants.UNIQUE_ID, uniqueId)
                .append(DatabaseConstants.NAME, name)
                .append(DatabaseConstants.DESCRIPTION, description)
                .append(DatabaseConstants.KPI_ID, kpiId)
                .append(DatabaseConstants.TYPE, type)
                .append(DatabaseConstants.UNIT, unit)
                .append(DatabaseConstants.VALUE, value)
                .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
    }
}
