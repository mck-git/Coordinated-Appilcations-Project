package Shared.Exceptions;

public class CommandException extends Exception {
    private String message;
    public CommandException(String msg)
    {
        message = msg;
    }

    public String getMessage() {
        return message;
    }
}
