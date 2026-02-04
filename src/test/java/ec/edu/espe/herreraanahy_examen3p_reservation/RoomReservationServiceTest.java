package ec.edu.espe.herreraanahy_examen3p_reservation;

import ec.edu.espe.herreraanahy_examen3p_reservation.dto.ReservationResponse;
import ec.edu.espe.herreraanahy_examen3p_reservation.model.RoomReservation;
import ec.edu.espe.herreraanahy_examen3p_reservation.repository.ReservationRepository;
import ec.edu.espe.herreraanahy_examen3p_reservation.service.ReservationService;
import ec.edu.espe.herreraanahy_examen3p_reservation.service.UserPolicyClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RoomReservationServiceTest {
    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private UserPolicyClient userPolicyClient;

    @BeforeEach
    public void setUp(){
        reservationRepository = Mockito.mock(ReservationRepository.class);
        userPolicyClient = Mockito.mock(UserPolicyClient.class);
        reservationService = new ReservationService(reservationRepository, userPolicyClient);
    }
    // 1.	Creación exitosa de una reserva con datos válidos.
    @Test
    void createReservation_validData_shouldSaveAndReturnResponse(){
        //Arrange
        String email = "anahy@gmail.com";
        String roomCode = "123";
        int hours = 7;
        String status = "CREATED";

        when(userPolicyClient.isBlocked(email)).thenReturn(Boolean.FALSE);
        when(reservationRepository.save(any(RoomReservation.class))).thenAnswer(i -> {
            RoomReservation roomReservation = i.getArgument(0);
            return new RoomReservation("123", roomReservation.getRoomCode(), roomReservation.getReservedByEmail(), roomReservation.getHours(), roomReservation.getStatus());
        });

        //Act
        ReservationResponse response = reservationService.createReservation(roomCode, email, hours, status);

        //Assert
        assertNotNull(response.getId());
        assertEquals(email, response.getReservedByEmail());

        verify(userPolicyClient).isBlocked(email);
        verify(reservationRepository).save(any(RoomReservation.class));
    }

    //2.	Error por correo electrónico inválido.
    @Test
    void createReservation_invalidEmail_NotSaveAndReturnResponse(){
        //Arrange
        String email = "aeherrera16espe.edu.ec"; // Email sin @
        String roomCode = "ABC-123";
        int hours = 7;
        String status = "CREATED";

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(roomCode, email, hours, status);
        });

        assertEquals("Email en formato incorrecto", exception.getMessage());
    }

    //3.	Error por número de horas fuera del rango permitido.
    @Test
    void createReservation_validEmail_InvalidHours_NotSaveAndReturnResponse(){
        //Arrange
        String email = "aeherrera16@espe.edu.ec";
        String roomCode = "ABC-123";
        int hours = 9; // Horas >= 8 no permitidas
        String status = "CREATED";

        //Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(roomCode, email, hours, status);
        });

        assertEquals("La hora debe ser menor a 8", exception.getMessage());
    }

    //4.	Error cuando el usuario está bloqueado.
    @Test
    void createReservation_validData_blockedUser_shouldThrowException(){
        //Arrange
        String email = "aeherrera16@espe.edu.ec";
        String roomCode = "ABC-123";
        int hours = 7;
        String status = "CREATED";

        when(userPolicyClient.isBlocked(email)).thenReturn(Boolean.TRUE);

        //Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.createReservation(roomCode, email, hours, status);
        });

        assertEquals("Usuario bloqueado", exception.getMessage());
        verify(userPolicyClient).isBlocked(email);
    }
}


