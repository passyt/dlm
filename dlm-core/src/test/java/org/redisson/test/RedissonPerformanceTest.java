package org.redisson.test;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.redisson.RedissonClient;
import org.redisson.core.RAtomicLong;
import org.redisson.core.RLock;

/**
 * redis的分布式锁测试
 * 
 */
public class RedissonPerformanceTest extends RedissonTestSupport {

	private static int TOTAL_PRE_THREADS = 10;
	private static long SECONDS = 10;
	protected static boolean isSkipLock = true;

	@Test
	public void test() {
		final RLock lock = redisson.getLock("testLock");
		final CountDownLatch startLatch = new CountDownLatch(1);
		final CountDownLatch endLatch = new CountDownLatch(TOTAL_PRE_THREADS);
		Thread[] threads = new Thread[TOTAL_PRE_THREADS];
		setZero(redisson);

		for (int i = 0; i < TOTAL_PRE_THREADS; i++) {
			PerformanceThread runningThread = new PerformanceThread(startLatch, endLatch, lock, redisson);
			runningThread.start();
			threads[i] = runningThread;
		}

		// 开始执行
		startLatch.countDown();
		try {
			Thread.sleep(SECONDS * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < threads.length; i++) {
			PerformanceThread runningThread = (PerformanceThread) threads[i];
			runningThread.stopThrad();
		}

		try {
			endLatch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		printCount(redisson);
		redisson.shutdown();
	}

	public static class PerformanceThread extends Thread {
		private volatile boolean isRunning = true;
		private CountDownLatch startLatch;
		private CountDownLatch endLatch;
		private RLock lock;
		private RedissonClient redissonCli;

		private PerformanceThread(CountDownLatch startLatch, CountDownLatch endLatch, RLock lock, RedissonClient redissonCli) {
			this.startLatch = startLatch;
			this.endLatch = endLatch;
			this.lock = lock;
			this.redissonCli = redissonCli;
		}

		@Override
		public void run() {
			try {
				startLatch.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			while (isRunning) {
				try {
					if (!isSkipLock) {
						lock.lock();
					}
					testUpdate(redissonCli);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (!isSkipLock) {
							lock.unlock();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			endLatch.countDown();
		}

		public void stopThrad() {
			isRunning = false;
		}
	}

	/**
	 * 更新对应的记录
	 */
	public static void testUpdate(RedissonClient redissonCli) {
		RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
		long totalUsed = addLong.addAndGet(1);
	}

	/**
	 * 最后获得总数
	 * 
	 * @param redissonCli
	 */
	public static void printCount(RedissonClient redissonCli) {
		RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
		System.out.println("count tps is:" + (addLong.get() / SECONDS));
	}

	/**
	 * 初始化
	 * 
	 * @param redissonCli
	 */
	public static void setZero(RedissonClient redissonCli) {
		RAtomicLong addLong = redissonCli.getAtomicLong("test_performance");
		addLong.set(0);
	}
}
