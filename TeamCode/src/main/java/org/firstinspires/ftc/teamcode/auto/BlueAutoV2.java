package org.firstinspires.ftc.teamcode.auto;

import android.widget.Switch;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.custom.CheeksKicker;
import org.firstinspires.ftc.teamcode.custom.Feeder;
import org.firstinspires.ftc.teamcode.custom.Flywheel;
import org.firstinspires.ftc.teamcode.custom.Intake;
import org.firstinspires.ftc.teamcode.custom.WaitAuto;

@Autonomous
public class BlueAutoV2 extends OpMode {

    MecanumDrive drive = null;
    Flywheel flywheel = null;
    Intake intake = null;
    Pose2d startPose = null;
    ElapsedTime time = null;
    CheeksKicker cheeksKicker = null;
    Action turnToObelisk = null;
    Action exitLaunchZoneTurn = null;
    Action exitLaunchZoneReverse = null;
    int step = 0;
    boolean done = false;
    double flywheelVel = 1200;
    @Override
    public void init() {
        startPose = new Pose2d(0, 0, Math.toRadians(0));
        time = new ElapsedTime();
        drive = new MecanumDrive(hardwareMap, startPose);
        intake = new Intake(hardwareMap, time);
        flywheel= new Flywheel(hardwareMap);
        cheeksKicker = new CheeksKicker(hardwareMap, time);

        turnToObelisk = drive.actionBuilder(startPose)
                .lineToXConstantHeading(48)
                .turn(Math.toRadians(175))
                .build();

        exitLaunchZoneTurn = drive.actionBuilder(new Pose2d(0,0,Math.toRadians(0)))
                .turn(90)
                .build();

        exitLaunchZoneReverse = drive.actionBuilder(new Pose2d(0,0,0))
                .lineToXConstantHeading(-24)
                .build();
    }

    @Override
    public void start(){
        cheeksKicker.intakeState(3);
        cheeksKicker.kickerRetract();
        done = false;
    }

    @Override
    public void loop() {
        switch (step){
            // go to initial launch position
            case 0:
                done = !turnToObelisk.run(new TelemetryPacket());

                flywheel.setVelocity(flywheelVel);
                if (done){
                    done = false;
                    step = 5;
                }
                break;
            //launch first ball by extending kicker, then after 0.5 seconds retract.
            case 5:
                cheeksKicker.kickerExtend();
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    done = false;
                    cheeksKicker.kickerRetract();
                    step = 10;
                }
                break;
            //wait 0.5 seconds for the kicker to retract
            case 10:
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    done = false;
                    step = 15;
                }
                break;
            //cycle next ball in and then kick servo
            case 15:
                cheeksKicker.intakeState(2);
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    cheeksKicker.kickerExtend();
                    done = false;
                    step = 20;
                }
                break;
            case 20:
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    cheeksKicker.kickerRetract();
                    done = false;
                    step = 22;
                }

                //cycle last ball
            case 22:
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    cheeksKicker.intakeState(1);
                    done = false;
                    step = 25;
                }
                break;
            case 25:
                done = WaitAuto.wait(2, time.seconds());
                if (done){
                    cheeksKicker.kickerExtend();
                    done = false;
                    step = 30;
                }
                break;
            case 30:
                done = WaitAuto.wait(2,time.seconds());
                if (done){
                    done = false;
                    step = 35;
                }
                break;
            case 35:
                done = !exitLaunchZoneTurn.run(new TelemetryPacket());
                if (done){
                    done = false;
                    step = 40;
                }
                break;
            case 40:
                done = !exitLaunchZoneReverse.run(new TelemetryPacket());
                if (done){
                    done = false;
                    step = 45;
                }
                break;
        }
        telemetry.addData("step",step);
        telemetry.update();
    }
}
