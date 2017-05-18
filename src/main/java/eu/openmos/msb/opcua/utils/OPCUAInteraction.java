/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.utils;

import eu.openmos.agentcloud.config.ConfigurationLoader;





/**
 *
 * @author Admin
 */
public class OPCUAInteraction
{

  public String discovery_url;


  /**
   *
   */
  public OPCUAInteraction()
  {

  }


  /**
   *
   */
  public void listen_to_servers_and_endpoints()
  {

    OPCDeviceDiscoveryItf opc_ua_servs_dynamic = new OPCDeviceDiscoveryItf()
    {
      /**
       *
       * @param name
       * @param app_uri
       */
      @Override
      public void on_new_server(String name, String app_uri)
      {
        registerServer(name, app_uri);
        System.out.println("onNewServer Registered Server: " + name + " " + app_uri);
      }


      /**
       *
       * @param parent_app_uri
       * @param app_uri
       */
      @Override
      public void on_new_endpoint(String parent_app_uri, String app_uri)
      {
        registerEndpoint(app_uri, parent_app_uri);
        System.out.println("onNewEndpointRegistered Server: " + app_uri + " " + parent_app_uri);
        String deviceid = app_uri.split("OPCUA/")[1];
        registerDevice(deviceid, "No description added yet.", "opcua_protocol");
        System.out.println("criar cliente para o device endpoint detetado! se não tiver sido já criado");

        DevicesDynamic devdyn = new DevicesDynamic()
        {
          @Override
          public void on_participant_disappear(String id, String reason)
          {
            throw new UnsupportedOperationException("Not supported yet.");
          }


          @Override
          public void on_participant_appear(String id, String description)
          {
            throw new UnsupportedOperationException("Not supported yet.");
          }


          @Override
          public void on_new_readonly_topic(String device_id, String topic_name)
          {
            registerTopic(topic_name, "ReadOnly", device_id);

          }


          @Override
          public void on_new_writeonly_topic(String device_id, String topic_name)
          {
            registerTopic(topic_name, "WriteOnly", device_id);

          }
        };

      }


      /**
       *
       */
      @Override
      public void on_endpoint_dissapeared()
      {

      }


      /**
       *
       */
      @Override
      public void reset_tables()
      {
        dropServersAndEndpoints();
        System.out.println("reset endpoint and servers tables...InteractionClass");
      }


      /**
       *
       * @param name
       * @param app_uri
       */
      @Override
      public void on_server_dissapeared(String name, String app_uri)
      {
        System.out.println("Server disapeared:" + name);
      }
    };

    OpcUaServersDiscoverySnippet opcua_discovery_snppt = new OpcUaServersDiscoverySnippet(discovery_url, opc_ua_servs_dynamic);
    opcua_discovery_snppt.start();

  }


  /**
   *
   * @param args
   */
  public static void main(String[] args)
  {
    OPCUAInteraction opcua_inter = new OPCUAInteraction();
    opcua_inter.discovery_url = ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service");
    opcua_inter.listen_to_servers_and_endpoints();
    System.out.println("listening to endpoints on " + opcua_inter.discovery_url);
  }


  /**
   *
   * @param endpointUri
   * @param parentServerUri
   * @return
   */
  private static String registerEndpoint(java.lang.String endpointUri, java.lang.String parentServerUri)
  {
    return "registerEndpoint";
  }


  /**
   *
   * @param serverName
   * @param serverUri
   * @return
   */
  private static String registerServer(java.lang.String serverName, java.lang.String serverUri)
  {
    return "registerServer";
  }


  /**
   *
   * @return
   */
  private static boolean dropServersAndEndpoints()
  {
    return true;
  }


  /**
   *
   * @param deviceId
   * @param deviceInfo
   * @param deviceProtocol
   * @return
   */
  private static String registerDevice(java.lang.String deviceId,
    java.lang.String deviceInfo, java.lang.String deviceProtocol)
  {
    return "registerDevice";
  }


  /**
   *
   * @param topicId
   * @param type
   * @param deviceId
   * @return
   */
  private static String registerTopic(java.lang.String topicId, java.lang.String type, java.lang.String deviceId)
  {
    return "registerTopic";
  }

}
