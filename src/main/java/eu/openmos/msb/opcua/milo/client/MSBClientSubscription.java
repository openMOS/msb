/*
 * TODO - Add licence
 */
package eu.openmos.msb.opcua.milo.client;

// IMPORTS
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

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

    System.out.println("\n****RUNNING Milo MSB Instance ****\n");

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
   * TODO - fabio Documentation
   *
   * @param client
   * @param objectId
   * @param methodId
   * @return
   */
  public CompletableFuture<String> InvoqueDeviceSkill(OpcUaClient client, NodeId objectId, NodeId methodId)
  {

    CallMethodRequest request = new CallMethodRequest(
            objectId, methodId, null); //new Variant[]{new Variant(null)}

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
    final List<Element> nodes = new ArrayList<Element>();
    try
    {
      BrowseDescription browse = new BrowseDescription(
              browseRoot,
              BrowseDirection.Forward,
              Identifiers.References,
              true,
              uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue() | NodeClass.ObjectType.getValue()),
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

        Element node = new Element(referenceName.replaceAll(":", "").replaceAll(" ", "_"));

        Element nodeType = new Element("Type");
        nodeType.setAttribute("id", rd.getNodeId().getType().toString());
        nodeType.setAttribute("namespace", rd.getNodeId().getIdentifier().toString());
        node.addContent(nodeType);

        Element nodePath = new Element("Path");
        nodePath.setAttribute("ns", rd.getNodeId().getNamespaceIndex().toString());
        nodePath.setText(rd.getNodeId().getIdentifier().toString());
        node.addContent(nodePath);

        Element nodeReferenceType = new Element("ReferenceType");
        nodeReferenceType.setAttribute("ns", rd.getNodeId().getNamespaceIndex().toString());
        nodeReferenceType.setText(rd.getNodeId().getIdentifier().toString());
        node.addContent(nodeReferenceType);

//        try
//        {
//          Element nodeValue = new Element("Value");
//          VariableNode vNode = client.getAddressSpace().createVariableNode(rd.getNodeId().local().get());
//          DataValue value = vNode.readValue().get();
//          nodeValue.setText(value.getValue().getValue().toString());
//          node.addContent(nodeValue);
//        }
//        catch(Exception e)
//        {
//          System.out.println("[ERROR] " + rd.getBrowseName() + " " + e.getMessage());
//          //logger.error("Browsing node={} failed: {}", rd.getBrowseName(), e.getMessage(), e);
//        }
//        
        try
        {
          Element nodeValue = new Element("Value");
          rd.getNodeId().local().ifPresent((NodeId) ->
          {
            try
            {
              VariableNode vNode = client.getAddressSpace().createVariableNode(NodeId);
              DataValue value = vNode.readValue().get();
              nodeValue.setText(value.getValue().getValue().toString());
            } catch (InterruptedException ex)
            {
              java.util.logging.Logger.getLogger(MSBClientSubscription.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex)
            {
              java.util.logging.Logger.getLogger(MSBClientSubscription.class.getName()).log(Level.SEVERE, null, ex);
            }
            
          });
          node.addContent(nodeValue);
          
          
          
        } catch (Exception e)
        {
          //System.out.println("[ERROR] " + rd.getBrowseName() + " " + e.getMessage());
          //logger.error("Browsing node={} failed: {}", rd.getBrowseName(), e.getMessage(), e);
        }

        // recursively browse to children
        if (nextInteraction > 0)
        {
          rd.getNodeId().local().ifPresent(nodeId ->
          {
            List<Element> list = browseNode(client, nodeId, nextInteraction, ignoreList);
            if (!list.isEmpty())
            {
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
