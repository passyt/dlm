package com.derby.nuke.dlm;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.MultiPermit;
import com.derby.nuke.dlm.local.SemaphorePermit;
import com.derby.nuke.dlm.local.TokenBucketPermit;

public class MultiPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new MultiPermit(new TokenBucketPermit(200), new SemaphorePermit(1));
	}

}
