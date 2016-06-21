package pl.tracking.androidapp.model;

public class Acceleration {
    private final long timestamp;
    private final double x;
    private final double y;
    private final double z;

    public Acceleration(final float x_value, final float y_value, final float z_value,
                        final long timestamp) {
        x = Double.valueOf(""+x_value);
        y = Double.valueOf(""+y_value);
        z = Double.valueOf(""+z_value);
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
