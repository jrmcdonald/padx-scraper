package com.jrmcdonald.padx.exceptions;

/**
 * Invalid Monster Exception
 * 
 * @author Jamie McDonald
 * @since 0.2
 */
public class InvalidMonsterException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public InvalidMonsterException() {
        super();
    }

    public InvalidMonsterException(String message) {
        super(message);
    }

    public InvalidMonsterException(Throwable cause) {
        super(cause);
    }

    public InvalidMonsterException(String message, Throwable cause) {
        super(message, cause);
    }
}