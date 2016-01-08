package com.derby.nuke;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Blocking to access some resource with a provided {@link IPermit} created by
 * {@link #permitBuilder}. This can be easily to support multiple types of
 * permit such as {@link com.derby.nuke.memory.SemaphorePermit},
 * {@link com.derby.nuke.memory.TrafficPermit} to access a limited resource
 * 
 * @author Passyt
 * @see com.derby.nuke.memory.SemaphorePermit
 * @see com.derby.nuke.memory.TrafficPermit
 */
public class ResourcePermit<R> implements IResourcePermit<R> {

	/**
	 * a builder to create {@link IPermit}
	 */
	private final IPermitBuilder<R> permitBuilder;
	/**
	 * created permits in cache
	 */
	private final ConcurrentMap<R, IPermit> permits = new ConcurrentHashMap<>();

	/**
	 * 
	 * @param permitBuilder
	 */
	public ResourcePermit(IPermitBuilder<R> permitBuilder) {
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
