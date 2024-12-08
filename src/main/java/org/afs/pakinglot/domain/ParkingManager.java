package org.afs.pakinglot.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.afs.pakinglot.domain.exception.InvalidLicensePlateException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.ParkingStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;

public class ParkingManager {
    private final List<ParkingLot> parkingLots = List.of(
        new ParkingLot(1, "Plaza Park", 9),
        new ParkingLot(2, "City Mall Garage", 12),
        new ParkingLot(3, "Office Tower Parking", 9)
    );

    private final ParkingBoy standardParkingBoy = new ParkingBoy(parkingLots, new SequentiallyStrategy());
    private final ParkingBoy smartParkingBoy = new ParkingBoy(parkingLots, new MaxAvailableStrategy());
    private final ParkingBoy superSmartParkingBoy = new ParkingBoy(parkingLots, new AvailableRateStrategy());

    private static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^[A-Z]{2}-\\d{4}$");

    public Ticket parkCar(ParkingStrategy parkingStrategy, Car car) {
        validateLicensePlate(car.plateNumber());
        validatePlateNumberUniqueness(car.plateNumber());
        ParkingBoy selectedParkingBoy = selectParkingBoy(parkingStrategy);
        return selectedParkingBoy.park(car);
    }

    private void validatePlateNumberUniqueness(String s) {
        if (parkingLots.stream().anyMatch(parkingLot -> parkingLot.getTickets().stream()
                .anyMatch(ticket -> ticket.plateNumber().equals(s)))) {
            throw new InvalidLicensePlateException("Duplicated license plate: " + s);
        }
    }

    public Car fetchCar(Ticket ticket) {
        return standardParkingBoy.fetch(ticket);
    }

    public List<ParkingLotDTO> displayParkingStatus() {
        return parkingLots.stream()
                .map(parkingLot -> new ParkingLotDTO(
                        parkingLot.getId(),
                        parkingLot.getName(),
                        parkingLot.getCapacity(),
                        IntStream.range(0, parkingLot.getCapacity())
                                .mapToObj(i -> parkingLot.getTickets().stream()
                                        .filter(ticket -> ticket.position() == i)
                                        .map(Ticket::plateNumber)
                                        .findFirst()
                                        .orElse("")
                                )
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    private static void validateLicensePlate(String plateNumber) {
        if (plateNumber == null || !LICENSE_PLATE_PATTERN.matcher(plateNumber).matches()) {
            throw new InvalidLicensePlateException("Invalid license plate: " + plateNumber);
        }
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

    public List<ParkingLot> getParkingLots() {
        return parkingLots;
    }


}