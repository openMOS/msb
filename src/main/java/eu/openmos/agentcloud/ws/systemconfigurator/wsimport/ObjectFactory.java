
package eu.openmos.agentcloud.ws.systemconfigurator.wsimport;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


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

    private final static QName _AcceptNewOrderResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "acceptNewOrderResponse");
    private final static QName _CreateNewAgent_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewAgent");
    private final static QName _RemoveAgentResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "removeAgentResponse");
    private final static QName _AcceptNewOrder_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "acceptNewOrder");
    private final static QName _CreateNewAgentResponse_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "createNewAgentResponse");
    private final static QName _RemoveAgent_QNAME = new QName("http://cloudinterface.agentcloud.openmos.eu/", "removeAgent");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.openmos.agentcloud.ws.systemconfigurator.wsimport
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AcceptNewOrder }
     * 
     */
    public AcceptNewOrder createAcceptNewOrder() {
        return new AcceptNewOrder();
    }

    /**
     * Create an instance of {@link CreateNewAgentResponse }
     * 
     */
    public CreateNewAgentResponse createCreateNewAgentResponse() {
        return new CreateNewAgentResponse();
    }

    /**
     * Create an instance of {@link RemoveAgent }
     * 
     */
    public RemoveAgent createRemoveAgent() {
        return new RemoveAgent();
    }

    /**
     * Create an instance of {@link CreateNewAgent }
     * 
     */
    public CreateNewAgent createCreateNewAgent() {
        return new CreateNewAgent();
    }

    /**
     * Create an instance of {@link AcceptNewOrderResponse }
     * 
     */
    public AcceptNewOrderResponse createAcceptNewOrderResponse() {
        return new AcceptNewOrderResponse();
    }

    /**
     * Create an instance of {@link RemoveAgentResponse }
     * 
     */
    public RemoveAgentResponse createRemoveAgentResponse() {
        return new RemoveAgentResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AcceptNewOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "acceptNewOrderResponse")
    public JAXBElement<AcceptNewOrderResponse> createAcceptNewOrderResponse(AcceptNewOrderResponse value) {
        return new JAXBElement<AcceptNewOrderResponse>(_AcceptNewOrderResponse_QNAME, AcceptNewOrderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewAgent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewAgent")
    public JAXBElement<CreateNewAgent> createCreateNewAgent(CreateNewAgent value) {
        return new JAXBElement<CreateNewAgent>(_CreateNewAgent_QNAME, CreateNewAgent.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link AcceptNewOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "acceptNewOrder")
    public JAXBElement<AcceptNewOrder> createAcceptNewOrder(AcceptNewOrder value) {
        return new JAXBElement<AcceptNewOrder>(_AcceptNewOrder_QNAME, AcceptNewOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateNewAgentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://cloudinterface.agentcloud.openmos.eu/", name = "createNewAgentResponse")
    public JAXBElement<CreateNewAgentResponse> createCreateNewAgentResponse(CreateNewAgentResponse value) {
        return new JAXBElement<CreateNewAgentResponse>(_CreateNewAgentResponse_QNAME, CreateNewAgentResponse.class, null, value);
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
