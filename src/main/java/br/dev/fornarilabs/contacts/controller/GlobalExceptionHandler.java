package br.dev.fornarilabs.contacts.controller;

import br.dev.fornarilabs.contacts.dto.BadRequestDTO;
import br.dev.fornarilabs.contacts.dto.ErrorResponseDTO;
import br.dev.fornarilabs.contacts.dto.FieldErrorDTO;
import br.dev.fornarilabs.contacts.service.InvalidCredentials;
import br.dev.fornarilabs.contacts.service.UserAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import tools.jackson.databind.exc.UnrecognizedPropertyException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponseDTO> handleException(UserAlreadyExists e){
        log.error(e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "User already exists.",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<ErrorResponseDTO> handleException(InvalidCredentials e){
        log.error(e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestDTO> handleException(MethodArgumentNotValidException e){
        log.error(e.getMessage());
        List<FieldErrorDTO> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
        );
        BadRequestDTO error = new BadRequestDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request.",
                errors,
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BadRequestDTO> handleException(HttpMessageNotReadableException e){
        log.error(e.getMessage());
        List<FieldErrorDTO> errors = null;
        if (e.getCause() instanceof UnrecognizedPropertyException unrecognized) {
            errors = List.of(new FieldErrorDTO(unrecognized.getPropertyName(), "Unexpected field."));
        }
        BadRequestDTO error = new BadRequestDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad request.",
                errors,
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e){
        log.error(e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error.",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
