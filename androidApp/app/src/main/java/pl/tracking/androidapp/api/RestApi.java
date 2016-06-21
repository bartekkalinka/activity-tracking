package pl.tracking.androidapp.api;

import pl.tracking.androidapp.model.Acceleration;
import pl.tracking.androidapp.model.Gyro;
import pl.tracking.androidapp.model.Training;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface RestApi {
    @POST("/acceleration")
    Response sendAccelerationValues(@Body Acceleration acceleration);

    @POST("/gyro")
    Response sendAccelerationValues(@Body Gyro gyro);

    @POST("/training")
    Response sendTrainingAccelerationValues(@Body Training training);
}
