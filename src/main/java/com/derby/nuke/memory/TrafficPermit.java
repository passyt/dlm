package com.derby.nuke.memory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.IPermit;

/**
 * TransactionLock is the tool to block some resource by a given {@link #total}
 * of {@link #duration} and time {@link #timeUnit}. For example, if you want to
 * block the traffic to 100 TPS, you can do it like that:
 * 
 * <pre>
 * class TpsController {
 * 	private IPermit permit = new TrafficPermit(100, 1, TimeUnit.SECONDS);
 * 
 * 	public <T> T execute(Callback<T> callback) {
 * 		String ticket = null;
 * 		try {
 * 			ticket = permit.acquire();
 * 			return callback.call();
 * 		} finally {
 * 			permit.release(ticket);
 * 		}
 * 	}
 * }
 * </pre>
 * 
 * @author Passyt
 *
 */
public class TrafficPermit implements IPermit {

	/**
	 * time unit of blocking resource
	 */
	private final TimeUnit timeUnit;
	/**
	 * duration of {@link #timeUnit}
	 */
	private final long duration;
	/**
	 * total count in {@link #duration} of {@link #timeUnit}
	 */
	private final int total;
	/**
	 * semaphore to block the resources
	 */
	private final Semaphore semaphore;
	/**
	 * background schedule to release resource
	 */
	private final ScheduledExecutorService backgroundRemoveSchedule = Executors.newSingleThreadScheduledExecutor();

	/**
	 * 
	 * @param total
	 *            maximum count of executing resource
	 * @param duration
	 * @param unit
	 */
	public TrafficPermit(int total, long duration, TimeUnit unit) {
		super();
		this.total = total;
		this.duration = duration;
		this.timeUnit = unit;
		this.semaphore = new Semaphore(total, true);
	}

	@Override
	public String acquire() throws InterruptedException {
		semaphore.acquire();
		releaseWithDelay();
		return DEFAULT_TICKET;
	}

	@Override
	public String tryAcquire(long time, TimeUnit unit) throws InterruptedException {
		if (!semaphore.tryAcquire(time, unit)) {
			throw new InterruptedException("Timeout to acquire a lock within " + time + " " + unit);
		}

		releaseWithDelay();
		return DEFAULT_TICKET;
	}

	/**
	 * do nothing to call this method as it's control by
	 * {@link #backgroundRemoveSchedule} to release lock
	 */
	@Override
	@Deprecated
	public void release(String ticketId) {
	}

	protected void releaseWithDelay() {
		backgroundRemoveSchedule.schedule(() -> {
			semaphore.release();
		} , duration, timeUnit);
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public long getDuration() {
		return duration;
	}

	public long getTotal() {
		return total;
	}

}
