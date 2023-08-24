package com.example.proyectocamara;

import com.google.gson.annotations.SerializedName;

public class ImageProcessingRequest {
    @SerializedName("prompt")
    private String prompt;

    @SerializedName("negative_prompt")
    private String negativePrompt;

    @SerializedName("image_path")
    private String imagePath;

    public ImageProcessingRequest(String prompt, String negativePrompt, String imagePath) {
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.imagePath = imagePath;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
