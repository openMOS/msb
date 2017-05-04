/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.openmos.msb.opcua.prosys.client;

/**
 * Prosys OPC UA Java SDK
 *
 * Copyright (c) Prosys PMS Ltd., <http://www.prosysopc.com>.
 * All rights reserved.
 */

import org.opcfoundation.ua.builtintypes.DataValue;

import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;

/**
 * A sampler listener for monitored data changes.
 */
public class MyMonitoredDataItemListener implements MonitoredDataItemListener {
	private final OPCUAClient client;

	/**
	 * @param client
	 */
	public MyMonitoredDataItemListener(OPCUAClient client) {
		super();
		this.client = client;
	}

	@Override
	public void onDataChange(MonitoredDataItem sender, DataValue prevValue, DataValue value) {
		//System.out.println(client.dataValueToString(sender.getNodeId(), sender.getAttributeId(), value));
                System.out.println("a value has Changed!");
	}
};
