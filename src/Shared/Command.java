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

    public String toString()
    {
        StringBuilder cmd = new StringBuilder();
        cmd.append(username);
        cmd.append(" command: ");

        if (forward)
            cmd.append(", forward");
        if (strafeLeft)
            cmd.append(", strafeLeft");
        if (strafeRight)
            cmd.append(", strafeRight");
        if (rotateLeft)
            cmd.append(", rotateLeft");
        if (rotateRight)
            cmd.append(", rotateRight");
        if (fire)
            cmd.append(", fire");

        return cmd.toString();
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
