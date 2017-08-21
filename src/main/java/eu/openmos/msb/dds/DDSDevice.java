/*
 * TODO - ADD LICENCE
 */
package eu.openmos.msb.dds;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.ANY_VIEW_STATE;
import DDS.DataReader;
import DDS.DataReaderQosHolder;
import DDS.DataWriter;
import DDS.DataWriterQosHolder;
import DDS.DomainParticipant;
import DDS.Publisher;
import DDS.PublisherQosHolder;
import DDS.QueryCondition;
import DDS.ReliabilityQosPolicyKind;
import DDS.STATUS_MASK_NONE;
import DDS.Subscriber;
import DDS.SubscriberQosHolder;
import DDS.Topic;
import DDS.TopicQosHolder;
import MSB2ADAPTER.GeneralMethodMessageTypeSupport;
import MSB2ADAPTER.StringMessageTypeSupport;
import java.util.HashMap;
import org.opensplice.dds.dcps.TypeSupportImpl;

/**
 *
 * @author andre
 */
public class DDSDevice
{

  public enum TopicType
  {
    GENERALMETHODMESSAGE,
    STRINGMESSAGE
  }

  private HashMap<String, Topic> topics;
  private DomainParticipant participant;
  private GeneralMethodMessageTypeSupport gmmts;
  private StringMessageTypeSupport smts;

  // Publisher and Subscriber QOS holders
  private PublisherQosHolder pubQos = new PublisherQosHolder();
  private SubscriberQosHolder subQos = new SubscriberQosHolder();
  private Subscriber subscriber;
  private Publisher publisher;

  // create one per topic ????
  private DataWriterQosHolder WQosH = new DataWriterQosHolder();
  private DataReaderQosHolder RQosH = new DataReaderQosHolder();

  private final HashMap<String, DDSTopicHelper> topicInstances;

  /**
   * DDSDevice constructor. Receives the deviceName and the domain to which will belong
   *
   * @param deviceName Name of the device (identifier)
   * @param participant
   */
  public DDSDevice(String deviceName, DomainParticipant participant)
  {
    this.participant = participant;
    int status = -1;
    topicInstances = new HashMap<String, DDSTopicHelper>();

    // create type
    gmmts = new GeneralMethodMessageTypeSupport();
    smts = new StringMessageTypeSupport();

    status = gmmts.register_type(this.participant, gmmts.get_type_name());
    DDSErrorHandler.checkStatus(status, "register_type");
    status = smts.register_type(this.participant, smts.get_type_name());
    DDSErrorHandler.checkStatus(status, "register_type");

    // deviceName of device represents the partition in DDS
    createPublisher(deviceName);
    createSubscriber(deviceName);

  }

  /**
   * Crate a new Topic for reading information from the network
   *
   * @param topicName Name of the topic
   * @param topicType Type of the topic, at the moment only two are supported
   */
  public void crateTopicReader(String topicName, TopicType topicType)
  {

    if (topicType == TopicType.GENERALMETHODMESSAGE)
    {
      createTopic(topicName, gmmts);
      createReader(topicName);
    } else if (topicType == TopicType.STRINGMESSAGE)
    {
      createTopic(topicName, smts);
      createReader(topicName);
    }

  }

  /**
   * Crate a new Topic to read information from the network
   *
   * @param topicName Name of the topic
   * @param topicType Type of the topic, at the moment only two are supported
   * @param query String representing the query condition (SQL like query)
   * @param query_args A sequence of strings which are the parameter values used in the SQL query string
   *
   * Please refer to the DDS section 3.5.2.2 of the OpenSplice DDS Java Reference Guide for more information about the
   * query conditions
   */
  public void crateTopicReader(String topicName, TopicType topicType, String query, String[] query_args)
  {
    crateTopicReader(topicName, topicType);
    createQueryCondition(topicName, query, query_args);
  }

  /**
   * Create a new Topic to write information to the network
   *
   * @param topicName Name of the topic
   * @param topicType Type of the topic, at the moment only two are supported
   */
  public void createTopicWriter(String topicName, TopicType topicType)
  {
    if (topicType == TopicType.GENERALMETHODMESSAGE)
    {
      createTopic(topicName, gmmts);
      createWriter(topicName);
    } else if (topicType == TopicType.STRINGMESSAGE)
    {

      createTopic(topicName, smts);
      createWriter(topicName);
    }

  }

