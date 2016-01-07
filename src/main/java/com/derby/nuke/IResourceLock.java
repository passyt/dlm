package com.derby.nuke;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public interface IResourceLock {

	/**
	 * 
	 * @return ticket id
	 */
	String lock(Serializable resource) throws InterruptedException;

	/**
	 * 
	 * @param time
	 * @param unit
	 * @return ticket id
	 */
	String lock(Serializable resource, long time, TimeUnit unit) throws InterruptedException;

	/**
	 * 
	 */
	void unlock(Serializable resource, String ticketId);

}
