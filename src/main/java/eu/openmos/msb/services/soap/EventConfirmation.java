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

    @WebMethod(operationName = "orderCreated")
    @WebResult(name="confirmationReceived")
    public boolean orderCreated(@WebParam(name = "orderId") String orderId, @WebParam(name = "agentIds") List<String> agentIds);
    
    @WebMethod(operationName = "orderNotCreated")
    @WebResult(name="confirmationReceived")
    public boolean orderNotCreated(@WebParam(name = "orderId") String orderId);

}
