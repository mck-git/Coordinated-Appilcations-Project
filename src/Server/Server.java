package Server;

import org.jspace.*;

public class Server {

    public static void main(String[] args)
    {
        Space lounge = new SequentialSpace();

        SpaceRepository repository = new SpaceRepository();
        repository.addGate("tcp://127.0.0.1:9002/?keep");
        repository.add("lounge", lounge);

        while (true) {
            try {
                Object[] t = lounge.query(new FormalField(String.class), new FormalField(String.class));
                System.out.println(t[0] + ":" + t[1]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}