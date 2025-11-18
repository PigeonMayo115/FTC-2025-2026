package org.firstinspires.ftc.teamcode.auto;


import android.widget.Switch;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;

@Autonomous
public class RedAutoV1 extends OpMode {
    MecanumDrive drive = null;
    Feeder feeder = null;
    Flywheel flywheel = null;
    Intake intake = null;
    Pose2d startPose = null;
    ElapsedTime time = null;

    Action turnToObelisk = null;
    int step;
    boolean done;
    @Override
    public void init() {
        startPose = new Pose2d(-70, 24, Math.toRadians(0));
        time = new ElapsedTime();
        drive = new MecanumDrive(hardwareMap, startPose);
        feeder = new Feeder(hardwareMap,time);
        intake = new Intake(hardwareMap, time);
        flywheel= new Flywheel(hardwareMap);

        turnToObelisk = drive.actionBuilder(startPose)
                .strafeTo(new Vector2d(-16,-16))
                .turnTo(Math.toRadians(220))
                .build();
                //.strafeToSplineHeading(new Vector2d(-16,16), Math.toRadians(135)).build();

        step = 0;
        done = false;


    }
    @Override
    public void start(){

    }

    @Override
    public void loop() {





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
                done = turnToObelisk.run(new TelemetryPacket());
                if(done){
                    step = 5;
                }
                break;
            case 5:
                done = feeder.spit(2,time.seconds());
                if (done){
                    step = 10;
                }
                break;
            case 10:
                done = intake.gulp(2,time.seconds());
                if (done){
                    step = 15;
                }
                break;
        }
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
