package ec.edu.espe.herreraanahy_examen3p_reservation.service;

import ec.edu.espe.herreraanahy_examen3p_reservation.dto.ReservationResponse;
import ec.edu.espe.herreraanahy_examen3p_reservation.model.RoomReservation;
import ec.edu.espe.herreraanahy_examen3p_reservation.repository.ReservationRepository;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserPolicyClient userPolicyClient;

    public ReservationService(ReservationRepository reservationRepository, UserPolicyClient userPolicyClient){
        this.reservationRepository = reservationRepository;
        this.userPolicyClient = userPolicyClient;
    }


    public ReservationResponse createReservation(String roomCode, String email, int hours, String status){
        if(roomCode == null || roomCode.isEmpty()){
            throw new IllegalArgumentException("El codigo no puede ser nulo ni vacio");
        }

        if(email == null){
            throw new IllegalArgumentException("Email en formato incorrecto");
        }
        if(email.isEmpty()){
            throw new IllegalArgumentException("Email en formato incorrecto");
        }
        if(!email.contains("@")){
            throw new IllegalArgumentException("Email en formato incorrecto");
        }

        if (hours < 0) {
            throw new IllegalArgumentException("La hora debe ser mayor a 0");
        }

        if (hours >= 8) {
            throw new IllegalArgumentException("La hora debe ser menor a 8");
        }
        if(userPolicyClient.isBlocked(email)){
            throw new IllegalStateException("Usuario bloqueado");
        }

        RoomReservation roomReservation = new RoomReservation("", roomCode, email, hours, status);
        RoomReservation save = reservationRepository.save(roomReservation);

        return new ReservationResponse(save.getId(), save.getRoomCode(), save.getReservedByEmail(), save.getHours(), save.getStatus());

    }

}


