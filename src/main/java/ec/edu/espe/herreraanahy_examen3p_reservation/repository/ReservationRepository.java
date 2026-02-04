package ec.edu.espe.herreraanahy_examen3p_reservation.repository;

import ec.edu.espe.herreraanahy_examen3p_reservation.model.RoomReservation;

import java.util.Optional;

public interface ReservationRepository {
    RoomReservation save(RoomReservation roomReservation);
    Optional<RoomReservation> findById(String id);
    boolean existByReservedByEmail(String reservedByEmail);
}