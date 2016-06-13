package com.derby.nuke.dlm.local;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

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