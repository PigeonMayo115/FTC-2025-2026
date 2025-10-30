package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class Flywheel {
    DcMotorEx flywheelMot = null;
    double p = 300;
    double i = 1;
    double d = 4;
    double f = 10;

    public Flywheel(HardwareMap hwmap) {
        flywheelMot = hwmap.get(DcMotorEx.class, "flywheel");
        flywheelMot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMot.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(p, i, d, f));
        flywheelMot.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public double getVelocity(){
        return flywheelMot.getVelocity();
    }

    public void setVelocity(double angularRate){

        flywheelMot.setVelocity(angularRate);
    }


}