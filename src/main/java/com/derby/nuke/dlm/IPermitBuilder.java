package com.derby.nuke.dlm;

/**
 * 
 * @author Passyt
 *
 * @param <R>
 */
public interface IPermitBuilder<R> {

	IPermit createPermit(R resource);

}
