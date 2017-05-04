/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.prosys.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.transport.security.HttpsSecurityPolicy;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.ModelException;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.SecureIdentityException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UaAddress;
import com.prosysopc.ua.UaApplication.Protocol;
import com.prosysopc.ua.nodes.UaProperty;
import com.prosysopc.ua.server.FileNodeManager;
import com.prosysopc.ua.server.HistoryManagerListener;
import com.prosysopc.ua.server.NodeBuilderException;
import com.prosysopc.ua.server.NodeManagerListener;
import com.prosysopc.ua.server.UaInstantiationException;
import com.prosysopc.ua.server.UaServer;
import com.prosysopc.ua.server.UaServerException;
import com.prosysopc.ua.server.UserValidator;
import com.prosysopc.ua.server.compliance.ComplianceNodeManager;
import com.prosysopc.ua.server.compliance.NonUaNodeComplianceNodeManager;
import com.prosysopc.ua.server.nodes.FileFolderType;
import com.prosysopc.ua.server.nodes.UaObjectNode;
import com.prosysopc.ua.server.nodes.UaVariableNode;
import com.prosysopc.ua.types.opcua.server.BuildInfoTypeNode;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import java.util.Random;
import java.util.logging.Level;
//import fabio_opcua_badjoraz.MSB_gui;
import org.eclipse.paho.client.mqttv3.MqttException;



/**
 *
 * @author Admin
 */
public class OPCUAServer {
    
    protected UaServer server = null;
    protected static String APP_NAME = "MSB_OPCUA_server";
    private MyNodeManager myNodeManager;
    private NodeManagerListener myNodeManagerListener  = new MyNodeManagerListener();
    private HistoryManagerListener myHistorian;
    private ComplianceNodeManager complianceNodeManager;
    private NonUaNodeComplianceNodeManager nonUaNodeComplianceManager;
    private MyBigNodeManager myBigNodeManager;
    private int bigAddressSpaceNodes;
    private FileNodeManager fileNodeManager;
    private OPCUAServer myserver;
    protected final CertificateValidationListener validationListener = new MyCertificateValidationListener();
    
    public OPCUAServer( /*GUIMain _guimain*/){
       // guimain = _guimain;
    }
    
