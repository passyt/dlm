package org.redisson.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.redisson.core.RLock;
import org.redisson.core.RMap;

public class RedissonTest extends RedissonTestSupport {

	@Test
	public void test() throws Exception {
		RMap<Object, Object> map = redisson.getMap("testmap");
		map.put("key1", "value1");

		RLock lock = redisson.getLock("testlock");
		List<Callable<Void>> tasks = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			final int index = i;
			tasks.add(() -> {
				try {
					lock.lock(3, TimeUnit.SECONDS);
					TimeUnit.MILLISECONDS.sleep(1000L);
					log.debug("{}", index);
					return null;
				} finally {
					lock.unlock();
				}
			});
		}

		ExecutorService service = Executors.newFixedThreadPool(10);
		service.invokeAll(tasks);
		service.shutdown();
	}

}
