/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.prosys.server;

/**
 *
 * @author fabio.miranda
 */

import java.util.Arrays;

import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.nodes.UaMethod;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.server.CallableListener;
import com.prosysopc.ua.server.MethodManager;
import com.prosysopc.ua.server.ServiceContext;
//import fabio_opcua_badjoraz.MSB_gui;
import static org.opcfoundation.ua.core.Identifiers.FileType;


public class deviceInfoXMLmethodManagerListener implements CallableListener{

    //MSB_gui msb;
    private static Logger logger = LoggerFactory.getLogger(deviceInfoXMLmethodManagerListener.class);
	final private UaNode device_XML_Method;
        /**
	 * @param device_XML_Method
	 *            the method node to handle.
	 */
	public deviceInfoXMLmethodManagerListener(UaNode device_XML_Method) {
		super();
		this.device_XML_Method = device_XML_Method;
	}
        
        
    @Override
    public boolean onCall(ServiceContext serviceContext, NodeId objectId, UaNode object, NodeId methodId, UaMethod method, Variant[] inputArguments, StatusCode[] inputArgumentResults, DiagnosticInfo[] inputArgumentDiagnosticInfos, Variant[] outputs) throws StatusException {
        
        
        // Handle method calls
		// Note that the outputs array is already allocated
		if (methodId.equals(device_XML_Method.getNodeId())) {
			logger.info("device_XML_Method: {}", Arrays.toString(inputArguments));
			MethodManager.checkInputArguments(new Class[] { String.class }, inputArguments,
					inputArgumentResults, inputArgumentDiagnosticInfos, false);
			// The argument #0 is the operation to perform
			String receivedData;
			try {
				receivedData = (String) inputArguments[0].getValue();
                                logger.info("received data: \n");
                                logger.info(receivedData);
                         //msb.setLogText(receivedData);
                        
			} catch (ClassCastException e) {
				throw inputError(0, e.getMessage(), inputArgumentResults, inputArgumentDiagnosticInfos);
			}
			

			// The result is the operation applied to input
			/*operation = operation.toLowerCase();
			double result;
			if (operation.equals("sin"))
				result = Math.sin(Math.toRadians(input));
			else if (operation.equals("cos"))
				result = Math.cos(Math.toRadians(input));
			else if (operation.equals("tan"))
				result = Math.tan(Math.toRadians(input));
			else if (operation.equals("pow"))
				result = input * input;
			else
				throw inputError(0, "Unknown function '" + operation + "': valid functions are sin, cos, tan, pow",
						inputArgumentResults, inputArgumentDiagnosticInfos);*/
                        
                        String feedBack="Success!";
			outputs[0] = new Variant(feedBack);
			return true; // Handled here
		} else
			return false;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
	 * Handle an error in method inputs.
	 *
	 * @param index
	 *            index of the failing input
	 * @param message
	 *            error message
	 * @param inputArgumentResults
	 *            the results array to fill in
	 * @param inputArgumentDiagnosticInfos
	 *            the diagnostics array to fill in
	 * @return StatusException that can be thrown to break further method
	 *         handling
	 */
    private StatusException inputError(final int index, final String message, StatusCode[] inputArgumentResults,
			DiagnosticInfo[] inputArgumentDiagnosticInfos) {
		logger.info("inputError: #{} message={}", index, message);
		inputArgumentResults[index] = new StatusCode(StatusCodes.Bad_InvalidArgument);
		final DiagnosticInfo di = new DiagnosticInfo();
		di.setAdditionalInfo(message);
		inputArgumentDiagnosticInfos[index] = di;
		return new StatusException(StatusCodes.Bad_InvalidArgument);
	}
}