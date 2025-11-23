package org.firstinspires.ftc.teamcode.custom;

public class WaitAuto {
    static double startTime = -1;
    static boolean hasCompleted = false;

    static double end = 0;
    static double start = 0;


    public static boolean wait(double howLong, double currentTime) {

            if (end == 0){
                start = currentTime;
                end = currentTime + howLong;
            }
            if (currentTime > start + howLong){
                start = 0;
                end = 0;
                return true;
            } else{
                return false;
            }
    }
}
