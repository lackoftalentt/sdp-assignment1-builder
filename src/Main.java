import main.DroneMission;

public class Main {
    public static void main(String[] args) {
        DroneMission mission = new DroneMission(
                "Mountain Survey",
                "LONG_RANGE",
                "Almaty Mountains",
                300,
                80,
                6000,
                true,
                true,
                true,
                true,
                1.5,
                90
        );

        System.out.println("Mission: " + mission.getMissionName());
        System.out.println("Type: " + mission.getMissionType());
        System.out.println("Destination: " + mission.getDestination());
        System.out.println("Altitude: " + mission.getAltitude() + " m");
        System.out.println("Speed: " + mission.getSpeed() + " km/h");
    }

    /* 1. Low readability.
        Parameters are passed positionally, so it’s difficult to understand the purpose of the values when creating an object.

        2. High risk of error.
        You can accidentally swap two values of the same type, for example, altitude and speed, and the compiler won’t notice anything.

        3. Poor extensibility.
        Adding a new optional field will require changing the constructor and all existing places where it is called.

        4. No obvious defaults.
        Optional settings have to be passed manually even when standard behavior is used.
     */
}