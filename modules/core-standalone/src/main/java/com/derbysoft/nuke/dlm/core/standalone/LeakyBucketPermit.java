package com.derbysoft.nuke.dlm.core.standalone;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * {@code RateLimiterPermit} is a simple implementation to support 1000 queries
 * per second, 50M per second etc with
 * {@link com.google.common.util.concurrent.RateLimiter}
 * 
 * @author Passyt
 *
 */
public class LeakyBucketPermit extends LocalPermit {

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
	public LeakyBucketPermit(double permitsPerSecond) {
		rateLimiter = RateLimiter.create(permitsPerSecond, 1L, TimeUnit.MICROSECONDS);
	}

	@Override
	public void acquire() {
		rateLimiter.acquire();
	}

	@Override
	public boolean tryAcquire() {
		return rateLimiter.tryAcquire();
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) {
		return rateLimiter.tryAcquire(timeout, unit);
	}

	@Override
	public void release() {
	}

}