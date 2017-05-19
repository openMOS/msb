/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DataReader;
import DDS.DataWriter;
import DDS.Publisher;
import DDS.Subscriber;
import DDS.Topic;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author andre
 */
public class DDSMSBDeviceTest
{
  
  public DDSMSBDeviceTest()
  {
  }
  

  @BeforeClass
  public static void setUpClass()
  {
  }
  

  @AfterClass
  public static void tearDownClass()
  {
  }
  

  @Before
  public void setUp()
  {
  }
  

  @After
  public void tearDown()
  {
  }


  /**
   * Test of createTopic method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testCreateTopic()
  {
    System.out.println("createTopic");
    String topicName = "";
    String topicType = "";
    DDSMSBDevice instance = null;
    instance.createTopic(topicName, topicType);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of deleteTopic method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testDeleteTopic()
  {
    System.out.println("deleteTopic");
    String topicName = "";
    DDSMSBDevice instance = null;
    instance.deleteTopic(topicName);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of registerWriter method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testRegisterWriter()
  {
    System.out.println("registerWriter");
    String topicName = "";
    DDSMSBDevice instance = null;
    instance.registerWriter(topicName);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of registerReader method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testRegisterReader()
  {
    System.out.println("registerReader");
    String topicName = "";
    DDSMSBDevice instance = null;
    instance.registerReader(topicName);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getReader method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetReader()
  {
    System.out.println("getReader");
    String topicName = "";
    DDSMSBDevice instance = null;
    DataReader expResult = null;
    DataReader result = instance.getReader(topicName);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getWriter method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetWriter()
  {
    System.out.println("getWriter");
    String topicName = "";
    DDSMSBDevice instance = null;
    DataWriter expResult = null;
    DataWriter result = instance.getWriter(topicName);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getPublisher method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetPublisher()
  {
    System.out.println("getPublisher");
    DDSMSBDevice instance = null;
    Publisher expResult = null;
    Publisher result = instance.getPublisher();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getSubscriber method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetSubscriber()
  {
    System.out.println("getSubscriber");
    DDSMSBDevice instance = null;
    Subscriber expResult = null;
    Subscriber result = instance.getSubscriber();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getTopic method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetTopic()
  {
    System.out.println("getTopic");
    String topicName = "";
    DDSMSBDevice instance = null;
    Topic expResult = null;
    Topic result = instance.getTopic(topicName);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }


  /**
   * Test of getTopicsList method, of class DDSMSBDevice.
   */
  @org.junit.Test
  public void testGetTopicsList()
  {
    System.out.println("getTopicsList");
    DDSMSBDevice instance = null;
    HashMap<String, Topic> expResult = null;
    HashMap<String, Topic> result = instance.getTopicsList();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
