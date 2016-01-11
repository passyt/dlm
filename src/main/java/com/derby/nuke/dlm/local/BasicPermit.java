package com.derby.nuke.dlm.local;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code BasicPermit} is a basic implemention of IPermit with
 * {@link java.util.concurrent.locks.ReentrantLock}
 * 
 * @author passyt
 *
 */
public class BasicPermit extends LocalPermit {

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
	public boolean release(String ticketId) {
		lock.unlock();
		return true;
	}

}
