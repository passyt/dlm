package com.derby.nuke.dlm.core;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derby.nuke.dlm.core.IPermit;
import com.google.common.collect.Lists;

public abstract class PermitTest {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected abstract IPermit getPermit();

	@Test
	public void test() throws Exception {
		IPermit permit = getPermit();
		AtomicLong al = new AtomicLong(0);

		List<Callable<Void>> tasks = Lists.newArrayList();
		for (int i = 0; i < getTaskSize(); i++) {
			final int index = i;
			tasks.add(() -> {
				try {
					permit.acquire();
					al.incrementAndGet();
					if (getTaskCostTime() > 0) {
						TimeUnit.MILLISECONDS.sleep(getTaskCostTime());
					}
					log.debug("Finished: {}", index);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				} finally {
					permit.release();
				}
			});
		}

		long now = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(getThreadPoolSize());
		executorService.invokeAll(tasks);
		executorService.shutdown();
		long cost = System.currentTimeMillis() - now;
		double value = al.get() * 1000 / cost;
		log.info(value + " tps = " + al.get() + "/" + cost + " ms");
	}

	protected int getThreadPoolSize() {
		return 10;
	}

	protected int getTaskSize() {
		return 50;
	}

	protected long getTaskCostTime() {
		return 50L;
	}
}
