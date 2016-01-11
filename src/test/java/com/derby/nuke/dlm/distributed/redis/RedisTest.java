package com.derby.nuke.dlm.distributed.redis;

import org.junit.Test;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class RedisTest {

	@Test
	public void testName() throws Exception {
		RedisClient redisClient = RedisClient.create("redis://10.200.152.40:6379/0");
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> syncCommands = connection.sync();

//		syncCommands.set("key", "Hello, Redis!");
		System.out.println(syncCommands.get("key"));

		connection.close();
		redisClient.shutdown();
	}

}
