package main.java.com.example.builder;

public class DroneMission {

    private final String missionName;
    private final MissionType missionType;
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
        private final MissionType missionType;
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
                MissionType missionType,
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
            validate();
            return new DroneMission(this);
        }

        private void validate() {

            // Single-field validation
            if (missionName == null || missionName.isBlank()) {
                throw new IllegalArgumentException(
                        "Mission name cannot be empty"
                );
            }

            if (missionType == null) {
                throw new IllegalArgumentException(
                        "Mission type cannot be null"
                );
            }

            if (destination == null || destination.isBlank()) {
                throw new IllegalArgumentException(
                        "Destination cannot be empty"
                );
            }

            if (altitude <= 0) {
                throw new IllegalArgumentException(
                        "Altitude must be greater than 0"
                );
            }

            if (speed <= 0) {
                throw new IllegalArgumentException(
                        "Speed must be greater than 0"
                );
            }

            if (batteryCapacity <= 0) {
                throw new IllegalArgumentException(
                        "Battery capacity must be greater than 0"
                );
            }

            if (payloadKg < 0) {
                throw new IllegalArgumentException(
                        "Payload cannot be negative"
                );
            }

            if (durationMinutes <= 0) {
                throw new IllegalArgumentException(
                        "Duration must be greater than 0"
                );
            }

            // Cross-field validation #1
            if (missionType == MissionType.LONG_RANGE && !gpsEnabled) {
                throw new IllegalArgumentException(
                        "Long-range missions require GPS to be enabled"
                );
            }

            // Cross-field validation #2
            if (missionType == MissionType.LONG_RANGE
                    && batteryCapacity < 5000) {
                throw new IllegalArgumentException(
                        "Long-range missions require at least 5000 mAh battery capacity"
                );
            }
        }
    }

    public String getMissionName() {
        return missionName;
    }

    public MissionType getMissionType() {
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