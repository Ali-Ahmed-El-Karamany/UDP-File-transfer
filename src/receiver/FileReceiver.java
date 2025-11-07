package receiver;

/**
 * FileReceiver is the main entry point of the receiver program.
 * It reads command-line arguments and creates a UDPReceiver instance
 * to receive file data over UDP and store it locally.
 */
public class FileReceiver {

    // Validate that the required arguments are provided
    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: java receiver.FileReceiver <output-file-path> <listening-port>");
            System.exit(1);
        }

        // Get the output file path
        String filePath = args[0];

        // Get the port number
        int port = Integer.parseInt(args[1]);

        // Create UDPReceiver instance and start to receive data.
        UDPReceiver receiver =new UDPReceiver(port, filePath);
        receiver.receiveData();

    }
}
