package org.afs.pakinglot.domain;

import java.util.Objects;

public record Ticket(String plateNumber, Integer position, Integer parkingLot) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return Objects.equals(plateNumber, ticket.plateNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(plateNumber);
    }
}
