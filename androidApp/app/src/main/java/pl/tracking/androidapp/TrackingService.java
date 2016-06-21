package pl.tracking.androidapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import pl.tracking.androidapp.api.RestApi;
import pl.tracking.androidapp.model.Acceleration;
import pl.tracking.androidapp.model.Gyro;

import java.util.Date;

import retrofit.RestAdapter;

public class TrackingService extends IntentService {

    private Context context;
    private RestApi api;
    private RestAdapter restAdapter;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                final Acceleration acceleration = getAccelerationFromSensor(sensorEvent);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        api.sendAccelerationValues(acceleration);
//                    }
//                }).run();
            }
        }

        @Override
        public void onAccuracyChanged(final Sensor sensor, final int i) {
        }
    };

    private final SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(final SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                final Gyro gyro = getGyroFromSensor(sensorEvent);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        api.sendAccelerationValues(gyro);
//                    }
//                }).run();
            }
        }

        @Override
        public void onAccuracyChanged(final Sensor sensor, final int i) {
        }
    };

    public TrackingService() {
        super("Tracking Intent Service");

        context = getApplicationContext();

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();

        switch (action) {
            case TrackingServiceActions.CONNECT_ACTION:
                final String url = intent.getStringExtra(TrackingServiceActions.EXTRA_DOMAIN);
                restAdapter = new RestAdapter.Builder().setEndpoint(url).build();
                api = restAdapter.create(RestApi.class);
                Toast.makeText(context, "CONNECTED", Toast.LENGTH_SHORT).show();
                break;
            case TrackingServiceActions.DISCONNECT_ACTION:

                break;
            case TrackingServiceActions.TRIGGER_STOP:

                break;
            case TrackingServiceActions.TRIGGER_START_TEST:

                break;
            default:
                new IllegalArgumentException("Unsupported action");
                break;
        }
    }

    private Acceleration getAccelerationFromSensor(final SensorEvent event) {
        final long timestamp = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
        return new Acceleration(event.values[0], event.values[1], event.values[2], timestamp);
    }

    private Gyro getGyroFromSensor(final SensorEvent event) {
        final long timestamp = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
        return new Gyro(event.values[0], event.values[1], event.values[2], timestamp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(accelerometerListener);
        sensorManager.unregisterListener(gyroListener);
    }
}
