package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.teamcode.custom.HuskyLensCustom;

import java.util.concurrent.TimeUnit;


@TeleOp
public class HuskylensTest extends LinearOpMode {

    //private HuskyLens huskyLens;

    //public HuskyLensCustom huskyLens;
    public HuskyLens huskyLens;

    private final int readPeriod = 1;


    @Override
    public void runOpMode(){

        //HuskyLensCustom huskyLens = new HuskyLensCustom(hardwareMap, telemetry);

        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");;

        //sets rate limit to read once per second I think
        Deadline rateLimit = new Deadline(readPeriod, TimeUnit.SECONDS);

        /*
         * Immediately expire so that the first time through we'll do the read.
         */
        rateLimit.expire();

        /*
         * Basic check to see if the device is alive and communicating.  This is not
         * technically necessary here as the HuskyLens class does this in its
         * doInitialization() method which is called when the device is pulled out of
         * the hardware map.  However, sometimes it's unclear why a device reports as
         * failing on initialization.  In the case of this device, it's because the
         * call to knock() failed.
         */
        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with huskyLens");
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();

            //huskyLens.blocksList();

            HuskyLens.Block[] blocks = huskyLens.blocks();
            for (int i = 0; i < blocks.length; i ++) {
                telemetry.addData("Block count", blocks.length);
                telemetry.addData("Blocks", blocks);
                telemetry.update();
            }

            telemetry.update();

        }
    }
}
