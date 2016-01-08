package com.derby.nuke.memory;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.IPermit;
import com.derby.nuke.IResourcePermit;

/**
 * 
 * @author Passyt
 *
 */
public class BasicResourcePermit implements IResourcePermit {

	private static BasicResourcePermit INSTANCE = new BasicResourcePermit();
	private ConcurrentMap<Serializable, IPermit> permits = new ConcurrentHashMap<>();

	private BasicResourcePermit() {
	}

	@Override
	public String acquire(Serializable resource) throws InterruptedException {
		return getPermit(resource).acquire();
	}

	@Override
	public String tryAcquire(Serializable resource, long time, TimeUnit unit) throws InterruptedException {
		return getPermit(resource).tryAcquire(time, unit);
	}

	@Override
	public void release(Serializable resource, String ticketId) {
		getPermit(resource).release(ticketId);
	}

	protected IPermit getPermit(Serializable resource) {
		IPermit lock = permits.get(resource);
		if (lock != null) {
			return lock;
		}

		synchronized (permits) {
			lock = permits.get(resource);
			if (lock != null) {
				return lock;
			}

			lock = buildPermit(resource);
			permits.put(resource, lock);
		}
		return lock;
	}

	protected IPermit buildPermit(Serializable resource) {
		return new BasicPermit();
	}

	public static final BasicResourcePermit getInstance() {
		return INSTANCE;
	}

}
