package Server;

import org.jspace.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

    public static void main(String[] args)
    {
        Space lounge = new SequentialSpace();

        String localhostAddress = null;
        try {
            localhostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(localhostAddress);
        SpaceRepository repository = new SpaceRepository();
        repository.addGate("tcp://"+localhostAddress+":9002/?keep");
        repository.add("lounge", lounge);


    }
}