package org.firstinspires.ftc.teamcode.auto;


import android.widget.Switch;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;
import org.opencv.core.Mat;

@Autonomous
public class RedAutoV1 extends OpMode {
    MecanumDrive drive = null;
    Feeder feeder = null;
    Flywheel flywheel = null;
    Intake intake = null;
    Pose2d startPose = null;
    ElapsedTime time = null;

    Action turnToObelisk = null;
    Action moveToPickup = null;
    Action reverse = null;
    Action returnToObelisk = null;
    Action wait = null;

    Action leaveLaunchZone = null;
    Pose2d currentPose = null;
    int step;
    boolean done;
    boolean done2;
    @Override
    public void init() {
        startPose = new Pose2d(-62, 36, Math.toRadians(180));
        time = new ElapsedTime();
        drive = new MecanumDrive(hardwareMap, startPose);
        feeder = new Feeder(hardwareMap,time);
        intake = new Intake(hardwareMap, time);
        flywheel= new Flywheel(hardwareMap);



        turnToObelisk = drive.actionBuilder(startPose)
                .strafeToConstantHeading(new Vector2d(-16,16))
                .turnTo(Math.toRadians(225))
                .build();
                //.strafeToSplineHeading(new Vector2d(-16,16), Math.toRadians(135)).build();

        moveToPickup = drive.actionBuilder(new Pose2d(new Vector2d(-16,16), Math.toRadians(225)))
                .turnTo(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(-20, 16))
                .build();

        reverse = drive.actionBuilder(new Pose2d(-20,16,Math.toRadians(90)))
                .strafeToConstantHeading(new Vector2d(-20,-16))
                .build();

        returnToObelisk = drive.actionBuilder(new Pose2d(-20, -16, Math.toRadians(90)))
                .strafeToConstantHeading(new Vector2d(-16,16))
                .turnTo(Math.toRadians(225))
                .build();

        leaveLaunchZone = drive.actionBuilder(new Pose2d(-16,16,Math.toRadians(225)))
                .strafeToConstantHeading(new Vector2d(-60,16))
                .build();

        wait = drive.actionBuilder(new Pose2d(0,0,0)).waitSeconds(2).build();





        step = 0;
        done = false;



    }
    @Override
    public void start(){

    }

    @Override
    public void loop() {
        currentPose = drive.localizer.getPose();




        //Action Sequence:
        //1. spline off the wall towards the obelisk
        //2. spin up flywheel
        //3. activate feeder servos (launch)
        //4. wait for flywheel to spin back up
        //5. spin intake
        //6. activate feeder servos (launch)
        //7. move into ending box
        //end

        switch (step){
            case 0:
                done = !turnToObelisk.run(new TelemetryPacket());
                flywheel.setVelocity(1240);
                if(done){
                    step = 5;
                }
                break;
            case 5:
                done = feeder.spit(2,time.seconds());

                if (done){
                    step = 7;
                }
                break;
            case 7:
                done = !wait.run(new TelemetryPacket());
                flywheel.setVelocity(1260);
                if (done){
                    step = 10;
                }
                break;
            case 10:
                done = intake.gulp(1,time.seconds());
                if (done){
                    step = 15;
                }
                break;
            case 15:
                done = feeder.spit(2,time.seconds());
                if (done){
                    step = 20;
                }
                break;
            case 20:
                done = !moveToPickup.run(new TelemetryPacket());
                flywheel.setVelocity(0);
                if (done){
                    step = 25;
                }
                break;
            case 25:
                done = !reverse.run(new TelemetryPacket());
            intake.gulp(1.5,time.seconds());
                if (done){
                    intake.hold();
                    step = 30;
                }
                break;
            case 30:
                done = !returnToObelisk.run(new TelemetryPacket());
                if (done){
                    step = 32;
                }
                break;
            case 32:
                flywheel.setVelocity(1260);
                done = (flywheel.getVelocity() > 1240) && (flywheel.getVelocity() < 1280);
                if (done){
                    step = 35;
                }
            case 35:
                done = feeder.spit(2,time.seconds());
                if (done){
                    step = 45;
                    done = false;
                }
                break;
            case 45:
                done = feeder.spit(2,time.seconds());
                intake.gulp();
                if (done){
                    step = 50;
                    intake.hold();
                }
                break;
            case 50:
                done = !leaveLaunchZone.run(new TelemetryPacket());
                flywheel.setVelocity(0);
                if (done){
                    step = 100;
                }
                break;



        }


        telemetry.addData("Step",step);
        telemetry.addData("heading", currentPose.heading);
/*
        Actions.runBlocking(new SequentialAction(
                turnToObelisk,
                flywheel.spinUp(),
                feeder.launch(),
                drive.actionBuilder(new Pose2d(0,0,0)).waitSeconds(3).build(),
                intake.launchAndIntake(),
                feeder.launch()));

*/
    }

}
