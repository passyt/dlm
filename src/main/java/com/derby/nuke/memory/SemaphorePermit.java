package com.derby.nuke.memory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.IPermit;

/**
 * {@code SemaphorePermit} is the tool to perform like a pool with a maximum
 * number of {@link #total}
 * 
 * @author Passyt
 *
 */
public class SemaphorePermit implements IPermit {

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
	public void release(String ticketId) {
		semaphore.release();
	}

	public int getTotal() {
		return total;
	}

}
