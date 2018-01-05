import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {

    // The Serial Number of the RPKI Cache
    public int serial_number;
    // Session ID to identify the instance of the cache
    public String session_id = "0";
    // List of ROAs from db
    private List<Rtr.Roa> roas = new ArrayList<>();

    public void server(Rtr db) {
        AtomicInteger numThreads = new AtomicInteger(0);
        // the list of threads is kept in a linked list
        ArrayList<Thread> list = new ArrayList<Thread>();

        try {
            // listen for incoming connections on port 15432
            ServerSocket socket = new ServerSocket(15432);
            System.out.println("Server listening on port 15432");

            // loop (forever) until program is stopped
            while(true) {
                // accept a new connection
                Socket client = socket.accept();
                // start a new ServerThread to handle the connection and send
                // output to the client
                Thread thrd = new Thread(new ServerThread(client, db));
                list.add(thrd);
                thrd.start();
                numThreads.incrementAndGet();
                System.out.println("Thread " + numThreads.get() + " started.");

                // ------------------------------------------------------------------------
                // a new router connected - start new session
                RtrSession rtrSession = new RtrSession(getCurrCacheSerial(), getCurrPrefixes(), getCurrSessionId());

                Pdu pdu = new Pdu(pdu_type,
                        serial_number,
                        seesion_id,
                        length,
                        flags,
                        prefix_length, max_length, prefix, asn);
                RtrSession rtr_session = new RtrSession(seesion_id, pdu);
                // ------------------------------------------------------------------------
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public int getCurrCacheSerial() {
        return this.serial_number;
    }

    public List<Rtr.Roa> getCurrPrefixes() {
        return this.roas;
    }

    public String getCurrSessionId() {
        return this.session_id;
    }
}