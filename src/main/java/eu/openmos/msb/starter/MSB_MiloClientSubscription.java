/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

//milo
import static com.google.common.collect.Lists.newArrayList;
import eu.openmos.msb.opcua.milo.client.Client;
import eu.openmos.msb.opcua.milo.client.ClientRunner;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author fabio.miranda
 */
public class MSB_MiloClientSubscription implements Client{
    
    public OpcUaClient milo_client_instanceMSB = null;
    
    public void startConnection(String endpointUrl) throws Exception
    {
        System.out.println("MILO StartConnection*************");  
        if (milo_client_instanceMSB == null)
        {
            System.out.println("Milo Client created. Trying to connect to: "+endpointUrl);
            //MSB_MiloClientSubscription example = new MSB_MiloClientSubscription();
            new ClientRunner(endpointUrl, this).run();   
        }
    }
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
     
    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {

        System.out.println("RUNNING miloMSB*****************"); 
       

// synchronous connect 
        milo_client_instanceMSB = client;
        milo_client_instanceMSB.connect().get();
        
        // create a subscription and a monitored item
        //UaSubscription subscription = client.getSubscriptionManager().createSubscription(100.0).get();
    
        
         // Get a typed reference to the Server object: ServerNode
        ServerNode serverNode = client.getAddressSpace().getObjectNode(
            Identifiers.Server,
            ServerNode.class
        ).get();

        // Read properties of the Server object...
        String[] serverArray = serverNode.getServerArray().get();
        String[] namespaceArray = serverNode.getNamespaceArray().get();

        logger.info("ServerArray={}", Arrays.toString(serverArray));
        logger.info("NamespaceArray={}", Arrays.toString(namespaceArray));

        // Read the value of attribute the ServerStatus variable component
        ServerStatusDataType serverStatus = serverNode.getServerStatus().get();

        logger.info("ServerStatus={}", serverStatus);
        
        
        // Get a typed reference to the ServerStatus variable
        // component and read value attributes individually
        ServerStatusNode serverStatusNode = serverNode.serverStatus().get();
        BuildInfo buildInfo = serverStatusNode.getBuildInfo().get();
        DateTime startTime = serverStatusNode.getStartTime().get();
        DateTime currentTime = serverStatusNode.getCurrentTime().get();
        ServerState state = serverStatusNode.getState().get();

        logger.info("ServerStatus.BuildInfo={}", buildInfo);
        logger.info("ServerStatus.StartTime={}", startTime);
        logger.info("ServerStatus.CurrentTime={}", currentTime);
        logger.info("ServerStatus.State={}", state);
        
        LocalDateTime MSBTime=LocalDateTime.now();
        Date MSBDate=new Date();
        long MSB_DateUTC=LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        
        Date ServerTime=currentTime.getJavaDate();
        long ServerDateUTC=ServerTime.getTime();
            
        System.out.println("SERVERTIME: "+ ServerTime + " MSBTIME: "+ MSBTime + " MSBDate: " + MSBDate);
        System.out.println("MSB_DateUTC: "+ MSB_DateUTC + " ServerDateUTC: "+ ServerDateUTC);
        

        //start browsing at root folder
        //browseNode("", client, Identifiers.RootFolder); //Identifiers.RootFolder
        //browseNodeWFilter("", client, Identifiers.RootFolder);

         // call the DeviceXMLmethod function ->call a method
        /*DeviceXMLmethod(client, "jasus").exceptionally(ex -> {
            logger.error("error invoking DeviceXMLmethod()", ex);
            return "-1.0";
        }).thenAccept(v -> {
            logger.info("DeviceXMLmethod(cenas)={}", v);

            future.complete(client);
        });*/
        
        // call the SendServerURL function ->call a method
        /*SendServerURL(client, "querias!").exceptionally(ex -> {
            logger.error("error invoking SendServerURL()", ex);
            return "-1.0";
        }).thenAccept(v -> {
            logger.info("SendServerURL(url)={}", v);

            future.complete(client);
        });*/
    }
    
