package com.wiserz.pbibi.util;

import android.os.AsyncTask;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jackie on 2017/8/26 13:46.
 * QQ : 971060378
 * Used as : 线程池类
 */
public final class GBExecutionPool {

    private static volatile Executor executor;

    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 1000;
    private static final int DEFAULT_KEEP_ALIVE = 1;

    private static final Object LOCK = new Object();

    private static final BlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue<>(10);

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {

        private final AtomicInteger counter = new AtomicInteger(0);

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "#" + counter.incrementAndGet());
        }
    };

    public static Executor getExecutor() {
        synchronized (LOCK) {
            if (GBExecutionPool.executor == null) {
                Executor executor = getAsyncTaskExecutor();
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE,
                            DEFAULT_MAXIMUM_POOL_SIZE, DEFAULT_KEEP_ALIVE,
                            TimeUnit.SECONDS, DEFAULT_WORK_QUEUE,
                            DEFAULT_THREAD_FACTORY,
                            new ThreadPoolExecutor.DiscardOldestPolicy());
                }
                GBExecutionPool.executor = executor;
            }
        }
        return GBExecutionPool.executor;
    }

    public static void setExecutor(Executor executor) {
        notNull(executor, "executor");
        synchronized (LOCK) {
            GBExecutionPool.executor = executor;
        }
    }

    private static Executor getAsyncTaskExecutor() {
        Field executorField = null;
        try {
            executorField = AsyncTask.class.getField("THREAD_POOL_EXECUTOR");
        } catch (NoSuchFieldException e) {
            return null;
        }

        if (executorField == null) {
            return null;
        }

        Object executorObject = null;
        try {
            executorObject = executorField.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }

        if (executorObject == null) {
            return null;
        }

        if (!(executorObject instanceof Executor)) {
            return null;
        }

        return (Executor) executorObject;
    }

    private static void notNull(Object arg, String name) {
        if (arg == null) {
            throw new NullPointerException("Argument '" + name + "' cannot be null");
        }
    }
}
