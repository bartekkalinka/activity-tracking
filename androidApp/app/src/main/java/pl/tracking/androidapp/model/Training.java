package pl.tracking.androidapp.model;

public class Training {
    private final String userID;
    private final String activity;
    private final long starttime;
    private final Acceleration acceleration;

    public Training(final String userID, final String activity, final long starttime,
                    final Acceleration acceleration) {
        this.userID = userID;
        this.activity = activity;
        this.starttime = starttime;
        this.acceleration = acceleration;
    }

    public String getUserID() {
        return userID;
    }

    public String getActivity() {
        return activity;
    }

    public long getStarttime() {
        return starttime;
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }
}
