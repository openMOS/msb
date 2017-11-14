/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.soap;

// import eu.openmos.agentcloud.data.recipe.Recipe;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.Recipe;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author valerio.gentile
 */
@WebService
public interface RecipesDeployment {
    @WebMethod(operationName = "updateRecipes")
    @WebResult(name="recipesDeploymentStatus")
    public boolean updateRecipes(@WebParam(name = "deviceName") String deviceName, @WebParam(name = "mode") int mode, @WebParam(name = "recipes") List<Recipe> recipes);
    
    @WebMethod(operationName = "updateExecutionTable")
    @WebResult(name="executionTableDeploymentStatus")
    public boolean updateExecutionTable(@WebParam(name = "executionTable") ExecutionTable executionTable);
}
