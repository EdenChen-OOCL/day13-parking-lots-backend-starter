package org.afs.pakinglot.domain.service;

import java.util.Objects;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotDTO;
import org.afs.pakinglot.domain.ParkingManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.constants.ParkingStrategyEnum;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    private ParkingManager parkingManager;

    public ParkingService() {
        this.parkingManager = new ParkingManager();
    }

    public List<ParkingLotDTO> displayParkingStatus() {
        return parkingManager.displayParkingStatus();
    }

    public Ticket parkCar(ParkingStrategyEnum parkingStrategyEnum, Car car) {
        ParkingStrategy parkingStrategy = Objects.nonNull(parkingStrategyEnum)? parkingStrategyEnum.getStrategy(): new SequentiallyStrategy();
        return parkingManager.parkCar(parkingStrategy, car);
    }

    public Car fetchCar(Ticket ticket) {
        return parkingManager.fetchCar(ticket);
    }
}