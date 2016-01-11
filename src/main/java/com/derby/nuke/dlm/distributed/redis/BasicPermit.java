package com.derby.nuke.dlm.distributed.redis;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.dlm.distributed.DistributedPermit;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

/**
 * 
 * @author Passyt
 *
 */
public class BasicPermit extends DistributedPermit {

	private final RedisClient client;
	private final String lockName;
	private int expireInSeconds;

	public BasicPermit(RedisClient client, String lockName, int expireInSeconds) {
		this.client = client;
		this.lockName = lockName;
		this.expireInSeconds = expireInSeconds;
	}

	@Override
	public String acquire() throws InterruptedException {
		return tryAcquireWithEnd(-1);
	}

	@Override
	public String tryAcquire(long timeout, TimeUnit unit) throws InterruptedException {
		return tryAcquireWithEnd(System.currentTimeMillis() + unit.toMillis(timeout));
	}

	protected String tryAcquireWithEnd(long end) throws InterruptedException {
		try (StatefulRedisConnection<String, String> connection = client.connect()) {
			RedisCommands<String, String> commands = connection.sync();
			String ticket = UUID.randomUUID().toString();
			while (end < 0 || System.currentTimeMillis() < end) {
				if (commands.setnx(lockName, ticket)) {
					commands.expire(lockName, expireInSeconds);
					return ticket;
				}

				if (commands.ttl(lockName) == -1) {
					commands.expire(lockName, expireInSeconds);
				}

				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}

			return null;
		}
	}

	@Override
	public boolean release(String ticketId) {
		try (StatefulRedisConnection<String, String> connection = client.connect()) {
			RedisCommands<String, String> commands = connection.sync();
			while (true) {
				commands.watch(lockName);
				if (ticketId.equals(commands.get(lockName))) {
					commands.multi();
					commands.del(lockName);
					List<Object> results = commands.exec();
					if (results == null) {
						continue;
					}
					return true;
				}

				commands.unwatch();
				break;
			}

			return false;
		}
	}

	public void setExpireInSeconds(int expireInSeconds) {
		this.expireInSeconds = expireInSeconds;
	}

}
