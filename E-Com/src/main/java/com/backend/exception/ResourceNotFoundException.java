package com.backend.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource Not Found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

}

