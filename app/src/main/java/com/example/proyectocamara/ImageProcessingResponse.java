package com.example.proyectocamara;

import com.google.gson.annotations.SerializedName;

public class ImageProcessingResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("output_path")
    private String outputPath;

    public ImageProcessingResponse(String message, String outputPath) {
        this.message = message;
        this.outputPath = outputPath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
