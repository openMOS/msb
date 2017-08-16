package eu.openmos.agentcloud.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * Utility class for configuration file access.
 * 
 * @author Gianluca Stella <gianluca.stella@we-plus.eu>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class ConfigurationLoader {
    private static Map<String, Properties> configurations = new HashMap<>();
    private static final String CONFIGURATION_FILE_NAME = "default.properties";
    private static final Logger logger = Logger.getLogger(ConfigurationLoader.class.getName());

    /**
     * Loads from the classpath the given configuration file,
     * parses values in the form ${parametername}, replacing the ${parametername} with the matching parametervalue.
     * 
     * @param configurationFileName  The configuration file to be loaded.
     * @return   the java Properties object filled with the configuration file content.
     */
    private static Properties load(String configurationFileName)
    {
        Properties customConfig = readConfigurationFile(configurationFileName);
        
        Properties defaultConfig = System.getProperties();
        if (!configurationFileName.equalsIgnoreCase(CONFIGURATION_FILE_NAME))
        {
            defaultConfig = configurations.get(CONFIGURATION_FILE_NAME);
            if (defaultConfig == null)
            {
                defaultConfig = load(CONFIGURATION_FILE_NAME);
            }
        }
        
        customConfig = replaceEnvPlaceholders(customConfig, defaultConfig);
        configurations.put(configurationFileName, customConfig);    
        
        return customConfig;
    }

    /**
     * Returns the property value corresponding to the property name read from the given configuration file.
     * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).

     * If the key does not exist or if the value is null it throws an exception.
     * 
     * @param configurationFileName  The given configuration file
     * @param propertyName   The property name to look for 
     * @return   The value of the matching property
     */
    public static final String getMandatoryProperty(String configurationFileName, String propertyName)
    {        
        String propertyValue = getProperty(configurationFileName, propertyName);
        if (propertyValue == null)
        {
            String msg = "Property " + propertyName + " not found into " + configurationFileName + " configuration file. Exiting, since the property is mandatory for the system.";
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        return propertyValue;
    }
    
    /**
     * Returns the property value corresponding to the property name read from the default configuration file.
     * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).
     * 
     * If the key does not exist or if the value is null it throws an exception.
     * 
     * @param propertyName  The property name to look for 
     * @return  The value of the matching property
     */
    public static final String getMandatoryProperty(String propertyName)
    {
        return getMandatoryProperty(CONFIGURATION_FILE_NAME, propertyName);
    }

    /**
     * Returns the property value corresponding to the property name read from the given configuration file.
     * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).
     * 
     * If the key does not exist or if the value is null it returns null.
     * 
     * @param configurationFileName  The given configuration file
     * @param propertyName   The property name to look for 
     * @return   The value of the matching property, or null if the property doesn't exist.
     */
    public static final String getProperty(String configurationFileName, String propertyName)
    {
        String propertyValue = null;

        Properties config = configurations.get(configurationFileName);
        if (config == null)
            config = load(configurationFileName);

        propertyValue = (String)config.get(propertyName);
        return propertyValue;
    }
    
    /**
     * Returns the property value corresponding to the property name read from the default configuration file.
     * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).
     * 
     * If the key does not exist or if the value is null it returns null.
     * 
     * @param propertyName   The property name to look for 
     * @return   The value of the matching property, or null if the property doesn't exist.
     */
    public static final String getProperty(String propertyName)
    {
        return getProperty(CONFIGURATION_FILE_NAME, propertyName);
    }

    /**
     * @see load
     * 
     * Loads from the classpath the given configuration file,
     * parses values in the form ${parametername}, replacing the ${parametername} with the matching parametervalue.
     * 
     * @param configurationFileName  The configuration file to be loaded.
     * @return   the java Properties object filled with the configuration file content.
     */
    public static Properties loadConfigurationFile(String configurationFileName)
    {
        return load(configurationFileName);
    }
    
    /**
     * Private method for accessing the physical configuration file and bringing it into memory.
     * It doesn't do any ${parametername} replacement.
     * 
     * @param configurationFileName  The configuration file to be loaded.
     * @return   the java Properties object filled with the configuration file content.
     */
    private static Properties readConfigurationFile(String configurationFileName)
    {
        Properties customConfig = null;
        InputStream localInputStream = null;
        
        customConfig = new Properties();
        
        try {            
            localInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configurationFileName);
            if (localInputStream == null)
            {
                String msg = "Could not load property file " + configurationFileName;
                logger.warn(msg);
                throw new RuntimeException(msg);
            }
            
            customConfig.load(localInputStream);
            logger.info("Property file " + configurationFileName + " successfully loaded.");
            
            return customConfig;
        } catch (IOException ex) {
            String msg = "Could not load property file " + configurationFileName;
            logger.error(msg);
            throw new RuntimeException(msg);
        } finally {
            if (localInputStream != null)
            {
                try
                {   
                    localInputStream.close();    
                }
                catch (IOException e)
                {
                    String msg = "Could not load property file " + configurationFileName;
                    logger.error(msg);
                    throw new RuntimeException(msg);
                }
            }
        }     
    }

    /**
     * Given a java Properties object, if any of the values matches the pattern ${parametername},
     * it replaces the ${parametername} with the "parametervalue" taken from the envConfiguration dictionary.
     * The dictionary can be the java system properties (for the default configuration file) 
     * or the default configuration file (for the other configuration files).
     * 
     * @param props  The java Properties object to do the replacements
     * @param envConfiguration   The java system properties (for the default configuration file) 
     * or the default configuration file (for the other configuration files)
     * @return  props after the replacements
     */
  private static Properties replaceEnvPlaceholders(Properties props, Properties envConfiguration)
  {
	  Object[] propArr = props.keySet().toArray();
	 
	  for (int i = 0; i < propArr.length; i++) 
	  {                  
		  String propName = (String)propArr[i];
                  String propValue = (String)props.get(propName);
                  if (propValue != null && propValue.indexOf("${") != -1)
                  {                      
                        props.setProperty(
                                propName, 
                                replaceEnvPlaceholder(propValue, envConfiguration)
                        );
                  }
	  }
	  
	  return props;
  }
  
 /**
   * Given a single string, if it matches the pattern ${parametername},
   * it replaces the ${parametername} with the "parametervalue" taken from the envConfiguration dictionary.
   * The dictionary can be the java system properties (for the default configuration file) 
   * or the default configuration file (for the other configuration files).
   * 
   * 
   * @param propertyValue   The single current property value
   * @param envConfiguration   The java system properties (for the default configuration file) 
     * or the default configuration file (for the other configuration files)
   * @return   propertyValue after the replacement
   */
  private static String replaceEnvPlaceholder(String propertyValue, Properties envConfiguration)
  {
      if (propertyValue.indexOf("${") == -1)
          return propertyValue;
      
	try 
	{
		if ((propertyValue != null) && (!"".equals(propertyValue)))
		{
			// Properties prop = System.getProperties();
			Properties prop = envConfiguration;
			Object[] envArr = prop.keySet().toArray();
		    for (int i = 0; i < envArr.length; i++) 
		    {
		      String envName = (String)envArr[i];
		      if ((envName != null) && (!"".equals(envName)))
		      {
		    	  // logger.debug("applyEnvVars: begin");
		    	  // logger.debug("environment: name=" + envName + "; value=" + (String)prop.get(envName) + ";");
		    	  // logger.debug("config file: value=" + propertyValue + ";");
		    	  // logger.debug("input value: value=" + propertyValue + ";");
		    	  propertyValue = propertyValue.replaceAll("\\$\\{" + envName + "\\}", (String)prop.get(envName));
		    	  // logger.debug("output value: value=" + propertyValue + ";");
		    	  // logger.debug("applyEnvVars: end");
		      }
		    }
		}
	}
	catch (SecurityException e)
	{
            String msg = "Unable to access environmental variables.";
		logger.error(msg);
		throw new RuntimeException(msg);
	}
	catch (Exception e) 
	{
            String msg = "Generic exception trying to retrieve environmental variables.";
		logger.error(msg);
		throw new RuntimeException(msg);
	}
	
	return propertyValue;
  }      

  /**
   * Returns the property value corresponding to the property name read from the given configuration file.
   * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).
   * 
   * If the key does not exist or if the value is null it returns the suggested defaultValue.
   * 
   * @param configurationFileName  The given configuration file
   * @param propertyName   The property name to look for 
   * @param defaultValue   The default value to return in case the property is not found
   * @return   The value of the matching property, or the defaultValue if the property doesn't exist.
   */
    public static String getDefaultedProperty(String configurationFileName, String propertyName, String defaultValue) {
        String propertyValue = getProperty(configurationFileName, propertyName);
        if (propertyValue == null)
            return defaultValue;
        else
            return propertyValue;
    }
    
    /**
    * Returns the property value corresponding to the property name read from the default configuration file.
    * If the value is in the form ${parametername}, the ${parametername} is replaced with the matching parametervalue taken from the system properties (for the default configuration file) or from the default configuration file (for the other configuration files).
    * 
    * If the key does not exist or if the value is null it returns the suggested defaultValue.
    * 
    * @param propertyName   The property name to look for 
    * @param defaultValue   The default value to return in case the property is not found
    * @return   The value of the matching property, or the defaultValue if the property doesn't exist.
     */
    public static String getDefaultedProperty(String propertyName, String defaultValue) {
        return getDefaultedProperty(CONFIGURATION_FILE_NAME, propertyName, defaultValue);
    }

}
