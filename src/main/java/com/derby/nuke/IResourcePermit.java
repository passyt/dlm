package com.derby.nuke;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public interface IResourcePermit<R> {

	/**
	 * 
	 * @return ticket id
	 */
	String acquire(R resource) throws InterruptedException;

	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @return ticket id
	 */
	String tryAcquire(R resource, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * 
	 * @param resource
	 * @param ticketId
	 */
	void release(R resource, String ticketId);

}
