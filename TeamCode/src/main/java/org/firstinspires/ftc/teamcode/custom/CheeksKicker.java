package org.firstinspires.ftc.teamcode.custom;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class CheeksKicker {


    Servo kicker = null;
    Servo leftCheek = null;
    Servo rightCheek = null;
    HardwareMap hwMap = null;

    boolean op1 = false;
    boolean op2 = false;
    
    boolean op3 = false;
    boolean op4 = false;
    final double kickerMin = 1;
    final double kickerMax = -1;
    final double leftMin = 0.65;
    final double leftMax = 0.35;
    final double rightMin = 0.2;
    final double rightMax = 0.5;

    public CheeksKicker(HardwareMap hardwareMap) {
        hwMap = hardwareMap;
        kicker = hwMap.servo.get("kicker");
        leftCheek = hwMap.servo.get("leftCheek");
        rightCheek = hwMap.servo.get("rightCheek");

    }

    public boolean leftUp(){
        leftCheek.setPosition(leftMax);
        if (leftCheek.getPosition() == leftMax){
            return true;
        } else {
            return false;
        }
    }

    public boolean leftDown(){
        leftCheek.setPosition(leftMin);
        if (leftCheek.getPosition() == leftMin){
            return true;
        } else {
            return false;
        }
    }

    public boolean rightUp(){
        rightCheek.setPosition(rightMax);
        if (rightCheek.getPosition() == rightMax){
            return true;
        } else {
            return false;
        }
    }

    public boolean rightDown(){
        rightCheek.setPosition(rightMin);
        if (rightCheek.getPosition() == rightMin){
            return true;
        } else {
            return false;
        }
    }

    public boolean kickerExtend(){
        kicker.setPosition(kickerMax);
        if (kicker.getPosition() == kickerMax){
            return true;
        } else {
            return false;
        }
    }


    public boolean kickerRetract(){
        kicker.setPosition(kickerMin);
        if (kicker.getPosition() == kickerMin){
            return true;
        } else {
            return false;
        }
    }

    public boolean launchState(int state, boolean isWheelAtVel){
        switch (state){
            case 1:
                op1 = leftDown();
                op2 = rightDown();
                if (isWheelAtVel) {
                    op3 = kickerExtend();
                    if (op3){
                        op4 = kickerRetract();
                    }
                }
                break;
            case 2:
                op1 = leftUp();
                op2 = rightDown();
                if (isWheelAtVel) {
                    op3 = kickerExtend();
                    if (op3){
                        op4 = kickerRetract();
                    }
                }
                break;
            case 3:
                op1 = leftUp();
                op2 = rightUp();
                if (isWheelAtVel) {
                    op3 = kickerExtend();
                    if (op3){
                        op4 = kickerRetract();
                    }
                }
                break;
            default:
                op1 = leftUp();
                op2 = rightUp();
                if (isWheelAtVel) {
                    op3 = kickerExtend();
                    if (op3){
                        op4 = kickerRetract();
                    }
                }
                break;
        }
        if (op1 && op2){
            op1 = false;
            op2 = false;
            op3 = false;
            op4 = false;
            return true;
        } else {
            return false;
        }

    }
    
    public boolean intakeState (int state){
        switch (state){
            case 1:
                op1 = leftDown();
                op2 = rightDown();
                break;
            case 2:
                op1 = leftUp();
                op2 = rightDown();
                break;
            case 3:
                op1 = leftUp();
                op2 = rightUp();
                break;
            default:
                op1 = leftUp();
                op2 = rightUp();
                break;
        }
        if (op1 && op2){
            op1 = false;
            op2 = false;
            return true;
        } else {
            return false;
        }
    }




}