    protected void initialize(int port, int httpsPort, String applicationName)
                    throws SecureIdentityException, IOException, UaServerException {

            PropertyConfigurator.configureAndWatch(OPCUAServer.class.getResource("log.properties").getFile(), 5000);
            // *** Create the server
            server = new UaServer();
            // Uncomment to enable IPv6 networking
            // server.setEnableIPv6(true);

            // *** Application Description is sent to the clients
            ApplicationDescription appDescription = new ApplicationDescription();
            // 'localhost' (all lower case) in the ApplicationName and
            // ApplicationURI is converted to the actual host name of the computer
            // in which the application is run
            appDescription.setApplicationName(new LocalizedText(applicationName + "@localhost"));
            appDescription.setApplicationUri("urn:localhost:OPCUA:" + applicationName);
            appDescription.setProductUri("urn:prosysopc.com:OPCUA:" + applicationName);
            appDescription.setApplicationType(ApplicationType.Server);

            // *** Server Endpoints
            // TCP Port number for the UA Binary protocol
            server.setPort(Protocol.OpcTcp, port);
            // TCP Port for the HTTPS protocol
            //server.setPort(Protocol.Https, httpsPort); //nao precisamos

            // optional server name part of the URI (default for all protocols)
            server.setServerName("OPCUA/" + applicationName);

            // Optionally restrict the InetAddresses to which the server is bound.
            // You may also specify the addresses for each Protocol.
            // This is the default (isEnableIPv6 defines whether IPv6 address should
            // be included in the bound addresses. Note that it requires Java 7 or
            // later to work in practice in Windows):
            server.setBindAddresses(EndpointUtil.getInetAddresses(server.isEnableIPv6()));

            final PkiFileBasedCertificateValidator validator = new PkiFileBasedCertificateValidator();
            server.setCertificateValidator(validator);
            // ...and react to validation results with a custom handler
            validator.setValidationListener(validationListener);

            File privatePath = new File(validator.getBaseDir(), "private");
            // Use 0 to use the default keySize and default file names as before
            // (for other values the file names will include the key size).
            // keySizes = new int[] { 0, 4096 };

            // *** Application Identity
            int[] keySizes = null;
            // Define the Server application identity, including the Application
            // Instance Certificate (but don't sign it with the issuerCertificate as
            // explained above).
            final ApplicationIdentity identity = ApplicationIdentity.loadOrCreateCertificate(appDescription,
				"Sample Organisation", /* Private Key Password */"opcua",
				/* Key File Path */privatePath,
				/* Issuer Certificate & Private Key */null,
				/* Key Sizes for instance certificates to create */keySizes,
				/* Enable renewing the certificate */true);


            
            server.setApplicationIdentity(identity);
            // *** Security settings
            // Define the security modes to support for the Binary protocol -
            // ALL is the default
            server.setSecurityModes(SecurityMode.NON_SECURE);
            // The TLS security policies to use for HTTPS
            //server.getHttpsSettings().setHttpsSecurityPolicies(HttpsSecurityPolicy.ALL);

            // Number of threads to reserve for the HTTPS server, default is 10
            // server.setHttpsWorkerThreadCount(10);

            // Or define just a validation rule to check the hostname defined for
            // the certificate; ALLOW_ALL_HOSTNAME_VERIFIER is the default
            // client.getHttpsSettings().setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // Define the supported user Token policies
            server.addUserTokenPolicy(UserTokenPolicy.ANONYMOUS);
            // Define a validator for checking the user accounts


            // Register on the local discovery server (if present)
            try {
                    // UaAddress discoveryAddress = new UaAddress("opc.tcp://localhost:4840");
        UaAddress discoveryAddress = new UaAddress(ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service"));
                    
                    
                    server.setDiscoveryServerAddress(discoveryAddress);
                    
            } catch (URISyntaxException e) {
                    System.out.println("DiscoveryURL is not valid");
            }

            // *** init() creates the service handlers and the default endpoints
            // according to the above settings
            server.init();

            //initBuildInfo();

            // "Safety limits" for ill-behaving clients
            server.getSessionManager().setMaxSessionCount(500);
            server.getSessionManager().setMaxSessionTimeout(3600000); // one hour
            server.getSubscriptionManager().setMaxSubscriptionCount(50);

            // You can do your own additions to server initializations here

    }
    
    protected void createAddressSpace() throws StatusException, UaInstantiationException, NodeBuilderException {
		// Load the standard information models
		//loadInformationModels();

		// My Node Manager
		myNodeManager = new MyNodeManager(server, MyNodeManager.NAMESPACE);
                
		myNodeManager.addListener(myNodeManagerListener);

		// My I/O Manager Listener
		myNodeManager.getIoManager().addListeners(new MyIoManagerListener());

		// My HistoryManager
		myNodeManager.getHistoryManager().setListener(myHistorian);

		// ComplianceNodeManagers
		complianceNodeManager = new ComplianceNodeManager(server, "http://www.prosysopc.com/OPCUA/ComplianceNodes");
		nonUaNodeComplianceManager = new NonUaNodeComplianceNodeManager(server,
				"http://www.prosysopc.com/OPCUA/ComplianceNonUaNodes");

		// A sample node manager that can handle a big amount of UA nodes
		// without creating UaNode objects in memory
		createBigNodeManager();

		createFileNodeManager();

		//logger.info("Address space created.");
	}
    
    public UaServer getServer() {
        if (server != null)    
            return server;
        else return null;
    }
    
    private void createBigNodeManager() {
            myBigNodeManager = new MyBigNodeManager(server, "http://www.prosysopc.com/OPCUA/SampleBigAddressSpace",
                            bigAddressSpaceNodes);
    }
    
    private void createFileNodeManager() throws StatusException {
            fileNodeManager = new FileNodeManager(getServer(), "http://www.prosysopc.com/OPCUA/FileTransfer", "Files");
            getServer().getNodeManagerRoot().getObjectsFolder().addReference(fileNodeManager.getRootFolder(),
                            Identifiers.Organizes, false);
            FileFolderType folder = fileNodeManager.addFolder("Folder");
            folder.setFilter("*");
    }
    
    protected void run(boolean enableSessionDiagnostics) throws UaServerException, StatusException {
            server.start();
            //initHistory();
            if (enableSessionDiagnostics)
                    server.getNodeManagerRoot().getServerData().getServerDiagnosticsNode().setEnabled(true);
            //startSimulation();

            // *** Main Menu Loop
            //mainMenu();
             
            
            while (true){
                	
		myNodeManager.simulate();
		myBigNodeManager.simulate();
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(OPCUAServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // *** End
            //stopSimulation();
            // Notify the clients about a shutdown, with a 5 second delay
           // println("Shutting down...");
            //server.shutdown(5, new LocalizedText("Closed by user", Locale.ENGLISH));
            //println("Closed.");
    }
    
   
    
    public static void println(String string) {
            System.out.println(string);
    }
    
    
    /*********************
    START MY IMPLS********
    *********************/
    
    public void add_node(String node) throws StatusException{
        myserver.myNodeManager.addNode(node);
    }
    
    public void delete_node(String node) throws StatusException{
        myserver.myNodeManager.deleteNode(node);
    }
    
    public void edit_node(String node, Object newValue) throws StatusException{
        myserver.myNodeManager.changeNodeValue(node, newValue);
    }
    
    public void create_server(final String server_name) throws Exception{
        
                Thread t = new Thread(){
                    private boolean keeprunning = true;
                    @Override
                    public void run() {
                        while (keeprunning){
                            //OPCUAServer sampleConsoleServer = new OPCUAServer();
                            myserver = new OPCUAServer();
                            try {
                                myserver.initialize(52520, 52443, server_name);
                                myserver.createAddressSpace();
                                myserver.run(false);
                            } catch (SecureIdentityException | IOException | UaServerException | StatusException | UaInstantiationException | NodeBuilderException ex) {
                                java.util.logging.Logger.getLogger(OPCUAServer.class.getName()).log(Level.SEVERE, null, ex);
                                keeprunning = false;
                            }
                                
                        }
                    }


                    @Override
                    public synchronized void start() {
                        System.out.println("Started thread...");
                        super.start(); //To change body of generated methods, choose Tools | Templates.
                    }   
                };
                
                t.start();
        
       /* OPCUAServer sampleConsoleServer = new OPCUAServer();
        sampleConsoleServer.initialize(52520, 52443, server_name);
        sampleConsoleServer.createAddressSpace();
        sampleConsoleServer.run(false);*/
    
    }
    
    /*********************
    END MY IMPLS**********
    *********************/

    
    
    public static void main(String[] args) throws Exception {
		// Initialize log4j logging
                Random rand = new Random();
                int randint = rand.nextInt();

		// *** Initialization and Start Up
		OPCUAServer sampleConsoleServer = new OPCUAServer();

		// Initialize the server
		sampleConsoleServer.initialize(52520, 52443, APP_NAME /*+ randint*/);

		// Create the address space
		sampleConsoleServer.createAddressSpace();
                             
		// TCP Buffer size parameters - this may help with high traffic
		// situations.
		// See http://fasterdata.es.net/host-tuning/background/ for some hints
		// how to use it
		// UATcpServer.setReceiveBufferSize(700000);

		// Start the server, when you have finished your own initializations
		// This will allow connections from the clients
		// Start up the server (enabling or disabling diagnostics according to
		// the cmd line args)
		sampleConsoleServer.run(false);
                
	}


    
    
}