  /**
   * Create a new Topic to write information to the network
   *
   * @param topicName Name of the topic
   * @param topicType Type of the topic, at the moment only two are supported
   * @param query String representing the query condition (SQL like query)
   * @param query_args A sequence of strings which are the parameter values used in the SQL query string
   *
   * Please refer to the DDS section 3.5.2.2 of the OpenSplice DDS Java Reference Guide for more information about the
   * query conditions
   */
  public void createTopicWriter(String topicName, TopicType topicType, String query, String[] query_args)
  {
    createTopicWriter(topicName, topicType);
    createQueryCondition(topicName, query, query_args);
  }

  /**
   * Obtain all the available topics registered in this device
   *
   * @return String array with all the topics already registered
   */
  public String[] getAvailableTopics()
  {

    return (String[]) topics.keySet().toArray();
  }

  /**
   * PRIVATE FUNCTIONS *
   */
  /**
   * Create a new topic
   *
   * @param topicName Topic Names
   * @param ts Topic Type (DDS object type)
   */
  private void createTopic(String topicName, TypeSupportImpl ts)
  {
    int status = -1;
    TopicQosHolder topicQos = new TopicQosHolder();
    topicInstances.put(topicName, new DDSTopicHelper());

    this.participant.get_default_topic_qos(topicQos);
    topicQos.value.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
    topicQos.value.durability.kind = DDS.DurabilityQosPolicyKind.TRANSIENT_DURABILITY_QOS;
    topicQos.value.deadline.period.nanosec = 0;
    topicQos.value.deadline.period.sec = 1;

    status = this.participant.set_default_topic_qos(topicQos.value);
    DDSErrorHandler.checkStatus(status, "DomainParticipant.set_default_topic_qos");

    Topic topic = this.participant.create_topic(
            topicName,
            ts.get_type_name(),
            topicQos.value,
            null,
            STATUS_MASK_NONE.value
    );
    DDSErrorHandler.checkHandle(topic, "DomainParticipant.create_topic");

    topicInstances.get(topicName).setTopic(topic);
    topicInstances.get(topicName).setTopicQos(topicQos);
  }

  /**
   * Create QueryCondition
   *
   * @param topicName Topic name
   * @param query String representing the query condition (SQL like query)
   * @param query_args A sequence of strings which are the parameter values used in the SQL query string
   *
   * Please refer to the DDS section 3.5.2.2 of the OpenSplice DDS Java Reference Guide for more information about the
   * query conditions
   */
  private void createQueryCondition(String topicName, String query, String[] query_args)
  {
    QueryCondition qc = topicInstances.get(topicName).getDataReader().create_querycondition(
            ANY_SAMPLE_STATE.value,
            ANY_VIEW_STATE.value,
            ANY_INSTANCE_STATE.value,
            query,
            query_args
    );
    topicInstances.get(topicName).setTopicQueryCondition(qc);
  }

  /**
   * Create a new publisher
   *
   * @param partition Name of the DDS partition
   * @return 0 if new publisher has been created, 1 if current publisher has been replaced and -1 if an error occurred
   */
  private int createPublisher(String partition)
  {
    try
    {
      if (this.publisher == null)
      {
        int status = this.participant.get_default_publisher_qos(pubQos);
        DDSErrorHandler.checkStatus(status, "DomainParticipant.get_default_publisher_qos");

        pubQos.value.partition.name = new String[1];
        pubQos.value.partition.name[0] = partition;
        this.publisher = this.participant.create_publisher(
                pubQos.value,
                null,
                STATUS_MASK_NONE.value
        );
        DDSErrorHandler.checkHandle(this.publisher, "DomainParticipant.create_publisher");
        return 0;
      } else
      {
        deletePublisher();
        createPublisher(partition);
        return 1;
      }
    } catch (Exception ex)
    {
      System.out.println("[ERROR] on createPublisher" + ex.getMessage());
      return -1;
    }
  }

  /**
   * Create a new subscriber
   *
   * @param partition Name of the DDS partition
   * @return 0 if new subscriber has been created, 1 if current subscriber has been replaced and -1 if an error occurred
   */
  private int createSubscriber(String partition)
  {
    try
    {

      if (this.subscriber == null)
      {

        int status = this.participant.get_default_subscriber_qos(subQos);
        DDSErrorHandler.checkStatus(status, "DomainParticipant.get_default_subscriber_qos");

        subQos.value.partition.name = new String[1];
        subQos.value.partition.name[0] = partition;
        this.subscriber = this.participant.create_subscriber(
                subQos.value,
                null,
                STATUS_MASK_NONE.value
        );
        DDSErrorHandler.checkHandle(this.subscriber, "DomainParticipant.create_subscriber");

        return 0;
      } else
      {
        deleteSubscriber();
        createSubscriber(partition);
        return 1;
      }
    } catch (Exception ex)
    {
      System.out.println("[Error] on createSubscriber" + ex.getMessage());
      return -1;
    }
  }

