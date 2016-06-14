package com.derby.nuke.dlm.core.distributed.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonClient;
import org.redisson.core.RSemaphore;

/**
 * 
 * @author Passyt
 *
 */
public class RSemaphorePermit extends RedissonPermit {

	private final RSemaphore semaphore;

	public RSemaphorePermit(RedissonClient client, String name, int count) {
		super(client);
		this.semaphore = client.getSemaphore(name);
		// TODO how to recover state while network is broken or app is restart
		if (this.semaphore.isExists()) {
			this.semaphore.delete();
		}
		this.semaphore.setPermits(count);
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

}
