/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.dds.instance;

import DDS.DomainParticipant;
import java.util.ArrayList;



/**
 * Description TODO
 *
 * @author af-silva
 *
 */
public class DDSDeviceManager
{

  private DomainParticipant domain;
  private ArrayList<DDSDevice> devices;


  /**
   * TODO - Construct doc
   *
   * @param dp
   */
  public DDSDeviceManager(DomainParticipant dp)
  {
    this.domain = dp;
    this.devices = new ArrayList<DDSDevice>();
  }


  /**
   *
   * @return
   */
  public int addDevice()
  {

    return -1;
  }


  /**
   *
   * @return
   */
  public int removeDevice()
  {

    return -1;
  }

}


// EOF
