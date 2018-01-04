package Client;

import org.jspace.*;

import java.util.List;
import java.util.Scanner;

public class Client {
    static RemoteSpace lounge;
    static String tcp = "tcp://10.16.172.127:9002/lounge?keep";
    static Scanner sc;
    static String usrName;

    public static void main(String[] args)
    {
        sc = new Scanner(System.in);
        initialize();

        while (true)
        {

            System.out.println("\n\nWhat do you want to do?");
            String input = sc.next();

            if (input.equals("Create_room"))
            {
                System.out.print("Choose a name for your room: ");
                String name = sc.next();
                createRoom(name);
            }

            if (input.equals("Exit"))
            {
                System.out.println("We hope you enjoyed your stay. Goodbye");
                break;
            }

        }

        System.exit(0);
    }


    // Initialize user when first starting app
    private static void initialize() {


        try
        {
            // Connect to the lounge
            lounge = new RemoteSpace(tcp);
            System.out.println("Connected to server!");
            boolean taken = false;

            while (true) {

                System.out.print("Please choose your desired username: ");

                // Wait for user to input desired username
                usrName = sc.next();

                System.out.println("Your username is: " + usrName);

                // Take the lock - Assures no two users try to register at once
                lounge.get(new ActualField("__registration_lock"));

                // Read all existing users in the lounge touplespace
                List<Object[]> userList = lounge.queryAll(new ActualField("user"), new FormalField(String.class));

                System.out.println("Checking if your username is taken...");


                // Go through found users - if one matches: set taken to true
                for (Object[] u : userList) {
                    String user = (String) u[1];

                    // If found username matches with desired username: break and set taken to true
                    if (user.equals(usrName)) {
                        System.out.println("That username was taken :(");
                        taken = true;
                        break;
                    }
                }

                // If username is taken: return
                if (taken) {
                    System.out.println("Please try again another time :)");
                }

                // If username is not taken: add username to lounge touplespace
                else {

                    System.out.println("You chose a unique username! Good job (y)");
                    lounge.put("user", usrName);
                }

                // Release lock
                lounge.put("__registration_lock");
                if (!taken)
                    break;
            }

        } catch (Exception e) {e.printStackTrace();}
    }


    private static void createRoom(String name) {
        lounge.put("createRoom",name, usrName);

        try
        {
            System.out.println("Trying to create room...");
            Object[] ack = lounge.get(new ActualField(usrName), new ActualField("response"), new FormalField(Boolean.class));

            if ((boolean) ack[2])
                System.out.println("Room created!");
            else
                System.out.println("Room did not get created. Try with a different name");

        } catch (Exception e) {System.out.println("Request failed, please try again");}
    }
}
