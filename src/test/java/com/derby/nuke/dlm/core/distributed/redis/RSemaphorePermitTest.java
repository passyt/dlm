package com.derby.nuke.dlm.core.distributed.redis;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.distributed.redis.RSemaphorePermit;

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
