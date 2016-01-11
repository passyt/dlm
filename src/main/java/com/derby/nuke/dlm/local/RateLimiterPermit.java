package com.derby.nuke.dlm.local;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * {@code RateLimiterPermit} is a simple implemention to support 1000 queries
 * per second, 50M per second etc with
 * {@link com.google.common.util.concurrent.RateLimiter}
 * 
 * @author Passyt
 *
 */
public class RateLimiterPermit extends LocalPermit {

	private final RateLimiter rateLimiter;

	/**
	 * Creates a {@code RateLimiterPermit} with the specified stable throughput,
	 * given as "permits per second" (commonly referred to as <i>QPS</i>,
	 * queries per second).
	 *
	 * <p>
	 * The can ensure that on average no more than {@code
	 * permitsPerSecond} are issued during any given second, with sustained
	 * requests being smoothly spread over each second. When the incoming
	 * request rate exceeds {@code permitsPerSecond} the rate limiter will
	 * release one permit every {@code
	 * (1.0 / permitsPerSecond)} seconds. When the rate limiter is unused,
	 * bursts of up to {@code permitsPerSecond} permits will be allowed, with
	 * subsequent requests being smoothly limited at the stable rate of
	 * {@code permitsPerSecond}.
	 *
	 * @param permitsPerSecond
	 *            the rate of the returned {@code RateLimiter}, measured in how
	 *            many permits become available per second
	 * @throws IllegalArgumentException
	 *             if {@code permitsPerSecond} is negative or zero
	 */
	public RateLimiterPermit(double permitsPerSecond) {
		rateLimiter = RateLimiter.create(permitsPerSecond);
	}

	@Override
	public String acquire() throws InterruptedException {
		rateLimiter.acquire();
		return DEFAULT_TICKET;
	}

	@Override
	public String tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		if (!rateLimiter.tryAcquire(timeout, unit)) {
			throw new InterruptedException("Timeout to acquire a permit within " + timeout + " " + unit);
		}

		return DEFAULT_TICKET;
	}

	/**
	 * do nothing to call this method as it's control by
	 * {@link #backgroundRemoveSchedule} to release lock
	 */
	@Override
	@Deprecated
	public boolean release(String ticketId) {
		return true;
	}

}
