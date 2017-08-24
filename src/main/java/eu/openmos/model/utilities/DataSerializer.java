/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.model.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author valerio.gentile
 */
public class DataSerializer {
    private static final Logger logger = Logger.getLogger(DataSerializer.class.getName());
    
    public static byte[] serialize(Object objectToSerialize) throws IOException {
        String outString = null;
    
//        try 
//        {
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();         
            ObjectOutputStream out = new ObjectOutputStream(bOut);
            out.writeObject(objectToSerialize);
            out.close();
            bOut.close();
            byte[] bytes = bOut.toByteArray();
            logger.debug("in DataSerializer class, " + bytes.length);
            return bytes;
//        }
//        catch(IOException i) 
//        {
//            logger.error(i);
//        }     
    }
    
    
}
