/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

import eu.openmos.agentcloud.config.ConfigurationLoader;

/**
 *
 * @author Introsys
 */
public class MSBConstants
{
  public static final String ADAPTER_STATE_IDLE = "IDLE";
  public static final String ADAPTER_STATE_RUNNING = "Running";
  public static final String ADAPTER_STATE_READY = "Ready";
  public static final String ADAPTER_STATE_SUSPEND = "Suspend";
  public static final String ADAPTER_STATE_ERROR = "Error";
  public static final String ADAPTER_STATE_EMERGENCY_STOP = "EMERGENCY_STOP";

  public static final String DEVICE_ADAPTER_TYPE_TRANSPORT = "TransportSystem";
  public static final String DEVICE_ADAPTER_TYPE_WORKSTATION = "WorkStation";
  public static final String DEVICE_ADAPTER_TYPE_UNKNOWNTYPE = "UnknownType";

  public static final String STAGE_RAMP_UP = "Ramp_Up";
  public static final String STAGE_PRE_PRODUCTION = "Pre_Production";
  public static final String STAGE_PRODUCTION = "Production";
  public static final String STAGE_RAMP_UP_TO_PREPROD = "Rampup_to_pre-production";
  public static final String STAGE_RAMP_UP_TO_PRODUCTION = "Rampup_to_production";
  public static final String STAGE_PREPROD_TO_PRODUCTION = "Pre-production_to_production";
  public static final String STAGE_PREPROD_TO_RAMP_UP = "Pre-production_to_rampup";
  public static final String STAGE_PRODUCTION_TO_RAMP_UP = "Production_to_rampup";
  public static final String STAGE_PRODUCTION_TO_PREPROD = "Production_to_preproduction";

  public static final String STATE_PRODUCT_QUEUE = "Queued";
  public static final String STATE_PRODUCT_PRODUCING = "Running";
  public static final String STATE_PRODUCT_READY = "Ready";
  
  public static final String PROJECT_PATH = System.getProperty("user.dir");
  public static final String CLOUD_ENDPOINT = ConfigurationLoader.getMandatoryProperty("openmos.agent.cloud.cloudinterface.ws.endpoint");
  public static final String XML_PATH = PROJECT_PATH + ConfigurationLoader.getMandatoryProperty("openmos.msb.xml.path");
  public static final String CERTS_PATH = PROJECT_PATH + ConfigurationLoader.getMandatoryProperty("openmos.msb.opcua.key.path");
  public static final String DB_SQL_PATH = PROJECT_PATH + ConfigurationLoader.getMandatoryProperty("openmos.msb.database.creation.file.path.string");
  public static final String DB_MEMORY_CONNECTION = "jdbc:sqlite:" + PROJECT_PATH + ConfigurationLoader.getMandatoryProperty("openmos.msb.database.connection.string");
  public static final String DATABASE_DRIVER_CLASS = ConfigurationLoader.getMandatoryProperty("openmos.msb.database.driver.class");
  public static final String LDS_ENDPOINT = ConfigurationLoader.getMandatoryProperty("openmos.msb.discovery.service");

  public static final Boolean USING_CLOUD = Boolean.parseBoolean(ConfigurationLoader.getMandatoryProperty("openmos.msb.use.cloud"));
  public static final Boolean MSB_OPTIMIZER = Boolean.parseBoolean(ConfigurationLoader.getMandatoryProperty("openmos.msb.use.optimizer"));
  public static final Boolean MSB_MODE_PASSIVE = Boolean.parseBoolean(ConfigurationLoader.getMandatoryProperty("openmos.msb.passive.mode"));

  public static final String MSB_IP = ConfigurationLoader.getMandatoryProperty("openmos.msb.ipaddress");
  
  public static final String QUEUE_TYPE_EXECUTE = "EXECUTE";
  public static final String QUEUE_TYPE_REMOVE = "REMOVE";
  
  
}
