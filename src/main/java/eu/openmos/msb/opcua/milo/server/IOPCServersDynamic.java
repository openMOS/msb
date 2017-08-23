/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.server;

/**
 *
 * @author Admin
 */
public interface IOPCServersDynamic
{

  void on_new_server(String name, String app_uri);

  void on_new_endpoint(String parent_app_uri, String app_uri);

  void on_server_dissapeared();

  void on_endpoint_dissapeared();

  void reset_tables();
}
