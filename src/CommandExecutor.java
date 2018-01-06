import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/*
 * Class containing methods for taking the command string (a string with a digit 1-6)
 * received from the server and converting that into its respective Linux shell command,
 * then executing it and returning the results.
 */
public class CommandExecutor {
    /**
     * Runs the shell command after first using parseCommand() to determine which
     * command to run.
     *
     * @param commandString		A string containing a single digit, 1-6;
     * @return			A string containing the results of the shell command.
     */
    static ByteArrayOutputStream run(String commandString, Rtr db, Pdu pdu, int session_id) {
        String result = "";
        String line;
        ByteArrayOutputStream pdu_bytes = null;
        try {
            // start the shell command running as a child processes
            if (parseCommand(commandString) == "print") {
                System.out.println("we are printing");
                result = db.printRoas();

            } else if(parseCommand(commandString) == "SERIAL_NOTIFY") {
                System.out.println("SERIAL_NOTIFY");
                pdu.serialNotify(session_id);
            } else if(parseCommand(commandString) == "SERIAL_QUERY") {
                System.out.println("SERIAL_QUERY");
                pdu.serialQuery(session_id);
            } else if(parseCommand(commandString) == "RESET_QUERY") {
                System.out.println("RESET_QUERY");
                pdu.resetQuery();
            } else if(parseCommand(commandString) == "CACHE_RESPONSE") {
                System.out.println("CACHE_RESPONSE");
                pdu.cacheResponsePDU(session_id);
            } else if(parseCommand(commandString) == "IPV4_PREFIX") {
                System.out.println("IPV4_PREFIX");
                pdu.ip4Prefix();
            } else if(parseCommand(commandString) == "IPV6_PREFIX") {
                System.out.println("IPV6_PREFIX");
                pdu.ip6Prefix();
            } else if(parseCommand(commandString) == "EOD") {
                System.out.println("EOD");
                pdu.endOfData(session_id);
            } else if(parseCommand(commandString) == "CACHE_RESET") {
                System.out.println("CACHE_RESET");
                pdu.cacheReset();
            }
            pdu_bytes = pdu.encode(null);

            // TODO: Implement here the sending protocol to the router

            result += "testing the result var";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pdu_bytes;
    }

    /**
     * Converts the digit string into its respective shell command.
     *
     * @param inputString
     * @return
     */
    static String parseCommand(String inputString) {
        int inputInt = Integer.parseInt(inputString);
        String commandString = "";
        switch (inputInt) {
            // Print ROAs
            case 1:
                commandString = "print";
                break;

            case 2: // SERIAL_NOTIFY
                commandString = "SERIAL_NOTIFY";
                break;

            case 3: // SERIAL_QUERY
                commandString = "SERIAL_QUERY";
                break;

            case 4: // RESET_QUERY
                commandString = "RESET_QUERY";
                break;

            case 5: // CACHE_RESPONSE
                commandString = "CACHE_RESPONSE";
                break;

            case 6: // IPV4_PREFIX
                commandString = "IPV4_PREFIX";
                break;

            case 7: // IPV6_PREFIX
                commandString = "IPV6_PREFIX";
                break;

            case 8: // EOD
                commandString = "EOD";
                break;

            case 9: // CACHE_RESET
                commandString = "CACHE_RESET";
                break;
        }

        return commandString;
    }
}