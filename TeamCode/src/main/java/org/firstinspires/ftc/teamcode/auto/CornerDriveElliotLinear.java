package org.firstinspires.ftc.teamcode.auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@Autonomous
public class CornerDriveElliotLinear extends LinearOpMode {
    //initializes servo
    //twin I have absolutely no idea why this is set up the way it is
    //but this is just the way that it was done in the roadrunner docs
    //I am just following up
    //go read the docs if confused, here is a link:
    //https://rr.brott.dev/docs/v1-0/guides/centerstage-auto/
    //also note this is for an old game, not decode or whatever you are working on

    public class Servo1 {
        private Servo servo1;

        public Servo1(HardwareMap hardwareMap){
            servo1 = hardwareMap.get(Servo.class,"servo1");
        }

        //action called FullRight that will turn the servo to position 1
        //which is I think all the way right, hence the name
        public class FullRight implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    //this is where the actual stuff for the action happens
                    servo1.setPosition(1);
                }
                return false;
            }
        }
        public Action fullRight(){
            return new FullRight();
        }

        public class Center implements Action {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    //this is where the actual stuff for the action happens
                    servo1.setPosition(0);
                }
                return false;
            }
        }
        public Action center(){
            return new Center();
        }

    }

    @Override
    public void runOpMode() throws InterruptedException {
        //new Pose2s starting pose and initialize drive class
        Pose2d startPose = new Pose2d(0,0,0);
        MecanumDrive drive = new MecanumDrive(hardwareMap,startPose);

        //initializes servo1

        Servo1 servo1 = new Servo1(hardwareMap);





        //build sequential action starting at startPose
        Action trajectory = drive.actionBuilder(startPose)
                //move forward 30 inches
                .lineToY(30)
                //move left 30 inches
                .lineToX(30)
                //waits one second before preforming next action
                .waitSeconds(1.0)
                //rotates to heading 180 which should make it turn all the way around
                .turn(Math.toRadians(180))
                //turns servo all the way to the right WHILE the robot is turning
                .stopAndAdd(servo1.fullRight())
                //builds the sequence
                .build();

        //wait for driver to press start button
        telemetry.addData("Status", "Ready to start");
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;
        //run sequential action
        Actions.runBlocking(trajectory);

        telemetry.addData("Status","Path Complete");
        telemetry.update();

    }
}
