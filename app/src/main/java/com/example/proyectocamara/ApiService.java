package com.example.proyectocamara;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("/generate_image")
    Call<ImageProcessingResponse> generateImage(@Body ImageProcessingRequest request);

    @GET("/get_image")
    Call<Void> getImage(@Query("output_path") String outputPath);
}
