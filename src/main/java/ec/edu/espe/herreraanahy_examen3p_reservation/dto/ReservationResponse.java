package ec.edu.espe.herreraanahy_examen3p_reservation.dto;

import java.util.UUID;

public class ReservationResponse {
    private final String id;
    private final String roomCode;
    private final String reservedByEmail;
    private final int hours;
    private final String status;

    public ReservationResponse(String id, String roomCode, String reservedByEmail, int hours, String status) {
        this.id = id;
        this.roomCode = UUID.randomUUID().toString();
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = "CREATED";
    }

    public String getId() {
        return id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getReservedByEmail() {
        return reservedByEmail;
    }

    public long getHours() {
        return hours;
    }

    public String getStatus() {
        return status;
    }
}
