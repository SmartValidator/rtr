import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    static String run(String commandString, Rtr db) {
        String result = "";
        String line;
        try {
            // start the shell command running as a child processes
            if (parseCommand(commandString) == "print") {
                System.out.println("we are printing");
                db.printRoas();
            } else if(parseCommand(commandString) == "send") {
                System.out.println("we are sending");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
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

            // Send ROAs
            case 2:
                commandString = "send";
                break;
        }

        return commandString;
    }
}