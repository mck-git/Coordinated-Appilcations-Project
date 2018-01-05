package Server;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.util.List;

enum Status {OPEN, LOCKED}

public class Room extends SequentialSpace implements Runnable
{
    private String name;
    private String owner;
    private Status status;


    public Room (String name, String owner)
    {
        this.name = name;
        this.owner = owner;
        this.status = Status.OPEN;
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
        int i =0;

        for (Object[] o : users)
        {
            users_string[i] = (String) o[1];
            i++;
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

    public void run ()
    {
        while(true)
        {
            displayMessages(getMessages());
            try {
                Thread.sleep(3000);
            } catch (Exception e) {}
        }
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
