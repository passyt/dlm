package com.derbysoft.nuke.dlm.core.standalone;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@code BasicPermit} is a basic implementation of IPermit with
 * {@link ReentrantLock}
 * 
 * @author passyt
 *
 */
public class ReentrantPermit extends StandalonePermit {

	private final Lock lock = new ReentrantLock();

	@Override
	public void acquire() {
		lock.lock();
	}

	@Override
	public boolean tryAcquire() {
		return lock.tryLock();
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) {
		try {
			return lock.tryLock(timeout, unit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	@Override
	public void release() {
		lock.unlock();
	}

}
