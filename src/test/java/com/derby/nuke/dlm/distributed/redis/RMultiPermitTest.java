package com.derby.nuke.dlm.distributed.redis;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.MultiPermit;

public class RMultiPermitTest extends RedissonTestSupport {

	@Override
	protected IPermit getPermit() {
		RLockPermit permit1 = new RLockPermit(redisson, "1");
		RLockPermit permit2 = new RLockPermit(redisson, "2");
		return new MultiPermit(permit1, permit2);
	}

}
