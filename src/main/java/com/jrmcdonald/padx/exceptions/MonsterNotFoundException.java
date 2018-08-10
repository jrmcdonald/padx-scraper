package com.jrmcdonald.padx.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * MonsterNotFoundException
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MonsterNotFoundException extends RuntimeException {
    
}