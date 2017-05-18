/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.ANY_INSTANCE_STATE;
import DDS.ANY_SAMPLE_STATE;
import DDS.DataReader;
import DDS.DataReaderListener;
import DDS.GuardCondition;
import DDS.LENGTH_UNLIMITED;
import DDS.LivelinessChangedStatus;
import DDS.NEW_VIEW_STATE;
import DDS.RequestedDeadlineMissedStatus;
import DDS.RequestedIncompatibleQosStatus;
import DDS.SampleInfoSeqHolder;
import DDS.SampleLostStatus;
import DDS.SampleRejectedStatus;
import DDS.SubscriptionMatchedStatus;
import eu.openmos.msb.opcua.utils.OPCDeviceItf;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.opensplice.dds.dcps.ListenerBase;
import org.xml.sax.SAXException;
import eu.openmos.msb.dds.topics.GeneralMethodMessage;
import eu.openmos.msb.dds.topics.GeneralMethodMessageDataReader;
import eu.openmos.msb.dds.topics.GeneralMethodMessageSeqHolder;
import eu.openmos.msb.dds.topics.StringMessage;
import eu.openmos.msb.dds.topics.StringMessageSeqHolder;


/**
 *
 * @author andre
 */
public class DDSDataReaderListener extends ListenerBase implements DataReaderListener
{

  private static OPCDeviceItf DeviceITF;

  boolean m_closed;
  GeneralMethodMessageDataReader m_MsgReader;
  DDS.GuardCondition m_guardCond = new GuardCondition();


  public DDSDataReaderListener()
  {
    DeviceITF = new OPCDeviceItf();
  }


  @Override
  public void on_data_available(DataReader arg0)
  {
    System.out.println("AVAILABLE");
    int status;
    GeneralMethodMessageSeqHolder msgList = new GeneralMethodMessageSeqHolder();
    SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();
    status = m_MsgReader.read(
      msgList,
      infoSeq,
      LENGTH_UNLIMITED.value,
      ANY_SAMPLE_STATE.value,
      NEW_VIEW_STATE.value,
      ANY_INSTANCE_STATE.value);
    DDSErrorHandler.checkStatus(status, "MsgDataReader.read");

    GeneralMethodMessage[] data = msgList.value;
    boolean hasValidData = false;
    if (data.length > 0)
    {
      System.out.println("=== [ListenerDataListener.on_data_available] - msgList.length : " + data.length);

      int i = 0;
      do
      {
        if (infoSeq.value[i].valid_data)
        {
          hasValidData = true;
          System.out.println("    --- message received ---");
          System.out.println("    Device  : " + data[i].device);
          System.out.println("    function : \"" + data[i].function + "\"");
          System.out.println("    args : \"" + data[i].args + "\"");
          System.out.println("    feedback : \"" + data[i].feedback + "\"");
        }
      }
      while (++i < data.length);

      if (hasValidData)
      {
        // unblock the wCaitset in Subscriber main loop
        m_guardCond.set_trigger_value(true);
      }
      else
      {
        System.out.println("=== [ListenerDataListener.on_data_available] ===> hasValidData is false!");
      }

      status = m_MsgReader.return_loan(msgList, infoSeq);
      DDSErrorHandler.checkStatus(status, "MsgDataReader.return_loan");

      // TODO - implement this
      try
      {
        DeviceITF.AllCases("Teste", "Teste");
      }
      catch (ParserConfigurationException | SAXException | IOException
        | JAXBException | TransformerException ex)
      {
        Logger.getLogger(DDSDataReaderListener.class.getName()).log(Level.SEVERE,
          null, ex);
      }

    }
  }


  @Override
  public void on_liveliness_changed(DataReader arg0,
    LivelinessChangedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_liveliness_changed] : triggered");
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }


  @Override
  public void on_requested_deadline_missed(DataReader arg0,
    RequestedDeadlineMissedStatus arg1)
  {

    // System.out.println("=== [ListenerDataListener.on_requested_deadline_missed] : triggered");
    // System.out.println("=== [ListenerDataListener.on_requested_deadline_missed] : stopping");
    m_closed = true;
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }


  @Override
  public void on_requested_incompatible_qos(DataReader arg0,
    RequestedIncompatibleQosStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_requested_incompatible_qos] : triggered");
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }


  @Override
  public void on_sample_lost(DataReader arg0, SampleLostStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_sample_lost] : triggered");
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }


  @Override
  public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_sample_rejected] : triggered");
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }


  @Override
  public void on_subscription_matched(DataReader arg0,
    SubscriptionMatchedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_subscription_matched] : triggered");
    // unblock the waitset in Subscriber main loop
    m_guardCond.set_trigger_value(true);
  }

}
//EOF
