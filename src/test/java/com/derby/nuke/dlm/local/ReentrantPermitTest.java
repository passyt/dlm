package com.derby.nuke.dlm.local;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

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
