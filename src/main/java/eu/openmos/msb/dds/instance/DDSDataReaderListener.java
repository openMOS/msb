/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.*;
import org.opensplice.dds.dcps.ListenerBase;



/**
 *
 * @author andre
 */
public class DDSDataReaderListener extends ListenerBase implements DataReaderListener
{

  @Override
  public void on_requested_deadline_missed(DataReader reader, RequestedDeadlineMissedStatus rdms)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_requested_incompatible_qos(DataReader reader, RequestedIncompatibleQosStatus riqs)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_sample_rejected(DataReader reader, SampleRejectedStatus srs)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_liveliness_changed(DataReader reader, LivelinessChangedStatus lcs)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_data_available(DataReader reader)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_subscription_matched(DataReader reader, SubscriptionMatchedStatus sms)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }


  @Override
  public void on_sample_lost(DataReader reader, SampleLostStatus sls)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
}


//EOF