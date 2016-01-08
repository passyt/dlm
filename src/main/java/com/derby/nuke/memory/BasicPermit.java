package com.derby.nuke.memory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.derby.nuke.IPermit;

/**
 * 
 * @author passyt
 *
 */
public class BasicPermit implements IPermit {

	private static final String DEFAULT_TICKET = "";
	private final Lock lock = new ReentrantLock();

	@Override
	public String acquire() throws InterruptedException {
		lock.lock();
		return DEFAULT_TICKET;
	}

	@Override
	public String tryAcquire(long time, TimeUnit unit) throws InterruptedException {
		lock.tryLock(time, unit);
		return DEFAULT_TICKET;
	}

	@Override
	public void release(String ticketId) {
		lock.unlock();
	}

}
