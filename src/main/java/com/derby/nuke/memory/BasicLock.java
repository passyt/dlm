package com.derby.nuke.memory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.derby.nuke.ILock;

/**
 * 
 * @author passyt
 *
 */
public class BasicLock implements ILock {

	private static final String DEFAULT_TICKET = "";
	private final Lock lock = new ReentrantLock();

	@Override
	public String lock() throws InterruptedException {
		lock.lock();
		return DEFAULT_TICKET;
	}

	@Override
	public String lock(long time, TimeUnit unit) throws InterruptedException {
		lock.tryLock(time, unit);
		return DEFAULT_TICKET;
	}

	@Override
	public void unlock(String ticketId) {
		lock.unlock();
	}

}
