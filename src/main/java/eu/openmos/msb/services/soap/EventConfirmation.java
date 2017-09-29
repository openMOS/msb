/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.services.soap;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 *
 * @author valerio.gentile
 */
@WebService
public interface EventConfirmation {
    
    @WebMethod(operationName = "agentCreated")
    @WebResult(name="confirmationReceived")
    public boolean agentCreated(@WebParam(name = "agentId") String agentId);
    
    @WebMethod(operationName = "agentNotCreated")
    @WebResult(name="confirmationReceived")
    public boolean agentNotCreated(@WebParam(name = "agentId") String agentId);

    @WebMethod(operationName = "agentRemoved")
    @WebResult(name="confirmationReceived")
    public boolean agentRemoved(@WebParam(name = "agentId") String agentId);
    
    @WebMethod(operationName = "agentNotRemoved")
    @WebResult(name="confirmationReceived")
    public boolean agentNotRemoved(@WebParam(name = "agentId") String agentId);

    @WebMethod(operationName = "orderInstanceCreated")
    @WebResult(name="confirmationReceived")
    public boolean orderInstanceCreated(@WebParam(name = "orderId") String orderId, @WebParam(name = "agentIds") List<String> agentIds);
    
    @WebMethod(operationName = "orderInstanceNotCreated")
    @WebResult(name="confirmationReceived")
    public boolean orderInstanceNotCreated(@WebParam(name = "orderId") String orderId);

    /**
     * When the cloud has started, notifies the MSB in order to receive workstations transports orders and so on.
     */
    @WebMethod(operationName = "cloudActive")
    public void cloudActive();    
}
