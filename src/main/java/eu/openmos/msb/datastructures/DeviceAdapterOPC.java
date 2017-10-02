/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.msb.opcua.milo.client.MSBClientSubscription;

/**
 *
 * @author andre
 */
public class DeviceAdapterOPC extends DeviceAdapter
{

  private MSBClientSubscription opcClient;

  /**
   * 
   */
  public DeviceAdapterOPC()
  {
    opcClient = new MSBClientSubscription();
  }

  @Override
  public MSBClientSubscription getClient()
  {
    return opcClient;
  }

}
