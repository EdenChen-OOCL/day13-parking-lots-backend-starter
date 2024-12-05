Background:
As a Parking Manager, I am responsible for managing three parking lots:
● The Plaza Park (9 parking capacity)
● City Mall Garage (12 parking capacity)
● Office Tower Parking (9 parking capacity)
I have employed three Parking Boys to help manage these parking lots, each utilizing a specific parking strategy:

1. Standard parking strategy
2. Smart parking strategy
3. Super Smart parking strategy

To optimize management and streamline operations, I need an application that assists me in visualizing and efficiently managing
the car parking and retrieval process, while also keeping track of the current usage of each parking lot.

Invoke the following classes:
ParkingManager -> ParkingBoy -> ParkingLot

第一次迭代：
Bussiness Requirements
1. Car Parking and Fetching
   ○ Enable the input of a user’s license plate number to perform car parking or fetching.
   ○ During parking, the license plate number should be correctly assigned to an available parking position and reflected in the
   parking lot’s status display.
   ○ During retrieval, the system should accurately release the corresponding parking slot and update the parking lot’s status.
2. Parking Boy Assignment
   ○ Support the selection of a specific Parking Boy for parking or fetching tasks. The Parking Boy will complete the operation
   based on their designated parking strategy.

Tasking:
1. Create a ParkingManager class
   1) static variables:
      1. List ParkingLot(PlazaPark, CityMallGarage, OfficeTowerParking) - each with a parking capacity
      2. Three ParkingBoy(StandardParkingBoy, SmartParkingBoy, SuperSmartParkingBoy) - each with a parking strategy
   2) member variables:
      1. none
   3) constructor:
      1. none
   4) method:
      1. parkCar(ParkingStrategy parkingStrategy, Car car): Ticket - select the ParkingBoy by the input "parkingStrategy" 
         which maybe "SequentiallyStrategy", "AvailableRateStrategy", "SuperSmartParkingStrategy", then park a car in the parking lot, and return a ticket.
      2. fetchCar(Ticket ticket): Car - fetch a car from the parking lot by the input ticket. If the ticket is invalid, throw an "UnrecognizedTicketException".
      
2. Create a ParkingManagerTest
   1) Requirement: Junit 5, method name should be "should_return_xxx_when_calledMethodName_given_xxx"
   2) Test case:
      1. should park a car then return a ticket
      2. should throw a "NoAvailablePositionException" exception when all parking lot is full.
      3. should throw a "UnrecognizedTicketException" exception when the ticket is invalid.
      4. should return a car when fetch given the ticket is valid.
      5. each strategy should have a single test and park a car in the parking lot correctly.

3. generate Code strictly following the tasking. keep the code clean and simple, no code smell. Use Stream API to simplify the code.

第二次迭代：
Bussiness Requirements:
1. License Plate Validation
   ○ License plates must follow the format standard: two letters + four digits (e.g., “AB-1234”).
   ○ The system must reject empty or invalid license plate entries.

Tasking:
1. create a sub class of RunTimeException called "InvalidLicensePlateException"
2. add validate logic in parkCar method of the ParkingManager class
   4) method:
      1. parkCar(ParkingStrategy parkingStrategy, Car car): Ticket - validate the car's license plate(String type), if it is empty or invalid, throw an "InvalidLicensePlateException".

3. add test method in ParkingManagerTest
   1) Requirement: Junit 5, method name should be "should_return_xxx_when_calledMethodName_given_xxx"
   2) Test case:
      1. should throw an "InvalidLicensePlateException" exception when the car's license plate is empty.
      2. should throw an "InvalidLicensePlateException" exception when the car's license plate is invalid.

3. generate Code strictly following the tasking. keep the code clean and simple, no code smell. Use Stream API to simplify the code.

第三次迭代：
Bussiness Requirements:
1. Parking Lot Status Display
   ○ The system should clearly display the current status of all three parking lots, showing the license plate of the car parked
   in each parking position to provide a real-time view of parking lot usage.

Tasking:
1. create a class ParkingLot DTO. Its variable is "parkingLotId", "parkingLotName", "position" - a ArrayList of parking position, each position is a string. 
   empty is "", and "AB-1234" is the platNumber of car , which represent that the car is using the position.
   josn is "{"parkingLotId": 1,"parkingLotName": "The Plaza Park", "position": ["AB-1234", "", ""] }"
