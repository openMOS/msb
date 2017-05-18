/*
 * TODO - ADD LICENCE
 */
package eu.openmos.msb.dds.instance;

import DDS.DATAREADER_QOS_USE_TOPIC_QOS;
import DDS.DataReader;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.DurabilityQosPolicyKind;
import DDS.Publisher;
import DDS.PublisherQosHolder;
import DDS.ReliabilityQosPolicyKind;
import DDS.STATUS_MASK_NONE;
import DDS.Subscriber;
import DDS.SubscriberQosHolder;
import DDS.Topic;
import DDS.TopicQosHolder;
import eu.openmos.msb.dds.topics.GeneralMethodMessageTypeSupport;
import eu.openmos.msb.dds.topics.StringMessageTypeSupport;
import java.util.HashMap;


/**
 *
 * @author andre
 */
public class DDSMSBDevice
{

  private Subscriber subscriber;
  private Publisher publisher;
  private HashMap<String, DataWriter> dataWriters;
  private HashMap<String, DataReader> datareaders;
  private HashMap<String, Topic> topics;
  private final DomainParticipant domain;
  private final String name;


  /**
   * DDSDevice constructor. Receives the name and the domain to which will belong
   *
   * @param name Name of the device (identifier)
   * @param dp Domain to join the device
   */
  public DDSMSBDevice(String name, DomainParticipant dp)
  {
    this.domain = dp;
    this.name = name;

    int status;

    // Create the Publisher for this device
    PublisherQosHolder pubQos = new PublisherQosHolder();
    status = this.domain.get_default_publisher_qos(pubQos);
    DDSErrorHandler.checkStatus(status, "DomainParticipant.get_default_publisher_qos");
    pubQos.value.partition.name = new String[1];
    pubQos.value.partition.name[0] = name;
    Publisher pub = this.domain.create_publisher(pubQos.value, null, STATUS_MASK_NONE.value);
    DDSErrorHandler.checkHandle(pub, "DomainParticipant.create_publisher");

    // Create the Subscriber for this device
    SubscriberQosHolder subQos = new SubscriberQosHolder();
    status = this.domain.get_default_subscriber_qos(subQos);
    DDSErrorHandler.checkStatus(status, "DomainParticipant.get_default_subscriber_qos");
    subQos.value.partition.name = new String[1];
    subQos.value.partition.name[0] = name;
    Subscriber sub = this.domain.create_subscriber(subQos.value, null, STATUS_MASK_NONE.value);
    DDSErrorHandler.checkHandle(sub, "DomainParticipant.create_subscriber");

    // Register the message type to the domain, this is specific to this network device
    GeneralMethodMessageTypeSupport gmmType = new GeneralMethodMessageTypeSupport();
    status = gmmType.register_type(domain, gmmType.get_type_name());
    DDSErrorHandler.checkStatus(status, "register_type");

    // Register the message type to the domain, this is specific to this network device
    StringMessageTypeSupport smType = new StringMessageTypeSupport();
    status = smType.register_type(domain, smType.get_type_name());
    DDSErrorHandler.checkStatus(status, "register_type");

  }


  /**
   * Create a topic with a know type and with the given name
   *
   * @param topicName Topic name
   * @param topicType Topic type
   */
  public void createTopic(String topicName, String topicType)
  {
    TopicQosHolder topicQos = new TopicQosHolder();
    int status = this.domain.get_default_topic_qos(topicQos);
    DDSErrorHandler.checkStatus(status, "DomainParticipant.set_default_topic_qos");
    topicQos.value.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
    topicQos.value.durability.kind = DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;

    Topic topic = this.domain.create_topic(topicName, topicType, topicQos.value, null, STATUS_MASK_NONE.value);
    DDSErrorHandler.checkHandle(topic, "DomainParticipant.create_topic");

    this.topics.put(topicName, topic);

  }


  /**
   * Delete a given topic
   *
   * @param topicName
   */
  public void deleteTopic(String topicName)
  {
    Topic topic = topics.get(topicName);
    int status = this.domain.delete_topic(topic);
    DDSErrorHandler.checkStatus(status, "DDS.DomainParticipant.delete_topic");
  }


  /**
   * Register a new writer for the given topic if this not exists in the current instance
   *
   * @param topicName Topic associated with the DataWriter
   */
  public void registerWriter(String topicName)
  {
    if (!dataWriters.containsKey(topicName) && topics.containsKey(topicName))
    {
      DataWriterQosHolder WQosH = new DataWriterQosHolder();
      TopicQosHolder topicQos = new TopicQosHolder();
      Topic topic = topics.get(topicName);
      topic.get_qos(topicQos);
      this.publisher.get_default_datawriter_qos(WQosH);
      this.publisher.copy_from_topic_qos(WQosH, topicQos.value);
      WQosH.value.writer_data_lifecycle.autodispose_unregistered_instances = false;
      DataWriter writer = this.publisher.create_datawriter(topic, WQosH.value, null, STATUS_MASK_NONE.value);
      DDSErrorHandler.checkHandle(writer, "Publisher.create_datawriter");
      this.dataWriters.put(topicName, writer);
    }
  }


  /**
   * Register a new reader for the given topic if this not exists in the current instance
   *
   * @param topicName Topic associated with the DataReader
   */
  public void registerReader(String topicName)
  {
    if (!datareaders.containsKey(topicName) && topics.containsKey(topicName))
    {
      Topic topic = topics.get(topicName);
      int mask = DDS.DATA_AVAILABLE_STATUS.value | DDS.REQUESTED_DEADLINE_MISSED_STATUS.value;
      DataReader reader = subscriber.create_datareader(topic,
        DATAREADER_QOS_USE_TOPIC_QOS.value,
        new DDSDataReaderListener(),
        mask);
      DDSErrorHandler.checkHandle(reader, "Subscriber.create_datareader");
      this.datareaders.put(topicName, reader);
    }
  }


  /**
   * Get the DataReader associated with a given topic
   *
   * @param topicName
   * @return
   */
  public DataReader getReader(String topicName)
  {
    return this.datareaders.get(topicName);
  }


  /**
   * Get the DataWriter associated with a given topic
   *
   * @param topicName
   * @return
   */
  public DataWriter getWriter(String topicName)
  {
    return this.dataWriters.get(topicName);
  }


  /**
   * Returns the device publisher instance
   *
   * @return publisher
   */
  public Publisher getPublisher()
  {
    return publisher;
  }


  /**
   * Returns the device subscriber instance
   *
   * @return subscriber
   */
  public Subscriber getSubscriber()
  {
    return subscriber;
  }


  /**
   * Get a instance of a given topic
   *
   * @param topicName The name of the topic to be return
   * @return return the Topic object for the given topic
   */
  public Topic getTopic(String topicName)
  {
    return this.topics.get(topicName);
  }


  /**
   * Get a list of topics (name and instance) registered to this device instance
   *
   * @return A HashMap with the name and topic
   */
  public HashMap<String, Topic> getTopicsList()
  {
    return topics;
  }

}
