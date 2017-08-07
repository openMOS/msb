/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.msb.opcua.milo.client.MSB_MiloClientSubscription;


/**
 *
 * @author andre
 */
public class DeviceAdapterOPC extends DeviceAdapter
{

  
  
  
  private MSB_MiloClientSubscription opcClient;
  
  public DeviceAdapterOPC()
  {
      opcClient = new MSB_MiloClientSubscription();
  }
  
  @Override
  public MSB_MiloClientSubscription getClient()
  {
    return opcClient;
  }
  
}
