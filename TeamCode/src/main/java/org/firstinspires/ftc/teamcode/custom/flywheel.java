package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class flywheel {
    DcMotorEx flywheel = null;
    double p = 300;
    double i = 1;
    double d = 4;
    double f = 10;

    public flywheel(HardwareMap hwmap) {
        flywheel = hwmap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));
    }
}