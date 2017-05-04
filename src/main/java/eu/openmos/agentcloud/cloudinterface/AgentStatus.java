/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterface;

/**
 * Object to be returned as an operation status for agent creation and removal operations.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class AgentStatus {

    /**
     * Code for an OK operation.
     */
    public static final String OK = "success.openmos.agents.cloudinterface.systemconfigurator";

    /**
     *
     */
    // public static final String SUCCESS = "success.openmos.agents.cloudinterface.systemconfigurator";

    /**
     *
     */
    // public static final String ERROR = "error.openmos.agents.cloudinterface.systemconfigurator";

    /**
     * Code for a KO operation.
     */
    public static final String KO = "error.openmos.agents.cloudinterface.systemconfigurator";
    /**
     * Code for message describing a successful agent creation operation.
     */
    public static final String CREATENEWAGENT_SUCCESS = "agents.CI.systemconfigurator.createnewagent.success";
    /**
     * Code for message describing a successful agent removal operation.
     */
    public static final String REMOVEAGENT_SUCCESS = "agents.CI.systemconfigurator.removeagent.success";
    /**
     * Code for message describing a missing parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETER_NULL = "agents.CI.systemconfigurator.createnewagent.newagentinfo.parameter.null";
    /**
     * Code for message describing a missing id parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_ID_NULL = "agents.CI.systemconfigurator.createnewagent.newagentinfo.id.null";
    /**
     * Code for message describing a missing agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_NULL = "agents.CI.systemconfigurator.createnewagent.newagentinfo.type.null";
    /**
     * Code for message describing an unknown agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_UNKNOWN = "agents.CI.systemconfigurator.createnewagent.newagentinfo.type.unknown";
    /**
     * Code for message describing missing agent parameters during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETERS_NULL = "agents.CI.systemconfigurator.createnewagent.newagentinfo.parameters.null";
    /**
     * 
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_TYPE_NULL = "agents.CI.systemconfigurator.removeagent.existingagentinfo.type.null";
    /**
     * Code for message describing a missing agent name parameter during agent removal operation.
     */
    public static final String REMOVEAGENT_EXISTINGAGENTINFO_PARAMETER_NULL = "agents.CI.systemconfigurator.removeagent.existingagentinfo.parameter.null";
    /**
     *
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_ID_NULL = "agents.CI.systemconfigurator.removeagent.existingagentinfo.id.null";
    /**
     *
     */
    // public static final String REMOVEAGENT_RESOURCE_NOT_FOUND = "agents.CI.systemconfigurator.removeagent.resource.not.found";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Constructor.
     * 
     * @param code   The message code
     * @param description    The message description
     */
    public AgentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Default constructor.
     */
    public AgentStatus() {
    }

    private String code;
    private String description;

}
