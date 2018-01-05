package Server;

import org.jspace.SequentialSpace;
import org.jspace.Space;

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

    public void run ()
    {
        
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
