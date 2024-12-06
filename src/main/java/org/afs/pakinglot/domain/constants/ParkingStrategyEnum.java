package org.afs.pakinglot.domain.constants;

import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

public enum ParkingStrategyEnum {
    // 每种停车策略的枚举值，分别对应不同的停车策略的实现类
    FIRST_PARKING_LOT(new SequentiallyStrategy()),
    SMART_PARKING_LOT(new MaxAvailableStrategy()),
    MAX_AVAILABLE_RATE_PARKING_LOT(new AvailableRateStrategy());

    private final ParkingStrategy strategy;

    ParkingStrategyEnum(org.afs.pakinglot.domain.strategies.ParkingStrategy strategy) {
        this.strategy = strategy;
    }

    public ParkingStrategy getStrategy() {
        return strategy;
    }

}
