package Client;

import org.jspace.*;

import java.util.List;
import java.util.Scanner;

public class Client {
    static RemoteSpace lobby;
    static String tcp = "tcp://" + "10.16.170.47" + ":9002/lounge?keep";
    static Scanner sc;
    static String userName = "";

    public static void main(String[] args)
    {
        sc = new Scanner(System.in);

        System.out.println("Welcome! Please wait while connecting to the server.");
        initialize(sc.next());

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
    public static boolean initialize(String nameInput) {


        try
        {
            // Connect to the lounge
            lobby = new RemoteSpace(tcp);
            System.out.println("Connected to server!");
            boolean taken = false;


            System.out.print("Please choose your desired username: ");

            // Wait for user to input desired username

            System.out.println("Your username is: " + nameInput);

            // Take the lock - Assures no two users try to register at once
            lobby.get(new ActualField("__registration_lock"));

            // Read all existing users in the lounge touplespace
            List<Object[]> userList = lobby.queryAll(new ActualField("user"), new FormalField(String.class));

            System.out.println("Checking if your username is taken...");


            // Go through found users - if one matches: set taken to true
            for (Object[] u : userList) {
                String user = (String) u[1];

                // If found username matches with desired username: break and set taken to true
                if (user.equals(nameInput)) {
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
                lobby.put("user", nameInput);
                userName = nameInput;
            }

            // Release lock
            lobby.put("__registration_lock");

            return !taken;



        } catch (Exception e) {e.printStackTrace(); return false;}
    }


    private static void createRoom(String name) {
        lobby.put("createRoom",name, userName);

        try
        {
            System.out.println("Trying to create room...");
            Object[] ack = lobby.get(new ActualField(userName), new ActualField("response"), new FormalField(Boolean.class));

            if ((boolean) ack[2])
                System.out.println("Room created!");
            else
                System.out.println("Room did not get created. Try with a different name");

        } catch (Exception e) {System.out.println("Request failed, please try again");}
    }
}
