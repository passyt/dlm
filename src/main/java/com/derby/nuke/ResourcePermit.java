package com.derby.nuke;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public class ResourcePermit<R> implements IResourcePermit<R> {

	private final IResourcePermitBuilder<R> permitBuilder;
	private final ConcurrentMap<R, IPermit> permits = new ConcurrentHashMap<>();

	public ResourcePermit(IResourcePermitBuilder<R> permitBuilder) {
		this.permitBuilder = permitBuilder;
	}

	@Override
	public String acquire(R resource) throws InterruptedException {
		return getPermit(resource).acquire();
	}

	@Override
	public String tryAcquire(R resource, long time, TimeUnit unit) throws InterruptedException {
		return getPermit(resource).tryAcquire(time, unit);
	}

	@Override
	public void release(R resource, String ticketId) {
		getPermit(resource).release(ticketId);
	}

	protected IPermit getPermit(R resource) {
		IPermit permit = permits.get(resource);
		if (permit != null) {
			return permit;
		}

		synchronized (permits) {
			permit = permits.get(resource);
			if (permit != null) {
				return permit;
			}

			permit = buildPermit(resource);
			permits.put(resource, permit);
		}
		return permit;
	}

	protected IPermit buildPermit(R resource) {
		return permitBuilder.createPermit(resource);
	}

}
