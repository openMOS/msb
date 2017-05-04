/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dummyclasses;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import com.introsys.mavenprojectxmltest;

/**
 *
 * @author fabio.miranda
 */
public class XMLutils {
        public static void main(String[] args){
        
           /* try {
                com.introsys.mavenprojectxmltest.parserFunc.FiletoXMLtoObject("RegFile.xml");
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(XMLutils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SAXException ex) {
                Logger.getLogger(XMLutils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(XMLutils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
                Logger.getLogger(XMLutils.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            
            
            
        try {
            FiletoXMLtoObject("RegFile.xml");
        } catch (ParserConfigurationException | SAXException | IOException | JAXBException ex) {
            Logger.getLogger(XMLutils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public static void FileToStringToObject(String regFilexml) {
   /*    File fXmlFile = new File(regFilexml);
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = null;
           dBuilder = dbFactory.newDocumentBuilder();
           org.w3c.dom.Document doc = dBuilder.parse(regFilexml);            StringWriter sw = new StringWriter();
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
           transformer.setOutputProperty(OutputKeys.METHOD, "xml");
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
           transformer.transform(new DOMSource((Node) doc), new StreamResult(sw));
           return sw.toString();*/
    }
    
    
    public static String XMLtoString(String path)
   {
       try
       {
                     
           File fXmlFile = new File(path);
           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
           Document doc = dBuilder.parse(fXmlFile);
           

           StringWriter sw = new StringWriter();
           TransformerFactory tf = TransformerFactory.newInstance();
           Transformer transformer = tf.newTransformer();
           transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
           transformer.setOutputProperty(OutputKeys.METHOD, "xml");
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
           transformer.transform(new DOMSource((Node) doc), new StreamResult(sw));
           return sw.toString();
       } catch (Exception ex)
       {
           throw new RuntimeException("Error converting to String", ex);
       }
   }    
    
    
    
    
    public static String StringtoXML(String stringFile)
   {
       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       DocumentBuilder db = null;
       
       try
       {
           db = dbf.newDocumentBuilder();
           InputSource is = new InputSource();
           is.setCharacterStream(new StringReader(stringFile));
           try
           {
               org.w3c.dom.Document doc = db.parse(is);
               String message = doc.getDocumentElement().getTextContent();
               System.out.println(message);
               
               JAXBContext jc = JAXBContext.newInstance(RegFile.class);
               Unmarshaller unmar = jc.createUnmarshaller();
               
               RegFile aux = (RegFile) unmar.unmarshal(doc);
               int i = 0;
           } catch (Exception e)
           {
               // handle SAXException
           }
       } catch (Exception e1)
       {
           // handle ParserConfigurationException
       }
       return "";
   }
    
    
    public static void stringToDom(String xmlSource)
            throws SAXException, ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException, JAXBException {
        // Parse the given input
        /*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlSource)));

        // Write the parsed document to an xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new File("my-file.xml"));
        transformer.transform(source, result);*/
        
        FiletoXMLtoObject("RegFile.xml");
        FileToStringToObject("RegFile.xml");
    }
    
     public static String FiletoXMLtoObject(String filepath) throws ParserConfigurationException, SAXException, IOException, JAXBException
   {

       java.io.File fXmlFile = new File(filepath);
       javax.xml.parsers.DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
       javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
       org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);

       //optional, but recommended
       //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
       doc.getDocumentElement().normalize();
       String message = doc.getDocumentElement().getTextContent();

       System.out.println(message);
       
       javax.xml.bind.JAXBContext jc = javax.xml.bind.JAXBContext.newInstance(RegFile.class);
       javax.xml.bind.Unmarshaller unmar = jc.createUnmarshaller();
       //eu.openmos.msb.dummyclasses.RegFile aux = (eu.openmos.msb.dummyclasses.RegFile) unmar.unmarshal(doc);
      RegFile aux = new RegFile();
       aux = (RegFile) unmar.unmarshal(fXmlFile);
       
       aux = (RegFile) unmar.unmarshal(doc);
        
       //not working renaxo
       /*DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       DocumentBuilder db = null;

       try
       {
           db = dbf.newDocumentBuilder();
           InputSource is = new InputSource();
           is.setCharacterStream(new StringReader(filepath));
            //is.setCharacterStream(new StringReader(filepath));
           try
           {
               org.w3c.dom.Document doc = db.parse(is);
               String message = doc.getDocumentElement().getTextContent();
               System.out.println(message);
               JAXBContext jc = JAXBContext.newInstance(RegFile.class);
               Unmarshaller unmar = jc.createUnmarshaller();
               RegFile aux = (RegFile) unmar.unmarshal(doc);
               int i = 0;
           } catch (IOException | JAXBException | DOMException | SAXException e)
           {
            System.out.println("prob1: "+e);
           }
       } catch (ParserConfigurationException e1) 
       {
            System.out.println("prob2: "+e1);
       }*/

       return "";

   }
}
