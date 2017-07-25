/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.database.stateless;

import eu.openmos.msb.database.interaction.DatabaseInteraction;
import java.util.ArrayList;
import javax.jws.WebParam;


public class DeviceRegistry implements IDeviceRegistry
{

  /**
   * Web service operation
   *
   * @param device_name
   * @param short_info
   * @param long_info
   * @param ip_addr
   * @param device_protocol
   * @return
   */
  //@WebMethod(operationName = "register_device") // [TODO - remove ???]
  @Override
  public String register_device(@WebParam(name = "device_name") String device_name,
    @WebParam(name = "short_info") String short_info,
    @WebParam(name = "long_info") String long_info,
    @WebParam(name = "ip_address") String ip_addr,
    @WebParam(name = "device_protocol") String device_protocol)
  {

    boolean res = DatabaseInteraction.getInstance().createDevice(device_name, device_protocol, short_info, long_info, "0", "0");

    if (res)
    {
      return "Server Registered Successfully";
    }
    else
    {
      return "Server Registered Failed";
    }
  }


  /**
   * Web service operation
   *
   * @param device_name
   * @return
   */
  //  @WebMethod(operationName = "read_device_info") // [TODO - remove ???]
  @Override
  public ArrayList<String> read_device_info(@WebParam(name = "device_id") String device_name)
  {
    return DatabaseInteraction.getInstance().readDeviceInfoByName(device_name);
  }


  /**
   * Web service operation
   *
   * @param topic_name
   * @param access_type
   * @param data_type
   * @param device_name
   * @return
   */
  //@WebMethod(operationName = "register_topic") // [TODO - remove ???]
  @Override
  public String register_topic(@WebParam(name = "topic_name") String topic_name,
    @WebParam(name = "access_type") String access_type,
    @WebParam(name = "data_type") String data_type,
    @WebParam(name = "device_name") String device_name)
  {
    throw new UnsupportedOperationException("Not supported anymore.");
  }


  /**
   * Web service operation
   *
   * @param parent_topic_name
   * @param content
   * @param target
   * @param header
   * @param timestamp
   * @return
   */
  //@WebMethod(operationName = "register_message") // [TODO - remove ???]
  @Override
  public String register_message(@WebParam(name = "parent_topic_name") String parent_topic_name,
    @WebParam(name = "target") String target,
    @WebParam(name = "timestamp") String timestamp,
    @WebParam(name = "header") String header,
    @WebParam(name = "content") String content)
  {
    throw new UnsupportedOperationException("Not supported anymore.");
  }


  /**
   * Web service operation
   *
   * @param device_name
   * @return
   */
  //@WebMethod(operationName = "get_topics_from_device") // [TODO - remove ???]
  @Override
  public ArrayList<String> get_topics_from_device(
    @WebParam(name = "device_id") String device_name)
  {
    throw new UnsupportedOperationException("Not supported anymore.");
  }


  /**
   * Web service operation
   *
   * @return
   */
  //@WebMethod(operationName = "list_all_devices") // [TODO - remove ???]
  @Override
  public ArrayList<String> list_all_devices()
  {
    return DatabaseInteraction.getInstance().listAllDevices();
  }


  /**
   * Web service operation
   *
   * @return
   */
  //@WebMethod(operationName = "list_all_devices") // [TODO - remove ???]
  public ArrayList<String> list_dds_devices()
  {
    return DatabaseInteraction.getInstance().listDevicesByProtocol("DDS");
  }


  /**
   * Web service operation
   *
   * @param device_id
   * @return
   */
  //@WebMethod(operationName = "get_device_address") // [TODO - remove ???]
  @Override
  public ArrayList<String> get_device_address_protocol(
    @WebParam(name = "device_id") String device_id)
  {

    return DatabaseInteraction.getInstance().getDeviceAddressProtocolById(device_id);
  }


  /**
   *
   * @param ip_address
   * @return
   */
  public String get_device_name(
    @WebParam(name = "ip_address") String ip_address)
  {
    return DatabaseInteraction.getInstance().getDeviceName(ip_address);
  }


  /**
   * Web service operation
   *
   * @return
   */
  //@WebMethod(operationName = "reset_db")
  @Override
  public boolean reset_DB()
  {
    return DatabaseInteraction.getInstance().resetDB();
  }


  /**
   * Web service operation
   *
   * @param device_id
   * @return
   */
  //@WebMethod(operationName = "deregister_device")
  @Override
  public int deregister_device(@WebParam(name = "device_id") String device_id)
  {
    return DatabaseInteraction.getInstance().removeDeviceById(device_id);
  }


  /**
   * Web service operation
   *
   * @param topic_name
   * @return
   */
  //@WebMethod(operationName = "deregister_topic")
  @Override
  public int deregister_topic(@WebParam(name = "topic_name") String topic_name)
  {
    throw new UnsupportedOperationException("Not supported anymore.");
  }


  @Override
  public String edit_device(String device_name, String short_info, String long_info, String client_id, String device_protocol, String agent_id)
  {
    boolean res = DatabaseInteraction.getInstance().updateDevice(device_name, device_protocol, short_info, long_info, client_id, agent_id);
    if (res)
    {
      return "ok";
    }
    else
    {
      return "nok";
    }
  }

  //@WebMethod(operationName = "list_all_execution_info")

  public ArrayList<String> list_all_execution_info()
  {
    throw new UnsupportedOperationException("Not supported anymore.");
  }

  //@WebMethod(operationName = "register_execution_info")

  public String register_execution_info(@WebParam(name = "workstation_name") String workstation_name,
    @WebParam(name = "recipe_IDs") String recipe_IDs,
    @WebParam(name = "AgentUniqueIDs") String AgentUniqueIDs,
    @WebParam(name = "ProductIDs") String ProductIDs,
    @WebParam(name = "MethodID") String MethodID,
    @WebParam(name = "ObjectID") String ObjectID)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  // @WebMethod

  public String edit_execute_info(String Workstation, String RecipeIDs, String AgentUniqueID, String ProductIDs, String MethodID, String ObjectID)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
