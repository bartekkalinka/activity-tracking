package pl.tracking.androidapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import pl.tracking.androidapp.model.Acceleration;

import java.util.Date;

public class TrackingService extends IntentService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private Context context;

    public TrackingService() {
        super("Tracking Intent Service");

        context = getApplicationContext();

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();

        switch (action) {
            case TrackingServiceActions.CONNECT_ACTION:
                final String url = intent.getStringExtra(TrackingServiceActions.EXTRA_DOMAIN);
                //TODO: Add code to connect to backend :)
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
        }
    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final Acceleration acceleration = getAccelerationFromSensor(sensorEvent);


        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            //TODO: Do something
        }
    }

    @Override
    public void onAccuracyChanged(final Sensor sensor, final int i) {
    }

    private Acceleration getAccelerationFromSensor(SensorEvent event) {
        final long timestamp = (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
        return new Acceleration(event.values[0], event.values[1], event.values[2], timestamp);
    }
}
