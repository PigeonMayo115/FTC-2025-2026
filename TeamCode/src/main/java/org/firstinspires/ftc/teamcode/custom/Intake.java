package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    DcMotorEx intakeMot;
    public Intake(HardwareMap hwmap) {
        intakeMot = hwmap.get(DcMotorEx.class, "intake");
    }

    public void spit(){
        intakeMot.setPower(1);
    }

    public void suck(){
        intakeMot.setPower(-1);
    }

    public void hold(){
        intakeMot.setPower(0);
    }
}
