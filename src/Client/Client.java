package Client;

import org.jspace.*;

import java.util.List;
import java.util.Scanner;

public class Client {
    static RemoteSpace lounge;
    static String tcp = "tcp://10.16.172.127:9002/lounge?keep";


    public static void main(String[] args)
    {
        while(!initialize()){}

    }


    // Initialize user when first starting app
    private static boolean initialize() {
        Scanner sc = new Scanner(System.in);

        try
        {
            // Connect to the lounge
            lounge = new RemoteSpace(tcp);
            System.out.println("Connected to server!");

            System.out.print("Please choose your desired username: ");
            String usrName;

            // Wait for user to input desired username
            usrName = sc.next();

            System.out.println("Your username is: " + usrName);

            // Take the lock - Assures no two users try to register at once
            lounge.get(new ActualField("__registration_lock"));

            // Read all existing users in the lounge touplespace
            List<Object[]> userList = lounge.queryAll(new ActualField("user"),new FormalField(String.class));

            System.out.println("Checking if your username is taken...");
            boolean taken = false;

            // Go through found users - if one matches: set taken to true
            for (Object[] u : userList)
            {
                String user = (String) u[1];

                // If found username matches with desired username: break and set taken to true
                if (user.equals(usrName))
                {
                    System.out.println("That username was taken :(");
                    taken = true;
                    break;
                }
            }

            // If username is taken: return
            if (taken)
            {
                System.out.println("Please try again another time :)");
            }

            // If username is not taken: add username to lounge touplespace
            else
            {

                System.out.println("You chose a unique username! Good job (y)");
                lounge.put("user", usrName);
            }

            // Release lock
            lounge.put("__registration_lock");
            return !taken;

        } catch (Exception e) {e.printStackTrace(); return false;}
    }

}
