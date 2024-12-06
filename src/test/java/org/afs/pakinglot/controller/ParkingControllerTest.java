package org.afs.pakinglot.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.IntStream;
import org.afs.pakinglot.domain.Car;
import org.afs.pakinglot.domain.ParkingLotDTO;
import org.afs.pakinglot.domain.ParkingManager;
import org.afs.pakinglot.domain.Ticket;
import org.afs.pakinglot.domain.controller.ParkingController;
import org.afs.pakinglot.domain.exception.InvalidLicensePlateException;
import org.afs.pakinglot.domain.exception.NoAvailablePositionException;
import org.afs.pakinglot.domain.exception.UnrecognizedTicketException;
import org.afs.pakinglot.domain.service.ParkingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
public class ParkingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ParkingService parkingService;

    @Autowired
    private JacksonTester<Car> json;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_display_parking_lot_status_correctly() throws Exception {
        List<ParkingLotDTO> status = List.of(
                new ParkingLotDTO(1, "Plaza Park", List.of("AB-1234", "", "")),
                new ParkingLotDTO(2, "City Mall Garage", List.of("", "", "")),
                new ParkingLotDTO(3, "Office Tower Parking", List.of("", "", ""))
        );
        when(parkingService.displayParkingStatus()).thenReturn(status);

        mockMvc.perform(get("/parking-lot/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void should_park_car_then_return_ticket() throws Exception {
        Ticket ticket = new Ticket("AB-1234", 1, 1);
        when(parkingService.parkCar(Mockito.any(Car.class))).thenReturn(ticket);

        mockMvc.perform(post("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"AB-1234\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("AB-1234"));
    }

    @Test
    void should_return_car_when_fetch_given_valid_ticket() throws Exception {
        Car car = new Car("AB-1234");
        String responseJSON = mockMvc.perform(post("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"AB-1234\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        when(parkingService.fetchCar(Mockito.any(Ticket.class))).thenReturn(car);

        mockMvc.perform(delete("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"AB-1234\",\"parkingLot\":1,\"position\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("AB-1234"));
    }

    @Test
    void should_throw_NoAvailablePositionException_when_all_parking_lot_is_full() throws Exception {
        when(parkingService.parkCar(Mockito.any(Car.class))).thenThrow(new NoAvailablePositionException());
        // fill all parking lots
        IntStream.range(0, 30).forEach(i -> {
            try {
                mockMvc.perform(post("/parking-car")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"plateNumber\":\"AB-1234\"}"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        mockMvc.perform(post("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"AB-1234\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("No available position."));
    }

    @Test
    void should_throw_UnrecognizedTicketException_when_ticket_is_invalid() throws Exception {
        when(parkingService.fetchCar(Mockito.any(Ticket.class))).thenThrow(new UnrecognizedTicketException());

        mockMvc.perform(delete("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"INVALID\",\"parkingLotId\":1,\"position\":1}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unrecognized parking ticket."));
    }

    @Test
    void should_throw_InvalidLicensePlateException_when_car_license_plate_is_empty() throws Exception {
        when(parkingService.parkCar(Mockito.any(Car.class))).thenThrow(new InvalidLicensePlateException("Invalid license plate"));

        mockMvc.perform(post("/parking-car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"plateNumber\":\"\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid license plate: "));
    }
}