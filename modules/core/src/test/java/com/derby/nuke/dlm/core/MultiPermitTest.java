package com.derby.nuke.dlm.core;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.MultiPermit;
import com.derby.nuke.dlm.core.local.SemaphorePermit;
import com.derby.nuke.dlm.core.local.TokenBucketPermit;

public class MultiPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new MultiPermit(new TokenBucketPermit(200), new SemaphorePermit(1));
	}

}
