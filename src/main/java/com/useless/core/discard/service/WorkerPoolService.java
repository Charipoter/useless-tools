package com.useless.core.discard.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorkerPoolService {

    private final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2, 3, 10,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    public void process(Runnable task) {
        pool.execute(task);
    }

    public void close() {
        pool.shutdownNow();
    }

}
