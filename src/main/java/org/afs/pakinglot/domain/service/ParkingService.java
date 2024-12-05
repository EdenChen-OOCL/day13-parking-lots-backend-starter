package org.afs.pakinglot.domain.service;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotDTO;
import org.afs.pakinglot.domain.ParkingManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingService {

    private final ParkingManager parkingManager;

    public ParkingService() {
        this.parkingManager = new ParkingManager();
    }

    public List<ParkingLotDTO> displayParkingStatus() {
        return parkingManager.displayParkingStatus();
    }

    public Ticket parkCar(Car car) {
        return parkingManager.parkCar(new SequentiallyStrategy(), car);
    }

    public Car fetchCar(Ticket ticket) {
        return parkingManager.fetchCar(ticket);
    }
}