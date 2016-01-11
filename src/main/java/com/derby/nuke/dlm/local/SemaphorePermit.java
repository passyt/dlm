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
	public String acquire() throws InterruptedException {
		semaphore.acquire();
		return DEFAULT_TICKET;
	}

	@Override
	public String tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		if (!semaphore.tryAcquire(timeout, unit)) {
			throw new InterruptedException("Timeout to acquire a permit within " + timeout + " " + unit);
		}

		return DEFAULT_TICKET;
	}

	@Override
	public boolean release(String ticketId) {
		semaphore.release();
		return true;
	}

	public int getTotal() {
		return total;
	}

}
