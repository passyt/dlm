package com.derby.nuke.dlm.core.distributed.redis;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.MultiPermit;
import com.derby.nuke.dlm.core.distributed.redis.RLockPermit;

public class RMultiPermitTest extends RedissonTestSupport {

	@Override
	protected IPermit getPermit() {
		RLockPermit permit1 = new RLockPermit(redisson, "1");
		RLockPermit permit2 = new RLockPermit(redisson, "2");
		return new MultiPermit(permit1, permit2);
	}

}
