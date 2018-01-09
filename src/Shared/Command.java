package Shared;

public class Command {
    private boolean forward = false;
    private boolean backward = false;
    private boolean left = false;
    private boolean right = false;
    private boolean rotateRight = false;
    private boolean rotateLeft = false;
    private boolean fire = false;

    public Command(boolean[] WASDLRF) {
        this.forward = WASDLRF[0];
        this.backward = WASDLRF[1];
        this.left = WASDLRF[2];
        this.right = WASDLRF[3];
        this.rotateRight = WASDLRF[4];
        this.rotateLeft = WASDLRF[5];
        this.fire = WASDLRF[6];
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return backward;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isRotateRight() {
        return rotateRight;
    }

    public boolean isRotateLeft() {
        return rotateLeft;
    }

    public boolean isFire() {
        return fire;
    }

}
