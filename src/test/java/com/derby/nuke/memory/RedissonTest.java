package com.derby.nuke.memory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;
import org.redisson.core.RSemaphore;

import com.google.common.collect.Lists;

public class RedissonTest {

	public static void main(String[] args) throws Exception {
		Config config = new Config();
		config.useSingleServer().setAddress("10.200.152.40:6379");

		RedissonClient client = Redisson.create(config);
		try {
			RSemaphore semaphore = client.getSemaphore("semaphore.test");
			if(semaphore.isExists()){
				semaphore.setPermits(2);
			}

			List<Callable<Void>> tasks = Lists.newArrayList();
			for (int i = 0; i < 50; i++) {
				int index = i;
				tasks.add(() -> {
					semaphore.acquire();
					try {
						TimeUnit.MILLISECONDS.sleep(500L);
						System.out.println(LocalDateTime.now().toString() + ": " + index);
						return null;
					} finally {
						semaphore.release();
					}
				});
			}

			long now = System.currentTimeMillis();
			ExecutorService executorService = Executors.newFixedThreadPool(100);
			executorService.invokeAll(tasks);
			executorService.shutdown();
			System.out.println(System.currentTimeMillis() - now);
		} finally {
			client.shutdown();
		}
	}

}
