/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.server;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.stack.core.application.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.application.DefaultCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TestStackExRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TestStackExResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.TestStackRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TestStackResponse;
import org.eclipse.milo.opcua.stack.core.util.CryptoRestrictions;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.client.UaTcpStackClient;
import org.eclipse.milo.opcua.stack.core.application.CertificateManager;
import org.eclipse.milo.opcua.stack.core.application.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisteredServer;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import eu.openmos.msb.datastructures.MSBConstants;
import static eu.openmos.msb.datastructures.MSBConstants.LDS_ENDPOINT;
import eu.openmos.msb.opcua.milo.client.X509IdentityProvider;

/**
 *
 * @author fabio.miranda
 */
public class OPCServer
{

  private final OpcUaServer server;
  public Boolean control = false;
  private OpcUaClient client;
  private final AtomicLong requestHandle = new AtomicLong(1L);
  Timer periodicRegisterTimer;
  RegisterServerRequest periodicRegServerRequest;

  //Period (in seconds) for executing the server registration.
  private static final long PERIODIC_REGISTER_TIME_IN_SEC = 300l;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected String bindingIP = "";
  protected String bindingPort = "";
  protected String serverName;
  protected String applicationUri = "";
  protected String productUri = "";
  protected String applicationName = "";

  /**
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {
    OPCServer server = new OPCServer("opc.tcp://localhost:12640/MSB-OPCUA-SERVER");

    server.startup().get();

    server.register(LDS_ENDPOINT);

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  /**
   *
   * @param serverURL
   * @throws Exception
   */
  public OPCServer(String serverURL) throws Exception
  {
    URL.setURLStreamHandlerFactory(protocol -> "opc.tcp".equals(protocol) ? new URLStreamHandler()
    {
      @Override
      protected URLConnection openConnection(URL url) throws IOException
      {
        return new URLConnection(url)
        {
          @Override
          public void connect() throws IOException
          {
            System.out.println("Connected!");
          }
        };
      }
    } : null);

    URL fullDiscoveryUrl = null;
    try
    {
      fullDiscoveryUrl = new URL(serverURL);
    } catch (MalformedURLException e)
    {
      e.printStackTrace();
      //return;
    }

    bindingIP = fullDiscoveryUrl.getHost();
    bindingPort = String.valueOf(fullDiscoveryUrl.getPort());
    serverName = fullDiscoveryUrl.getPath();
    if (serverName.startsWith("/") && serverName.length() > 1)
    {
      serverName = serverName.substring(1);
    } else
    {
      serverName = "";
    }

    setApplicationUri("urn:MSB:opcua:server");
    setProductUri("urn:msb:opcua:sdk");
    setApplicationName("MSB Milo server");

    System.out.println("MSB Server data: Ip,Port,Name: " + bindingIP + ":" + bindingPort + serverName);
    System.out.println("MSB Server data: appURI,prodURI,appName: " + applicationUri + productUri + applicationName);

    CryptoRestrictions.remove();
    File securityDir = new File("./security/");
    if (!securityDir.exists() && !securityDir.mkdirs())
    {
      throw new Exception("unable to create security directory");
    }
//    KeyStoreLoader loader = new KeyStoreLoader().load();

    LoggerFactory.getLogger(getClass())
            .info("security temp dir: {}", securityDir.getAbsolutePath());

    // allow access            
    UsernameIdentityValidator identityValidator = new UsernameIdentityValidator(true,
            challenge ->
    {
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

//    List<UserTokenPolicy> userTokenPolicies = newArrayList(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
//            OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME);
    CertificateManager certificateManager = new DefaultCertificateManager();
    CertificateValidator certificateValidator = new DefaultCertificateValidator(securityDir);

    OpcUaServerConfig serverConfig = OpcUaServerConfig.builder()
            .setApplicationUri(applicationUri)
            .setApplicationName(LocalizedText.english(applicationName))
            .setBindAddresses(newArrayList(bindingIP))
            .setBindPort(Integer.parseInt(bindingPort))
            .setBuildInfo(
                    new BuildInfo(
                            productUri,
                            "OpenMOS",
                            "MSB OPC-UA Server",
                            OpcUaServer.SDK_VERSION,
                            "", DateTime.now()))
            .setCertificateManager(certificateManager)
            .setCertificateValidator(certificateValidator)
            .setIdentityValidator(identityValidator)
            .setProductUri(productUri)
            .setServerName(serverName)
            .setSecurityPolicies(
                    EnumSet.of(
                            SecurityPolicy.None,
                            SecurityPolicy.Basic128Rsa15,
                            SecurityPolicy.Basic256,
                            SecurityPolicy.Basic256Sha256))
            .setUserTokenPolicies(
                    ImmutableList.of(
                            USER_TOKEN_POLICY_ANONYMOUS,
                            USER_TOKEN_POLICY_USERNAME))
            .build();
    server = new OpcUaServer(serverConfig);
    server.getNamespaceManager().registerAndAdd(
            OPCServerNamespace.NAMESPACE_URI,
            idx -> new OPCServerNamespace(server, idx));
    server.getServer().addRequestHandler(TestStackRequest.class, service ->
    {
      TestStackRequest request = service.getRequest();
      ResponseHeader header = service.createResponseHeader();
      service.setResponse(new TestStackResponse(header, request.getInput()));
    });
    server.getServer().addRequestHandler(TestStackExRequest.class, service ->
    {
      TestStackExRequest request = service.getRequest();
      ResponseHeader header = service.createResponseHeader();
      service.setResponse(new TestStackExResponse(header, request.getInput()));
    });

  }

  /**
   *
   * @return
   */
  public OpcUaServer getServer()
  {
    return server;
  }

  /**
   *
   * @return
   */
  public CompletableFuture<OpcUaServer> startup()
  {
    control = true;
    return server.startup();
  }

  /**
   *
   * @return
   */
  public CompletableFuture<OpcUaServer> shutdown()
  {
    return server.shutdown();
  }

  /**
   *
   * @param discoveryEndpoint
   * @return
   */
  public int register(String discoveryEndpoint)
  {
    System.out.println("REGISTER AT " + discoveryEndpoint);
    try
    {
      // Process registration data
      createRegisterServerData(discoveryEndpoint);
      // Perform periodic registration
      periodicRegisterTimer = new Timer();
      periodicRegisterTimer.schedule(new PeriodicRegistrationManager(), 0, PERIODIC_REGISTER_TIME_IN_SEC * 1000);
      return 1;
    } catch (Exception ex)
    {
      java.util.logging.Logger.getLogger(OPCServer.class.getName()).log(Level.SEVERE, null, ex);
      return -1;
    }
  }

  /**
   *
   * @param appURI
   */
  void setApplicationUri(String appURI)
  {
    applicationUri = appURI;
  }

  /**
   *
   * @param prodURI
   */
  void setProductUri(String prodURI)
  {
    productUri = prodURI;
  }

  /**
   *
   * @param appName
   */
  void setApplicationName(String appName)
  {
    applicationName = appName;
  }

  /**
   *
   * @return
   */
  public String getApplicationUri()
  {
    return applicationUri;
  }

  /**
   *
   * @return
   */
  public String getProductUri()
  {
    return productUri;
  }

  /**
   *
   * @return
   */
  public LocalizedText getApplicationName()
  {
    return LocalizedText.english(applicationName);
  }

  /**
   *
   * @param discoveryEndpoint
   * @throws Exception
   */
  void createRegisterServerData(String discoveryEndpoint) throws Exception
  {

    SecurityPolicy securityPolicy = SecurityPolicy.Basic256;
    String securityMode = "SignAndEncrypt";

    EndpointDescription[] endpoints = UaTcpStackClient.getEndpoints(discoveryEndpoint).get();

    EndpointDescription endpoint = Arrays.stream(endpoints)
            .filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getSecurityPolicyUri()))
            .filter(e -> e.getSecurityMode().toString().compareTo(securityMode) == 0)
            .findFirst()
            .orElseThrow(() -> new Exception("no desired endpoints returned"));
  
    X509IdentityProvider x509IdentityProvider = new X509IdentityProvider(MSBConstants.CERTS_PATH + "\\openssl_crt.der",
            MSBConstants.CERTS_PATH + "\\herong.key");

    X509Certificate cert = x509IdentityProvider.getCertificate();
    KeyPair keyPair = new KeyPair(cert.getPublicKey(), x509IdentityProvider.getPrivateKey());
    OpcUaClientConfig clientConfig = OpcUaClientConfig.builder().setApplicationName(LocalizedText.english("opc-ua client"))
            .setApplicationUri("urn:opcua client")
            .setCertificate(cert)
            .setKeyPair(keyPair)
            .setEndpoint(endpoint)
            .setIdentityProvider(x509IdentityProvider)
            .setRequestTimeout(uint(10000))
            .build();
    client = new OpcUaClient(clientConfig);

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

  /**
   *
   */
  class PeriodicRegistrationManager extends TimerTask
  {

    public void run()
    {
      CompletableFuture<RegisterServerResponse> future = client.sendRequest(periodicRegServerRequest);
      future.whenComplete((response, ex) ->
      {
        if (response != null)
        {
          logger.info("Received RegisterServerResponse output={}", response.getResponseHeader().toString());
        } else
        {
          logger.error("Error: {}", ex.getMessage(), ex);
        }
      });
    }
  }
}
