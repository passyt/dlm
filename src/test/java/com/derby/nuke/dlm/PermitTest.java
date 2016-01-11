package com.derby.nuke.dlm;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

public abstract class PermitTest {

	protected abstract IPermit getPermit();

	@Test
	public void test() throws Exception {
		IPermit permit = getPermit();

		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < 50; i++) {
			final int index = i;
			tasks.add(() -> {
				String ticket = null;
				try {
					ticket = permit.acquire();
					TimeUnit.MILLISECONDS.sleep(500L);
					System.out.println(LocalDateTime.now().toString() + ": " + index);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					permit.release(ticket);
				}
			});
		}

		long now = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		executorService.invokeAll(tasks);
		executorService.shutdown();
		System.out.println(System.currentTimeMillis() - now);
	}

}