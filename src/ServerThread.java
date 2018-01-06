import java.io.*;
import java.net.Socket;

/*
 * Individual ServerThread listens for the client to tell it what command to run, then
 * runs that command and sends the output of that command to the client
 *
 */
public class ServerThread extends Thread {
    Socket client = null;
    Rtr db;
    PrintWriter output;
    BufferedReader input;
    int session_id;

    public ServerThread(Socket client, Rtr db, int session_id) {
        this.db = db;
        this.client = client;
        this.session_id = session_id;
    }

    public void run() {
        System.out.print("Accepted connection. ");

        int serial_num = 0; //TODO: ge serial from the router
        //int pdu_type_from_router = getPduTypeFromPacket(inString);
        //int serial_num_from_router = getSerialNumFromPacket(inString);
        Pdu pdu = new Pdu(serial_num, db.getValidated_roas());

        ByteArrayOutputStream outString;

        // Router sends reset query
        try {
            output = new PrintWriter(client.getOutputStream(), true);

            // Send cache response
            pdu.cacheResponsePDU(session_id);
            outString = pdu.encode(null);
            System.out.println("Server sending result to client");
            // send the result of the command to the client
            output.println(outString);

            // Send IPv6
            int range = db.getValidated_roas().size();
            for(int i = 0; i < range; i++) {
                pdu.ip6Prefix();

                outString = pdu.encode(pdu.getRoaInPos(i));
                System.out.println("Server sending result to client");
                output.println(outString);
            }

            // Send EOD
            pdu.endOfData(session_id);
            outString = pdu.encode(null);
            System.out.println("Server sending result to client");
            // send the result of the command to the client
            output.println(outString);
        } catch (IOException e) {
                e.printStackTrace();
        }

        try {
            // open a new PrintWriter and BufferedReader on the socket
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            System.out.print("Reader and writer created. ");
            String inString;
            // read the command from the client
            while  ((inString = input.readLine()) == null);

            System.out.println("Read command " + inString);

            // run the command using CommandExecutor and get its output

            int range;

            //if (Integer.parseInt(inString) == 6) {
            if (Integer.parseInt(inString) == 6) {
                range = db.getValidated_roas().size();
                for(int i = 0; i < range; i++) {
                    pdu.ip4Prefix();

                    outString = pdu.encode(pdu.getRoaInPos(i));
                    System.out.println("Server sending result to client");
                    // send the result of the command to the client
                    output.println(outString);
                }

            } else {
                System.out.println("Read command2 " + inString);

                outString = CommandExecutor.run(inString, this.db, pdu, this.session_id);
                System.out.println("Server sending result to client");
                // send the result of the command to the client
                output.println(outString);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // close the connection to the client
            try {
                client.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Output closed.");
        }

    }

    private int getPduTypeFromPacket(int inString) {
        return 6; // TODO: finish
    }
}

