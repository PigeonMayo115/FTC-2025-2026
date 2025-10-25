package org.firstinspires.ftc.teamcode.custom;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher {

    DcMotorEx motor = null;

    public Launcher(HardwareMap hwmap) {
        motor = hwmap.get(DcMotorEx.class, "flywheel");


    }

}
