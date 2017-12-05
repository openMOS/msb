package eu.openmos.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author valerio.gentile
 */
public class RecipeExecutionDataFilter implements Serializable
{
    private static final long serialVersionUID = 6529685098267757501L;            
    
    private String recipeId;
    private String productInstanceId;
    private String kpiSettingName;
    private String startInterval;
    private String stopInterval;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(String productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public String getKpiSettingName() {
        return kpiSettingName;
    }

    public void setKpiSettingName(String kpiSettingName) {
        this.kpiSettingName = kpiSettingName;
    }

    public String getStartInterval() {
        return startInterval;
    }

    public void setStartInterval(String startInterval) {
        this.startInterval = startInterval;
    }

    public String getStopInterval() {
        return stopInterval;
    }

    public void setStopInterval(String stopInterval) {
        this.stopInterval = stopInterval;
    }    

    @Override
    public String toString() {
        return "RecipeExecutionDataFilter{" + "recipeId=" + recipeId + ", productInstanceId=" + productInstanceId + ", kpiSettingName=" + kpiSettingName + ", startInterval=" + startInterval + ", stopInterval=" + stopInterval + '}';
    }

}