    private CompletableFuture<String> DeviceXMLmethod(OpcUaClient client, String input) {
        NodeId objectId = NodeId.parse("ns=2;s=MyObjectsFolder_OPC_Device_id");
        NodeId methodId = NodeId.parse("ns=2;s=DeviceXMLmethod");

        System.out.println("Trying to call DeviceXMLmethod...");
        CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]{new Variant(input)});

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue().toString();
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
    public CompletableFuture<String> SendServerURL(OpcUaClient client, String input) {
        NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
        NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendServerURL");

        CallMethodRequest request = new CallMethodRequest(
                objectId, methodId, new Variant[]{new Variant(input)});

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue();
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
    public CompletableFuture<String> InvoqueDeviceSkill(OpcUaClient client, NodeId objectId, NodeId methodId) {
        //NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
        //NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendServerURL");

        CallMethodRequest request = new CallMethodRequest(
                objectId, methodId, null ); //new Variant[]{new Variant(null)}

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue();
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
    public CompletableFuture<String> SendRecipetoDevice(OpcUaClient client, String input) {
        NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
        NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendRecipes");

        System.out.println("Trying to call SendRecipetoDevice...");
        CallMethodRequest request = new CallMethodRequest(
                objectId, methodId, new Variant[]{new Variant(input)});

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue();
                System.out.println("SendRecipetoDevice returned: "+value);
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
    public CompletableFuture<String> RequestProduct(OpcUaClient client, String ProductID) {
        NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
        NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendRequestProduct");

        System.out.println("Trying to call RequestProduct...");
        CallMethodRequest request = new CallMethodRequest(
                objectId, methodId, new Variant[]{new Variant(ProductID)});

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue();
                System.out.println("RequestProduct returned: "+value);
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
    //Function to list namespaces of the respective Device methods.
    private Map<String, NodeId> browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
        System.out.println("****Browse node with NO filter****");
        Map<String, NodeId> nodes1 = null;
        
        try {
            List<Node> nodes = null;
            nodes = client.getAddressSpace().browse(browseRoot).get();
            nodes1 = new HashMap<String, NodeId>();
            
            for (Node node : nodes) {
                // logger.info("{} Node={} ID={}", indent, node.getBrowseName().get().getName(), node.getNodeId());
                logger.info("{} Node={} ID={}", node.getBrowseName().get().getName(), node.getNodeId());

                // recursively browse to children
                browseNode(indent + "  ", client, node.getNodeId().get());
            }
            
            //put nodeID and namespaces in the Hashmap and return it
            for (int i = 0; i < nodes.size(); i++) {
                nodes1.put(nodes.get(i).getBrowseName().get().getName(), nodes.get(i).getNodeId().get());
            }
            
            return nodes1;
        } catch (InterruptedException | ExecutionException e) {
            // logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage());
            return nodes1;
        }
    }
    
    //Function to call the selected device method
    private CompletableFuture<String> CallDeviceMethods(OpcUaClient client, String input, String paths) {
        //TODO extract NodeID from the previous function
        NodeId objectId = NodeId.parse("ns=2;s=MyObjectsFolder_OPC_Device_id");
        NodeId methodId = NodeId.parse("ns=2;s=DeviceXMLmethod");

        System.out.println("Trying to call DeviceXMLmethod...");
        CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]{new Variant(input)});

        return client.call(request).thenCompose(result -> {
            StatusCode statusCode = result.getStatusCode();

            if (statusCode.isGood()) {
                String value = (String) l(result.getOutputArguments()).get(0).getValue().toString();
                return CompletableFuture.completedFuture(value);
            } else {
                CompletableFuture<String> f = new CompletableFuture<>();
                f.completeExceptionally(new UaException(statusCode));
                return f;
            }
        });
    }
    
        private void browseNodeWFilter(String indent, OpcUaClient client, NodeId browseRoot) {
        System.out.println("****Browse node with filter****");
            BrowseDescription browse = new BrowseDescription(
            browseRoot,
            BrowseDirection.Forward,
            Identifiers.References,
            true,
            uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
            uint(BrowseResultMask.All.getValue())
        );

        try {
            BrowseResult browseResult = client.browse(browse).get();

            List<ReferenceDescription> references = toList(browseResult.getReferences());

            for (ReferenceDescription rd : references) {
                logger.info("{} Node={}", indent, rd.getBrowseName().getName());

                // recursively browse to children
                rd.getNodeId().local().ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
            }
        } catch (InterruptedException | ExecutionException e) {
            // logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage());
        }
    }
    
    public void exitProgram()
    {
        if (milo_client_instanceMSB != null)
        {
            milo_client_instanceMSB.disconnect();
        }
    }
    
    public OpcUaClient getClientObject()
    {
       return milo_client_instanceMSB;
    }

  
    
}
