package org.firstinspires.ftc.teamcode.tests;

import com.acmerobotics.dashboard.DashboardCore;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "CornerDriveElliot")
public class CornerDriveElliot extends OpMode{

    //member variables for the opmode
    private MecanumDrive drive;
    private Action currentAction;

    private FtcDashboard dashboard;

    Pose2d startPose = null;

    TelemetryPacket telemetryPacket = new TelemetryPacket();


    @Override
    public void init() {
        //declares mecanum drive and defines the real starting position of the robot on the field
        startPose = new Pose2d(0,0,Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, startPose);

        dashboard = FtcDashboard.getInstance();


/*
        currentAction = drive.actionBuilder(startPose)

                //move forward 30 inches
                .lineToY(48)
                //stop and wait for 1 second
                .stopAndAdd(new SleepAction(1))
                .strafeTo(new Vector2d(48,48))
                //strafe to the left 30 inches while maintaining
                //the current y position
                //.strafeTo(new Vector2d(-30,0))
                //turn to heading 180
                //.turnTo(Math.toRadians(180))
                //build tha thing
                .build();




*/

    }

    @Override
    public void loop() {
        //legit no idea what this does, AI says that this checks if
        //the currect action has completed yet
        /*if (currentAction.run(telemetryPacket)){

        } else {
            //if the current action is complete, stop the opmode.
            requestOpModeStop();
        }
*/

        Actions.runBlocking(drive.actionBuilder(startPose)
                .lineToY(48)
                //stop and wait for 1 second
                .stopAndAdd(new SleepAction(1))
                .strafeTo(new Vector2d(48,48))
                .build()
        );
        requestOpModeStop();
/*
        FtcDashboard dash = FtcDashboard.getInstance();
        TelemetryPacket p = new TelemetryPacket();
        dash.sendTelemetryPacket(p);
*/
        //Actions.runBlocking(currentAction);
    }
}
