package com.derby.nuke;

public interface IResourcePermitBuilder<R> {

	IPermit createPermit(R resource);

}
