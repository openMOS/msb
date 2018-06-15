/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator;
import eu.openmos.agentcloud.ws.systemconfigurator.wsimport.SystemConfigurator_Service;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Introsys
 */
public class MSBVar
{
  private static String systemStage = "";

  public static String getSystemStage()
  {
    return systemStage;
  }

  public static void setSystemStage(String systemStage)
  {
    try
    {
      if (MSBConstants.USING_CLOUD)
      {
        try
        {
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, MSBConstants.CLOUD_ENDPOINT);

          systemConfigurator.changeSystemStage(systemStage);
        } catch (Exception ex)
        {
          System.out.println("Error trying to connect to cloud!: " + ex.getMessage());
        }
      }
    } catch (Exception ex)
    {

    }

    MSBVar.systemStage = systemStage;
  }
  
  
}
