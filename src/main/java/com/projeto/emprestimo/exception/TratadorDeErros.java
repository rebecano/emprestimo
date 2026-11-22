package com.projeto.emprestimo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> naoEncontrado(NaoEncontradoException e) {
        return new HashMap<>(Map.of("mensagem", e.getMessage()));
    }

    @ExceptionHandler(ConflitoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflito(ConflitoException e) {
        return new HashMap<>(Map.of("mensagem", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validacao(MethodArgumentNotValidException e) {
        List<FieldError> listaErros = e.getBindingResult().getFieldErrors();
        Map<String, String> mapa = new HashMap<>();

        for (FieldError erro : listaErros) {
            String campo = erro.getField();
            String mensagem = erro.getDefaultMessage();
            mapa.put(campo, mensagem);
        }

        return mapa;
    }

}


