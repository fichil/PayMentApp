package com.cykj.utils;


/**
 * @Author: fichil
 * @Date: 2024-10-12 11:02
 * @Description: 雪花ID生成器
 */
public class SnowflakeIdGenerator {

    // 初始时间戳 (这个可以自定义)
    private final static long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00

    // 每部分占用的位数
    private final static long SEQUENCE_BITS = 12; // 序列号占用 12 位
    private final static long MACHINE_BITS = 5;   // 机器 ID 占用 5 位
    private final static long DATACENTER_BITS = 5; // 数据中心占用 5 位

    // 每部分的最大值
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BITS);  // 最大支持 32 个数据中心
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BITS);        // 最大支持 32 台机器
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);          // 每毫秒内最多生成 4096 个 ID

    // 每部分向左的位移
    private final static long MACHINE_SHIFT = SEQUENCE_BITS;                       // 机器 ID 左移 12 位
    private final static long DATACENTER_SHIFT = SEQUENCE_BITS + MACHINE_BITS;     // 数据中心 ID 左移 17 位
    private final static long TIMESTAMP_SHIFT = DATACENTER_SHIFT + DATACENTER_BITS; // 时间戳左移 22 位

    private long datacenterId;  // 数据中心 ID
    private long machineId;     // 机器 ID
    private long sequence = 0L; // 序列号
    private long lastTimestamp = -1L; // 上一次生成 ID 的时间戳

    public SnowflakeIdGenerator(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("DataCenter ID can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Machine ID can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    // 生成下一个 ID（线程安全）
    public synchronized long nextId() {
        long currentTimestamp = getCurrentTimestamp();

        // 如果当前时间小于上一次生成 ID 的时间，说明系统时钟回退了
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        // 如果在同一毫秒内
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE; // 序列号加一，取模4096
            // 如果序列号溢出，等待下一毫秒
            if (sequence == 0L) {
                currentTimestamp = getNextMillisecond();
            }
        } else {
            // 如果是新的毫秒，序列号置为0
            sequence = 0L;
        }

        // 更新最后生成 ID 的时间戳
        lastTimestamp = currentTimestamp;

        // 移位并通过或运算拼到一起，组成 64 位的 ID
        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) // 时间戳部分
                | (datacenterId << DATACENTER_SHIFT)                      // 数据中心 ID 部分
                | (machineId << MACHINE_SHIFT)                            // 机器 ID 部分
                | sequence;                                               // 序列号部分
    }

    // 获取当前的时间戳（毫秒）
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // 等待下一毫秒
    private long getNextMillisecond() {
        long mill = getCurrentTimestamp();
        while (mill <= lastTimestamp) {
            mill = getCurrentTimestamp();
        }
        return mill;
    }
}

