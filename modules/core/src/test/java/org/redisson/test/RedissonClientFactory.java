package org.redisson.test;

import java.io.IOException;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;

public class RedissonClientFactory {

	private static RedissonClientFactory INSTANCE = new RedissonClientFactory();

	private RedissonClientFactory() {
	}

	public static RedissonClientFactory getInstance() {
		return INSTANCE;
	}

	public RedissonClient getClient() {
		try {
			return Redisson.create(Config.fromJSON(RedissonClientFactory.class.getClassLoader().getResourceAsStream("redisson.json")));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
