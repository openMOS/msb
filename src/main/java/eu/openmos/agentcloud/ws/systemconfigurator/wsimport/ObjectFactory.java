
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import eu.openmos.model.Equipment;
import eu.openmos.model.Recipe;
import eu.openmos.model.Skill;
import eu.openmos.model.SkillRequirement;
import eu.openmos.model.SubSystem;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.openmos.agentcloud.ws.systemconfigurator.wsimport package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Recipe_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "recipe");
    private final static QName _AcceptNewOrderInstanceResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "acceptNewOrderInstanceResponse");
    private final static QName _CreateNewTransportAgentResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewTransportAgentResponse");
    private final static QName _SkillRequirement_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "skillRequirement");
    private final static QName _OrderInstanceRemovalResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "orderInstanceRemovalResponse");
    private final static QName _FinishedProduct_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "finishedProduct");
    private final static QName _CreateNewSkill_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewSkill");
    private final static QName _OrderInstanceUpdate_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "orderInstanceUpdate");
    private final static QName _DeviceAdapter_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "deviceAdapter");
    private final static QName _OrderInstanceUpdateResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "orderInstanceUpdateResponse");
    private final static QName _Skill_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "skill");
    private final static QName _CreateNewRecipe_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewRecipe");
    private final static QName _InitializePlatformResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "initializePlatformResponse");
    private final static QName _CreateNewResourceAgentResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewResourceAgentResponse");
    private final static QName _OrderInstanceRemoval_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "orderInstanceRemoval");
    private final static QName _AcceptNewOrderInstance_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "acceptNewOrderInstance");
    private final static QName _InitializePlatform_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "initializePlatform");
    private final static QName _CreateNewSkillResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewSkillResponse");
    private final static QName _CreateNewResourceAgent_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewResourceAgent");
    private final static QName _Device_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "device");
    private final static QName _RemoveAgentResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "removeAgentResponse");
    private final static QName _FinishedProductResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "finishedProductResponse");
    private final static QName _CreateNewTransportAgent_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewTransportAgent");
    private final static QName _CreateNewRecipeResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewRecipeResponse");
    private final static QName _RemoveAgent_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "removeAgent");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.openmos.agentcloud.ws.systemconfigurator.wsimport
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrderInstanceRemovalResponse }
     * 
     */
    public OrderInstanceRemovalResponse createOrderInstanceRemovalResponse() {
        return new OrderInstanceRemovalResponse();
    }

    /**
     * Create an instance of {@link CreateNewSkill }
     * 
     */
    public CreateNewSkill createCreateNewSkill() {
        return new CreateNewSkill();
    }

    /**
     * Create an instance of {@link FinishedProduct }
     * 
     */
    public FinishedProduct createFinishedProduct() {
        return new FinishedProduct();
    }

    /**
     * Create an instance of {@link AcceptNewOrderInstanceResponse }
     * 
     */
    public AcceptNewOrderInstanceResponse createAcceptNewOrderInstanceResponse() {
        return new AcceptNewOrderInstanceResponse();
    }

    /**
     * Create an instance of {@link CreateNewTransportAgentResponse }
     * 
     */
    public CreateNewTransportAgentResponse createCreateNewTransportAgentResponse() {
        return new CreateNewTransportAgentResponse();
    }

    /**
     * Create an instance of {@link OrderInstanceUpdateResponse }
     * 
     */
    public OrderInstanceUpdateResponse createOrderInstanceUpdateResponse() {
        return new OrderInstanceUpdateResponse();
    }

    /**
     * Create an instance of {@link OrderInstanceUpdate }
     * 
     */
    public OrderInstanceUpdate createOrderInstanceUpdate() {
        return new OrderInstanceUpdate();
    }

    /**
     * Create an instance of {@link CreateNewResourceAgentResponse }
     * 
     */
    public CreateNewResourceAgentResponse createCreateNewResourceAgentResponse() {
        return new CreateNewResourceAgentResponse();
    }

    /**
     * Create an instance of {@link OrderInstanceRemoval }
     * 
     */
    public OrderInstanceRemoval createOrderInstanceRemoval() {
        return new OrderInstanceRemoval();
    }

    /**
     * Create an instance of {@link InitializePlatform }
     * 
     */
    public InitializePlatform createInitializePlatform() {
        return new InitializePlatform();
    }

    /**
     * Create an instance of {@link CreateNewSkillResponse }
     * 
     */
    public CreateNewSkillResponse createCreateNewSkillResponse() {
        return new CreateNewSkillResponse();
    }

    /**
     * Create an instance of {@link AcceptNewOrderInstance }
     * 
     */
    public AcceptNewOrderInstance createAcceptNewOrderInstance() {
        return new AcceptNewOrderInstance();
    }

    /**
     * Create an instance of {@link CreateNewRecipe }
     * 
     */
    public CreateNewRecipe createCreateNewRecipe() {
        return new CreateNewRecipe();
    }

    /**
     * Create an instance of {@link InitializePlatformResponse }
     * 
     */
    public InitializePlatformResponse createInitializePlatformResponse() {
        return new InitializePlatformResponse();
    }

    /**
     * Create an instance of {@link FinishedProductResponse }
     * 
     */
    public FinishedProductResponse createFinishedProductResponse() {
        return new FinishedProductResponse();
    }

    /**
     * Create an instance of {@link RemoveAgent }
     * 
     */
    public RemoveAgent createRemoveAgent() {
        return new RemoveAgent();
    }

    /**
     * Create an instance of {@link CreateNewTransportAgent }
     * 
     */
    public CreateNewTransportAgent createCreateNewTransportAgent() {
        return new CreateNewTransportAgent();
    }

    /**
     * Create an instance of {@link CreateNewRecipeResponse }
     * 
     */
    public CreateNewRecipeResponse createCreateNewRecipeResponse() {
        return new CreateNewRecipeResponse();
    }

    /**
     * Create an instance of {@link RemoveAgentResponse }
     * 
     */
    public RemoveAgentResponse createRemoveAgentResponse() {
        return new RemoveAgentResponse();
    }

    /**
     * Create an instance of {@link CreateNewResourceAgent }
     * 
     */
    public CreateNewResourceAgent createCreateNewResourceAgent() {
        return new CreateNewResourceAgent();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Recipe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "recipe")
    public JAXBElement<Recipe> createRecipe(Recipe value) {
        return new JAXBElement<Recipe>(_Recipe_QNAME, Recipe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcceptNewOrderInstanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "acceptNewOrderInstanceResponse")
    public JAXBElement<AcceptNewOrderInstanceResponse> createAcceptNewOrderInstanceResponse(AcceptNewOrderInstanceResponse value) {
        return new JAXBElement<AcceptNewOrderInstanceResponse>(_AcceptNewOrderInstanceResponse_QNAME, AcceptNewOrderInstanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewTransportAgentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewTransportAgentResponse")
    public JAXBElement<CreateNewTransportAgentResponse> createCreateNewTransportAgentResponse(CreateNewTransportAgentResponse value) {
        return new JAXBElement<CreateNewTransportAgentResponse>(_CreateNewTransportAgentResponse_QNAME, CreateNewTransportAgentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SkillRequirement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "skillRequirement")
    public JAXBElement<SkillRequirement> createSkillRequirement(SkillRequirement value) {
        return new JAXBElement<SkillRequirement>(_SkillRequirement_QNAME, SkillRequirement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInstanceRemovalResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "orderInstanceRemovalResponse")
    public JAXBElement<OrderInstanceRemovalResponse> createOrderInstanceRemovalResponse(OrderInstanceRemovalResponse value) {
        return new JAXBElement<OrderInstanceRemovalResponse>(_OrderInstanceRemovalResponse_QNAME, OrderInstanceRemovalResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinishedProduct }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "finishedProduct")
    public JAXBElement<FinishedProduct> createFinishedProduct(FinishedProduct value) {
        return new JAXBElement<FinishedProduct>(_FinishedProduct_QNAME, FinishedProduct.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewSkill }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewSkill")
    public JAXBElement<CreateNewSkill> createCreateNewSkill(CreateNewSkill value) {
        return new JAXBElement<CreateNewSkill>(_CreateNewSkill_QNAME, CreateNewSkill.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInstanceUpdate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "orderInstanceUpdate")
    public JAXBElement<OrderInstanceUpdate> createOrderInstanceUpdate(OrderInstanceUpdate value) {
        return new JAXBElement<OrderInstanceUpdate>(_OrderInstanceUpdate_QNAME, OrderInstanceUpdate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubSystem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "deviceAdapter")
    public JAXBElement<SubSystem> createDeviceAdapter(SubSystem value) {
        return new JAXBElement<SubSystem>(_DeviceAdapter_QNAME, SubSystem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInstanceUpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "orderInstanceUpdateResponse")
    public JAXBElement<OrderInstanceUpdateResponse> createOrderInstanceUpdateResponse(OrderInstanceUpdateResponse value) {
        return new JAXBElement<OrderInstanceUpdateResponse>(_OrderInstanceUpdateResponse_QNAME, OrderInstanceUpdateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Skill }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "skill")
    public JAXBElement<Skill> createSkill(Skill value) {
        return new JAXBElement<Skill>(_Skill_QNAME, Skill.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewRecipe }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewRecipe")
    public JAXBElement<CreateNewRecipe> createCreateNewRecipe(CreateNewRecipe value) {
        return new JAXBElement<CreateNewRecipe>(_CreateNewRecipe_QNAME, CreateNewRecipe.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializePlatformResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "initializePlatformResponse")
    public JAXBElement<InitializePlatformResponse> createInitializePlatformResponse(InitializePlatformResponse value) {
        return new JAXBElement<InitializePlatformResponse>(_InitializePlatformResponse_QNAME, InitializePlatformResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewResourceAgentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewResourceAgentResponse")
    public JAXBElement<CreateNewResourceAgentResponse> createCreateNewResourceAgentResponse(CreateNewResourceAgentResponse value) {
        return new JAXBElement<CreateNewResourceAgentResponse>(_CreateNewResourceAgentResponse_QNAME, CreateNewResourceAgentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderInstanceRemoval }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "orderInstanceRemoval")
    public JAXBElement<OrderInstanceRemoval> createOrderInstanceRemoval(OrderInstanceRemoval value) {
        return new JAXBElement<OrderInstanceRemoval>(_OrderInstanceRemoval_QNAME, OrderInstanceRemoval.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcceptNewOrderInstance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "acceptNewOrderInstance")
    public JAXBElement<AcceptNewOrderInstance> createAcceptNewOrderInstance(AcceptNewOrderInstance value) {
        return new JAXBElement<AcceptNewOrderInstance>(_AcceptNewOrderInstance_QNAME, AcceptNewOrderInstance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializePlatform }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "initializePlatform")
    public JAXBElement<InitializePlatform> createInitializePlatform(InitializePlatform value) {
        return new JAXBElement<InitializePlatform>(_InitializePlatform_QNAME, InitializePlatform.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewSkillResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewSkillResponse")
    public JAXBElement<CreateNewSkillResponse> createCreateNewSkillResponse(CreateNewSkillResponse value) {
        return new JAXBElement<CreateNewSkillResponse>(_CreateNewSkillResponse_QNAME, CreateNewSkillResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewResourceAgent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewResourceAgent")
    public JAXBElement<CreateNewResourceAgent> createCreateNewResourceAgent(CreateNewResourceAgent value) {
        return new JAXBElement<CreateNewResourceAgent>(_CreateNewResourceAgent_QNAME, CreateNewResourceAgent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Equipment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "device")
    public JAXBElement<Equipment> createDevice(Equipment value) {
        return new JAXBElement<Equipment>(_Device_QNAME, Equipment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveAgentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "removeAgentResponse")
    public JAXBElement<RemoveAgentResponse> createRemoveAgentResponse(RemoveAgentResponse value) {
        return new JAXBElement<RemoveAgentResponse>(_RemoveAgentResponse_QNAME, RemoveAgentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FinishedProductResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "finishedProductResponse")
    public JAXBElement<FinishedProductResponse> createFinishedProductResponse(FinishedProductResponse value) {
        return new JAXBElement<FinishedProductResponse>(_FinishedProductResponse_QNAME, FinishedProductResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewTransportAgent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewTransportAgent")
    public JAXBElement<CreateNewTransportAgent> createCreateNewTransportAgent(CreateNewTransportAgent value) {
        return new JAXBElement<CreateNewTransportAgent>(_CreateNewTransportAgent_QNAME, CreateNewTransportAgent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewRecipeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewRecipeResponse")
    public JAXBElement<CreateNewRecipeResponse> createCreateNewRecipeResponse(CreateNewRecipeResponse value) {
        return new JAXBElement<CreateNewRecipeResponse>(_CreateNewRecipeResponse_QNAME, CreateNewRecipeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveAgent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "removeAgent")
    public JAXBElement<RemoveAgent> createRemoveAgent(RemoveAgent value) {
        return new JAXBElement<RemoveAgent>(_RemoveAgent_QNAME, RemoveAgent.class, null, value);
    }

}
