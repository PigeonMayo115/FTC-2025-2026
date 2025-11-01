package org.firstinspires.ftc.teamcode.custom;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Feeder {
    CRServo rightFeeder;
    CRServo leftFeeder;
    double spitStart = 0;
    double spitEnd = 0;
    double suckStart = 0;
    double suckEnd = 0;
    ElapsedTime time = null;
    


    public Feeder(HardwareMap hwmap, ElapsedTime elapsedTime) {
        rightFeeder = hwmap.crservo.get("rightFeeder");
        leftFeeder = hwmap.crservo.get("leftFeeder");
        time = elapsedTime;
    }


    public void setPower(double power) {
        rightFeeder.setPower(power);
        leftFeeder.setPower(-power);
    }

    public void spit(){
        setPower(0.5);
    }

    public boolean spit (double howLong, double currentTime){
        if (spitEnd == 0){
            spitStart = currentTime;
            spitEnd = currentTime + howLong;
        }
        if (currentTime > spitStart + howLong){
            spitStart = 0;
            spitEnd = 0;
            hold();
            return false;
        } else{
            spit();
            return true;
        }


    }

    public void suck(){
        setPower(-0.5);
    }

    public boolean suck (double howLong, double currentTime){
        if (suckEnd == 0){
            suckStart = currentTime;
            suckEnd = currentTime + howLong;
        }
        if (currentTime > suckStart + howLong){
            suckStart = 0;
            suckEnd = 0;
            hold();
            return false;
        } else{
            suck();
            return true;
        }
    }

    public void hold(){
        setPower(0);
    }

    public class Launch implements Action {
        private boolean initialized = false;
        private boolean done = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
            }
            done = spit(0.5, time.seconds());
            if (!done){
                return false;
            } else {
                initialized = false;
                done = true;
                hold();
                return true;
            }
        }

    }

    public Action launch(){
        return new Launch();
    }

    public class Reject implements Action {
        private boolean initialized = false;
        private boolean done = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
            }
            done = suck(0.5, time.seconds());
            if (!done){
                return false;
            } else {
                initialized = false;
                done = true;
                hold();
                return true;
            }
        }
    }

    public Action reject(){
        return new Reject();
    }
}

