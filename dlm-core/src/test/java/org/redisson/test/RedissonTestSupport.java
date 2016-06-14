package org.redisson.test;

import org.junit.After;
import org.junit.Before;
import org.redisson.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedissonTestSupport {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected RedissonClient redisson;

	@Before
	public void init() {
		redisson = RedissonClientFactory.getInstance().getClient();
	}

	@After
	public void destory() {
		if(redisson != null){
			redisson.shutdown();
		}
	}

}
