package org.afs.pakinglot.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.strategies.AvailableRateStrategy;
import org.afs.pakinglot.domain.strategies.MaxAvailableStrategy;
import org.afs.pakinglot.domain.strategies.SequentiallyStrategy;
import org.junit.jupiter.api.Test;

class ParkingManagerTest {

    private final ParkingManager parkingManager = new ParkingManager();

    @Test
    void should_return_ticket_when_parkCar_given_a_car() {
        Car car = new Car(CarPlateGenerator.generatePlate());
        Ticket ticket = parkingManager.parkCar(new SequentiallyStrategy(), car);
        assertNotNull(ticket);
    }

    @Test
    void should_throw_NoAvailablePositionException_when_parkCar_given_all_parking_lots_are_full() {
        ParkingManager.getParkingLots().forEach(parkingLot ->
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

}