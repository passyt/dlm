package com.derby.nuke;

public interface IPermitBuilder<R> {

	IPermit createPermit(R resource);

}
