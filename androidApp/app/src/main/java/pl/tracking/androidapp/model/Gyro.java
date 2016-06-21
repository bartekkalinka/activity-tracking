package pl.tracking.androidapp.model;

public class Gyro {
    private final long timestamp;
    private final float X_roll;
    private final float Y_pitch;
    private final float Z_yaw;

    public Gyro(final float x_roll, final float y_pitch, final float z_yaw, final long timestamp) {
        X_roll = x_roll;
        Y_pitch = y_pitch;
        Z_yaw = z_yaw;
        this.timestamp = timestamp;
    }

    public float getZ_yaw() {
        return Z_yaw;
    }

    public float getY_pitch() {
        return Y_pitch;
    }

    public float getX_roll() {
        return X_roll;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
