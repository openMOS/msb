package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class StartedProductInfo extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757499L;    

    private String productInstanceId;
    private Date startedTime;

    // reflection stuff
    public StartedProductInfo() { super();    }

    public StartedProductInfo(
            String productInstanceId, 
            Date startedTime,
            Date registeredTimestamp) {
        
        super(registeredTimestamp);

        this.productInstanceId = productInstanceId;
        this.startedTime = startedTime;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public Document toBSON() {
        return toBSON2();
    }
}
