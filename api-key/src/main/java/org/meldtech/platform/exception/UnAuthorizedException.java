package org.meldtech.platform.exception;

import org.meldtech.platform.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized api access")
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String message) {
        super(message);
        System.out.println("Got here "+message);
    }

    public ApiError getApiError(String path) {
        return new ApiError(HttpStatus.UNAUTHORIZED.value(), getMessage(), HttpStatus.UNAUTHORIZED.name(), path);
    }

}
