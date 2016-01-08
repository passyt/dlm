package com.derby.nuke;

/**
 * 
 * @author Passyt
 *
 * @param <R>
 */
public interface IPermitBuilder<R> {

	IPermit createPermit(R resource);

}
