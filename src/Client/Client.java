package Client;

import org.jspace.*;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args)
    {

        String tcp = "tcp://10.16.174." + "236" + ":9002/lounge?keep";

        try
        {
            RemoteSpace room = new RemoteSpace(tcp);
            System.out.println("Connected to server!");

            while( true )
            {

                room.put("message","#hacked");
                try
                {
                    room.get(new ActualField("keyHacked"));
                } catch (InterruptedException e) {}
            }

        } catch (IOException e ){System.out.println("Host not found");}


//        Scanner sc = new Scanner(System.in);
//
//        System.out.print("Please type your username: ");
//
//        String usr = sc.next();
//
//        room.put(usr);
//
//
//        while(true)
//        {
//            if(sc.hasNext())
//            {
//                room.put(sc.next(),usr);
//            }
//
//
//        }

    }

}
