package Server;

import Shared.Command;
import Shared.GameController;
import Shared.GameState;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;

import java.util.ArrayList;
import java.util.List;

enum Status {OPEN, LOCKED}

public class Room extends SequentialSpace implements Runnable
{
    private String name;
    private String owner;
    private Status status;

    private GameState master_gs;
    private GameController g_controller;


    public Room (String name, String owner)
    {
        this.name = name;
        this.owner = owner;
        this.status = Status.OPEN;

        this.master_gs = new GameState();
        this.g_controller = new GameController();
        this.put("gamestate", master_gs);
    }

    public void run ()
    {
        while(true)
        {
            try {
                updateGamestate();
                System.out.println(master_gs.getMessages());
                Thread.sleep(3000);
            } catch (Exception ignored) {}
        }
    }

    private void updateGamestate() throws Exception
    {
        master_gs = g_controller.updatePlayerList(getUsers());
        master_gs = g_controller.applyCommands(getCommands());

        // getP?
        this.get(new ActualField("gamestate"), new FormalField(GameState.class));

        this.put("gamestate", master_gs);
    }

    private List<Command> getCommands()
    {
        List<Command> commands = new ArrayList<Command>();
        List<Object[]> tuple_commands = this.queryAll(new ActualField("command"),
                                                      new FormalField(String.class),
                                                      new FormalField(Command.class));
        System.out.println("Got following " + tuple_commands.size() + " commands:");
        for (Object[] o : tuple_commands)
        {
            System.out.println(((Command) o[2]).toString());
            commands.add((Command) o[2]);
        }

        return commands;
    }

    public String[] getMessages()
    {
        List<Object[]> messages = this.queryAll(
                new ActualField("message"),
                new FormalField(String.class),
                new FormalField(String.class)
        );
        String[] messages_string = new String[messages.size()];
        int i = 0;
        for (Object[] o : messages)
        {
            messages_string[i] = "[" + name + "]" + o[1] + ": " + o[2];
            i++;
        }

        return messages_string;
    }

    public String[] getUsers()
    {
        List<Object[]> users = this.queryAll(new FormalField(String.class));
        String[] users_string = new String[users.size()];

        for (int i = 0; i < users.size(); i++)
        {
            users_string[i] = (String) users.get(i)[0];
        }

        return users_string;
    }

    private void displayMessages(String[] messages)
    {
        for (String s : messages)
        {
            System.out.println(s);
        }
    }

    private void displayUsers(String[] users)
    {
        System.out.println("Users in room " +name+ ":");
        for (String s : users)
        {
            System.out.print(" " + s);
        }
        System.out.println();
    }



    public void lock(String name, String user)
    {
        boolean ack = false;
        if (this.name.equals(name))
        {
            if (this.owner.equals(user))
            {
                this.status = Status.LOCKED;
            }
        }
    }

    public boolean isOpen()
    {
        return (status == Status.OPEN);
    }

    public String getName()
    {
        return name;
    }

    public String getOwner()
    {
        return owner;
    }
}
