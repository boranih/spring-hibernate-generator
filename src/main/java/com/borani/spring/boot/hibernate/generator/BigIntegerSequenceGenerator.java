package com.borani.spring.boot.hibernate.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.hibernate.generator.Generator;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.Instant;
import java.util.EnumSet;

public class BigIntegerSequenceGenerator implements BeforeExecutionGenerator, Serializable {

    private static final long EPOCH = 1672531200000L; // Custom epoch (e.g., Jan 1, 2023)

    private static final long REGION_ID_BITS = 5L;
    private static final long MACHINE_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_REGION_ID = (1L << REGION_ID_BITS) - 1;
    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long REGION_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS + REGION_ID_BITS;

    private final long regionId;
    private final long machineId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public BigIntegerSequenceGenerator() {
        this(1, 1); // Default Region 1, Machine 1; You should load from config
    }

    public BigIntegerSequenceGenerator(long regionId, long machineId) {
        if (regionId > MAX_REGION_ID || regionId < 0) {
            throw new IllegalArgumentException(String.format("Region ID must be between 0 and %d", MAX_REGION_ID));
        }
        if (machineId > MAX_MACHINE_ID || machineId < 0) {
            throw new IllegalArgumentException(String.format("Machine ID must be between 0 and %d", MAX_MACHINE_ID));
        }
        this.regionId = regionId;
        this.machineId = machineId;
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        long currentTimestamp = getCurrentTimestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate id.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        long id = ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (regionId << REGION_ID_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;
        return BigInteger.valueOf(id);
    }

    @Override
    public boolean generatedOnExecution() {
        // generated BEFORE execution (in Java)
        return false;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }

    private long waitNextMillis(long currentMillis) {
        while (currentMillis == lastTimestamp) {
            currentMillis = getCurrentTimestamp();
        }
        return currentMillis;
    }

    private long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }
}
