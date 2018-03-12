/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.datastructures;

/**
 *
 * @author Introsys
 */
public class MSBConstants {

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

    public static Boolean MSB_OPTIMIZER = true;

}
