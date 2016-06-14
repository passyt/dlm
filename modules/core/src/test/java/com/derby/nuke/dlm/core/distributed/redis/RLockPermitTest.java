package com.derby.nuke.dlm.core.distributed.redis;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.distributed.redis.RLockPermit;

public class RLockPermitTest extends RedissonTestSupport {

	@Override
	protected IPermit getPermit() {
		return new RLockPermit(redisson, "r.lock");
	}
	
	@Override
	protected int getTaskSize() {
		return 500;
	}
	
	@Override
	protected int getThreadPoolSize() {
		return 100;
	}
	
	@Override
	protected long getTaskCostTime() {
		return 0L;
	}

}
