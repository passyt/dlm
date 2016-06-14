package com.derby.nuke.dlm.core.distributed.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonClient;
import org.redisson.core.RLock;

/**
 * 
 * @author Passyt
 *
 */
public class RLockPermit extends RedissonPermit {

	private final RLock lock;

	public RLockPermit(RedissonClient client, String name) {
		super(client);
		this.lock = this.client.getLock(name);
	}

	@Override
	public void acquire() {
		lock.lock();
	}

	@Override
	public boolean tryAcquire() {
		return lock.tryLock();
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) {
		try {
			return lock.tryLock(timeout, unit);
		} catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void release() {
		lock.unlock();
	}

}
