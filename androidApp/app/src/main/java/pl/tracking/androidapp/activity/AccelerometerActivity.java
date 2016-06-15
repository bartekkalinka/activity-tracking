package pl.tracking.androidapp.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import pl.tracking.androidapp.R;
import pl.tracking.androidapp.model.Acceleration;

import java.util.Date;


public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {
    private String restURL;
    private TextView acceleration;
    private Button myStartButton;
    private Button myStopButton;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accelerometer);
        acceleration = (TextView) findViewById(R.id.acceleration);

        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    public void onSensorChanged(final SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

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
