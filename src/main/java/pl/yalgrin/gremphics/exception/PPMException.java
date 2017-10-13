package pl.yalgrin.gremphics.exception;

import java.io.IOException;

public class PPMException extends IOException {
    public PPMException() {
    }

    public PPMException(String message) {
        super(message);
    }

    public PPMException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPMException(Throwable cause) {
        super(cause);
    }
}
