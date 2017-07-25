/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.starter;

import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;
import eu.openmos.msb.datastructures.MSBClients;
import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import eu.openmos.msb.opcua.utils.OpcUaServersDiscoverySnippet;
import eu.openmos.msb.opcua.utils.OPCDeviceDiscoveryItf;
import eu.openmos.msb.database.stateless.DeviceRegistry;
import com.prosysopc.ua.ServiceException;
import eu.openmos.agentcloud.config.ConfigurationLoader;
import eu.openmos.msb.opcua.milo.server.opcuaServerMSB;
import eu.openmos.msb.recipesmanagement.RecipesDeployerImpl;
import eu.openmos.msb.opcua.utils.OpcUaDiscoveryMilo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import javax.xml.ws.Endpoint;
import org.slf4j.LoggerFactory;


/**
 *
 * @author fabio.miranda
 */
public class MSB_Struct
{

  private static final Map<String, Object> OPCclientIDMap = new HashMap<String, Object>();
  private static String MSB_OPCUA_SERVER_ADDRESS = null;
  private static eu.openmos.msb.opcua.milo.server.opcuaServerMSB opcuaServerInstanceMILO;
  private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());


  /**
   * @param args the command line arguments
   * @throws java.lang.Exception
   */
  public static void main(String[] args) throws Exception
  {

    opcuaServerInstanceMILO = new opcuaServerMSB(ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.server")); //new MSB Milo server
    //launch MILO MSB OPCUA Server endpoint
    if (opcuaServerInstanceMILO.control == false)
    {
      try
      {
        opcuaServerInstanceMILO.startup().get();
        opcuaServerInstanceMILO.register(ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service"));

      }
      catch (Exception ex)
      {
        Logger.getLogger(MSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      System.out.println("MSB Server already created!\n");
    }

    try
    {

    }
    catch (Exception ex)
    {
      java.util.logging.Logger.getLogger(OpcUaDiscoveryMilo.class.getName()).log(Level.SEVERE, null, ex);
    }

    try
    {//test to see if the server is running at the first discovery
      Thread.sleep(500);
    }
    catch (InterruptedException ex)
    {
      Thread.currentThread().interrupt();
    }

    //launch Discovery Service to search for other devices (OPCUA servers from devices)
    OPCDeviceDiscoveryItf OPCdevDiscItf;
    OPCdevDiscItf = new OPCDeviceDiscoveryItf()
    {
      /**
       *
       * @param name
       * @param app_uri
       */
      @Override
      public void on_new_server(String name, String app_uri)
      {
        System.out.println("server found: " + app_uri);
      }


      /**
       *
       * @param name
       * @param app_uri
       */
      @Override
      public void on_new_endpoint(String name, String app_uri)
      {
        //every time there is a "polling" cycle in discovery, check OPC client/server connections
        System.out.println("POLLING CYCLE********************************");
        if (OPCclientIDMap.size() > 0)
        { //verify client connection status in the end of the polling cycle
          // isto e preciso aqui?????
        }

        System.out.println("NAME: " + name + " URL: " + app_uri);

        DeviceRegistry dbMSB = new DeviceRegistry();
        ArrayList ArrayData = dbMSB.read_device_info(name);

        if (ArrayData.isEmpty())
        { //if the device name doesn't exist in the database, register 
          registerServer(name, app_uri);
          System.out.println("onNewServer Registered Server: " + name + " " + app_uri);
          if (name.contains("MSB"))
          {
            MSB_OPCUA_SERVER_ADDRESS = app_uri;
          }

          if (app_uri.contains("4840"))
          {
            System.out.println("Don't create an opc client for LDS!");
          }
          else
          {

            try
            {

              MSB_MiloClientSubscription instance = new MSB_MiloClientSubscription();
              instance.startConnection(app_uri);

              //save the client objectID and the name of the device as hashmap
              OPCclientIDMap.put(name, instance);

              //singleton to access client objects in other classes
              MSBClients myOpcUaClientsMap = MSBClients.getInstance();
              myOpcUaClientsMap.setOPCclientIDMaps(name, instance);

              // Iterate over all values, using the keySet method.
              //call SendServerURL() method from device
              OpcUaClient client = instance.getClientObject();
              if (!name.contains("MSB"))
              {

                instance.SendServerURL(client, MSB_OPCUA_SERVER_ADDRESS).exceptionally(ex
                  ->
                {
                  System.out.println("error invoking SendServerURL() for server: " + name + "\n" + ex); //logger.error("error invoking SendServerURL()", ex);
                  return "-1.0";
                }).thenAccept(v
                  ->
                {
                  //logger.info("SendServerURL(cenas)={}", v);
                  System.out.println("SendServerURL(uri)={}\n" + v);
                  //future.complete(client);
                });
              }

              if (client == null)
              {
                System.out.println("Client = null?");
              }

              for (String key : OPCclientIDMap.keySet())
              {
                System.out.println(key + " ->hashmap<- - " + OPCclientIDMap.get(key));
              }
              MSBClients myOpcUaClientsMap2 = MSBClients.getInstance(); //singleton to access client objects in other classes
              for (String key : myOpcUaClientsMap.getOPCclientIDMaps().keySet())
              {
                System.out.println(key + " ->singleton hashmap<- - " + myOpcUaClientsMap.getOPCclientIDMaps().get(key));
              }

            }
            catch (ServiceException ex)
            {
              Logger.getLogger(MSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (URISyntaxException ex)
            {
              Logger.getLogger(MSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (Exception ex)
            {
              Logger.getLogger(MSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
            }

          }

        }
        else
        {  //if the device name exist in the database, check if it is still available 
          System.out.println("Server : " + name + " " + app_uri + " Already registered in the Database!");

        }
      }


      /**
       *
       */
      @Override
      public void on_endpoint_dissapeared()
      {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }


      /**
       *
       */
      @Override
      public void reset_tables()
      {

        System.out.println("reset endpoint and servers tables...");
      }


      /**
       *
       * @param name
       * @param app_uri
       */
      @Override
      public void on_server_dissapeared(String name, String app_uri)
      {
        System.out.println("This server has disapeared: " + name);
        RemoveDownServer(name);

        for (String key : OPCclientIDMap.keySet())
        {
          System.out.println(key + " ->new local hashmap<- - " + OPCclientIDMap.get(key));
        }
        MSBClients myOpcUaClientsMap = MSBClients.getInstance(); //singleton to access client objects in other classes
        for (String key : myOpcUaClientsMap.getOPCclientIDMaps().keySet())
        {
          System.out.println(key + " ->new singleton hashmap<- - " + myOpcUaClientsMap.getOPCclientIDMaps().get(key));
        }
      }

    };

    String LDS_uri = ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service");

    OpcUaServersDiscoverySnippet OPCDiscoverySnippet = new OpcUaServersDiscoverySnippet(LDS_uri, OPCdevDiscItf); //descomentar 13-2-17
    OPCDiscoverySnippet.start();

    OPCDeviceItf Device = new OPCDeviceItf(); //inputs? endpoints, MAP<ID, OPCclientObject> ?

    String address = "http://0.0.0.0:9997/wsRecipesDeployer";
    //Endpoint.publish(address, new NewWebService());
    Endpoint.publish(address, new RecipesDeployerImpl());
    System.out.println("Listening: " + address);

  }


  /**
   *
   * @param serverName
   * @param serverUri
   * @return
   */
  private static String registerServer(java.lang.String serverName, java.lang.String serverUri)
  {
    DeviceRegistry dbMSB = new DeviceRegistry();
    String res = dbMSB.register_device(serverName, "null", "null", serverUri, "opcua");
    System.out.println(res);

    return res;
  }


  /**
   *
   * @param ServerName
   * @return
   */
  public static int RemoveDownServer(String ServerName)
  {

    MSBClients myOpcUaClientsMap = MSBClients.getInstance(); //singleton to access client objects in other classes
    myOpcUaClientsMap.deleteOPCclientIDMaps(ServerName); //remove server from singleton Hashmap
    OPCclientIDMap.remove(ServerName); //remove server from HashMap
    //delete server from DB
    DeviceRegistry dbMSB = new DeviceRegistry();
    int res = dbMSB.deregister_device(ServerName);

    if (res != -999)
    {
      System.out.println("DownServer successfully deleted from DB!");
      return 1;
    }
    else
    {
      System.out.println("ERROR deleting DownServer from DB!");
      return -1;
    }

  }


  /**
   *
   * @param address
   * @return
   */
  public static boolean pingUrl(final String address)
  {

    String[] temp = address.split(":");
    String addressFiltered = temp[1];
    System.out.println("Trying to Ping: " + addressFiltered);

    try
    {
      //boolean reachable = InetAddress.getByName(addressFiltered).isReachable(200);
      if (InetAddress.getByName(addressFiltered).isReachable(200))
      {
        return true;
      }

    }
    catch (IOException ex)
    {
      Logger.getLogger(MSB_Struct.class.getName()).log(Level.SEVERE, null, ex);
    }

    return false;
  }

}
