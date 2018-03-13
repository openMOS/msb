/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.config.ConfigurationLoader;
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
      String USE_CLOUD_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud");
      boolean withAGENTCloud = new Boolean(USE_CLOUD_VALUE).booleanValue();
      if (withAGENTCloud)
      {
        try
        {
          SystemConfigurator_Service systemConfiguratorService = new SystemConfigurator_Service();
          SystemConfigurator systemConfigurator = systemConfiguratorService.getSystemConfiguratorImplPort();
          String CLOUDINTERFACE_WS_VALUE = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
          BindingProvider bindingProvider = (BindingProvider) systemConfigurator;
          bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, CLOUDINTERFACE_WS_VALUE);

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
