package org.firstinspires.ftc.teamcode.custom;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    double belchStart = 0;
    double belchEnd = 0;
    double gulpStart = 0;
    double gulpEnd = 0;
    ElapsedTime time = null;

    DcMotorEx intakeMot;
    public Intake(HardwareMap hwmap, ElapsedTime elapsedTime) {
        intakeMot = hwmap.get(DcMotorEx.class, "intake");
        time = elapsedTime;
    }

    public void belch(){
        intakeMot.setPower(1);
    }

    public boolean belch (double howLong, double currentTime){
        if (belchEnd == 0){
            belchStart = currentTime;
            belchEnd = currentTime + howLong;
        }
        if (currentTime > belchStart + howLong){
            belchStart = 0;
            belchEnd = 0;
            hold();
            return false;
        } else{
            belch();
            return true;
        }
    }

    public void gulp(){
        intakeMot.setPower(-1);
    }

    public boolean gulp (double howLong, double currentTime){
        if (gulpEnd == 0){
            gulpStart = currentTime;
            gulpEnd = currentTime + howLong;
        }
        if (currentTime > gulpStart + howLong){
            gulpStart = 0;
            gulpEnd = 0;
            hold();
            return false;
        } else{
            gulp();
            return true;
        }
    }

    public void hold(){
        intakeMot.setPower(0);
    }
    
    public class Belch implements Action {
        private boolean initialized = false;
        private boolean done = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
            }
            done = belch(0.5, time.seconds());
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

    public Action reject () {
        return new Belch();
    }

    public class Gulp implements Action {
        private boolean initialized = false;
        private boolean done = true;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized){
                initialized = true;
            }
            done = gulp(0.5, time.seconds());
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

    public Action launchAndIntake () {
        return new Gulp();
    }


}
