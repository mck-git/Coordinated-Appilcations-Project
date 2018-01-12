package Server.Networking;

import Shared.Command;
import Shared.GameController;
import Shared.GameState;
import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;

import java.util.*;

import static Shared.Constants.*;

enum Status {OPEN, LOCKED}

public class Room extends SequentialSpace implements Runnable
{
    private String name;
    private String owner;
    private Status status;
    private GameState master_gs;
    private GameController g_controller;

    public Room (String name, String owner) throws InterruptedException
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
        try {
            while (true) {
                updateGamestate();
                Thread.sleep(1000 / SERVER_TICKRATE );
            }
        } catch (Exception e){e.printStackTrace();}
    }

    private void updateGamestate() throws Exception
    {
        master_gs = g_controller.updateActivePlayers(getUsers());
        master_gs = g_controller.applyCommands(getCommands());
        master_gs = g_controller.updateStatus();

        this.getp(new ActualField("gamestate"), new FormalField(GameState.class));
        this.put("gamestate", master_gs);
    }

    private List<Command> getCommands()
    {
        List<Command> commands = new ArrayList<>();
        List<Object[]> tuple_commands = this.queryAll(new ActualField("command"),
                                                      new FormalField(String.class),
                                                      new FormalField(Command.class));
        for (Object[] tuple_command : tuple_commands) {
            commands.add((Command) tuple_command[2]);
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
            messages_string[i++] = "[" + name + "]" + o[1] + ": " + o[2];
        }

        return messages_string;
    }

    public String[] getUsers()
    {
        Object[] users = this.queryAll(new FormalField(String.class)).toArray();
        String[] users_string = new String[users.length];
        for (int i = 0; i < users.length; i++)
        {
            users_string[i] = (String) ((Object[])(users[i]))[0];
        }

        return users_string;
    }

    public void lock(String name, String user)
    {
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
