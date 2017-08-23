/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.server;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.LinkedList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.AccessContext;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.EventItem;
import org.eclipse.milo.opcua.sdk.server.api.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.api.Namespace;
import org.eclipse.milo.opcua.sdk.server.api.ServerNodeMap;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.ServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.AddReferencesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteNodesItem;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.openmos.msb.opcua.milo.server.methods.GeneralMethod;
import eu.openmos.msb.opcua.milo.server.methods.ChangeState;
import eu.openmos.msb.opcua.milo.server.methods.UpdateDevice;

/**
 *
 * @author fabio.miranda
 */
public class OPCUAMSBServerNamespace implements Namespace
{

  public static final String NAMESPACE_URI = "urn:eclipse:milo:MSB-namespace";

  private static final Object[][] STATIC_SCALAR_NODES = new Object[][]
  {
    {
      "Boolean", Identifiers.Boolean, new Variant(false)
    },
    {
      "Byte", Identifiers.Byte, new Variant(ubyte(0x00))
    },
    {
      "SByte", Identifiers.SByte, new Variant((byte) 0x00)
    },
    {
      "Int16", Identifiers.Int16, new Variant((short) 16)
    },
    {
      "Int32", Identifiers.Int32, new Variant(32)
    },
    {
      "Int64", Identifiers.Int64, new Variant(64L)
    },
    {
      "UInt16", Identifiers.UInt16, new Variant(ushort(16))
    },
    {
      "UInt32", Identifiers.UInt32, new Variant(uint(32))
    },
    {
      "UInt64", Identifiers.UInt64, new Variant(ulong(64L))
    },
    {
      "Float", Identifiers.Float, new Variant(3.14f)
    },
    {
      "Double", Identifiers.Double, new Variant(3.14d)
    },
    {
      "String", Identifiers.String, new Variant("string value")
    },
    {
      "DateTime", Identifiers.DateTime, new Variant(DateTime.now())
    },
    {
      "Guid", Identifiers.Guid, new Variant(UUID.randomUUID())
    },
    {
      "ByteString", Identifiers.ByteString, new Variant(new ByteString(new byte[]
      {
        0x01, 0x02, 0x03, 0x04
      }))
    },
    {
      "XmlElement", Identifiers.XmlElement, new Variant(new XmlElement("<a>hello</a>"))
    },
    {
      "LocalizedText", Identifiers.LocalizedText, new Variant(LocalizedText.english("localized text"))
    },
    {
      "QualifiedName", Identifiers.QualifiedName, new Variant(new QualifiedName(1234, "defg"))
    },
    {
      "NodeId", Identifiers.NodeId, new Variant(new NodeId(1234, "abcd"))
    },
    {
      "Duration", Identifiers.Duration, new Variant(1.0)
    },
    {
      "UtcTime", Identifiers.UtcTime, new Variant(DateTime.now())
    },
  };

  private static final Object[][] STATIC_ARRAY_NODES = new Object[][]
  {
    {
      "BooleanArray", Identifiers.Boolean, false
    },
    {
      "ByteArray", Identifiers.Byte, ubyte(0)
    },
    {
      "SByteArray", Identifiers.SByte, (byte) 0x00
    },
    {
      "Int16Array", Identifiers.Int16, (short) 16
    },
    {
      "Int32Array", Identifiers.Int32, 32
    },
    {
      "Int64Array", Identifiers.Int64, 64L
    },
    {
      "UInt16Array", Identifiers.UInt16, ushort(16)
    },
    {
      "UInt32Array", Identifiers.UInt32, uint(32)
    },
    {
      "UInt64Array", Identifiers.UInt64, ulong(64L)
    },
    {
      "FloatArray", Identifiers.Float, 3.14f
    },
    {
      "DoubleArray", Identifiers.Double, 3.14d
    },
    {
      "StringArray", Identifiers.String, "string value"
    },
    {
      "DateTimeArray", Identifiers.DateTime, new Variant(DateTime.now())
    },
    {
      "GuidArray", Identifiers.Guid, new Variant(UUID.randomUUID())
    },
    {
      "ByteStringArray", Identifiers.ByteString, new Variant(new ByteString(new byte[]
      {
        0x01, 0x02, 0x03, 0x04
      }))
    },
    {
      "XmlElementArray", Identifiers.XmlElement, new Variant(new XmlElement("<a>hello</a>"))
    },
    {
      "LocalizedTextArray", Identifiers.LocalizedText, new Variant(LocalizedText.english("localized text"))
    },
    {
      "QualifiedNameArray", Identifiers.QualifiedName, new Variant(new QualifiedName(1234, "defg"))
    },
    {
      "NodeIdArray", Identifiers.NodeId, new Variant(new NodeId(1234, "abcd"))
    }
  };

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final SubscriptionModel subscriptionModel;
  private final OpcUaServer server;
  private final UShort namespaceIndex;
  private final UaFolderNode parentFolder;

