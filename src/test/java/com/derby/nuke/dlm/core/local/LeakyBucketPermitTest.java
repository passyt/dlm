package com.derby.nuke.dlm.core.local;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.PermitTest;
import com.derby.nuke.dlm.core.local.LeakyBucketPermit;

public class LeakyBucketPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new LeakyBucketPermit(50);
	}
	
	@Override
	protected long getTaskCostTime() {
		return 0L;
	}
	
}