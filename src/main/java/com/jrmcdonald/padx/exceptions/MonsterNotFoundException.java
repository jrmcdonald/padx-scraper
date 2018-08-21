package com.jrmcdonald.padx.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Monster Not Found Exception
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MonsterNotFoundException extends RuntimeException {
    
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3101535119526445840L;
}