2. add a displayParkingStatus method of the ParkingManager class
   4) method:
      1. displayParkingStatus(): List<ParkingLotDTO> - return the parking lot status display. The return value is a list of ParkingLot DTO. 
         json is like "[{"parkingLotId": 1,"parkingLotName": "The Plaza Park", "position": ["AB-1234", "", ""] }, {"parkingLotId": 2,"parkingLotName": "City Mall Garage", "position": ["", "", ""] }, {"parkingLotId": 3,"parkingLotName": "Office Tower Parking", "position": ["", "", ""] }]"
         
3. add test method in ParkingManagerTest
   1) Requirement: Junit 5, method name should be "should_return_xxx_when_calledMethodName_given_xxx"
   2) Test case:
      1. should display the parking lot status correctly.

3. generate Code strictly following the tasking. keep the code clean and simple, no code smell. Use Stream API to simplify the code.

第四次迭代：
Bussiness Requirements:
1. Create Controller and Service Layer

Tasking:
1. Create a ParkingController class to handle the HTTP requests and responses, using @RestController and @RequestMapping annotations. The basic url is "/".
    1) member variables:
       1. ParkingService parkingService
    2) constructor:
       1. ParkingController(ParkingService parkingService)
    3) method:
       1. @GetMapping("/parking-lot/status") displayParkingStatus(): ResponseEntity<List<ParkingLot>> - return the parking lot status display. The return value is a list of ParkingLot DTO. 
            json is like "[{"parkingLotId": 1,"parkingLotName": "The Plaza Park", "position": ["AB-1234", "", ""] }, {"parkingLotId": 2,"parkingLotName": "City Mall Garage", "position": ["", "", ""] }, {"parkingLotId": 3,"parkingLotName": "Office Tower Parking", "position": ["", "", ""] }]"
       2. @PostMapping("/parking-car") parkCar(@RequestBody Car car): ResponseEntity<Ticket> - park a car in the parking lot, and return a ticket.
       3. @DeleteMapping("/parking-car") fetchCar(@RequestBody Ticket ticket): ResponseEntity<Car> - fetch a car from the parking lot by the input ticket.
2. Create a ParkingService class to handle the business logic and interact with the ParkingManager class.
   1) member variables:
      1. ParkingManager parkingManager
   2) constructor:
        1. ParkingService() - assign the ParkingManager instance to the parkingManager variable.
   3) method:
     1. displayParkingStatus(): List<ParkingLotDTO> - invoke the displayParkingStatus method of the ParkingManager class and return the parking lot status display.
     2. parkCar(Car car): Ticket - invoke the parkCar method of the ParkingManager class and return a ticket.
     3. fetchCar(Ticket ticket): Car - invoke the fetchCar method of the ParkingManager class and return a car.
3. create global exception handler class to handle the exception.
   1) create a class called "GlobalExceptionHandler" and add @ControllerAdvice annotation and @RestController annotation. All response is ResponseEntity<String>.
   2) add a method to handle the "NoAvailablePositionException" exception.
   3) add a method to handle the "UnrecognizedTicketException" exception.
   4) add a method to handle the "InvalidLicensePlateException" exception.
   5) add a method to handle the "Exception" exception.
4. create a ParkingControllerTest class
   1) Requirement: Junit 5, Mockito, integration test, method name should be "should_return_xxx_when_calledMethodName_given_xxx"
   2) Test case:
      1. should display the parking lot status correctly. - use MockMvc to test the "/parking-lot/status" url. assert the response body is the same as the expected JSON.
      2. should park a car then return a ticket. - use MockMvc to test the "/parking-car" url. assert the response body is not null.
      3. should return a car when fetch given the ticket is valid. - use MockMvc to test the "/parking-car" url. assert the response body is not null.
      4. should throw a "NoAvailablePositionException" exception when all parking lot is full. - use MockMvc to test the "/parking-car" url. assert the response status is 500 and the response body is "No available position".
      5. should throw a "UnrecognizedTicketException" exception when the ticket is invalid. - use MockMvc to test the "/parking-car" url. assert the response status is 500 and the response body is "Unrecognized ticket".
      6. should throw an "InvalidLicensePlateException" exception when the car's license plate is empty. - use MockMvc to test the "/parking-car" url. assert the response status is 500 and the response body is "Invalid license plate".

3. generate Code strictly following the tasking. keep the code clean and simple, no code smell. Use Stream API to simplify the code.


