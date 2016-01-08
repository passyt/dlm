package com.derby.nuke;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public interface IResourcePermit {

	/**
	 * 
	 * @return ticket id
	 */
	String acquire(Serializable resource) throws InterruptedException;

	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @return ticket id
	 */
	String tryAcquire(Serializable resource, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * 
	 * @param resource
	 * @param ticketId
	 */
	void release(Serializable resource, String ticketId);

}
