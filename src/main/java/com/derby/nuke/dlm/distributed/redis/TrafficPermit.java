package com.derby.nuke.dlm.distributed.redis;

import java.util.concurrent.TimeUnit;

import com.derby.nuke.dlm.IPermit;

public class TrafficPermit implements IPermit {

	@Override
	public String acquire() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void release(String ticketId) {
		// TODO Auto-generated method stub

	}

}
