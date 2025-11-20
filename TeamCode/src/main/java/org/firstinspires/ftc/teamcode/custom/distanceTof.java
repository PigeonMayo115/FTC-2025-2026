package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class distanceTof {
    private com.qualcomm.robotcore.hardware.DistanceSensor tofFront;
    double frontDistance;

    public distanceTof(HardwareMap hwmap) {
        tofFront = hwmap.get(com.qualcomm.robotcore.hardware.DistanceSensor.class,"tofFront");
    }

    public double getDistance(String location) {
        switch (location) {
            case "front":
                return tofFront.getDistance(DistanceUnit.INCH);
            default:
                return Double.NaN;
        }
    }
}
