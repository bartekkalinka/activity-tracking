package pl.tracking.androidapp.api;

import pl.tracking.androidapp.model.Acceleration;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface RestApi {
    @POST("/acceleration")
    Response sendAccelerationValues(@Body Acceleration acceleration);


//    @POST("/training")
//    public Response sendTrainingAccelerationValues(@Body TrainingAcceleration trainingAcceleration);
}