  /**
   *
   * @param server
   * @param namespaceIndex
   */
  public OPCUAMSBServerNamespace(OpcUaServer server, UShort namespaceIndex)
  {
    this.server = server;
    this.namespaceIndex = namespaceIndex;

    NodeId parentFolderNodeId = new NodeId(namespaceIndex, "MSB");

    parentFolder = new UaFolderNode(server.getNodeMap(), parentFolderNodeId,
            new QualifiedName(namespaceIndex, "MSB"), LocalizedText.english("MSB"));

    server.getNodeMap().put(parentFolderNodeId, parentFolder);

    try
    {
      server.getUaNamespace().addReference(Identifiers.ObjectsFolder, Identifiers.Organizes, true,
              parentFolderNodeId.expanded(), NodeClass.Object);
    } catch (UaException e)
    {
      logger.error("Error adding reference to Objects folder.", e);
    }

    // af-silva - declerations of the OPC-UA methods
    addGeneralMethodNode(parentFolder); 
    addChangeStateNode(parentFolder); 
    addUpdateDeviceNode(parentFolder);

    subscriptionModel = new SubscriptionModel(server, this);
  }

  /**
   *
   * @return
   */
  @Override
  public UShort getNamespaceIndex()
  {
    return namespaceIndex;
  }

  /**
   *
   * @return
   */
  @Override
  public String getNamespaceUri()
  {
    return NAMESPACE_URI;
  }

  /**
   *
   * @param context
   * @param maxAge
   * @param timestamps
   * @param readValueIds
   */
  @Override
  public void read(ReadContext context, Double maxAge, TimestampsToReturn timestamps, List<ReadValueId> readValueIds)
  {
    List<DataValue> results = Lists.newArrayListWithCapacity(readValueIds.size());

    for (ReadValueId id : readValueIds)
    {
      ServerNode node = server.getNodeMap().get(id.getNodeId());

      if (node != null)
      {
        DataValue value = node.readAttribute(new AttributeContext(server), id.getAttributeId(), timestamps, id.getIndexRange());

        if (logger.isTraceEnabled())
        {
          Variant variant = value.getValue();
          Object o = variant != null ? variant.getValue() : null;
          logger.trace("Read value={} from attributeId={} of {}", o, id.getAttributeId(), id.getNodeId());
        }

        results.add(value);
      } else
      {
        results.add(new DataValue(new StatusCode(StatusCodes.Bad_NodeIdUnknown)));
      }
    }

    context.complete(results);
  }

  /**
   *
   * @param context
   * @param writeValues
   */
  @Override
  public void write(WriteContext context, List<WriteValue> writeValues)
  {
    List<StatusCode> results = Lists.newArrayListWithCapacity(writeValues.size());

    for (WriteValue writeValue : writeValues)
    {
      try
      {
        ServerNode node = server.getNodeMap().getNode(writeValue.getNodeId())
                .orElseThrow(() -> new UaException(StatusCodes.Bad_NodeIdUnknown));

        node.writeAttribute(new AttributeContext(server), writeValue.getAttributeId(),
                writeValue.getValue(), writeValue.getIndexRange());

        if (logger.isTraceEnabled())
        {
          Variant variant = writeValue.getValue().getValue();
          Object o = variant != null ? variant.getValue() : null;
          logger.trace("Wrote value={} to attributeId={} of {}", o, writeValue.getAttributeId(),
                  writeValue.getNodeId());
        }

        results.add(StatusCode.GOOD);
      } catch (UaException e)
      {
        results.add(e.getStatusCode());
      }
    }

    context.complete(results);
  }

