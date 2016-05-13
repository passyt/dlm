package com.derby.nuke.dlm.distributed.redis;

import org.junit.After;
import org.junit.Before;
import org.redisson.RedissonClient;
import org.redisson.test.RedissonClientFactory;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;

public class BasicPermitTest extends PermitTest {

	protected RedissonClient redisson;

	@Before
	public void init() {
		redisson = RedissonClientFactory.getInstance().getClient();
	}

	@After
	public void destory() {
		redisson.shutdown();
	}

	@Override
	protected IPermit getPermit() {
		return new BasicPermit(redisson, "lock.test");
	}

}
