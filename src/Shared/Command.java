package Shared;

public class Command {
    private boolean forward;
    private boolean left;
    private boolean backward;
    private boolean right;
    private boolean rotateLeft;
    private boolean rotateRight;
    private boolean fire;
    private String message;

    public Command(boolean[] WASDLRF, String msg) {
        this.forward = WASDLRF[0];
        this.left = WASDLRF[1];
        this.backward = WASDLRF[2];
        this.right = WASDLRF[3];
        this.rotateLeft = WASDLRF[5];
        this.rotateRight = WASDLRF[4];
        this.fire = WASDLRF[6];

        this.message = msg;
    }
    public Command(boolean forward, boolean left, boolean backward, boolean right, boolean rotateLeft, boolean rotateRight, boolean fire, String msg) {
        this.forward = forward;
        this.left = left;
        this.backward = backward;
        this.right = right;
        this.rotateLeft = rotateLeft;
        this.rotateRight = rotateRight;
        this.fire = fire;

        this.message = msg;
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

    public String getMessage() {
        return message;
    }
}