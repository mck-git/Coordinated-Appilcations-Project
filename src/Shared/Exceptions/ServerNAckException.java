package Shared.Exceptions;

public class ServerNAckException extends Exception {

    public ServerNAckException(String operation)
    {
        super("Server denied the " + operation + " operation.");
    }
}
