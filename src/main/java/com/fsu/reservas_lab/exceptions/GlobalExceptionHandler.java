package com.fsu.reservas_lab.exceptions;

import com.fsu.reservas_lab.exceptions.curso.CourseAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryActiveReservationsException;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryNotFoundException;
import com.fsu.reservas_lab.exceptions.reserva.*;
import com.fsu.reservas_lab.exceptions.turma.ClassAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.turma.ClassNotFoundException;
import com.fsu.reservas_lab.exceptions.turma.TeacherNotAllowedException;
import com.fsu.reservas_lab.exceptions.usuario.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // constant formater for timestamp
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

    // method to get the timestamp
    public String getCurrentTimestamp() {
        return FORMATTER.format(Instant.now());
    }

    // User
    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(CustomBadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleCustomAccessDeniedException(CustomAccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Você não tem permissões suficiente para esta ação");
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleNameExistsException(NameAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EnrollmentAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEnrollmentAlreadyExistsException(EnrollmentAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Course
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCourseNotFoundException(CourseNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(CourseAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCourseAlreadyExistsException(CourseAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Laboratory
    @ExceptionHandler(LaboratoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLaboratoryNotFoundException(LaboratoryNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(LaboratoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleLaboratoryAlreadyExistsException(LaboratoryAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(LaboratoryActiveReservationsException.class)
    public ResponseEntity<Map<String, Object>> handleLaboratoryActiveReservationsException(LaboratoryActiveReservationsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Class
    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleClassNotFoundException(ClassNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ClassAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleClassAlreadyExistsException(ClassAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(TeacherNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleTeacherNotAllowedException(TeacherNotAllowedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Reservation
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReservationNotFound(ReservationNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ReservationCannotBeDeletedException.class)
    public ResponseEntity<Map<String, Object>> handleReservationCannotBeDeletedException(ReservationCannotBeDeletedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ReservationTimeConflictException.class)
    public ResponseEntity<Map<String, Object>> handleReservationTimeConflictException(ReservationTimeConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ReservationCannotBeUpdatedException.class)
    public ResponseEntity<Map<String, Object>> handleReservationCannotBeUpdatedException(ReservationCannotBeUpdatedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ReservationInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleReservationInvalidException(ReservationInvalidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(OrderApprovalInvalidatedException.class)
    public ResponseEntity<Map<String, Object>> handleOrderApprovalInvalidatedException(OrderApprovalInvalidatedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // Generics
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Erro inesperado.");
        response.put("details", ex.getMessage());
        response.put("timestamp", getCurrentTimestamp());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
