package com.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
public class ImageResponce {
    private String message;
    private boolean success;

    private String imageName;

    private String status;

    public ImageResponce(String message, boolean success, String imageName, String status) {
        this.message = message;
        this.success = success;
        this.imageName = imageName;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ImageResponce{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", imageName='" + imageName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
