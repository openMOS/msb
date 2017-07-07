/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import java.net.*;
import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import org.eclipse.milo.opcua.stack.core.application.CertificateManager;
import org.eclipse.milo.opcua.stack.core.application.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.application.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.application.DefaultCertificateValidator;
import eu.openmos.msb.opcua.milo.client.KeyStoreLoader;

import java.util.EnumSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.client.config.UaTcpStackClientConfig;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisteredServer;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import java.util.Timer;
import java.util.TimerTask;
import static com.google.common.collect.Lists.newArrayList;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import eu.openmos.msb.opcua.milo.client.X509IdentityProvider;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;

/**
 * Created by fabio on 08.03.17. based on profanter MsbGenericComponent class
 */
public class OpcUaDiscoveryMilo {

    private static String MDNS_LISTENING = "_opcua-tcp._tcp.local.";

    Timer periodicRegisterTimer;
    /**
     * Period (in seconds) for executing the server registration.
     */
    private static final long PERIODIC_REGISTER_TIME_IN_SEC = 300l;

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private OpcUaServer server;
    //private UaTcpStackClient stackClient;

    private OpcUaClient client;
    private final AtomicLong requestHandle = new AtomicLong(1L);

    protected String bindingIP = "";
    protected String bindingPort = "";
    protected String serverName;

    private String selfConfiguration;

    private Thread waitThread;

    RegisterServerRequest periodicRegServerRequest;

