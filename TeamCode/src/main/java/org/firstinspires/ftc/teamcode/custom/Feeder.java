package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Feeder {
    CRServo rightFeeder;
    CRServo leftFeeder;

    public Feeder(HardwareMap hwmap) {
        rightFeeder = hwmap.crservo.get("rightFeeder");
        leftFeeder = hwmap.crservo.get("leftFeeder");
    }

    public void setPower(double power) {
        rightFeeder.setPower(-power);
        leftFeeder.setPower(-power);
    }

    public void spit(){
        setPower(0.5);
    }

    public void suck(){
        setPower(-0.5);
    }

    public void hold(){
        setPower(0);
    }
}