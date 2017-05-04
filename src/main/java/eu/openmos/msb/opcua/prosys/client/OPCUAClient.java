/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.prosys.client;

/**
 *
 * @author Admin
 */


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ServerConnectionException;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.MethodArgumentException;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaMethod;
import com.prosysopc.ua.nodes.UaNode;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowsePathTarget;
import org.opcfoundation.ua.core.DataChangeFilter;
import org.opcfoundation.ua.core.DataChangeTrigger;
import org.opcfoundation.ua.core.DeadbandType;
import org.opcfoundation.ua.core.EUInformation;
import org.opcfoundation.ua.core.ElementOperand;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.EventFilter;
import org.opcfoundation.ua.core.FilterOperator;
import org.opcfoundation.ua.core.HistoryEventFieldList;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.LiteralOperand;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.Range;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.SimpleAttributeOperand;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.MultiDimensionArrayUtils;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.ContentFilterBuilder;
import com.prosysopc.ua.EventNotifierClass;
import com.prosysopc.ua.MethodCallStatusException;
import com.prosysopc.ua.MonitoredItemBase;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UaAddress;
import com.prosysopc.ua.UaApplication.Protocol;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredEventItem;
import com.prosysopc.ua.client.MonitoredEventItemListener;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.ServerConnectionException;
import com.prosysopc.ua.client.ServerList;
import com.prosysopc.ua.client.ServerListException;
import com.prosysopc.ua.client.ServerStatusListener;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionAliveListener;
import com.prosysopc.ua.client.SubscriptionNotificationListener;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.client.UaClientListener;
import com.prosysopc.ua.nodes.MethodArgumentException;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaInstance;
import com.prosysopc.ua.nodes.UaMethod;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaObject;
import com.prosysopc.ua.nodes.UaReferenceType;
import com.prosysopc.ua.nodes.UaType;
import com.prosysopc.ua.nodes.UaVariable;
import com.prosysopc.ua.types.opcua.AnalogItemType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A very minimal client application. Connects to the server and reads one
 * variable. Works with a non-secure connection.
 */
public class OPCUAClient {
	/**
	 * @param args
	 */
	private UaClient client;
                
        
        public OPCUAClient (/*String endpoint*/)throws ServiceException, URISyntaxException {
                //client = new UaClient(endpoint);
                //client.setSecurityMode(SecurityMode.NONE);
                
            /*try {
                initialize(client);
                client.connect();
            } catch (SecureIdentityException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        
        }
	
	public static void main(String[] args) throws Exception {
		///*UaClient */client = new UaClient("opc.tcp://172.18.1.255:52520/OPCUA/SampleConsoleServer");
		//client.setSecurityMode(SecurityMode.NONE);
		//initialize(client);
		//client.connect();
		
		////callMethod();
		//DataValue value = client.readValue(Identifiers.Server_ServerStatus_State);
		//System.out.println(value);
		//client.disconnect();
	}
	
	
	public void initialize(String endpoint) throws Exception{
		
                System.out.println("Client trying to connect to "+endpoint);
                client = new UaClient(endpoint);
                client.setSecurityMode(SecurityMode.NONE);
                initialize(client);
                client.connect();

		
		/*****************/
		/*NodeId nodeId = Identifiers.RootFolder;
		List<ReferenceDescription> references;
		references = client.getAddressSpace().browse(nodeId);
		ReferenceDescription r = references.get(0);
		NodeId target;
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(1);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(0);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		
	
		List<UaMethod> methods = client.getAddressSpace().getMethods(target);
		
		//UaMethod method = client.getAddressSpace().getMethod(methodId);
		UaMethod method = methods.get(0);*/
		/*********/
	
			/*while (true){
				if (opt == 1){
					DataValue value = client.readValue(Identifiers.Server_ServerStatus_State);
					//System.out.println(value);

				} else if (opt == 2){
					callMethod(target, method);		
				}
				
		        try {
					TimeUnit.MILLISECONDS.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/

		//client.disconnect();		
		
	}
        
        
        
        
        
        public Variant[] callTrignometryMethod(String arg1, String arg2) throws ServiceException, StatusException, AddressSpaceException, ServerConnectionException, MethodArgumentException{
            /*****************/
		NodeId nodeId = Identifiers.RootFolder;
		List<ReferenceDescription> references;
		references = client.getAddressSpace().browse(nodeId);
		ReferenceDescription r = references.get(0);
		NodeId target;
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(1);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(0);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		
	
		List<UaMethod> methods = client.getAddressSpace().getMethods(target);
		
		//UaMethod method = client.getAddressSpace().getMethod(methodId);
		UaMethod method = methods.get(0);
            
                return callMethod(target, method, arg1, arg2);
                
        }
        
        
        
