package com.derby.nuke.dlm.local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TrafficPermitTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() throws Exception {
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

}
