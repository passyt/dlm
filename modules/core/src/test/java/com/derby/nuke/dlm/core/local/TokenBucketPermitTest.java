package com.derby.nuke.dlm.core.local;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.PermitTest;
import com.derby.nuke.dlm.core.local.TokenBucketPermit;

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
