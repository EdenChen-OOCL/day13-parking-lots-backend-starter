package org.afs.pakinglot.domain.controller;

import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotDTO;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.service.ParkingService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @GetMapping("/parking-lot/status")
    public ResponseEntity<List<ParkingLotDTO>> displayParkingStatus() {
        List<ParkingLotDTO> status = parkingService.displayParkingStatus();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/parking-car")
    public ResponseEntity<Ticket> parkCar(@RequestBody Car car) {
        Ticket ticket = parkingService.parkCar(car);
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/parking-car")
    public ResponseEntity<Car> fetchCar(@RequestBody Ticket ticket) {
        Car car = parkingService.fetchCar(ticket);
        return ResponseEntity.ok(car);
    }
}