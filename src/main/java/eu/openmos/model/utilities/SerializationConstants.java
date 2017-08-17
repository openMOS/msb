/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.utilities;

/**
 * Set of technical constants for recipes serialization/deserialization.
 * 
 * @author Pedro Monteiro <pedro.monteiro@uninova.pt>
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class SerializationConstants {

    public static final String DATE_REPRESENTATION = "yyyyMMddHHmmssSSS";

    public static final String EMPTY_LIST = " ";
    
    public static final String TOKEN_AGENT_PARAMETER = "\u00a0";

    //Serialization Tokens
    public static final String TOKEN_RECIPE = "\u00a1";
    public static final String TOKEN_RECIPE_LIST_ITEM = "\u00bb";
    public static final String TOKEN_RECIPE_ID_LIST_ITEM = "\u00c7";

    public static final String TOKEN_ORDER = "\u00a3";
    public static final String TOKEN_PRODUCT_DESCRIPTION = "\u00ba";
    public static final String TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM = "\u00a4";

    public static final String TOKEN_KPI = "\u00a5";
    public static final String TOKEN_KPI_LIST_ITEM = "\u00a6";
    public static final String TOKEN_KPI_ID_LIST_ITEM = "\u00cb";
    public static final String TOKEN_KPI_SETTING = "\u00ac";
    public static final String TOKEN_KPI_SETTING_LIST_ITEM = "\u00be";

    public static final String TOKEN_PHYSICAL_LOCATION = "\u00ad";
    public static final String TOKEN_LOGICAL_LOCATION = "\u00ae";

    public static final String TOKEN_SKILL = "\u00b8";
    public static final String TOKEN_SKILL_LIST_ITEM = "\u00a7";

    public static final String TOKEN_SKILL_REQUIREMENT = "\u00b9";
    public static final String TOKEN_SKILL_REQUIREMENT_LIST_ITEM = "\u00bc";
    public static final String TOKEN_SKILL_REQUIREMENT_ID_LIST_ITEM = "\u00cd";

    public static final String TOKEN_PARAMETER = "\u00bd";

    public static final String TOKEN_PARAMETER_SETTING = "\u00c0";
    public static final String TOKEN_PARAMETER_SETTING_LIST_ITEM = "\u00c1";
    public static final String TOKEN_PARAMETER_LIST_ITEM = "\u00c2";
    public static final String TOKEN_PARAMETER_ID_LIST_ITEM = "\u00cc";

    public static final String TOKEN_PRODUCTION_OPTIMIZATION_REQUEST = "\u00c3";
    public static final String TOKEN_PRODUCTION_OPTIMIZATION_REQUEST_LIST_ITEM = "\u00c4";

    public static final String TOKEN_OPTIMIZATION_PARAMETER = "\u00c5";
    public static final String TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION = "\u00ab";
    public static final String TOKEN_EXTRACTED_DATA = "\u00aa";

    public static final String TOKEN_DEPLOY_RECIPES_REQUEST = "\u00c6";
    public static final String TOKEN_DEPLOY_RECIPES_REQUEST_LIST_ITEM = "\u00a2";

    public static final String TOKEN_EXECUTION_TABLE_ROW_LIST_ITEM = "\u00a8";
    public static final String TOKEN_EXECUTION_TABLE_ROW = "\u00a9";
    public static final String TOKEN_EXECUTION_TABLE = "\u00b0";
    
    public static final String TOKEN_SENSOR_VALUE_LIST_ITEM = "\u00b3";
    public static final String TOKEN_RAW_EQUIPMENT_DATA = "\u00b4";
    public static final String TOKEN_RAW_PRODUCT_DATA = "\u00b5";

    public static final String TOKEN_COMPONENT = "\u00b2";
    public static final String TOKEN_COMPONENT_LIST_ITEM = "\u00b6";

    public static final String TOKEN_EQUIPMENT = "\u00b1";
    public static final String TOKEN_EQUIPMENT_LIST_ITEM = "\u00b7";

    public static final String TOKEN_UNEXPECTED_PRODUCT_DATA = "\u00c8";
    public static final String TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA = "\u00c9";
    public static final String TOKEN_RECIPE_EXECUTION_DATA = "\u00ca";
    
    public static final String TOKEN_POSSIBLE_RECIPE_CHOICE_LIST_ITEM = "\u00d0";

    public static final String INVALID_FORMAT_FIELD_COUNT_ERROR = "The string has incorrect format, number of fields expected: ";
/*
    //Serialization Tokens
    public static final String TOKEN_AGENT_PARAMETER = "\u00a0";
    public static final String TOKEN_RECIPE = "\u00a1";
    public static final String TOKEN_DEPLOY_RECIPES_REQUEST_LIST_ITEM = "\u00a2";
    public static final String TOKEN_ORDER = "\u00a3";
    public static final String TOKEN_PRODUCT_DESCRIPTION_LIST_ITEM = "\u00a4";
    public static final String TOKEN_KPI = "\u00a5";
    public static final String TOKEN_KPI_LIST_ITEM = "\u00a6";
    public static final String TOKEN_SKILL_LIST_ITEM = "\u00a7";
    public static final String TOKEN_EXECUTION_TABLE_ROW_LIST_ITEM = "\u00a8";
    public static final String TOKEN_EXECUTION_TABLE_ROW = "\u00a9";
    public static final String TOKEN_EXTRACTED_DATA = "\u00aa";
    public static final String TOKEN_CYBER_PHYSICAL_AGENT_DESCRIPTION = "\u00ab";
    public static final String TOKEN_KPI_SETTING = "\u00ac";
    public static final String TOKEN_PHYSICAL_LOCATION = "\u00ad";
    public static final String TOKEN_LOGICAL_LOCATION = "\u00ae";
    public static final String TOKEN_EXECUTION_TABLE = "\u00b0";
    public static final String TOKEN_EQUIPMENT = "\u00b1";
    public static final String TOKEN_COMPONENT = "\u00b2";
    public static final String TOKEN_SENSOR_VALUE_LIST_ITEM = "\u00b3";
    public static final String TOKEN_RAW_EQUIPMENT_DATA = "\u00b4";
    public static final String TOKEN_RAW_PRODUCT_DATA = "\u00b5";
    public static final String TOKEN_COMPONENT_LIST_ITEM = "\u00b6";
    public static final String TOKEN_EQUIPMENT_LIST_ITEM = "\u00b7";    
    public static final String TOKEN_SKILL = "\u00b8";
    public static final String TOKEN_SKILL_REQUIREMENT = "\u00b9";
    public static final String TOKEN_PRODUCT_DESCRIPTION = "\u00ba";
    public static final String TOKEN_RECIPE_LIST_ITEM = "\u00bb";
    public static final String TOKEN_SKILL_REQUIREMENT_LIST_ITEM = "\u00bc";
    public static final String TOKEN_PARAMETER = "\u00bd";
    public static final String TOKEN_KPI_SETTING_LIST_ITEM = "\u00be";
    public static final String TOKEN_PARAMETER_SETTING = "\u00c0";
    public static final String TOKEN_PARAMETER_SETTING_LIST_ITEM = "\u00c1";
    public static final String TOKEN_PARAMETER_LIST_ITEM = "\u00c2";
    public static final String TOKEN_PRODUCTION_OPTIMIZATION_REQUEST = "\u00c3";
    public static final String TOKEN_PRODUCTION_OPTIMIZATION_REQUEST_LIST_ITEM = "\u00c4";
    public static final String TOKEN_OPTIMIZATION_PARAMETER = "\u00c5";
    public static final String TOKEN_DEPLOY_RECIPES_REQUEST = "\u00c6";
    public static final String TOKEN_RECIPE_ID_LIST_ITEM = "\u00c7";
    public static final String TOKEN_UNEXPECTED_PRODUCT_DATA = "\u00c8";
    public static final String TOKEN_PRODUCT_LEAVING_WORKSTATION_OR_TRANSPORT_DATA = "\u00c9";
    public static final String TOKEN_RECIPE_EXECUTION_DATA = "\u00ca";
    public static final String TOKEN_KPI_ID_LIST_ITEM = "\u00cb";
    public static final String TOKEN_PARAMETER_ID_LIST_ITEM = "\u00cc";
    public static final String TOKEN_SKILL_REQUIREMENT_ID_LIST_ITEM = "\u00cd";
    public static final String TOKEN_POSSIBLE_RECIPE_CHOICE_LIST_ITEM = "\u00d0";
*/    
}
