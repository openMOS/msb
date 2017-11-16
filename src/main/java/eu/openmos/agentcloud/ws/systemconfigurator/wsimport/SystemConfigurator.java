
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import eu.openmos.agentcloud.utilities.ServiceCallStatus;
import eu.openmos.model.ExecutionTable;
import eu.openmos.model.FinishedProductInfo;
import eu.openmos.model.OrderInstance;
import eu.openmos.model.Product;
import eu.openmos.model.ProductInstance;
import eu.openmos.model.Recipe;
import eu.openmos.model.RecipeExecutionData;
import eu.openmos.model.Skill;
import eu.openmos.model.SubSystem;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "SystemConfigurator", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface SystemConfigurator {


    /**
     * 
     * @param recipeExecutionData
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "newRecipeExecutionData", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.NewRecipeExecutionData")
    @ResponseWrapper(localName = "newRecipeExecutionDataResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.NewRecipeExecutionDataResponse")
    public ServiceCallStatus newRecipeExecutionData(
        @WebParam(name = "recipeExecutionData", targetNamespace = "")
        RecipeExecutionData recipeExecutionData);

    /**
     * 
     * @param orderInstanceId
     * @return
     *     returns eu.openmos.model.OrderInstance
     */
    @WebMethod
    @WebResult(name = "orderInstance", targetNamespace = "")
    @RequestWrapper(localName = "getOrderInstance", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetOrderInstance")
    @ResponseWrapper(localName = "getOrderInstanceResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetOrderInstanceResponse")
    public OrderInstance getOrderInstance(
        @WebParam(name = "orderInstanceId", targetNamespace = "")
        String orderInstanceId);

    /**
     * 
     * @param finishedProductInfo
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "finishedProduct", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.FinishedProduct")
    @ResponseWrapper(localName = "finishedProductResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.FinishedProductResponse")
    public ServiceCallStatus finishedProduct(
        @WebParam(name = "finishedProductInfo", targetNamespace = "")
        FinishedProductInfo finishedProductInfo);

    /**
     * 
     * @param productInstance
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "startedProduct", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.StartedProduct")
    @ResponseWrapper(localName = "startedProductResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.StartedProductResponse")
    public ServiceCallStatus startedProduct(
        @WebParam(name = "productInstance", targetNamespace = "")
        ProductInstance productInstance);

    /**
     * 
     * @param skill
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "createNewSkill", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewSkill")
    @ResponseWrapper(localName = "createNewSkillResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewSkillResponse")
    public ServiceCallStatus createNewSkill(
        @WebParam(name = "skill", targetNamespace = "")
        Skill skill);

    /**
     * 
     * @param executionTableId
     * @return
     *     returns eu.openmos.model.ExecutionTable
     */
    @WebMethod
    @WebResult(name = "executionTable", targetNamespace = "")
    @RequestWrapper(localName = "getExecutionTableById", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTableById")
    @ResponseWrapper(localName = "getExecutionTableByIdResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTableByIdResponse")
    public ExecutionTable getExecutionTableById(
        @WebParam(name = "executionTableId", targetNamespace = "")
        String executionTableId);

    /**
     * 
     * @param order
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "orderInstanceUpdate", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.OrderInstanceUpdate")
    @ResponseWrapper(localName = "orderInstanceUpdateResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.OrderInstanceUpdateResponse")
    public ServiceCallStatus orderInstanceUpdate(
        @WebParam(name = "order", targetNamespace = "")
        OrderInstance order);

    /**
     * 
     * @return
     *     returns java.util.List<eu.openmos.model.SubSystem>
     */
    @WebMethod
    @WebResult(name = "subSystems", targetNamespace = "")
    @RequestWrapper(localName = "getSubSystems", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetSubSystems")
    @ResponseWrapper(localName = "getSubSystemsResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetSubSystemsResponse")
    public List<SubSystem> getSubSystems();

    /**
     * 
     * @return
     *     returns java.util.List<eu.openmos.model.RecipeExecutionData>
     */
    @WebMethod
    @WebResult(name = "recipeExecutionData", targetNamespace = "")
    @RequestWrapper(localName = "getRecipeExecutionData", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipeExecutionData")
    @ResponseWrapper(localName = "getRecipeExecutionDataResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipeExecutionDataResponse")
    public List<RecipeExecutionData> getRecipeExecutionData();

    /**
     * 
     * @param productDefinition
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "newProductDefinition", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.NewProductDefinition")
    @ResponseWrapper(localName = "newProductDefinitionResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.NewProductDefinitionResponse")
    public ServiceCallStatus newProductDefinition(
        @WebParam(name = "productDefinition", targetNamespace = "")
        Product productDefinition);

    /**
     * 
     * @param recipe
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "createNewRecipe", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewRecipe")
    @ResponseWrapper(localName = "createNewRecipeResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewRecipeResponse")
    public ServiceCallStatus createNewRecipe(
        @WebParam(name = "recipe", targetNamespace = "")
        Recipe recipe);

    /**
     * 
     * @param productId
     * @return
     *     returns eu.openmos.model.Product
     */
    @WebMethod
    @WebResult(name = "product", targetNamespace = "")
    @RequestWrapper(localName = "getProduct", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetProduct")
    @ResponseWrapper(localName = "getProductResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetProductResponse")
    public Product getProduct(
        @WebParam(name = "productId", targetNamespace = "")
        String productId);

    /**
     * 
     * @param operationTimestamp
     * @param orderId
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "orderInstanceRemoval", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.OrderInstanceRemoval")
    @ResponseWrapper(localName = "orderInstanceRemovalResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.OrderInstanceRemovalResponse")
    public ServiceCallStatus orderInstanceRemoval(
        @WebParam(name = "orderId", targetNamespace = "")
        String orderId,
        @WebParam(name = "operationTimestamp", targetNamespace = "")
        String operationTimestamp);

    /**
     * 
     * @param subSystemId
     * @return
     *     returns eu.openmos.model.ExecutionTable
     */
    @WebMethod
    @WebResult(name = "executionTable", targetNamespace = "")
    @RequestWrapper(localName = "getExecutionTableBySubSystemId", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTableBySubSystemId")
    @ResponseWrapper(localName = "getExecutionTableBySubSystemIdResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTableBySubSystemIdResponse")
    public ExecutionTable getExecutionTableBySubSystemId(
        @WebParam(name = "subSystemId", targetNamespace = "")
        String subSystemId);

    /**
     * 
     * @param newOrder
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "acceptNewOrderInstance", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.AcceptNewOrderInstance")
    @ResponseWrapper(localName = "acceptNewOrderInstanceResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.AcceptNewOrderInstanceResponse")
    public ServiceCallStatus acceptNewOrderInstance(
        @WebParam(name = "newOrder", targetNamespace = "")
        OrderInstance newOrder);

    /**
     * 
     * @param orders
     * @param cyberPhysicalAgentDescriptions
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "initializePlatform", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.InitializePlatform")
    @ResponseWrapper(localName = "initializePlatformResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.InitializePlatformResponse")
    public ServiceCallStatus initializePlatform(
        @WebParam(name = "cyberPhysicalAgentDescriptions", targetNamespace = "")
        List<SubSystem> cyberPhysicalAgentDescriptions,
        @WebParam(name = "orders", targetNamespace = "")
        List<OrderInstance> orders);

    /**
     * 
     * @param recipes
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "putRecipes", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.PutRecipes")
    @ResponseWrapper(localName = "putRecipesResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.PutRecipesResponse")
    public ServiceCallStatus putRecipes(
        @WebParam(name = "recipes", targetNamespace = "")
        List<Recipe> recipes);

    /**
     * 
     * @param cyberPhysicalAgentDescription
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "createNewResourceAgent", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewResourceAgent")
    @ResponseWrapper(localName = "createNewResourceAgentResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewResourceAgentResponse")
    public ServiceCallStatus createNewResourceAgent(
        @WebParam(name = "cyberPhysicalAgentDescription", targetNamespace = "")
        SubSystem cyberPhysicalAgentDescription);

    /**
     * 
     * @return
     *     returns java.util.List<eu.openmos.model.Recipe>
     */
    @WebMethod
    @WebResult(name = "recipes", targetNamespace = "")
    @RequestWrapper(localName = "getRecipes", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipes")
    @ResponseWrapper(localName = "getRecipesResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipesResponse")
    public List<Recipe> getRecipes();

    /**
     * 
     * @param subSystemId
     * @return
     *     returns java.util.List<eu.openmos.model.Recipe>
     */
    @WebMethod
    @WebResult(name = "recipes", targetNamespace = "")
    @RequestWrapper(localName = "getRecipesBySubSystemId", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipesBySubSystemId")
    @ResponseWrapper(localName = "getRecipesBySubSystemIdResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetRecipesBySubSystemIdResponse")
    public List<Recipe> getRecipesBySubSystemId(
        @WebParam(name = "subSystemId", targetNamespace = "")
        String subSystemId);

    /**
     * 
     * @return
     *     returns java.util.List<eu.openmos.model.ExecutionTable>
     */
    @WebMethod
    @WebResult(name = "executionTables", targetNamespace = "")
    @RequestWrapper(localName = "getExecutionTables", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTables")
    @ResponseWrapper(localName = "getExecutionTablesResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetExecutionTablesResponse")
    public List<ExecutionTable> getExecutionTables();

    /**
     * 
     * @param skillId
     * @return
     *     returns eu.openmos.model.Skill
     */
    @WebMethod
    @WebResult(name = "skill", targetNamespace = "")
    @RequestWrapper(localName = "getSkill", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetSkill")
    @ResponseWrapper(localName = "getSkillResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.GetSkillResponse")
    public Skill getSkill(
        @WebParam(name = "skillId", targetNamespace = "")
        String skillId);

    /**
     * 
     * @param executionTable
     * @param agentUniqueName
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "updateExecutionTable", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.UpdateExecutionTable")
    @ResponseWrapper(localName = "updateExecutionTableResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.UpdateExecutionTableResponse")
    public ServiceCallStatus updateExecutionTable(
        @WebParam(name = "agentUniqueName", targetNamespace = "")
        String agentUniqueName,
        @WebParam(name = "executionTable", targetNamespace = "")
        ExecutionTable executionTable);

    /**
     * 
     * @param cyberPhysicalAgentDescription
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "createNewTransportAgent", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewTransportAgent")
    @ResponseWrapper(localName = "createNewTransportAgentResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.CreateNewTransportAgentResponse")
    public ServiceCallStatus createNewTransportAgent(
        @WebParam(name = "cyberPhysicalAgentDescription", targetNamespace = "")
        SubSystem cyberPhysicalAgentDescription);

    /**
     * 
     * @param agentUniqueName
     * @return
     *     returns eu.openmos.agentcloud.utilities.ServiceCallStatus
     */
    @WebMethod
    @WebResult(name = "serviceCallStatus", targetNamespace = "")
    @RequestWrapper(localName = "removeAgent", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.RemoveAgent")
    @ResponseWrapper(localName = "removeAgentResponse", targetNamespace = "http://cloudinterface.agentcloud.openmos.eu/", className = "eu.openmos.agentcloud.ws.systemconfigurator.wsimport.RemoveAgentResponse")
    public ServiceCallStatus removeAgent(
        @WebParam(name = "agentUniqueName", targetNamespace = "")
        String agentUniqueName);

}
