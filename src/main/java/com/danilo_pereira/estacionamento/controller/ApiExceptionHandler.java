package com.danilo_pereira.estacionamento.controller;

import com.danilo_pereira.estacionamento.exception.RegraNegocioException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ProblemDetail> tratarRegraNegocio(RegraNegocioException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Regra de negocio violada");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(problem);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ProblemDetail> tratarArgumentoInvalido(RuntimeException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle("Requisicao invalida");
        problem.setProperty("timestamp", LocalDateTime.now());
        return ResponseEntity.badRequest().body(problem);
    }
}
