package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
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
 * Information port implementation.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class InformationPort extends Port implements Serializable {
    private static final Logger logger = Logger.getLogger(Module.class.getName());    
    private static final long serialVersionUID = 6529685098267757006L;
    
    private List<KPI> kpis;

    /**
     * Default constructor, for reflection purpose.
     */
    public InformationPort() {super();}

    /**
     * Constructor.
     * 
     * @param uniqueId
     * @param name
     * @param description
     * @param type
     * @param direction
     * @param kpis
     * @param registeredTimestamp 
     */
    public InformationPort(String uniqueId, String name, String description, 
            String type, String direction, List<KPI> kpis, Date registeredTimestamp) {
        super(uniqueId, name, description, type, direction, registeredTimestamp);
        
        this.kpis = kpis;
    }

    public List<KPI> getKpis() {
        return kpis;
    }

    public void setKpis(List<KPI> kpis) {
        this.kpis = kpis;
    }
    

    /**
     * Method that serializes the object into a BSON document.
     * 
     * @return BSON Document format of the object. 
     */
//    public Document toBSON() {
//        return toBSON2();
//    }
    public Document toBSON() {
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        String stringRegisteredTimestamp = sdf.format(this.registered);
        
        List<String> kpiIds = null;
        if (kpis != null)
            kpiIds = kpis.stream().map(kpi -> kpi.getUniqueId()).collect(Collectors.toList());        

        return new Document()
                .append(DatabaseConstants.DESCRIPTION, description)
                .append(DatabaseConstants.UNIQUE_ID, uniqueId)
                .append(DatabaseConstants.NAME, name)
                .append(DatabaseConstants.TYPE, type)
                .append(DatabaseConstants.DIRECTION, direction)
                .append(DatabaseConstants.KPI_IDS, kpiIds)
                .append(DatabaseConstants.REGISTERED, stringRegisteredTimestamp);
    }    
    
    /**
     * Method that deserializes a BSON object.
     * 
     * @param bsonPort - BSON to be deserialized.
     * @return Deserialized object.
     */
//    public static InformationPort fromBSON(Document bsonPort)
//    throws ParseException 
//    {
//            return (InformationPort)InformationPort.fromBSON2(bsonPort, InformationPort.class);
//    }        
}
