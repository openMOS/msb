/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.prosys.server;

import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.nodes.UaMethod;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.server.CallableListener;
import com.prosysopc.ua.server.MethodManager;
import com.prosysopc.ua.server.ServiceContext;
import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author fabio.miranda
 */
public class GeneralMethodManagerListener implements CallableListener {
//MSB_gui msb;
    private static Logger logger = LoggerFactory.getLogger(GeneralMethodManagerListener.class);
	final private UaNode general_method;
        /**
	 * @param general_method
	 *            the method node to handle.
	 */
	public GeneralMethodManagerListener(UaNode general_method) {
		super();
		this.general_method = general_method;
	}
    @Override
    public boolean onCall(ServiceContext serviceContext, NodeId objectId, UaNode object, NodeId methodId, UaMethod method, Variant[] inputArguments, StatusCode[] inputArgumentResults, DiagnosticInfo[] inputArgumentDiagnosticInfos, Variant[] outputs) throws StatusException {
                String feedBack="NOT OK";
        // Handle method calls
		// Note that the outputs array is already allocated
		if (methodId.equals(general_method.getNodeId())) {
			logger.info("general_method: {}", Arrays.toString(inputArguments));
			MethodManager.checkInputArguments(new Class[] { String.class , String.class }, inputArguments,
					inputArgumentResults, inputArgumentDiagnosticInfos, false);
			// The argument #0 is the function to execute
			String FunctionToExecute;
                        String InputArguments;
			try {
				FunctionToExecute = (String) inputArguments[0].getValue();
                                logger.info("a receber input1 \n");
                                logger.info(FunctionToExecute);
                                InputArguments = (String) inputArguments[1].getValue();
                                logger.info("a receber input2 \n");
                                logger.info(InputArguments);
                         //msb.setLogText(receivedData);
                         OPCDeviceItf dev = new OPCDeviceItf();
                         feedBack=dev.AllCases(FunctionToExecute, InputArguments);
                         
                        
			} catch (ClassCastException e) {
				throw inputError(0, e.getMessage(), inputArgumentResults, inputArgumentDiagnosticInfos);
			} catch (ParserConfigurationException ex) {
                        java.util.logging.Logger.getLogger(GeneralMethodManagerListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        java.util.logging.Logger.getLogger(GeneralMethodManagerListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(GeneralMethodManagerListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JAXBException ex) {
                        java.util.logging.Logger.getLogger(GeneralMethodManagerListener.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TransformerException ex) {
                        java.util.logging.Logger.getLogger(GeneralMethodManagerListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        
                        // The argument #1 is the argument input
			String input;
			try {
				input = (String) inputArguments[1].getValue();
                                logger.info("a receber input2 \n");
                                logger.info(input);
			} catch (ClassCastException e) {
				throw inputError(1, e.getMessage(), inputArgumentResults, inputArgumentDiagnosticInfos);
			}
			
                        //do what need to be done and respond
                        
                        
                        
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
