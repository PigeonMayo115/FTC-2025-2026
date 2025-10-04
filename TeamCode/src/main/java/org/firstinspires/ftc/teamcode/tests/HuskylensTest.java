package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;


@TeleOp
public class HuskylensTest extends LinearOpMode {

    private HuskyLens huskyLens;

    private final int readPeriod = 1;

    int id1CenterX;
    int id1CenterY;

    @Override
    public void runOpMode() throws InterruptedException {

        huskyLens = hardwareMap.get(HuskyLens.class, "huskyLens");

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
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        /*
         * The device uses the concept of an algorithm to determine what types of
         * objects it will look for and/or what mode it is in.  The algorithm may be
         * selected using the scroll wheel on the device, or via software as shown in
         * the call to selectAlgorithm().

         * Other algorithm choices for FTC might be: OBJECT_RECOGNITION, COLOR_RECOGNITION or OBJECT_CLASSIFICATION.
         */

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();

            /*
             * All algorithms, except for LINE_TRACKING, return a list of Blocks where a
             * Block represents the outline of a recognized object along with its ID number.
             * ID numbers allow you to identify what the device saw.  See the HuskyLens documentation
             * referenced in the header comment above for more information on IDs and how to
             * assign them to objects.
             *
             * Returns an empty array if no objects are seen.
             */
            HuskyLens.Block[] blocks = huskyLens.blocks();
            telemetry.addData("Block count", blocks.length);
            for (int i = 0; i < blocks.length; i++) {
                telemetry.addData("Block", blocks[i].toString());
                /*
                 * Here inside the FOR loop, you could save or evaluate specific info for the currently recognized Bounding Box:
                 * - blocks[i].width and blocks[i].height   (size of box, in pixels)
                 * - blocks[i].left and blocks[i].top       (edges of box)
                 * - blocks[i].x and blocks[i].y            (center location)
                 * - blocks[i].id                           (Color ID)
                 *
                 * These values have Java type int (integer).
                 */
            }

            if (blocks.length>0){

                for (int i = 0; i < blocks.length; i++){

                    if (blocks[i].id == 1){
                        //blue id = 1
                        //red id = 2
                        //gpp id = 3
                        //pgp id = 4
                        //ppg id = 5
                        id1CenterX = blocks[i].x;
                        id1CenterY = blocks[i].y;

                        telemetry.addData("blue pos", id1CenterX + "," + id1CenterY);
                    }

                }

            }

            telemetry.update();


        }
    }
}
