package com.derby.nuke.memory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TransactionLockTest {

	@Test
	public void test1() throws Exception {
		TransactionLock lock = new TransactionLock(1, 1, TimeUnit.SECONDS);
		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < 50; i++) {
			final int index = i;
			tasks.add(() -> {
				String ticket = lock.lock();
				try {
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

//		ExecutorService executorService = Executors.newFixedThreadPool(1);
//		executorService.invokeAll(tasks);
//		executorService.shutdown();
		for(Callable<Void> task : tasks){
			task.call();
		}
	}

}