  /**
   * Create a new writer, responsible to publish messages at the given topic. A topic with the same name has to be
   * created first!
   *
   * @param topicName Name of the topic
   * @return 0 if the writer was created, 1 if the given topic does not exists and -1 if error
   */
  private int createWriter(String topicName)
  {
    try
    {
      if (topicInstances.containsKey(topicName))
      {
        publisher.get_default_datawriter_qos(WQosH);
        publisher.copy_from_topic_qos(WQosH, topicInstances.get(topicName).getTopicQos().value);
        WQosH.value.writer_data_lifecycle.autodispose_unregistered_instances = false;
        DataWriter writer = publisher.create_datawriter(
                topicInstances.get(topicName).getTopic(),
                WQosH.value,
                null,
                STATUS_MASK_NONE.value
        );
        DDSErrorHandler.checkHandle(writer, "Publisher.create_datawriter");
        topicInstances.get(topicName).setDataWriter(writer);
        return 1;
      } else
      {
        System.out.println("[createWriter] The given topic does not exists!");
        return 0;
      }
    } catch (Exception ex)
    {
      System.out.println("[ERROR] on createWriter" + ex.getMessage());
      return -1;
    }

  }

  /**
   * Create a new reader, responsible to subscribe messages at the given topic. A topic with the same name has to be
   * created first!
   *
   * @param topicName Name of the topic
   * @return 0 if the reader was created, 1 if the given topic does not exists and -1 if error
   */
  private int createReader(String topicName)
  {
    try
    {
      if (topicInstances.containsKey(topicName))
      {
        subscriber.get_default_datareader_qos(RQosH);
        subscriber.copy_from_topic_qos(RQosH, topicInstances.get(topicName).getTopicQos().value);
        DataReader reader = subscriber.create_datareader(
                topicInstances.get(topicName).getTopic(),
                RQosH.value,
                new ListenerDataListener(),
                DDS.DATA_AVAILABLE_STATUS.value
        );
        DDSErrorHandler.checkHandle(reader, "Subscriber.create_datareader");

        topicInstances.get(topicName).setDataReader(reader);
        return 0;
      } else
      {
        System.out.println("[createReader] The given topic does not exists.");
        return 1;
      }
    } catch (Exception ex)
    {
      System.out.println("[ERROR] on createReader");
      return -1;
    }

  }

  /**
   * Delete Topic
   *
   * @param topicName Name of the topic
   */
  private void deleteTopic(String topicName)
  {
    int status = this.participant.delete_topic(topicInstances.get(topicName).getTopic());
    DDSErrorHandler.checkStatus(status, "DDS.DomainParticipant.delete_topic");
    topicInstances.remove(topicName);
  }

  /**
   * Delete the publisher associated with this device
   */
  private void deletePublisher()
  {
    this.participant.delete_publisher(publisher);
    this.publisher = null;
  }

  /**
   * Delete the subscriber associated with this device
   */
  private void deleteSubscriber()
  {
    this.participant.delete_subscriber(subscriber);
    this.subscriber = null;
  }

  /**
   * Get the Data Reader of the topic
   *
   * @param topicName The name of the topic
   * @return Data Reader of the given topic
   */
  public DataReader getReader(String topicName)
  {
    return topicInstances.get(topicName).getDataReader();
  }

  /**
   * Get the Data Writer of the Topic
   *
   * @param topicName The name of the topic
   * @return Data Writer of the given topic
   */
  public DataWriter getWriter(String topicName)
  {
    return topicInstances.get(topicName).getDataWriter();
  }

  /**
   * Get the publisher associated with this device
   *
   * @return Publisher
   */
  public Publisher getPublisher()
  {
    return publisher;
  }

  /**
   * Get the subscriber associated with this device
   *
   * @return Subscriber
   */
  public Subscriber getSubscriber()
  {
    return subscriber;
  }

  /**
   * Get the Topic object of a given topic
   *
   * @param topicName Name of the topic
   * @return Topic
   */
  public Topic getTopic(String topicName)
  {
    return topicInstances.get(topicName).getTopic();
  }

  /**
   * Get the Query Condition of a given topic
   *
   * @param topicName Name of the topic
   * @return QueryCondition
   */
  public QueryCondition getQueryCondition(String topicName)
  {
    return topicInstances.get(topicName).getTopicQueryCondition();
  }

  /**
   * Get the DDS Participant associated with this device
   *
   * @return Participant
   */
  public DomainParticipant getParticipant()
  {
    return this.participant;
  }
}

//EOF
