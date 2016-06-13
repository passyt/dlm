package com.derby.nuke.dlm.local;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

public class TokenBucketPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new TokenBucketPermit(20);
	}

	@Override
	protected long getTaskCostTime() {
		return 0L;
	}

}
