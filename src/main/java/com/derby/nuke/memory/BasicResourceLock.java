package com.derby.nuke.memory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.ILock;
import com.derby.nuke.IResourceLock;

/**
 * 
 * @author Passyt
 *
 */
public class BasicResourceLock implements IResourceLock {

	private static BasicResourceLock INSTANCE = new BasicResourceLock();
	private ConcurrentMap<Serializable, ILock> locks = new ConcurrentHashMap<>();

	private BasicResourceLock() {
	}

	@Override
	public String lock(Serializable resource) throws InterruptedException {
		return getLock(resource).lock();
	}

	@Override
	public String lock(Serializable resource, long time, TimeUnit unit) throws InterruptedException {
		return getLock(resource).lock(time, unit);
	}

	@Override
	public void unlock(Serializable resource, String ticketId) {
		getLock(resource).unlock(ticketId);
	}

	protected ILock getLock(Serializable resource) {
		ILock lock = locks.get(resource);
		if (lock != null) {
			return lock;
		}

		synchronized (locks) {
			lock = locks.get(resource);
			if (lock != null) {
				return lock;
			}

			lock = buildLock(resource);
			locks.put(resource, lock);
		}
		return lock;
	}

	protected ILock buildLock(Serializable resource) {
		return new BasicLock();
	}

	public static final BasicResourceLock getInstance() {
		return INSTANCE;
	}

}
