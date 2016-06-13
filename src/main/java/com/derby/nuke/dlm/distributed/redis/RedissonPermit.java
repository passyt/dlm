package com.derby.nuke.dlm.distributed.redis;

import org.redisson.RedissonClient;

import com.derby.nuke.dlm.distributed.DistributedPermit;

public abstract class RedissonPermit extends DistributedPermit {

	protected final RedissonClient client;

	public RedissonPermit(RedissonClient client) {
		this.client = client;
	}

	public RedissonClient getClient() {
		return client;
	}

}
