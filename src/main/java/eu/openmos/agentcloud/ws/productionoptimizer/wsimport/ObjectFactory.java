
package eu.openmos.agentcloud.ws.productionoptimizer.wsimport;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.openmos.agentcloud.ws.productionoptimizer.wsimport package. 
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

    private final static QName _StopOptimizerResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "stopOptimizerResponse");
    private final static QName _OptimizeResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "optimizeResponse");
    private final static QName _ResetOptimizerResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "resetOptimizerResponse");
    private final static QName _ResetOptimizer_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "resetOptimizer");
    private final static QName _Optimize_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "optimize");
    private final static QName _StopOptimizer_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "stopOptimizer");
    private final static QName _IsOptimizableResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "isOptimizableResponse");
    private final static QName _ReparametrizeOptimizer_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "reparametrizeOptimizer");
    private final static QName _ReparametrizeOptimizerResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "reparametrizeOptimizerResponse");
    private final static QName _InitializeOptimizer_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "initializeOptimizer");
    private final static QName _IsOptimizable_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "isOptimizable");
    private final static QName _InitializeOptimizerResponse_QNAME = new QName("http://productionoptimizer.optimizer.agentcloud.openmos.eu/", "initializeOptimizerResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.openmos.agentcloud.ws.productionoptimizer.wsimport
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResetOptimizer }
     * 
     */
    public ResetOptimizer createResetOptimizer() {
        return new ResetOptimizer();
    }

    /**
     * Create an instance of {@link OptimizeResponse }
     * 
     */
    public OptimizeResponse createOptimizeResponse() {
        return new OptimizeResponse();
    }

    /**
     * Create an instance of {@link ResetOptimizerResponse }
     * 
     */
    public ResetOptimizerResponse createResetOptimizerResponse() {
        return new ResetOptimizerResponse();
    }

    /**
     * Create an instance of {@link StopOptimizerResponse }
     * 
     */
    public StopOptimizerResponse createStopOptimizerResponse() {
        return new StopOptimizerResponse();
    }

    /**
     * Create an instance of {@link Optimize }
     * 
     */
    public Optimize createOptimize() {
        return new Optimize();
    }

    /**
     * Create an instance of {@link IsOptimizableResponse }
     * 
     */
    public IsOptimizableResponse createIsOptimizableResponse() {
        return new IsOptimizableResponse();
    }

    /**
     * Create an instance of {@link ReparametrizeOptimizer }
     * 
     */
    public ReparametrizeOptimizer createReparametrizeOptimizer() {
        return new ReparametrizeOptimizer();
    }

    /**
     * Create an instance of {@link ReparametrizeOptimizerResponse }
     * 
     */
    public ReparametrizeOptimizerResponse createReparametrizeOptimizerResponse() {
        return new ReparametrizeOptimizerResponse();
    }

    /**
     * Create an instance of {@link StopOptimizer }
     * 
     */
    public StopOptimizer createStopOptimizer() {
        return new StopOptimizer();
    }

    /**
     * Create an instance of {@link InitializeOptimizerResponse }
     * 
     */
    public InitializeOptimizerResponse createInitializeOptimizerResponse() {
        return new InitializeOptimizerResponse();
    }

    /**
     * Create an instance of {@link InitializeOptimizer }
     * 
     */
    public InitializeOptimizer createInitializeOptimizer() {
        return new InitializeOptimizer();
    }

    /**
     * Create an instance of {@link IsOptimizable }
     * 
     */
    public IsOptimizable createIsOptimizable() {
        return new IsOptimizable();
    }

    /**
     * Create an instance of {@link ProductionOptimizerResponseBean }
     * 
     */
    public ProductionOptimizerResponseBean createProductionOptimizerResponseBean() {
        return new ProductionOptimizerResponseBean();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopOptimizerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "stopOptimizerResponse")
    public JAXBElement<StopOptimizerResponse> createStopOptimizerResponse(StopOptimizerResponse value) {
        return new JAXBElement<StopOptimizerResponse>(_StopOptimizerResponse_QNAME, StopOptimizerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OptimizeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "optimizeResponse")
    public JAXBElement<OptimizeResponse> createOptimizeResponse(OptimizeResponse value) {
        return new JAXBElement<OptimizeResponse>(_OptimizeResponse_QNAME, OptimizeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetOptimizerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "resetOptimizerResponse")
    public JAXBElement<ResetOptimizerResponse> createResetOptimizerResponse(ResetOptimizerResponse value) {
        return new JAXBElement<ResetOptimizerResponse>(_ResetOptimizerResponse_QNAME, ResetOptimizerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResetOptimizer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "resetOptimizer")
    public JAXBElement<ResetOptimizer> createResetOptimizer(ResetOptimizer value) {
        return new JAXBElement<ResetOptimizer>(_ResetOptimizer_QNAME, ResetOptimizer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Optimize }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "optimize")
    public JAXBElement<Optimize> createOptimize(Optimize value) {
        return new JAXBElement<Optimize>(_Optimize_QNAME, Optimize.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StopOptimizer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "stopOptimizer")
    public JAXBElement<StopOptimizer> createStopOptimizer(StopOptimizer value) {
        return new JAXBElement<StopOptimizer>(_StopOptimizer_QNAME, StopOptimizer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsOptimizableResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "isOptimizableResponse")
    public JAXBElement<IsOptimizableResponse> createIsOptimizableResponse(IsOptimizableResponse value) {
        return new JAXBElement<IsOptimizableResponse>(_IsOptimizableResponse_QNAME, IsOptimizableResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReparametrizeOptimizer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "reparametrizeOptimizer")
    public JAXBElement<ReparametrizeOptimizer> createReparametrizeOptimizer(ReparametrizeOptimizer value) {
        return new JAXBElement<ReparametrizeOptimizer>(_ReparametrizeOptimizer_QNAME, ReparametrizeOptimizer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReparametrizeOptimizerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "reparametrizeOptimizerResponse")
    public JAXBElement<ReparametrizeOptimizerResponse> createReparametrizeOptimizerResponse(ReparametrizeOptimizerResponse value) {
        return new JAXBElement<ReparametrizeOptimizerResponse>(_ReparametrizeOptimizerResponse_QNAME, ReparametrizeOptimizerResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializeOptimizer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "initializeOptimizer")
    public JAXBElement<InitializeOptimizer> createInitializeOptimizer(InitializeOptimizer value) {
        return new JAXBElement<InitializeOptimizer>(_InitializeOptimizer_QNAME, InitializeOptimizer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsOptimizable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "isOptimizable")
    public JAXBElement<IsOptimizable> createIsOptimizable(IsOptimizable value) {
        return new JAXBElement<IsOptimizable>(_IsOptimizable_QNAME, IsOptimizable.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InitializeOptimizerResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://productionoptimizer.optimizer.agentcloud.openmos.eu/", name = "initializeOptimizerResponse")
    public JAXBElement<InitializeOptimizerResponse> createInitializeOptimizerResponse(InitializeOptimizerResponse value) {
        return new JAXBElement<InitializeOptimizerResponse>(_InitializeOptimizerResponse_QNAME, InitializeOptimizerResponse.class, null, value);
    }

}
