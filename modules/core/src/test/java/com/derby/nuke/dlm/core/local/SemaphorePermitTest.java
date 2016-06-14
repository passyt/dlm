package com.derby.nuke.dlm.core.local;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.PermitTest;
import com.derby.nuke.dlm.core.local.SemaphorePermit;

public class SemaphorePermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new SemaphorePermit(50);
	}
	
}
