package com.derby.nuke.dlm.local;

import java.util.concurrent.TimeUnit;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

public class TrafficPermitTest extends PermitTest {

	@Override
	protected IPermit getPermit() {
		return new TrafficPermit(5, 1, TimeUnit.SECONDS);
	}

}
