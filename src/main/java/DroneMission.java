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

    private DroneMission(Builder builder) {
        this.missionName = builder.missionName;
        this.missionType = builder.missionType;
        this.destination = builder.destination;
        this.altitude = builder.altitude;
        this.speed = builder.speed;
        this.batteryCapacity = builder.batteryCapacity;
        this.gpsEnabled = builder.gpsEnabled;
        this.cameraEnabled = builder.cameraEnabled;
        this.returnToHome = builder.returnToHome;
        this.obstacleAvoidance = builder.obstacleAvoidance;
        this.payloadKg = builder.payloadKg;
        this.durationMinutes = builder.durationMinutes;
    }

    public static class Builder {

        private final String missionName;
        private final String missionType;
        private final String destination;

        private int altitude = 100;
        private int speed = 50;
        private int batteryCapacity = 4000;
        private boolean gpsEnabled = false;
        private boolean cameraEnabled = false;
        private boolean returnToHome = true;
        private boolean obstacleAvoidance = true;
        private double payloadKg = 0.0;
        private int durationMinutes = 30;

        public Builder(
                String missionName,
                String missionType,
                String destination
        ) {
            this.missionName = missionName;
            this.missionType = missionType;
            this.destination = destination;
        }

        public Builder altitude(int altitude) {
            this.altitude = altitude;
            return this;
        }

        public Builder speed(int speed) {
            this.speed = speed;
            return this;
        }

        public Builder batteryCapacity(int batteryCapacity) {
            this.batteryCapacity = batteryCapacity;
            return this;
        }

        public Builder enableGps() {
            this.gpsEnabled = true;
            return this;
        }

        public Builder enableCamera() {
            this.cameraEnabled = true;
            return this;
        }

        public Builder disableReturnToHome() {
            this.returnToHome = false;
            return this;
        }

        public Builder disableObstacleAvoidance() {
            this.obstacleAvoidance = false;
            return this;
        }

        public Builder payloadKg(double payloadKg) {
            this.payloadKg = payloadKg;
            return this;
        }

        public Builder durationMinutes(int durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        public DroneMission build() {
            return new DroneMission(this);
        }
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