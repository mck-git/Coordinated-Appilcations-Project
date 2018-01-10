package Shared;

public class Command {
    private boolean forward;
    private boolean strafeLeft;
    private boolean backward;
    private boolean strafeRight;
    private boolean rotateLeft;
    private boolean rotateRight;
    private boolean fire;
    private String username;

    public Command(boolean[] WASDLRF, String user) {
        this.forward = WASDLRF[0];
        this.strafeLeft = WASDLRF[1];
        this.backward = WASDLRF[2];
        this.strafeRight = WASDLRF[3];
        this.rotateLeft = WASDLRF[5];
        this.rotateRight = WASDLRF[4];
        this.fire = WASDLRF[6];

        this.username = user;
    }
    public Command(boolean forward, boolean left, boolean backward, boolean right, boolean rotateLeft, boolean rotateRight, boolean fire) {
        this.forward = forward;
        this.strafeLeft = left;
        this.backward = backward;
        this.strafeRight = right;
        this.rotateLeft = rotateLeft;
        this.rotateRight = rotateRight;
        this.fire = fire;
    }

    public String toString()
    {
        String cmd = username + " command: ";
        if (forward)
            cmd += ", forward";
        if (strafeLeft)
            cmd += ", strafeLeft";
        if (strafeRight)
            cmd += ", strafeRight";
        if (rotateLeft)
            cmd += ", rotateLeft";
        if (rotateRight)
            cmd += ", rotateRight";
        if (fire)
            cmd += ", fire";

        return cmd;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return backward;
    }

    public boolean isStrafeLeft() {
        return strafeLeft;
    }

    public boolean isStrafeRight() {
        return strafeRight;
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

    public String getUsername() { return username; }
}
