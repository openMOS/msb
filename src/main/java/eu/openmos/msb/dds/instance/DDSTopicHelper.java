/*
 * TODO add licence
 */
package eu.openmos.msb.dds.instance;

import DDS.DataReader;
import DDS.DataReaderQosHolder;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.Publisher;
import DDS.QueryCondition;
import DDS.Topic;
import DDS.TopicQosHolder;


/**
 *
 * @author andre
 */
public class DDSTopicHelper
{
  private Topic topic;
  private TopicQosHolder topicQos;
  private QueryCondition topicQueryCondition;
  private DataWriterQosHolder WQosH;
  private DataReaderQosHolder RQosH;
  private DataReader dataReader;
  private DataWriter dataWriter;


  /**
   *
   * @return
   */
  public Publisher getPublisher()
  {
    return publisher;
  }


  /**
   *
   * @param publisher
   */
  public void setPublisher(Publisher publisher)
  {
    this.publisher = publisher;
  }
  private Publisher publisher;


  /**
   *
   * @return
   */
  public Topic getTopic()
  {
    return topic;
  }


  /**
   *
   * @param topic
   */
  public void setTopic(Topic topic)
  {
    this.topic = topic;
  }


  /**
   *
   * @return
   */
  public TopicQosHolder getTopicQos()
  {
    return topicQos;
  }


  /**
   *
   * @param topicQos
   */
  public void setTopicQos(TopicQosHolder topicQos)
  {
    this.topicQos = topicQos;
  }


  /**
   *
   * @return
   */
  public QueryCondition getTopicQueryCondition()
  {
    return topicQueryCondition;
  }


  /**
   *
   * @param topicQueryCondition
   */
  public void setTopicQueryCondition(QueryCondition topicQueryCondition)
  {
    this.topicQueryCondition = topicQueryCondition;
  }


  /**
   *
   * @return
   */
  public DataWriterQosHolder getWQosH()
  {
    return WQosH;
  }


  /**
   *
   * @param WQosH
   */
  public void setWQosH(DataWriterQosHolder WQosH)
  {
    this.WQosH = WQosH;
  }


  /**
   *
   * @return
   */
  public DataReaderQosHolder getRQosH()
  {
    return RQosH;
  }


  /**
   *
   * @param RQosH
   */
  public void setRQosH(DataReaderQosHolder RQosH)
  {
    this.RQosH = RQosH;
  }


  /**
   *
   * @return
   */
  public DataReader getDataReader()
  {
    return dataReader;
  }


  /**
   *
   * @param dataReader
   */
  public void setDataReader(DataReader dataReader)
  {
    this.dataReader = dataReader;
  }


  /**
   *
   * @return
   */
  public DataWriter getDataWriter()
  {
    return dataWriter;
  }


  /**
   *
   * @param dataWriter
   */
  public void setDataWriter(DataWriter dataWriter)
  {
    this.dataWriter = dataWriter;
  }

}

//EOF