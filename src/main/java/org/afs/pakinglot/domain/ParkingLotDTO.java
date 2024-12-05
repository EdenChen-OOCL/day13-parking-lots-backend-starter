package org.afs.pakinglot.domain;

import java.util.List;

public class ParkingLotDTO {

    private Integer parkingLotId;
    private String parkingLotName;
    private List<String> position;

    public ParkingLotDTO(int parkingLotId, String parkingLotName, List<String> position) {
        this.parkingLotId = parkingLotId;
        this.parkingLotName = parkingLotName;
        this.position = position;
    }

    public int getParkingLotId() {
        return parkingLotId;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public List<String> getPosition() {
        return position;
    }


    @Override
    public String toString() {
        return "{" +
                "\"parkingLotId\": " + parkingLotId +
                ", \"parkingLotName\": \"" + parkingLotName + "\"" +
                ", \"position\": " + position +
                '}';
    }
}