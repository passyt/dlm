package com.derby.nuke.dlm.local;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

public class BasicPermitTest extends PermitTest{

	@Override
	protected IPermit getPermit() {
		return new BasicPermit();
	}

}
