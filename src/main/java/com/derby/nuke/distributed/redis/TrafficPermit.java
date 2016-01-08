package com.derby.nuke.distributed.redis;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.redisson.RedissonClient;
import org.redisson.core.RSemaphore;

import com.derby.nuke.IPermit;

public class TrafficPermit implements IPermit {

	private final String keyspace;
	private final RedissonClient client;
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
	private final RSemaphore semaphore;
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
	public TrafficPermit(String keyspace, RedissonClient client, int total, long duration, TimeUnit unit) {
		super();
		this.keyspace = keyspace;
		this.client = client;
		this.total = total;
		this.duration = duration;
		this.timeUnit = unit;
		this.semaphore = this.client.getSemaphore(keyspace);
		if (!this.semaphore.isExists()) {
			this.semaphore.setPermits(total);
		}
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
			throw new InterruptedException("Timeout to acquire a permit within " + time + " " + unit);
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

	public String getKeyspace() {
		return keyspace;
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
