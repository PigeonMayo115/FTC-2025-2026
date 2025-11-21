package org.firstinspires.ftc.teamcode.custom;

public class WaitAuto {
    static double startTime = -1;
    static boolean hasCompleted = false;


    public static boolean wait(double howLong, double currentTime) {

        if (startTime < 0) {
            startTime = currentTime; // First call - start timing
            return false;
        }

        double elapsed = currentTime - startTime;
        if (elapsed >= howLong) {
            startTime = -1;
            hasCompleted = false;
            return true;
        }

        return false;
    }
}
