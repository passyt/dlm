package com.derby.nuke;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Passyt
 *
 */
public interface ILock {

	/**
	 * 
	 * @return ticket id
	 */
	String lock() throws InterruptedException;

	/**
	 * 
	 * @param timeout
	 * @param unit
	 * @return ticket id
	 */
	String tryLock(long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * 
	 */
	void unlock(String ticketId);

}
