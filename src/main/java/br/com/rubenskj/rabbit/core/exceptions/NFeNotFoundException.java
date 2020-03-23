package br.com.rubenskj.rabbit.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NFeNotFoundException extends RuntimeException {
    public NFeNotFoundException(String message) {
        super(message);
    }
}
