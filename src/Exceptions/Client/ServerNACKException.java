package Exceptions.Client;

public class ServerNACKException extends Exception {

    public ServerNACKException(String operation)
    {
        super("Server denied the " + operation + " operation.");
    }
}
