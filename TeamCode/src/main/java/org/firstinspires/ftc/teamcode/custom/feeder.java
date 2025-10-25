package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class feeder {
    CRServo rightFeeder;
    CRServo leftFeeder;

    public feeder(HardwareMap hwmap) {
        rightFeeder = hwmap.crservo.get("rightFeeder");
        leftFeeder = hwmap.crservo.get("leftFeeder");
    }

    public void setPower(double power) {
        rightFeeder.setPower(-power);
        leftFeeder.setPower(-power);
    }
}