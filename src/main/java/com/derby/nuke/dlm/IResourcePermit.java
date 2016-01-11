package com.derby.nuke.dlm;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public interface IResourcePermit<R> {

	/**
	 * Acquires the permit on a given resource, return a ticket id to call in
	 * {@link #release} method to release the permit
	 * 
	 * @return ticket id
	 */
	String acquire(R resource) throws InterruptedException;

	/**
	 * Acquires the permit if it is free within the given waiting time on a
	 * provided resource, return a ticket id to call in {@link #release} method
	 * to release the permit.
	 * 
	 * <br/>
	 * <b>If it's timeout by the given time, an InterruptedException will be
	 * throw out</b>
	 * 
	 * @param resource
	 * @param timeout
	 *            a time for timeout
	 * @param unit
	 *            time unit for timeout
	 * @return ticket id
	 */
	String tryAcquire(R resource, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Release the permit with a given tickedId on given resource
	 * 
	 * @param resource
	 * @param ticketId
	 */
	void release(R resource, String ticketId);

}
