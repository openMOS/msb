/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.recipesmanagement;

import eu.openmos.agentcloud.data.recipe.Recipe;
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
public interface RecipesDeployer {
    @WebMethod(operationName = "sendRecipes")
    @WebResult(name="recipesDeploymentStatus")
    public boolean sendRecipes(@WebParam(name = "deviceName") String deviceName, @WebParam(name = "mode") int mode, @WebParam(name = "recipes") List<Recipe> recipes);
    
}
