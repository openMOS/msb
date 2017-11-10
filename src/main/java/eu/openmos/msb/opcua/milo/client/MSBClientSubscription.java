/*
 * TODO - Add licence
 */
package eu.openmos.msb.opcua.milo.client;

// IMPORTS
import static com.google.common.collect.Lists.newArrayList;
import eu.openmos.msb.datastructures.PerformanceMasurement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import org.apache.commons.lang3.time.StopWatch;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jdom2.Element;
import org.jdom2.Attribute;

/**
 *
 * @author fabio.miranda
 */
public class MSBClientSubscription implements IClient
{

  // GLOBAL VARIABLES
  private OpcUaClient msbClientInstance = null;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private UaSubscription subscription;
  private ArrayList<ReadValueId> readValueList;
  private final AtomicLong clientHandles = new AtomicLong(1L);
  private List<UaMonitoredItem> items;
  private final StopWatch recipeExecutionWatch = new StopWatch();

  /**
   *
   * TODO - fabio Documentation
   *
   * @param endpointUrl
   * @throws Exception
   */
  public void startConnection(String endpointUrl) throws Exception
  {
    System.out.println("MILO StartConnection*************");
    if (msbClientInstance == null)
    {
      System.out.println("Milo Client created. Trying to connect to: " + endpointUrl);
      //MSB_MiloClientSubscription example = new MSB_MiloClientSubscription();
      new ClientRunner(endpointUrl, this).run();
    }
  }

  /**
   *
   * TODO - fabio Documentation
   *
   * @param client
   * @param future
   * @throws Exception
   */
  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception
  {

    System.out.println("\n****RUNNING Milo MSB Instance****\n");

    // synchronous connect 
    
    msbClientInstance = client;
    msbClientInstance.connect().get();

    // create a subscription and a monitored item
    // UaSubscription subscription = client.getSubscriptionManager().createSubscription(100.0).get();
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

    LocalDateTime MSBTime = LocalDateTime.now();
    Date MSBDate = new Date();
    long MSB_DateUTC = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

    Date ServerTime = currentTime.getJavaDate();
    long ServerDateUTC = ServerTime.getTime();

    System.out.println("SERVERTIME: " + ServerTime + " MSBTIME: " + MSBTime + " MSBDate: " + MSBDate);
    System.out.println("MSB_DateUTC: " + MSB_DateUTC + " ServerDateUTC: " + ServerDateUTC);

    //subscription to keep session open
    subscription = msbClientInstance.getSubscriptionManager().createSubscription(500.0).get();
    //server status
    NodeId nodeServerStatus = new NodeId(0, 2256);
    readValueList = new ArrayList<>();
    readValueList.add(new ReadValueId(nodeServerStatus, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE));
    setSubscription(readValueList);

  }

  private void setSubscription(ArrayList<ReadValueId> readValueList) throws InterruptedException, ExecutionException
  {
    ArrayList<MonitoredItemCreateRequest> requestList = new ArrayList<>();
    for (int i = 0; i < readValueList.size(); i++)
    {
      // client handle must be unique per item
      UInteger clientHandle = uint(clientHandles.getAndIncrement());
      MonitoringParameters parameters = new MonitoringParameters(
              clientHandle,
              500.0, // sampling interval
              null, // filter, null means use default
              uint(10), // queue size
              true);      // discard oldest
      ReadValueId readValueId = readValueList.get(i);
      MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, parameters);
      requestList.add(request);
    }

