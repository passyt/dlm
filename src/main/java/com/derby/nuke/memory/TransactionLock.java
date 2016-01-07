package com.derby.nuke.memory;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.derby.nuke.ILock;

/**
 * 
 * @author Passyt
 *
 */
public class TransactionLock implements ILock {

	private static final String DEFAULT_TICKET = "";

	private final TimeUnit unit;
	private final long duration;
	private final int total;
	private final Semaphore semaphore;
	private final AtomicInteger ai = new AtomicInteger();
	private final ScheduledExecutorService backgroundRemoveSchedule = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 
	 * @param total
	 * @param duration
	 * @param unit
	 */
	public TransactionLock(int total, long duration, TimeUnit unit) {
		super();
		this.total = total;
		this.duration = duration;
		this.unit = unit;
		this.semaphore = new Semaphore(total, true);
		backgroundRemoveSchedule.scheduleAtFixedRate(() -> {
			System.out.println(new Date().toString() + ": release");
			semaphore.release(ai.get());
		} , duration, duration, unit);
	}

	@Override
	public String lock() throws InterruptedException {
		semaphore.acquire();
		ai.incrementAndGet();
		return DEFAULT_TICKET;
	}

	@Override
	public String lock(long time, TimeUnit unit) throws InterruptedException {
		if (!semaphore.tryAcquire(time, unit)) {
			throw new InterruptedException("Timeout");
		}

		ai.incrementAndGet();
		return DEFAULT_TICKET;
	}

	@Override
	public void unlock(String ticketId) {
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public long getDuration() {
		return duration;
	}

	public long getTotal() {
		return total;
	}

}
