package main.java;

public class DroneMission {
    private final String missionName;
    private final String missionType;
    private final String destination;
    private final int altitude;
    private final int speed;
    private final int batteryCapacity;
    private final boolean gpsEnabled;
    private final boolean cameraEnabled;
    private final boolean returnToHome;
    private final boolean obstacleAvoidance;
    private final double payloadKg;
    private final int durationMinutes;

    public DroneMission(
            String missionName,
            String missionType,
            String destination,
            int altitude,
            int speed,
            int batteryCapacity,
            boolean gpsEnabled,
            boolean cameraEnabled,
            boolean returnToHome,
            boolean obstacleAvoidance,
            double payloadKg,
            int durationMinutes
    ) {
        this.missionName = missionName;
        this.missionType = missionType;
        this.destination = destination;
        this.altitude = altitude;
        this.speed = speed;
        this.batteryCapacity = batteryCapacity;
        this.gpsEnabled = gpsEnabled;
        this.cameraEnabled = cameraEnabled;
        this.returnToHome = returnToHome;
        this.obstacleAvoidance = obstacleAvoidance;
        this.payloadKg = payloadKg;
        this.durationMinutes = durationMinutes;
    }

    public String getMissionName() {
        return missionName;
    }

    public String getMissionType() {
        return missionType;
    }

    public String getDestination() {
        return destination;
    }

    public int getAltitude() {
        return altitude;
    }

    public int getSpeed() {
        return speed;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public boolean isCameraEnabled() {
        return cameraEnabled;
    }

    public boolean isReturnToHome() {
        return returnToHome;
    }

    public boolean isObstacleAvoidance() {
        return obstacleAvoidance;
    }

    public double getPayloadKg() {
        return payloadKg;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }
}