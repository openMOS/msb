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
public interface IOPCNotifyGUI
{

  void on_polling_cycle();

  void on_new_endpoint_discovered(String parent_app_uri, String app_uri);

  void on_endpoint_dissapeared(String name);

  void notify_error();
}