	/**
	 * Define a minimal ApplicationIdentity. If you use secure connections, you
	 * will also need to define the application instance certificate and manage
	 * server certificates. See the SampleConsoleClient.initialize() for a full
	 * example of that.
	 */
	protected static void initialize(UaClient client)
			throws SecureIdentityException, IOException, UnknownHostException {
		// *** Application Description is sent to the server
		ApplicationDescription appDescription = new ApplicationDescription();
		appDescription.setApplicationName(new LocalizedText("SimpleClient", Locale.ENGLISH));
		// 'localhost' (all lower case) in the URI is converted to the actual
		// host name of the computer in which the application is run
		appDescription.setApplicationUri("urn:localhost:UA:SimpleClient");
		appDescription.setProductUri("urn:prosysopc.com:UA:SimpleClient");
		appDescription.setApplicationType(ApplicationType.Client);

		final ApplicationIdentity identity = new ApplicationIdentity();
		identity.setApplicationDescription(appDescription);
		client.setApplicationIdentity(identity);
	}
        
        public String searchMyDeviceNamespace(){
            String string_to_return = new String("");
            
            try {
                
                NodeId nodeId = Identifiers.RootFolder;
		List<ReferenceDescription> references;
		references = client.getAddressSpace().browse(nodeId);
		ReferenceDescription r = references.get(0);
		NodeId target;
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(1);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(0);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
                
                //Let's know which device node we are searching on...:
                string_to_return = r.getDisplayName().getText() + "#";
                // Now let's search for objects inside "MyDevice" namespace. This namespace is known, so the previows search in the nodes tree was "hard-coded"
                references = client.getAddressSpace().browse(target);
                for(int xk = 0; xk<references.size(); xk++){
                    string_to_return = string_to_return + references.get(xk).getDisplayName().getText() + "#";
                }
                //returning format "   DeviceNode#object1#object2#...#object_n#    "
                return string_to_return;
               
                
            } catch (ServiceException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (StatusException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            return null;
        
        }
        
        
        public String readValue(String nodeName) {
            
            try {
                
                NodeId nodeId = Identifiers.RootFolder;
		List<ReferenceDescription> references;
		references = client.getAddressSpace().browse(nodeId);
		ReferenceDescription r = references.get(0);
		NodeId target;
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(1);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
		references = client.getAddressSpace().browse(target);
		r = references.get(0);
		try {
			target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
                // Now let's search inside "MyDevice" namespace. This namespace is known, so the previows search in the nodes tree was "hard-coded"
                references = client.getAddressSpace().browse(target);
                    for(int xk = 0; xk<references.size(); xk++){
                        if (references.get(xk).getDisplayName().getText().equals(nodeName)){
                            r = references.get(xk);
                            break;
                        }
                    }
                
                try {
                    target = client.getAddressSpace().getNamespaceTable().toNodeId(r.getNodeId());
                    //UaNode node = client.getAddressSpace().getNode(target);
                    DataValue value = client.readValue(target/*Identifiers.Server_ServerStatus_State*/);
                    System.out.println("Read value : " + value);
                    return value.toString();
		} catch (ServiceResultException e) {
			throw new ServiceException(e);
		}
               
                
            } catch (ServiceException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (StatusException ex) {
                Logger.getLogger(OPCUAClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            return null;
        }
        
	
	/*
	 * 
	 * Calls Sin(1) method from the SampleOPCUAServer
	 * 
	 */
	protected Variant[] callMethod(NodeId nodeId, UaMethod method, String arg1, String arg2) throws ServiceException, ServerConnectionException,
		AddressSpaceException, MethodArgumentException, StatusException {
		
		Argument[] inputArguments = method.getInputArguments();
		Variant[] inputs = new Variant[inputArguments.length];//readInputArguments(method);
		
		UaDataType dataType = (UaDataType) client.getAddressSpace().getType(inputArguments[0].getDataType());
		inputs[0] = client.getAddressSpace().getDataTypeConverter().parseVariant(arg1,
				dataType);
		
		dataType = (UaDataType) client.getAddressSpace().getType(inputArguments[1].getDataType());
		inputs[1] = client.getAddressSpace().getDataTypeConverter().parseVariant(arg2,
				dataType);
		
		Variant[] outputs = client.call(nodeId, method.getNodeId(), inputs);
		//printOutputArguments(method, outputs);
                return outputs;
	}	
	
	protected void printOutputArguments(UaMethod method, Variant[] outputs)
			throws ServiceException, AddressSpaceException, MethodArgumentException, StatusException {
		if ((outputs != null) && (outputs.length > 0)) {
			println("Output values:");
			Argument[] outputArguments = method.getOutputArguments();
			for (int i = 0; i < outputArguments.length; i++) {
				UaNode dataType = client.getAddressSpace().getType(outputArguments[i].getDataType());
				println(String.format("%s: %s {%s} = %s", outputArguments[i].getName(), dataType.getBrowseName(),
						outputArguments[i].getDescription().getText(), outputs[i].getValue()));
			}
		} else
			println("OK (no output)");
	}
	
	protected void println(String string) {
		System.out.println(string);
	}

}

