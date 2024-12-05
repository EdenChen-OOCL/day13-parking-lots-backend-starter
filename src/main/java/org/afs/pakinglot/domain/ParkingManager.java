package org.afs.pakinglot.domain;

import java.util.List;
import java.util.Optional;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

public class ParkingManager {
    private static final List<ParkingLot> parkingLots = List.of(
        new ParkingLot(1, "Plaza Park", 9),
        new ParkingLot(2, "City Mall Garage", 12),
        new ParkingLot(3, "Office Tower Parking", 9)
    );

    private static final ParkingBoy standardParkingBoy = new ParkingBoy(parkingLots, new SequentiallyStrategy());
    private static final ParkingBoy smartParkingBoy = new ParkingBoy(parkingLots, new MaxAvailableStrategy());
    private static final ParkingBoy superSmartParkingBoy = new ParkingBoy(parkingLots, new AvailableRateStrategy());

    public Ticket parkCar(ParkingStrategy parkingStrategy, Car car) {
        ParkingBoy selectedParkingBoy = selectParkingBoy(parkingStrategy);
        return selectedParkingBoy.park(car);
    }

    public Car fetchCar(Ticket ticket) {
        return standardParkingBoy.fetch(ticket);
    }

    private ParkingBoy selectParkingBoy(ParkingStrategy parkingStrategy) {
        if (parkingStrategy instanceof SequentiallyStrategy) {
            return standardParkingBoy;
        } else if (parkingStrategy instanceof MaxAvailableStrategy) {
            return smartParkingBoy;
        } else if (parkingStrategy instanceof AvailableRateStrategy) {
            return superSmartParkingBoy;
        } else {
            throw new IllegalArgumentException("Unknown parking strategy.");
        }
    }

    public static List<ParkingLot> getParkingLots() {
        return parkingLots;
    }
}