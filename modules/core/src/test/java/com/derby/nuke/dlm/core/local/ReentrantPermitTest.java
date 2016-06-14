package com.derby.nuke.dlm.core.local;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.PermitTest;
import com.derby.nuke.dlm.core.local.ReentrantPermit;

public class ReentrantPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new ReentrantPermit();
	}
	
	@Override
	protected long getTaskCostTime() {
		return 0L;
	}

}
