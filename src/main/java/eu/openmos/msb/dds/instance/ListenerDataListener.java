package eu.openmos.msb.dds.instance;

/*
 * OpenSplice DDS
 *
 * This software and documentation are Copyright 2006 to 2013 PrismTech Limited and its licensees. All rights reserved.
 * See file:
 *
 * $OSPL_HOME/LICENSE
 *
 * for full copyright notice and license terms.
 *
 */
import DDS.*;
import MSB2ADAPTER.GeneralMethodMessage;
import MSB2ADAPTER.GeneralMethodMessageDataReader;
import MSB2ADAPTER.GeneralMethodMessageDataReaderImpl;
import MSB2ADAPTER.GeneralMethodMessageSeqHolder;
import MSB2ADAPTER.StringMessageDataReaderImpl;


public class ListenerDataListener implements DDS.DataReaderListener
{

  @Override
  public void on_data_available(DataReader arg0)
  {
    System.out.println("AVAILABLE");
    int status;

    
    
    
    
    
    if(arg0 instanceof GeneralMethodMessageDataReaderImpl)
    {
      System.out.println("GeneralMethodMessageDataReaderImpl");
    }
    else if(arg0 instanceof StringMessageDataReaderImpl)
    {
      System.out.println("StringMessageDataReaderImpl");
    }
      
    
    
    
    
    
    
    GeneralMethodMessageSeqHolder msgList = new GeneralMethodMessageSeqHolder();
    SampleInfoSeqHolder infoSeq = new SampleInfoSeqHolder();

    status = ((GeneralMethodMessageDataReader) arg0).take(
      msgList,
      infoSeq,
      LENGTH_UNLIMITED.value,
      NOT_READ_SAMPLE_STATE.value,
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
          System.out.println("    userID  : " + data[i].function);
          System.out.println("    Message : \"" + data[i].args + "\"");

        }
      }
      while (++i < data.length);

      

      status = ((GeneralMethodMessageDataReader) arg0).return_loan(msgList, infoSeq);
      DDSErrorHandler.checkStatus(status, "MsgDataReader.return_loan");
    }
  }


  @Override
  public void on_liveliness_changed(DataReader arg0, LivelinessChangedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_liveliness_changed] : triggered");

  }


  @Override
  public void on_requested_deadline_missed(DataReader arg0, RequestedDeadlineMissedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_requested_deadline_missed] : triggered");
    // System.out.println("=== [ListenerDataListener.on_requested_deadline_missed] : stopping");

  }


  @Override
  public void on_requested_incompatible_qos(DataReader arg0, RequestedIncompatibleQosStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_requested_incompatible_qos] : triggered");

  }


  @Override
  public void on_sample_lost(DataReader arg0, SampleLostStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_sample_lost] : triggered");

  }


  @Override
  public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_sample_rejected] : triggered");

  }


  @Override
  public void on_subscription_matched(DataReader arg0, SubscriptionMatchedStatus arg1)
  {

    System.out.println("=== [ListenerDataListener.on_subscription_matched] : triggered");

  }

}
