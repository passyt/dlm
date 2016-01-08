package com.derby.nuke.memory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TrafficLockTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test1() throws Exception {
		TrafficLock lock = new TrafficLock(5, 1, TimeUnit.SECONDS);
		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < 50; i++) {
			final int index = i;
			tasks.add(() -> {
				String ticket = null;
				try {
					ticket = lock.tryLock(50L, TimeUnit.MILLISECONDS);
//					ticket = lock.lock();
					TimeUnit.MILLISECONDS.sleep(500L);
					System.out.println(new Date().toString() + ": " + index);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					lock.unlock(ticket);
				}
			});
		}

		 ExecutorService executorService = Executors.newFixedThreadPool(100);
		 executorService.invokeAll(tasks);
		 executorService.shutdown();
	}

}
