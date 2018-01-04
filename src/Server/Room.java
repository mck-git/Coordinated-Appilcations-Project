package Server;

import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Room extends SequentialSpace
{
    private String name;
    private String owner;


    public Room (String name, String owner)
    {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
}
