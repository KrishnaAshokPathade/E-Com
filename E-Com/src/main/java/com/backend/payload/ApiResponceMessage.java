package com.backend.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponceMessage {
    private String message;
    private boolean success;

    public ApiResponceMessage(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    @Override
    public String toString() {
        return "ApiResponceMessage{" +
                "message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
