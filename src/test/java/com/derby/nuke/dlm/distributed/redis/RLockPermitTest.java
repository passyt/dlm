package com.derby.nuke.dlm.distributed.redis;

import com.derby.nuke.dlm.IPermit;

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
