package Client;

import org.jspace.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String tcp = "tcp://10.16.172.127:9002/lounge?keep";

        try
        {
            RemoteSpace room = new RemoteSpace(tcp);
            System.out.println("Connected to server!");

            System.out.print("Please choose your desired username: ");
            String usrName;

            usrName = sc.next();

            System.out.println("Your username is: " + usrName);

            room.get(new ActualField("__registration_lock"));
            List<Object[]> userList = room.queryAll(new ActualField("user"),new FormalField(String.class));

            System.out.println("Checking if your username is taken...");
            boolean taken = false;
            for(int i = 0; i < userList.size(); i++)
            {
                String user = (String) userList.get(i)[1];

                if (user.equals(usrName))
                {
                    System.out.println("That username was taken :(");
                    taken = true;
                    break;
                }
            }

            if (taken)
            {
                System.out.println("Please try again another time :)");
                return;
            }

            System.out.println("You chose a unique username! Good job (y)");
            room.put("user", usrName);

            room.put("__registration_lock");


        } catch (Exception e) {e.printStackTrace();}
    }

}
