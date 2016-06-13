package com.derby.nuke.dlm.distributed.redis;

import org.junit.After;
import org.junit.Before;
import org.redisson.RedissonClient;
import org.redisson.test.RedissonClientFactory;

import com.derby.nuke.dlm.PermitTest;

public abstract class RedissonTestSupport extends PermitTest {

	protected RedissonClient redisson;

	@Before
	public void init() {
		redisson = RedissonClientFactory.getInstance().getClient();
	}

	@After
	public void destory() {
		redisson.shutdown();
	}

}