  /**
   *
   * @param context
   * @param requests
   */
  @Override
  public void call(CallContext context, List<CallMethodRequest> requests)
  {
    Namespace.super.call(context, requests); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param methodId
   * @return
   */
  @Override
  public Optional<MethodInvocationHandler> getInvocationHandler(NodeId methodId)
  {
    //return Namespace.super.getInvocationHandler(methodId); //To change body of generated methods, choose Tools | Templates.
    ServerNode node = server.getNodeMap().get(methodId);

    if (node instanceof UaMethodNode)
    {
      return ((UaMethodNode) node).getInvocationHandler();
    } else
    {
      return Optional.empty();
    }
  }

  /**
   *
   * @param dataItems
   */
  @Override
  public void onDataItemsCreated(List<DataItem> dataItems)
  {
    subscriptionModel.onDataItemsCreated(dataItems);
  }

  /**
   *
   * @param dataItems
   */
  @Override
  public void onDataItemsModified(List<DataItem> dataItems)
  {
    subscriptionModel.onDataItemsModified(dataItems);
  }

  /**
   *
   * @param dataItems
   */
  @Override
  public void onDataItemsDeleted(List<DataItem> dataItems)
  {
    subscriptionModel.onDataItemsDeleted(dataItems);
  }

  /**
   *
   * @param list
   */
  @Override
  public void onEventItemsCreated(List<EventItem> list)
  {
    Namespace.super.onEventItemsCreated(list); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param list
   */
  @Override
  public void onEventItemsModified(List<EventItem> list)
  {
    Namespace.super.onEventItemsModified(list); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param list
   */
  @Override
  public void onEventItemsDeleted(List<EventItem> list)
  {
    Namespace.super.onEventItemsDeleted(list); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param monitoredItems
   */
  @Override
  public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems)
  {
    subscriptionModel.onMonitoringModeChanged(monitoredItems);
  }

  /**
   *
   * @param context
   * @param nodesToAdd
   */
  @Override
  public void addNode(AddNodesContext context, List<AddNodesItem> nodesToAdd)
  {
    Namespace.super.addNode(context, nodesToAdd); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param context
   * @param nodesToDelete
   */
  @Override
  public void deleteNode(DeleteNodesContext context, List<DeleteNodesItem> nodesToDelete)
  {
    Namespace.super.deleteNode(context, nodesToDelete); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param context
   * @param referencesToAdd
   */
  @Override
  public void addReference(AddReferencesContext context, List<AddReferencesItem> referencesToAdd)
  {
    Namespace.super.addReference(context, referencesToAdd); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param context
   * @param referencesToDelete
   */
  @Override
  public void deleteReference(DeleteReferencesContext context, List<DeleteReferencesItem> referencesToDelete)
  {
    Namespace.super.deleteReference(context, referencesToDelete); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   *
   * @param context
   * @param nodeId
   * @return
   */
  @Override
  public CompletableFuture<List<Reference>> browse(AccessContext context, NodeId nodeId)
  {
    ServerNode node = server.getNodeMap().get(nodeId);

    if (node != null)
    {
      return CompletableFuture.completedFuture(node.getReferences());
    } else
    {
      CompletableFuture<List<Reference>> f = new CompletableFuture<>();
      f.completeExceptionally(new UaException(StatusCodes.Bad_NodeIdUnknown));
      return f;
    }
  }

  /**
   *
   */
  protected void addStaticScalarNodes()
  {
    UaObjectNode folder = addFoldersToRoot(parentFolder, "/Static/AllProfiles/Scalar");

    for (Object[] os : STATIC_SCALAR_NODES)
    {
      String name = (String) os[0];
      NodeId typeId = (NodeId) os[1];
      Variant variant = (Variant) os[2];

      UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(server.getNodeMap())
              .setNodeId(new NodeId(namespaceIndex, "/Static/AllProfiles/Scalar/" + name))
              .setAccessLevel(ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
              .setBrowseName(new QualifiedName(namespaceIndex, name)).setDisplayName(LocalizedText.english(name))
              .setDataType(typeId).setTypeDefinition(Identifiers.BaseDataVariableType).build();

      node.setValue(new DataValue(variant));

      folder.addReference(new Reference(folder.getNodeId(), Identifiers.Organizes, node.getNodeId().expanded(),
              node.getNodeClass(), true));

      logger.debug("Added reference: {} -> {}", folder.getNodeId(), node.getNodeId());

      server.getNodeMap().put(node.getNodeId(), node);
    }

  }

  /**
   *
   * @param root
   * @param path
   * @return
   */
  protected UaObjectNode addFoldersToRoot(UaNode root, String path)
  {
    if (path.startsWith("/"))
    {
      path = path.substring(1, path.length());
    }
    String[] elements = path.split("/");

    LinkedList<UaObjectNode> folderNodes = processPathElements(Lists.newArrayList(elements), Lists.newArrayList(),
            Lists.newLinkedList());

    UaObjectNode firstNode = folderNodes.getFirst();

    if (!server.getNodeMap().containsKey(firstNode.getNodeId()))
    {
      server.getNodeMap().put(firstNode.getNodeId(), firstNode);

      server.getNodeMap().get(root.getNodeId()).addReference(new Reference(root.getNodeId(),
              Identifiers.Organizes, firstNode.getNodeId().expanded(), firstNode.getNodeClass(), true));

      logger.debug("Added reference: {} -> {}", root.getNodeId(), firstNode.getNodeId());
    }

    PeekingIterator<UaObjectNode> iterator = Iterators.peekingIterator(folderNodes.iterator());

    while (iterator.hasNext())
    {
      UaObjectNode node = iterator.next();

      server.getNodeMap().putIfAbsent(node.getNodeId(), node);

      if (iterator.hasNext())
      {
        UaObjectNode next = iterator.peek();

        if (!server.getNodeMap().containsKey(next.getNodeId()))
        {
          server.getNodeMap().put(next.getNodeId(), next);

          server.getNodeMap().get(node.getNodeId()).addReference(new Reference(node.getNodeId(),
                  Identifiers.Organizes, next.getNodeId().expanded(), next.getNodeClass(), true));
          logger.debug("Added reference: {} -> {}", node.getNodeId(), next.getNodeId());
        }
      }
    }

    return folderNodes.getLast();
  }

  /**
   *
   * @param elements
   * @param path
   * @param nodes
   * @return
   */
  private LinkedList<UaObjectNode> processPathElements(List<String> elements, List<String> path,
          LinkedList<UaObjectNode> nodes)
  {
    if (elements.size() == 1)
    {
      String name = elements.get(0);
      String prefix = String.join("/", path) + "/";
      if (!prefix.startsWith("/"))
      {
        prefix = "/" + prefix;
      }

      UaObjectNode node = UaObjectNode.builder(server.getNodeMap())
              .setNodeId(new NodeId(namespaceIndex, prefix + name))
              .setBrowseName(new QualifiedName(namespaceIndex, name)).setDisplayName(LocalizedText.english(name))
              .setTypeDefinition(Identifiers.FolderType).build();

      nodes.add(node);

      return nodes;
    } else
    {
      String name = elements.get(0);
      String prefix = String.join("/", path) + "/";
      if (!prefix.startsWith("/"))
      {
        prefix = "/" + prefix;
      }

      UaObjectNode node = UaObjectNode.builder(server.getNodeMap())
              .setNodeId(new NodeId(namespaceIndex, prefix + name))
              .setBrowseName(new QualifiedName(namespaceIndex, name)).setDisplayName(LocalizedText.english(name))
              .setTypeDefinition(Identifiers.FolderType).build();

      nodes.add(node);
      path.add(name);

      return processPathElements(elements.subList(1, elements.size()), path, nodes);
    }
  }

  /**
   * @return the server.getNodeManager()
   */
  public ServerNodeMap getNodeMap()
  {
    return server.getNodeMap();
  }

  /**
   * @return the server
   */
  public OpcUaServer getServer()
  {
    return server;
  }

  /**
   * @return the parentFolder
   */
  public UaFolderNode getParentFolder()
  {
    return parentFolder;
  }

  // ---------------------------------------------------------------------------------------------------------------- //
  /**
   *
   * @param folderNode
   */
  private void addChangeStateNode(UaFolderNode folderNode)
  {
    UaMethodNode methodChangeSate = UaMethodNode.builder(server.getNodeMap())
            .setNodeId(new NodeId(namespaceIndex, "MSB/ChangeState"))
            .setBrowseName(new QualifiedName(namespaceIndex, "ChangeState"))
            .setDisplayName(new LocalizedText(null, "ChangeState"))
            .setDescription(
                    LocalizedText.english("Returns the sum of two values."))
            .build();

    try
    {
      AnnotationBasedInvocationHandler invocationHandler
              = AnnotationBasedInvocationHandler.fromAnnotatedObject(server.getNodeMap(), new ChangeState());

      methodChangeSate.setProperty(UaMethodNode.InputArguments, invocationHandler.getInputArguments());
      methodChangeSate.setProperty(UaMethodNode.OutputArguments, invocationHandler.getOutputArguments());
      methodChangeSate.setInvocationHandler(invocationHandler);

      server.getNodeMap().addNode(methodChangeSate);

      folderNode.addReference(new Reference(
              folderNode.getNodeId(),
              Identifiers.HasComponent,
              methodChangeSate.getNodeId().expanded(),
              methodChangeSate.getNodeClass(),
              true
      ));

      System.out.println("folderNode.getNodeId():" + folderNode.getNodeId().toString());

      methodChangeSate.addReference(new Reference(
              methodChangeSate.getNodeId(),
              Identifiers.HasComponent,
              folderNode.getNodeId().expanded(),
              folderNode.getNodeClass(),
              false
      ));

      System.out.println("methodNodeSUM.getNodeId():" + methodChangeSate.getNodeId().toString());

    } catch (Exception e)
    {
      logger.error("Error creating ChangeState() method.", e);
    }
  }

  /**
   *
   * @param folderNode
   */
  private void addGeneralMethodNode(UaFolderNode folderNode)
  {
    UaMethodNode methodGeneralMethod = UaMethodNode.builder(server.getNodeMap())
            .setNodeId(new NodeId(namespaceIndex, "MSB/GeneralMethod"))
            .setBrowseName(new QualifiedName(namespaceIndex, "GeneralMethod"))
            .setDisplayName(new LocalizedText(null, "GeneralMethod"))
            .setDescription(
                    LocalizedText.english("Calls a MSB function with the respective arguments. Returns a feedback message"))
            .build();

    try
    {
      AnnotationBasedInvocationHandler invocationHandler
              = AnnotationBasedInvocationHandler.fromAnnotatedObject(
                      server.getNodeMap(), new GeneralMethod());

      methodGeneralMethod.setProperty(UaMethodNode.InputArguments, invocationHandler.getInputArguments());
      methodGeneralMethod.setProperty(UaMethodNode.OutputArguments, invocationHandler.getOutputArguments());
      methodGeneralMethod.setInvocationHandler(invocationHandler);

      server.getNodeMap().addNode(methodGeneralMethod);

      folderNode.addReference(new Reference(
              folderNode.getNodeId(),
              Identifiers.HasComponent,
              methodGeneralMethod.getNodeId().expanded(),
              methodGeneralMethod.getNodeClass(),
              true
      ));

      System.out.println("folderNode.getNodeId():" + folderNode.getNodeId().toString());

      methodGeneralMethod.addReference(new Reference(
              methodGeneralMethod.getNodeId(),
              Identifiers.HasComponent,
              folderNode.getNodeId().expanded(),
              folderNode.getNodeClass(),
              false
      ));

      System.out.println("GeneralMethod.getNodeId():" + methodGeneralMethod.getNodeId().toString());

    } catch (Exception e)
    {
      logger.error("Error creating GeneralMethod() method.", e);
    }
  }

  /**
   *
   * @param folderNode
   */
  private void addUpdateDeviceNode(UaFolderNode folderNode)
  {
    UaMethodNode methodUpdateDevice = UaMethodNode.builder(server.getNodeMap())
            .setNodeId(new NodeId(namespaceIndex, "MSB/UpdateDevice"))
            .setBrowseName(new QualifiedName(namespaceIndex, "UpdateDevice"))
            .setDisplayName(new LocalizedText(null, "UpdateDevice"))
            .setDescription(
                    LocalizedText.english("Calls a MSB function with the respective arguments. Returns a feedback message"))
            .build();

    try
    {
      AnnotationBasedInvocationHandler invocationHandler
              = AnnotationBasedInvocationHandler.fromAnnotatedObject(
                      server.getNodeMap(), new UpdateDevice());

      methodUpdateDevice.setProperty(UaMethodNode.InputArguments, invocationHandler.getInputArguments());
      methodUpdateDevice.setProperty(UaMethodNode.OutputArguments, invocationHandler.getOutputArguments());
      methodUpdateDevice.setInvocationHandler(invocationHandler);

      server.getNodeMap().addNode(methodUpdateDevice);

      folderNode.addReference(new Reference(
              folderNode.getNodeId(),
              Identifiers.HasComponent,
              methodUpdateDevice.getNodeId().expanded(),
              methodUpdateDevice.getNodeClass(),
              true
      ));

      System.out.println("folderNode.getNodeId():" + folderNode.getNodeId().toString());

      methodUpdateDevice.addReference(new Reference(
              methodUpdateDevice.getNodeId(),
              Identifiers.HasComponent,
              folderNode.getNodeId().expanded(),
              folderNode.getNodeClass(),
              false
      ));

      System.out.println("UpdateDevice.getNodeId():" + methodUpdateDevice.getNodeId().toString());

    } catch (Exception e)
    {
      logger.error("Error creating UpdateDevice() method.", e);
    }
  }

} // end of class
// EOF
