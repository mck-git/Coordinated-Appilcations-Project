package Server;

import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

import java.util.List;

enum Status {OPEN, CLOSED}

public class Room extends SequentialSpace implements Runnable
{
    private String name;
    private String owner;
    private Status  status;


    public Room (String name, String owner)
    {
        this.name = name;
        this.owner = owner;
        this.status = Status.OPEN;
        this.run();
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
            messages_string[i] = "" + o[1] + ": " + o[2];
        }

        return messages_string;
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

    public boolean isOpen()
    {
        return (status == Status.OPEN);
    }


    public void close()
    {
        status = Status.CLOSED;
    }


    public String getName() {
        return name;
    }

}
