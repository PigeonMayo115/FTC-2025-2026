package org.firstinspires.ftc.teamcode.custom;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
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


    public class SpinUp implements Action {
        private boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
                setVelocity(1350);
            }

            if (flywheelMot.getVelocity() <= 1380 && flywheelMot.getVelocity() >= 1320){
                return false;
            } else {
                return true;
            }
        }
    }

    public Action spinUp () {
        return new SpinUp();
    }

    public class SpinDown implements Action {
        private boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
                setVelocity(0);
            }

            if (flywheelMot.getVelocity() <= 50){
                return false;
            } else {
                return true;
            }
        }
    }

    public Action spinDown () {
        return new SpinDown();
    }




}