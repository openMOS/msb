/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.agentcloud.cloudinterface;

/**
 * Object to be returned as an operation status for new order submission.
 * 
 * @author Valerio Gentile <valerio.gentile@we-plus.eu>
 */
public class OrderStatus {

    /**
     * Code for an OK operation.
     */
    public static final String OK = "success.openmos.agents.cloudinterface.systemconfigurator";

    /**
     * Code for a KO operation.
     */
    public static final String KO = "error.openmos.agents.cloudinterface.systemconfigurator";

    /**
     * Code for message describing a successful order submission.
     */
    public static final String ACCEPTNEWORDER_SUCCESS = "agents.CI.systemconfigurator.acceptneworder.success";    

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
    public OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Default constructor.
     */
    public OrderStatus() {
    }

    private String code;
    private String description;

}
