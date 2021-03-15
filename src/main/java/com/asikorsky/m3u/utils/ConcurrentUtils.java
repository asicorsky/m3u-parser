package com.asikorsky.m3u.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public interface ConcurrentUtils {

	public static final int MAX_AWAITING_TIME = 60 * 60 * 1000; // 1 hours in milliseconds

	static <T> List<T> awaitAndMap(List<? extends Future<T>> futures) {

		awaitAll(futures);
		return futures.stream().map(future -> {
			try {
				return future.get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	static void awaitAll(List<? extends Future<?>> futures) {

		try {
			long initiatedAt = System.currentTimeMillis();
			while (!isFuturesDone(futures)) {
				long current = System.currentTimeMillis();
				if (current - initiatedAt < MAX_AWAITING_TIME) {
					TimeUnit.MILLISECONDS.sleep(100);
				} else {
					throw new RuntimeException("Process take too many time...");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static boolean isFuturesDone(Collection<? extends Future<?>> futures) {

		return futures.stream().allMatch(future -> future.isDone() || future.isCancelled());
	}
}
