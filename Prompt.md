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


