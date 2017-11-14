package eu.openmos.model;

import eu.openmos.model.utilities.DatabaseConstants;
import eu.openmos.model.utilities.SerializationConstants;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.Document;

/**
 * Object that represents data regarding recipe execution and that then are passed on to the optimizer.
 * 
 * @author Pedro Lima Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 *
 */
public class RecipeExecutionData extends Base implements Serializable {
    private static final long serialVersionUID = 6529685098267757401L;
    
    private String productInstanceId;
    private String recipeId;
    private List<KPISetting> kpiSettings;
    private List<ParameterSetting> parameterSettings;

    // reflection stuff
    public RecipeExecutionData() { super();
    }

    public RecipeExecutionData(
            String productInstanceId, 
            String recipeId, 
            List<KPISetting> kpiSettings, 
            List<ParameterSetting> parameterSettings, 
            Date registeredTimestamp) 
    {
        super(registeredTimestamp);
        
        this.productInstanceId = productInstanceId;
        this.recipeId = recipeId;
        this.kpiSettings = kpiSettings;
        this.parameterSettings = parameterSettings;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public List<KPISetting> getKpiSettings() {
        return kpiSettings;
    }
    
    public void setKpiSettings(List<KPISetting> kpiSettings) {
        this.kpiSettings = kpiSettings;
    }

    public List<ParameterSetting> getParameterSettings() {
        return parameterSettings;
    }

    public void setParameterSettings(List<ParameterSetting> parameterSettings) {
        this.parameterSettings = parameterSettings;
    }

    /**
     * Method that serializes the object into a BSON document.
     * The returned BSON document has the following format:
    productId
    recipeId
    list of kpi ids
    list of parameter ids
    registered
     *
     * @return BSON form of the object. 
     */
    public Document toBSON() {
        Document doc = new Document();
        
        List<String> kpiSettingIds = null;
        if (kpiSettings != null)
            kpiSettingIds = kpiSettings.stream().map(kpiSetting -> kpiSetting.getUniqueId()).collect(Collectors.toList());
        
        List<String> parameterSettingIds = null;
        if (parameterSettings != null)
            parameterSettingIds = parameterSettings.stream().map(parameterSetting -> parameterSetting.getUniqueId()).collect(Collectors.toList());        
        
        SimpleDateFormat sdf = new SimpleDateFormat(SerializationConstants.DATE_REPRESENTATION);
        
        doc.append(DatabaseConstants.PRODUCT_INSTANCE_ID, productInstanceId);
        doc.append(DatabaseConstants.RECIPE_ID, recipeId);
        doc.append(DatabaseConstants.KPI_SETTING_IDS, kpiSettingIds);
        doc.append(DatabaseConstants.PARAMETER_SETTING_IDS, parameterSettingIds);        
        doc.append(DatabaseConstants.REGISTERED, this.registered == null ? "null" : sdf.format(this.registered));
        
        return doc;
    }
}
