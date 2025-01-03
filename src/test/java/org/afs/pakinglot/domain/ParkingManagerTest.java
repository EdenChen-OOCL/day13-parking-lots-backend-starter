package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;
import org.afs.pakinglot.domain.exception.InvalidLicensePlateException;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParkingManagerTest {

    private ParkingManager parkingManager = new ParkingManager();

    @BeforeEach
    void init() {
        parkingManager = new ParkingManager();
    }

    @Test
    void should_return_ticket_when_parkCar_given_a_car() {
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingManager.parkCar(new SequentiallyStrategy(), car);
        assertNotNull(ticket);
    }

    @Test
    void should_throw_NoAvailablePositionException_when_parkCar_given_all_parking_lots_are_full() {
        parkingManager.getParkingLots().forEach(parkingLot ->
                IntStream.range(0, parkingLot.getCapacity()).forEach(i ->
                        parkingManager.parkCar(new SequentiallyStrategy(), new Car(CarPlateGenerator.generatePlate()))
                )
        );
        Car car = new Car(CarPlateGenerator.generatePlate());
        assertThrows(NoAvailablePositionException.class, () -> parkingManager.parkCar(new SequentiallyStrategy(), car));
    }

    @Test
    void should_park_car_correctly_when_parkCar_with_each_strategy() {
        Car car1 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket1 = parkingManager.parkCar(new SequentiallyStrategy(), car1);
        assertNotNull(ticket1);

        Car car2 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket2 = parkingManager.parkCar(new MaxAvailableStrategy(), car2);
        assertNotNull(ticket2);

        Car car3 = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket3 = parkingManager.parkCar(new AvailableRateStrategy(), car3);
        assertNotNull(ticket3);
    }

    @Test
    void should_throw_InvalidLicensePlateException_when_parkCar_given_empty_license_plate() {
        Car car = new Car("");
        assertThrows(InvalidLicensePlateException.class, () -> {
            parkingManager.parkCar(new SequentiallyStrategy(), car);
        });
    }

    @Test
    void should_throw_InvalidLicensePlateException_when_parkCar_given_invalid_license_plate() {
        Car car = new Car("123-ABCD");
        assertThrows(InvalidLicensePlateException.class, () -> {
            parkingManager.parkCar(new SequentiallyStrategy(), car);
        });
    }

    @Test
    void should_throw_UnrecognizedTicketException_when_fetchCar_given_invalid_ticket() {
        Ticket invalidTicket = new Ticket(CarPlateGenerator.generatePlate(), 1, 1);
        assertThrows(UnrecognizedTicketException.class, () -> parkingManager.fetchCar(invalidTicket));
    }

    @Test
    void should_return_car_when_fetchCar_given_valid_ticket() {
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingManager.parkCar(new SequentiallyStrategy(), car);
        Car fetchedCar = parkingManager.fetchCar(ticket);
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_return_parking_lot_status_when_displayParkingStatus_called() {
        Car car = new Car("FD-6346");
        parkingManager.parkCar(new SequentiallyStrategy(), car);
        String expectJson = "[{\"parkingLotId\": 1, \"parkingLotName\": \"Plaza Park\", \"position\": [, FD-6346, , , , , , , ]}, {\"parkingLotId\": 2, \"parkingLotName\": \"City Mall Garage\", \"position\": [, , , , , , , , , , , ]}, {\"parkingLotId\": 3, \"parkingLotName\": \"Office Tower Parking\", \"position\": [, , , , , , , , ]}]";
        List<ParkingLotDTO> status = parkingManager.displayParkingStatus();
        assertNotNull(status);
        assertEquals(3, status.size());
        assertEquals(9, status.get(0).getPosition().size());
        assertEquals(12, status.get(1).getPosition().size());
        assertEquals(9, status.get(2).getPosition().size());
        assertEquals(expectJson, status.toString());
    }

}