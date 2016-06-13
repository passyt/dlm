package com.derby.nuke.dlm.distributed.redis;

import java.util.concurrent.TimeUnit;

import org.redisson.RedissonClient;
import org.redisson.core.RLock;

import com.derby.nuke.dlm.distributed.DistributedPermit;

/**
 * 
 * @author Passyt
 *
 */
public class RedissonLockPermit extends DistributedPermit {

	private final RedissonClient client;
	private final RLock lock;

	public RedissonLockPermit(RedissonClient client, String lockName) {
		this.client = client;
		this.lock = this.client.getLock(lockName);
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
