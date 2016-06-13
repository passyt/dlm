package com.derby.nuke.dlm.distributed.redis;

import com.derby.nuke.dlm.IPermit;

public class RSemaphorePermitTest extends RedissonTestSupport {

	@Override
	protected IPermit getPermit() {
		return new RSemaphorePermit(redisson, "r.semaphore", 10);
	}

	@Override
	protected long getTaskCostTime() {
		return 200L;
	}

}
