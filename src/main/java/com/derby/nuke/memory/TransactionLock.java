package com.derby.nuke.memory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.derby.nuke.ILock;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;

/**
 * 
 * @author Passyt
 *
 */
public class TransactionLock implements ILock {

	private static final byte[] REFERENCE = new byte[0];

	private final TimeUnit unit;
	private final long duration;
	private final long total;

	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();
	private final LoadingCache<Long, byte[]> cache;
	private final AtomicLong tickets = new AtomicLong();

	/**
	 * 
	 * @param total
	 * @param duration
	 * @param unit
	 */
	public TransactionLock(long total, long duration, TimeUnit unit) {
		super();
		this.total = total;
		this.duration = duration;
		this.unit = unit;
		this.cache = CacheBuilder.newBuilder().expireAfterWrite(this.duration, this.unit)
				.build(new CacheLoader<Long, byte[]>() {

					@Override
					public byte[] load(Long key) throws Exception {
						return REFERENCE;
					}

				});
	}

	@Override
	public String lock() throws InterruptedException {
		try {
			lock.lock();
			System.out.println("lock: " + sizeOfCache());
			if (sizeOfCache() >= total) {
				condition.await();
			}

			long ticket = tickets.getAndIncrement();
			cache.put(ticket, REFERENCE);
			return String.valueOf(ticket);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String lock(long time, TimeUnit unit) throws InterruptedException {
		try {
			lock.lock();
			try {
				return new FutureTask<String>(() -> {
					return lock();
				}).get(time, unit);
			} catch (ExecutionException e) {
				throw new IllegalStateException(e);
			} catch (TimeoutException e) {
				throw new InterruptedException(e.getMessage());
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void unlock(String ticketId) {
		boolean locking = sizeOfCache() > total;
		cache.invalidate(ticketId);
		System.out.println("unlock: " + sizeOfCache());
		if (locking) {
			condition.signal();
		}
	}

	protected long sizeOfCache() {
		return Maps.newHashMap(this.cache.asMap()).size();
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
