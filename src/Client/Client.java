package Client;

import org.jspace.*;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args)
    {
        try {
            RemoteSpace room = new RemoteSpace("tcp://130.225.93.172:9002/lounge?keep");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Connected to server!");

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
