package com.derby.nuke.dlm.distributed.redis;

import org.junit.After;
import org.junit.Before;

import com.derby.nuke.dlm.IPermit;
import com.derby.nuke.dlm.PermitTest;
import com.lambdaworks.redis.RedisClient;

public class BasicPermitTest extends PermitTest {

	private RedisClient redisClient;

	@Before
	public void init() {
		redisClient = RedisClient.create("redis://10.200.152.40:6379/0");
	}

	@After
	public void destory() {
		this.redisClient.shutdown();
	}

	@Override
	protected IPermit getPermit() {
		return new BasicPermit(redisClient, "lock.test", 5);
	}

}
