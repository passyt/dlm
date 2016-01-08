package com.derby.nuke.memory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.RedissonClient;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;

public class TrafficPermitTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test1() throws Exception {
		TrafficPermit lock = new TrafficPermit(5, 1, TimeUnit.SECONDS);
		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < 50; i++) {
			final int index = i;
			tasks.add(() -> {
				String ticket = null;
				try {
					// ticket = lock.tryLock(50L, TimeUnit.MILLISECONDS);
					ticket = lock.acquire();
					TimeUnit.MILLISECONDS.sleep(500L);
					System.out.println(LocalDateTime.now().toString() + ": " + index);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					lock.release(ticket);
				}
			});
		}

		long now = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		executorService.invokeAll(tasks);
		executorService.shutdown();
		System.out.println(System.currentTimeMillis() - now);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test2() throws Exception {
		Config config = new Config();
		config.useSingleServer().setAddress("10.200.152.40:6379");

		RedissonClient client = Redisson.create(config);

		com.derby.nuke.distributed.redis.TrafficPermit lock = new com.derby.nuke.distributed.redis.TrafficPermit("test.semaphore", client, 5, 1,
				TimeUnit.SECONDS);
		try {
			List<Callable<Void>> tasks = Lists.newArrayList();
			for (int i = 0; i < 500; i++) {
				final int index = i;
				tasks.add(() -> {
					String ticket = null;
					try {
						// ticket = lock.tryLock(50L, TimeUnit.MILLISECONDS);
						ticket = lock.acquire();
						TimeUnit.MILLISECONDS.sleep(500L);
						System.out.println(LocalDateTime.now().toString() + ": " + index);
						return null;
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					} finally {
						lock.release(ticket);
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

	@Test
	public void test3() throws Exception {
		RateLimiter limiter = RateLimiter.create(5);

		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < 50; i++) {
			final int index = i;
			tasks.add(() -> {
				try {
					limiter.acquire();
					TimeUnit.MILLISECONDS.sleep(1500L);
					System.out.println(LocalDateTime.now().toString() + ": " + index);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
			});
		}

		long now = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		executorService.invokeAll(tasks);
		executorService.shutdown();
		System.out.println(System.currentTimeMillis() - now);
	}

}