    public OpcUaDiscoveryMilo(String arg) {

        // add opc.tcp protocol for URL class
        URL.setURLStreamHandlerFactory(protocol -> "opc.tcp".equals(protocol) ? new URLStreamHandler() {
            protected URLConnection openConnection(URL url) throws IOException {
                return new URLConnection(url) {
                    public void connect() throws IOException {
                        System.out.println("Connected!");
                    }
                };
            }
        } : null);

        URL fullDiscoveryUrl = null;
        try {
            fullDiscoveryUrl = new URL(arg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }

        bindingIP = fullDiscoveryUrl.getHost();
        bindingPort = String.valueOf(fullDiscoveryUrl.getPort());
        serverName = fullDiscoveryUrl.getPath();
        if (serverName.startsWith("/") && serverName.length() > 1) {
            serverName = serverName.substring(1);
        } else {
            serverName = "";
        }
        // System.out.println("opc.tcp://" + bindingIP + ":" + bindingPort + "/"
        // + serverName);
        // throw new UnsupportedOperationException("Not supported yet."); //To
        // change body of generated methods, choose Tools | Templates.
    }

    private static EndpointDescription getDiscoveryEndpoint(String discoveryUrl) {

        EndpointDescription[] endpoints;
        try {
            endpoints = UaTcpStackClient.getEndpoints(discoveryUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        if (endpoints.length == 0) {
            return null;
        }

        // sort by highest security level
        Arrays.sort(endpoints, (e1, e2) -> e2.getSecurityLevel().intValue() - e1.getSecurityLevel().intValue());

        // Filter the endpoints:
        for (EndpointDescription e : endpoints) {
            // we want opc.tcp
            if (e.getTransportProfileUri()
                    .compareToIgnoreCase("http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary") != 0) {
                continue;
            }
            if (e.getServer().getApplicationUri().compareToIgnoreCase("urn:fortiss:opcua:discovery:server") != 0) {
                continue;
            }

            return e;
        }

        return null;
    }

    void createRegisterServerData(String discoveryEndpoint) throws Exception {

        SecurityPolicy securityPolicy = SecurityPolicy.Basic256; // For example : SecurityPolicy.Basic256
        String securityMode = "SignAndEncrypt"; // For example : "SignAndEncrypt"

        EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(discoveryEndpoint).get();
        ApplicationDescription[] endpointsDesc = UaTcpStackClient.findServers(discoveryEndpoint).get();
        // EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints("opc.tcp://fmoranda-pc.introsys.lan:4840").get();

        EndpointDescription endpoint = Arrays.stream(endpoints)
                .filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getSecurityPolicyUri()))//
                .filter(e -> e.getSecurityMode().toString().compareTo(securityMode) == 0)//
                .findFirst()//
                .orElseThrow(() -> new Exception("no desired endpoints returned"));
        X509IdentityProvider x509IdentityProvider = new X509IdentityProvider("openssl_crt.der",
                "herong.key");
        X509Certificate cert = x509IdentityProvider.getCertificate();
        KeyPair keyPair = new KeyPair(cert.getPublicKey(), x509IdentityProvider.getPrivateKey());
        OpcUaClientConfig clientConfig = OpcUaClientConfig.builder().setApplicationName(LocalizedText.english("opc-ua client"))//
                .setApplicationUri("urn:opcua client")//
                .setCertificate(cert)//
                .setKeyPair(keyPair)//
                .setEndpoint(endpoint)//
                .setIdentityProvider(x509IdentityProvider)//
                // .setIdentityProvider(clientExample.getIdentityProvider())//
                .setRequestTimeout(uint(10000))//
                .build();
        client = new OpcUaClient(clientConfig);

        // KeyStoreLoader loader = new KeyStoreLoader().load();
        // UaTcpStackClientConfig config = UaTcpStackClientConfig.builder()
        //		.setApplicationName(LocalizedText.english("Stack Example Client"))
        //		.setApplicationUri(String.format("urn:example-client:%s", UUID.randomUUID()))
        //		.setCertificate(loader.getClientCertificate())
        //        .setKeyPair(loader.getClientKeyPair())
        //		.setEndpointUrl(discoveryEndpoint).build();
        UaTcpStackClientConfig config = UaTcpStackClientConfig.builder()
                .setApplicationName(LocalizedText.english("Stack Example Client"))
                .setApplicationUri(String.format("urn:example-client:%s", UUID.randomUUID()))
                .setCertificate(cert)
                .setKeyPair(keyPair)
                .setEndpointUrl(discoveryEndpoint).build();

        // stackClient = new UaTcpStackClient(config);
        RequestHeader header = new RequestHeader(NodeId.NULL_VALUE, DateTime.now(),
                uint(requestHandle.getAndIncrement()), uint(0), null, uint(60), null);

        LocalizedText[] serverNames = new LocalizedText[1];
        serverNames[0] = getApplicationName();
        ApplicationType serverType = ApplicationType.ClientAndServer;
        String gatewayServerUri = null;
        String[] discoveryUrls = new String[1];
        discoveryUrls[0] = "opc.tcp://" + bindingIP + ":" + bindingPort + "/" + serverName;
        String semaphoreFilePath = null;
        Boolean isOnline = true;
        RegisteredServer serverToBeRegistered = new RegisteredServer(getApplicationUri(), getProductUri(), serverNames,
                serverType, gatewayServerUri, discoveryUrls, semaphoreFilePath, isOnline);

        periodicRegServerRequest = new RegisterServerRequest(header, serverToBeRegistered);
    }

    class PeriodicRegistrationManager extends TimerTask {

        public void run() {
            // CompletableFuture<RegisterServerResponse> future = stackClient.sendRequest(periodicRegServerRequest);
            CompletableFuture<RegisterServerResponse> future = client.sendRequest(periodicRegServerRequest);
            future.whenComplete((response, ex) -> {
                if (response != null) {
                    logger.info("Received RegisterServerResponse output={}", response.getResponseHeader().toString());
                } else {
                    logger.error("Error: {}", ex.getMessage(), ex);
                }
            });
        }
    }

    public void register(String discoveryEndpoint) throws Exception {
        // Process registration data
        createRegisterServerData(discoveryEndpoint);
        // Perform periodic registration

        periodicRegisterTimer = new Timer();
        periodicRegisterTimer.schedule(new PeriodicRegistrationManager(), 0, PERIODIC_REGISTER_TIME_IN_SEC * 1000);

    }

    public void waitUntilFinish() throws IOException {
        System.out.println("Press any key to exit");
        System.in.read();
        server.shutdown();
    }

    public void startupMdns() {
        server.startup();
    }

    public void shutdown() {
        server.shutdown();
    }

    protected OpcUaServerConfig getServerConfig() throws Exception {
        UsernameIdentityValidator identityValidator = new UsernameIdentityValidator(true, // allow
                // anonymous
                // access
                challenge -> {
                    String user0 = "user";
                    String pass0 = "password";

                    char[] cs = new char[1000];
                    Arrays.fill(cs, 'a');
                    String user1 = new String(cs);
                    String pass1 = new String(cs);

                    boolean match0 = user0.equals(challenge.getUsername()) && pass0.equals(challenge.getPassword());

                    boolean match1 = user1.equals(challenge.getUsername()) && pass1.equals(challenge.getPassword());

                    return match0 || match1;
                });

        List<UserTokenPolicy> userTokenPolicies = newArrayList(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
                OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME);

        KeyStoreLoader loader = new KeyStoreLoader().load();

        /*
		 * TestCertificateManager certificateManager = new
		 * TestCertificateManager( loader.getServerKeyPair(),
		 * loader.getServerCertificate() );
		 * 
		 * TestCertificateValidator certificateValidator = new
		 * TestCertificateValidator( loader.getClientCertificate() );
         */
        // Enforce with security
        // CertificateManager certificateManager = new
        // DefaultCertificateManager(loader.getServerKeyPair(),
        // loader.getServerCertificate());
        // No security
        CertificateManager certificateManager = new DefaultCertificateManager();
        File securityDir = new File("./security/");
        if (!securityDir.exists() && !securityDir.mkdirs()) {
            throw new Exception("unable to create security directory");
        }
        CertificateValidator certificateValidator = new DefaultCertificateValidator(securityDir);

        return OpcUaServerConfig.builder().setApplicationName(getApplicationName())
                .setApplicationUri(getApplicationUri()).setBindAddresses(newArrayList(bindingIP))
                .setBindPort(Integer.parseInt(bindingPort)).setCertificateManager(certificateManager)
                .setCertificateValidator(certificateValidator)
                .setSecurityPolicies(EnumSet.of(SecurityPolicy.None, SecurityPolicy.Basic128Rsa15))
                .setProductUri(getProductUri()).setServerName(serverName).setUserTokenPolicies(userTokenPolicies)
                .setIdentityValidator(identityValidator).build();
    }

    protected String getApplicationUri() {
        return "urn:device:opcua:server";
    }

    protected String getProductUri() {
        return "urn:device:opcua:sdk";
    }

    protected LocalizedText getApplicationName() {
        return LocalizedText.english("device opc-ua server");
    }

    public void start() throws IOException, Exception {

        logger.info("startServer()");

        OpcUaServerConfig serverConfig = getServerConfig();

        server = new OpcUaServer(serverConfig);

        server.startup();

        /*
		KeyStoreLoader loader = new KeyStoreLoader().load();

        DeviceUaServer server = new DeviceUaServer(loader.getServerCertificate(), loader.getServerKeyPair());
        server.startupMdns();
         */
        //
    }
}
