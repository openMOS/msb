/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.milo.server.methods;

import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.eclipse.milo.opcua.sdk.server.annotations.UaInputArgument;
import org.eclipse.milo.opcua.sdk.server.annotations.UaMethod;
import org.eclipse.milo.opcua.sdk.server.annotations.UaOutputArgument;
import org.eclipse.milo.opcua.sdk.server.util.AnnotationBasedInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author fabio.miranda
 */
public class GeneralMethod {
      private final Logger logger = LoggerFactory.getLogger(getClass());

    @UaMethod
    public void invoke(
        AnnotationBasedInvocationHandler.InvocationContext context,

        @UaInputArgument(
            name = "Function",
            description = "Function to call")
            String func,
 
        @UaInputArgument(
            name = "Arguments",
            description = "Arguments to send")
            String args,
        
        @UaOutputArgument(
            name = "feedback",
            description = "Feedback of the operation")
            AnnotationBasedInvocationHandler.Out<String> feedback) throws ParserConfigurationException, SAXException, IOException, JAXBException {

        System.out.println("GeneralMethod(" + func + ")");
        logger.debug("Invoking GeneralMethod() method of Object '{}'", context.getObjectNode().getBrowseName().getName());
               
        OPCDeviceItf dev = new OPCDeviceItf();
        String ret = null;
          try {
              ret = dev.AllCases(func, args);
          } catch (TransformerException ex) {
              java.util.logging.Logger.getLogger(GeneralMethod.class.getName()).log(Level.SEVERE, null, ex);
          }
        feedback.set(ret);
    }
}
