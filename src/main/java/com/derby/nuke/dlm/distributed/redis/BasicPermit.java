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
public class BasicPermit extends DistributedPermit {

	private final RedissonClient client;
	private final RLock lock;

	public BasicPermit(RedissonClient client, String lockName) {
		this.client = client;
		this.lock = this.client.getLock(lockName);
	}

	@Override
	public String acquire() throws InterruptedException {
		lock.lock();
		return null;
	}

	@Override
	public String tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		lock.lock(timeout, unit);
		return null;
	}

	@Override
	public boolean release(String ticketId) {
		lock.unlock();
		return true;
	}

}
