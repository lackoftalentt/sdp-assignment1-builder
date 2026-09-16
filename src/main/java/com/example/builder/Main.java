package main.java.com.example.builder;

public class Main {

    public static void main(String[] args) {

        DroneMission mission =
                new DroneMission.Builder(
                        "Mountain Survey",
                        MissionType.LONG_RANGE,
                        "Almaty Mountains"
                )
                        .altitude(300)
                        .speed(80)
                        .batteryCapacity(6000)
                        .enableGps()
                        .enableCamera()
                        .payloadKg(1.5)
                        .durationMinutes(90)
                        .build();

        System.out.println("Mission: " + mission.getMissionName());
        System.out.println("Type: " + mission.getMissionType());
        System.out.println("Destination: " + mission.getDestination());
        System.out.println("Altitude: " + mission.getAltitude() + " m");
        System.out.println("GPS: " + mission.isGpsEnabled());
        System.out.println("Camera: " + mission.isCameraEnabled());
    }
}