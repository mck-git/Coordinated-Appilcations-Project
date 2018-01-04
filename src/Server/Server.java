package Server;

import org.jspace.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

// Templates
// client template: ("user", string name)
// room template: ("room", string name, string owner)
// createRoom template: ("createRoom", string name, string owner)


public class Server {
    static Space lounge = new SequentialSpace();
    static ArrayList<Room> rooms = new ArrayList<Room>();

    public static void main(String[] args)
    {


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

        try {
            // Setup registration_lock
            lounge.put("__registration_lock");

            while (true)
            {
                List<Object[]> users = lounge.queryAll(new ActualField("user"), new FormalField(String.class));
                for (Object[] o : users)
                {
                    System.out.println(o[1]);
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) { e.printStackTrace(); }


    }

    private static boolean createRoom(String name, String owner)
    {
        // Check unique
        boolean taken = false;




    }


}