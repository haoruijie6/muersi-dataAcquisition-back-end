package com.common.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成工具类
 *
 * @author haoruijie
 */
public class OrderNumberUtils {
    private static final FastDateFormat PATTERN = FastDateFormat.getInstance("yyyyMMddHHmmss");
    private static final AtomicInteger ATOMICINTEGER = new AtomicInteger(1);
    private static ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<StringBuilder>();

    /**
     * 长订单码生成策略
     *
     * @param lock uuid
     * @param
     * @return 20201116114351753590384993 类型
     */
    public static String  getTimeId(String lock) {
        if (Objects.isNull(threadLocal.get())) {
            lock = Objects.isNull(lock) ? UUID.randomUUID().toString() : lock;
            // 取系统当前时间作为订单号前半部分
            StringBuilder builder = new StringBuilder(PATTERN.format(Instant.now().toEpochMilli()));
            // HASH-CODE
            builder.append(Math.abs(lock.hashCode()));
            // 自增顺序
            builder.append(ATOMICINTEGER.getAndIncrement());
            threadLocal.set(builder);
        }
        return threadLocal.get().toString();
    }

    /**
     * 短码生成策略
     * 1307891882965
     *
     * @param lock
     * @return
     */
    public static String getD(String lock) {
        if (Objects.isNull(threadLocal.get())) {
            lock = Objects.isNull(lock) ? UUID.randomUUID().toString() : lock;
            // 线程局部随机数
            StringBuilder builder = new StringBuilder(ThreadLocalRandom.current().nextInt(0, 999));
            // HASH-CODE
            builder.append(Math.abs(lock.hashCode()));
            // 自增顺序
            builder.append(ATOMICINTEGER.getAndIncrement());
            threadLocal.set(builder);
        }
        return threadLocal.get().toString();
    }


}