    BiConsumer<UaMonitoredItem, Integer> onItemCreated = (item, id) -> item.setValueConsumer(this::onSubscriptionValue);
    items = subscription.createMonitoredItems(TimestampsToReturn.Both, newArrayList(requestList), onItemCreated).get();
    items.forEach((item) ->
    {
      if (item.getStatusCode().isGood())
      {
        System.out.println("MSBClient - - 1 item created for nodeId= " + item.getReadValueId().getNodeId() + "\n");
        //logger.info("1 item created for nodeId={}", item.getReadValueId().getNodeId());
        //MainWindow.mainLogger.append("Subscription_EXECUTE - 1 item created for nodeId= " + item.getReadValueId().getNodeId() + "\n");
      } else
      {
        System.out.println("MSBClient - failed to create item for nodeId= " + item.getReadValueId().getNodeId() + " (status=" + ")\n");
        //logger.warn("failed to create item for nodeId={} (status={})", item.getReadValueId().getNodeId(), item.getStatusCode());
        //MainWindow.mainLogger.append("Subscription_EXECUTE - failed to create item for nodeId= " + item.getReadValueId().getNodeId() + " (status=" + ")\n");
      }
    });
  }

  private void onSubscriptionValue(UaMonitoredItem item, DataValue value)
  {
    String aux = value.getStatusCode().toString();
    boolean ServerStats = aux.contains("quality=bad");
    if (ServerStats)
    {
      //MainWindow.mainLogger.append("Subscription_EXECUTE - subscription value received: item=" + item.getReadValueId().getNodeId() + ", value=" + value.getValue().getValue().toString() + "\n");
    } else
    {
      //MainWindow.mainLogger.append("Subscription_EXECUTE - subscription value received: item=" + item.getReadValueId().getNodeId() + ", value=" + value.getValue().getValue().toString() + "\n");
    }
  }

  /**
   * TODO - fabio Documentation
   *
   * @param client
   * @param input
   * @return
   */
  public CompletableFuture<String> SendServerURL(OpcUaClient client, String input)
  {
    NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
    NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendServerURL");

    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]
            {
              new Variant(input)
            });

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue();
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        return f;
      }
    });
  }

  /**
   * TODO - Documentation
   *
   * @param client
   * @param objectId
   * @param methodId
   * @param productId
   * @return
   */
  public boolean InvokeDeviceSkill(OpcUaClient client, NodeId objectId, NodeId methodId, String productId)
  {
    PerformanceMasurement perfMeasurement = PerformanceMasurement.getInstance();
    recipeExecutionWatch.reset();
    recipeExecutionWatch.start();
    
    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]{new Variant(productId)});

    try
    {
      StatusCode res=client.call(request).get().getStatusCode();
      
      if(res.isGood()){
        perfMeasurement.getOrderTillRecipeCallTimers().add(recipeExecutionWatch.getTime());
        recipeExecutionWatch.stop();
        return true;
      }else if(res.isBad()){
        perfMeasurement.getOrderTillRecipeCallTimers().add(recipeExecutionWatch.getTime());
        recipeExecutionWatch.stop();
        return false;
      }else{
        perfMeasurement.getOrderTillRecipeCallTimers().add(recipeExecutionWatch.getTime());
        recipeExecutionWatch.stop();
        return false;
      }
      
      /*return client.call(request).thenCompose(result
      ->
      {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
      String value = (String) l(result.getOutputArguments()).get(0).getValue();
      return CompletableFuture.completedFuture(value);
      } else
      {
      CompletableFuture<String> f = new CompletableFuture<>();
      f.completeExceptionally(new UaException(statusCode));
      return f;
      }
      });*/
    } catch (InterruptedException | ExecutionException ex)
    {
      perfMeasurement.getOrderTillRecipeCallTimers().add(recipeExecutionWatch.getTime());
      recipeExecutionWatch.stop();
      java.util.logging.Logger.getLogger(MSBClientSubscription.class.getName()).log(Level.SEVERE, null, ex);
      
    }

    
    return false;
  }

  public CompletableFuture<String> InvokeDeviceMARTELO(OpcUaClient client, NodeId objectId, NodeId methodId, String productId , String daid, String repid)
  {

    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]{new Variant(daid),
              new Variant(repid),
              new Variant(productId),
            });

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue();
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        return f;
      }
    });
  }
  
  /**
   * TODO - fabio Documentation
   *
   * @param client
   * @param input
   * @return
   */
  public CompletableFuture<String> SendRecipetoDevice(OpcUaClient client, String input)
  {
    NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
    NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendRecipes");

    System.out.println("Trying to call SendRecipetoDevice...");
    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]
            {
              new Variant(input)
            });

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue();
        System.out.println("SendRecipetoDevice returned: " + value);
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        return f;
      }
    });
  }

  /**
   * TODO - fabio Documentation
   *
   * @param client
   * @param ProductID
   * @return
   */
  public CompletableFuture<String> RequestProduct(OpcUaClient client, String ProductID)
  {
    NodeId objectId = NodeId.parse("ns=2;s=OPC_Device");
    NodeId methodId = NodeId.parse("ns=2;s=OPC_Device/SendRequestProduct");

    System.out.println("Trying to call RequestProduct...");
    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, new Variant[]
            {
              new Variant(ProductID)
            });

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue();
        System.out.println("RequestProduct returned: " + value);
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        return f;
      }
    });
  }

  /**
   * Function to call the selected device method
   *
   * @param client
   * @param input
   * @param paths
   * @return
   */
  private CompletableFuture<String> CallDeviceMethods(OpcUaClient client, String input, String paths)
  {
    //TODO extract NodeID from the previous function
    NodeId objectId = NodeId.parse("ns=2;s=MyObjectsFolder_OPC_Device_id");
    NodeId methodId = NodeId.parse("ns=2;s=DeviceXMLmethod");

    System.out.println("Trying to call DeviceXMLmethod...");
    CallMethodRequest request = new CallMethodRequest(objectId, methodId, new Variant[]
    {
      new Variant(input)
    });

    return client.call(request).thenCompose(result
            ->
    {
      StatusCode statusCode = result.getStatusCode();

      if (statusCode.isGood())
      {
        String value = (String) l(result.getOutputArguments()).get(0).getValue().toString();
        return CompletableFuture.completedFuture(value);
      } else
      {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.completeExceptionally(new UaException(statusCode));
        return f;
      }
    });
  }

  /**
   * equipmentNamespace = "openMOSRoleClassLib/Equipment"; skillNamespace = "openMOSRoleClassLib/Skill"; moduleNamespace
   * = "openMOSRoleClassLib/Equipment/Module";
   *
   * @param indent
   * @param client
   * @param browseRoot
   * @param ignoreList
   * @param level
   * @return
   */
  public List<Element> browseNode(OpcUaClient client, NodeId browseRoot, int level, Set<String> ignoreList)
  {

    final int nextInteraction = level - 1;
    final List<Element> nodes = new ArrayList<>();
    try
    {
      BrowseDescription browse = new BrowseDescription(
              browseRoot,
              BrowseDirection.Forward,
              Identifiers.References,
              true,
              uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue() | NodeClass.ObjectType.getValue() | NodeClass.Method.getValue()),
              uint(BrowseResultMask.All.getValue())
      );

      BrowseResult browseResult = client.browse(browse).get();
      List<ReferenceDescription> references = toList(browseResult.getReferences());

      for (ReferenceDescription rd : references)
      {
        String referenceName = rd.getBrowseName().getName();
        if (ignoreList.contains(referenceName))
        {
          continue;
        }
        /*
        String attributeName = rd.getNodeId().getIdentifier().toString();
        if (attributeName.contains("openMOSRoleClassLib"))
        {
          continue;
        }
        */

        Element node = new Element(referenceName.replaceAll(":", "").replaceAll(" ", "_"));

        Element nodeType = new Element("Type");
        nodeType.setAttribute("id", rd.getNodeId().getType().toString());
        nodeType.setAttribute("namespace", rd.getNodeId().getIdentifier().toString());
        node.addContent(nodeType);

        Element nodePath = new Element("Path");
        nodePath.setAttribute("ns", rd.getNodeId().getNamespaceIndex().toString());
        nodePath.setText(rd.getNodeId().getIdentifier().toString());
        node.addContent(nodePath);

//        // if we need the Reference Type
//        Element nodeReferenceType = new Element("ReferenceType");
//        nodeReferenceType.setAttribute("ns", rd.getNodeId().getNamespaceIndex().toString());
//        nodeReferenceType.setText(rd.getNodeId().getIdentifier().toString());
//        node.addContent(nodeReferenceType);
        try
        {
          Element nodeValue = new Element("Value");
          rd.getNodeId().local().ifPresent((NodeId) ->
          {
            try
            {
              // read the value from the variable exposed in the OPC-UA Server
              VariableNode vNode = client.getAddressSpace().createVariableNode(NodeId);
              DataValue value = vNode.readValue().get();
              nodeValue.setText(value.getValue().getValue().toString());
            } catch (InterruptedException | ExecutionException ex)
            {
              java.util.logging.Logger.getLogger(MSBClientSubscription.class.getName()).log(Level.SEVERE, null, ex);
            }

          });
          node.addContent(nodeValue);
        } catch (Exception ex)
        {
          // this is empty on purpose, since every time a variable does not have a value an exception is thrown
          // TODO - handle this in another way, maybe or let the oompa loopas do there work and forget about this
        }

        // recursively browse to children
        if (nextInteraction > 0)
        {
          rd.getNodeId().local().ifPresent(nodeId ->
          {
            // recursive call HERE
            List<Element> list = browseNode(client, nodeId, nextInteraction, ignoreList);
            if (!list.isEmpty())
            {
              // the recursive function returns a list of nodes (XML), so if a node has children they are added here
              // for each recursvie call   
              node.addContent(list);
            }
          });
        }
        nodes.add(node);
      } // end of for
    } catch (InterruptedException | ExecutionException | NullPointerException e)
    {
      logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);

    }
    return nodes;
  }

  /**
   *
   */
  public void exitProgram()
  {
    if (msbClientInstance != null)
    {
      msbClientInstance.disconnect();
    }
  }

  /**
   *
   * @return
   */
  public OpcUaClient getClientObject()
  {
    return msbClientInstance;
  }

}
// EOF
