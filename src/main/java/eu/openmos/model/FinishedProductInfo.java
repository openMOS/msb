package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;
import org.bson.Document;

/**
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class FinishedProductInfo extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757499L;    

    private String productInstanceId;
    private Date finishedTime;

    // reflection stuff
    public FinishedProductInfo() { super();    }

    public FinishedProductInfo(
            String productInstanceId, 
            Date finishedTime,
            Date registeredTimestamp) {
        
        super(registeredTimestamp);

        this.productInstanceId = productInstanceId;
        this.finishedTime = finishedTime;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public Date getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Document toBSON() {
        return toBSON2();
    }
}
