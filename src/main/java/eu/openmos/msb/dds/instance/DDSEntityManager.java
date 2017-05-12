/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DataReader;
import DDS.DataWriter;
import DDS.DomainParticipant;
import DDS.Publisher;
import DDS.Subscriber;
import DDS.Topic;
import org.opensplice.dds.dcps.TypeSupportImpl;

/** 
 * Description TODO
 * @author af-silva
 * 
 */
public class DDSEntityManager
{

  /** ******************************************************************************************************************
   * 
   * @param partitionName 
   */
  public void createParticipant(String partitionName) 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   */
  public void deleteParticipant() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   * @param ts 
   */
  public void registerType(TypeSupportImpl ts) 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  
  /** ******************************************************************************************************************
   * 
   * @param topicName 
   */
  public void createTopic(String topicName) 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   */
  public void deleteTopic() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  
  /** ******************************************************************************************************************
   * 
   */
  public void createPublisher() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   */
  public void deletePublisher() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   */
  public void createWriter() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ****************************************************************************************************************** 
   * 
   */
  public void createReader() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ****************************************************************************************************************** 
   * 
   */
  public void createSubscriber() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   */
  public void deleteSubscriber() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ****************************************************************************************************************** 
   * 
   * @return 
   */
  public DataReader getReader() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ****************************************************************************************************************** 
   * 
   * @return 
   */
  public DataWriter getWriter() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   * @return 
   */
  public Publisher getPublisher() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   * @return 
   */
  public Subscriber getSubscriber() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   * @return 
   */
  public Topic getTopic() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }

  /** ******************************************************************************************************************
   * 
   * @return 
   */
  public DomainParticipant getParticipant() 
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
 
  
 
}


// EOF