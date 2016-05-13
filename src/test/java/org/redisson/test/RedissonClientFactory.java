package org.redisson.test;

import java.io.IOException;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;

public class RedissonClientFactory {

	private static RedissonClientFactory INSTANCE = new RedissonClientFactory();
	private RedissonClient client;

	private RedissonClientFactory() {
	}

	public static RedissonClientFactory getInstance() {
		return INSTANCE;
	}

	public RedissonClient getClient() {
		if (client != null) {
			return client;
		}

		synchronized (INSTANCE) {
			if (client != null) {
				return client;
			}

			try {
				this.client = Redisson.create(Config.fromJSON(RedissonClientFactory.class.getResourceAsStream("redisson.json")));
				return this.client;
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

}
