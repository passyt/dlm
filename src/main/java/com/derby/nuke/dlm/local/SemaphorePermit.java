package com.derby.nuke.dlm.local;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * {@code SemaphorePermit} is the tool to perform like a pool with a maximum
 * number of {@link #total}
 * 
 * @author Passyt
 *
 */
public class SemaphorePermit extends LocalPermit {

	private final Semaphore semaphore;
	private final int total;

	public SemaphorePermit(int total) {
		this.total = total;
		this.semaphore = new Semaphore(total);
	}

	@Override
	public void acquire() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public boolean tryAcquire() {
		return semaphore.tryAcquire();
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) {
		try {
			return semaphore.tryAcquire(timeout, unit);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	@Override
	public void release() {
		semaphore.release();
	}

	public int getTotal() {
		return total;
	}

}
