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
    public int session_id;
    // List of ROAs from db
    private List<Rtr.Roa> roas = new ArrayList<>();

    public Server(int cache_serial_num, List<Rtr.Roa> valid_roas, int session_id) {
        this.roas = valid_roas;
        this.serial_number = cache_serial_num;
        this.session_id = session_id;
    }

    public void server(Rtr db) {
        AtomicInteger numThreads = new AtomicInteger(0);
        // the list of threads is kept in a linked list
        ArrayList<Thread> client_list = new ArrayList<Thread>();

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

                Pdu pdu = new Pdu(this.getCurrCacheSerial(), this.getCurrRoas());

                Thread thrd = new Thread(new ServerThread(client, db, this.session_id));
                client_list.add(thrd);
                thrd.start();
                numThreads.incrementAndGet();
                System.out.println("Thread " + numThreads.get() + " started.");

                // ------------------------------------------------------------------------
                // a new router connected - start new session
                //RtrSession rtrSession = new RtrSession(getCurrCacheSerial(), getCurrPrefixes(), getCurrSessionId());
//
//                Pdu pdu = new Pdu(pdu_type,
//                        serial_number,
//                        seesion_id,
//                        length,
//                        flags,
//                        prefix_length, max_length, prefix, asn);
//                RtrSession rtr_session = new RtrSession(seesion_id, pdu);
                // ------------------------------------------------------------------------
            }
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void notifyChildren(Pdu pdu) {

    }

    public int getCurrCacheSerial() {
        return this.serial_number;
    }

    public List<List<String>> getCurrPrefixes() {
        List<List<String>> prefix_asn_pairs = new ArrayList<>();
        for (Rtr.Roa roa : this.roas) {
            List prefix_asn = new ArrayList<String>();

            prefix_asn.add(roa.getPrefix());
            prefix_asn.add(roa.getAsn());
            prefix_asn_pairs.add(prefix_asn);
        }

        return prefix_asn_pairs;
    }

    public List<Rtr.Roa> getCurrRoas() {
        return this.roas;
    }

    public int getCurrSessionId() {
        return this.session_id;
    }
}