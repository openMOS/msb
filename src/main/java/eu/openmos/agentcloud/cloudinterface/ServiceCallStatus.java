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
public class ServiceCallStatus {

    /**
     * Code for an OK operation.
     */
    public static final String OK = "success.openmos.agentcloud.cloudinterface.systemconfigurator";

    /**
     * Code for a KO operation.
     */
    public static final String KO = "error.openmos.agentcloud.cloudinterface.systemconfigurator";

    /**
     * Code for message describing a successful agent creation operation call.
     */
    public static final String CREATENEWAGENT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.createnewagent.success";
//    /**
//     * Code for message describing a successful agent creation operation.
//     */
//    public static final String CREATENEWAGENT_SUCCESS = "agentcloud.CI.systemconfigurator.createnewagent.call.success";
    /**
     * Code for message describing a successful agent removal operation call.
     */
    public static final String REMOVEAGENT_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.removeagent.call.success";
//    /**
//     * Code for message describing a successful agent removal operation.
//     */
//    public static final String REMOVEAGENT_SUCCESS = "agentcloud.CI.systemconfigurator.removeagent.success";
    /**
     * Code for message describing a missing parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.parameter.null";
    /**
     * Code for message describing a missing id parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_ID_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.id.null";
    /**
     * Code for message describing a missing agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.type.null";
    /**
     * Code for message describing an unknown agent type parameter during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_TYPE_UNKNOWN = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.type.unknown";
    /**
     * Code for message describing missing agent parameters during agent creation operation.
     */
    public static final String CREATENEWAGENT_NEWAGENTINFO_PARAMETERS_NULL = "agentcloud.CI.systemconfigurator.createnewagent.newagentinfo.parameters.null";
    /**
     * 
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_TYPE_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.type.null";
    /**
     * Code for message describing a missing agent name parameter during agent removal operation.
     */
    public static final String REMOVEAGENT_EXISTINGAGENTINFO_PARAMETER_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.parameter.null";
    /**
     *
     */
    // public static final String REMOVEAGENT_EXISTINGAGENTINFO_ID_NULL = "agentcloud.CI.systemconfigurator.removeagent.existingagentinfo.id.null";
    /**
     *
     */
    // public static final String REMOVEAGENT_RESOURCE_NOT_FOUND = "agentcloud.CI.systemconfigurator.removeagent.resource.not.found";

    /**
     * Code for message describing a successful order submission.
     */
    public static final String ACCEPTNEWORDER_CALL_SUCCESS = "agentcloud.CI.systemconfigurator.acceptneworder.call.success";    
    /**
     * Code for message describing a failure of the order submission operation.
     */
    public static final String ACCEPTNEWORDER_CALL_FAILURE = "agentcloud.CI.systemconfigurator.acceptneworder.call.failure";    

    public static final String DEPLOYMENTAGENT_NULL = "agentcloud.CI.systemconfigurator.deploymentagent.null";
    public static final String CLOUDINTERFACEAGENT_NULL = "agentcloud.CI.systemconfigurator.cloudinterfaceagent.null";
    
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * Constructor.
     * 
     * @param className   Java class name
     * @param operationName   Operation, or java method name
     * @param code   Message code
     * @param description    Message description
     */
    public ServiceCallStatus(String className, String operationName, String code, String description) {
        this.code = code;
        this.description = description;
        this.className = className;
        this.operationName = operationName;
    }

    
    /**
     * Default constructor, for reflection purpose.
     */
    public ServiceCallStatus() {
    }

    private String code;
    private String description;
    private String className;
    private String operationName;
          

}
