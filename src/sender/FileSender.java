package sender;

import java.util.concurrent.TimeUnit;

/**
 * The FileSender class coordinates reading a file line by line
 * and transmitting its content over UDP to a specified receiver.
 *
 * It uses the FileReaderHelper class to read text from a file and
 * the UDPSender class to send data packets over the network.
 * Between sending each line, a short delay (200 ms) to avoid
 * packet congestion or loss.
 */
public class FileSender {
    // Handle sending data through UDP
    private final UDPSender udpSender;
    // Handle reading lines from the file
    private final FileReaderHelper fileReader;

    /**
     * Sets up the file and sender by initializing both the UDP sender and file reader.
     *
     * @param file     Path to the file to be sent
     * @param hostName The receiver's hostname or IP address
     * @param port     The receiver's port number
     */
    public FileSender(String file, String hostName, int port) {
        this.udpSender = new UDPSender(hostName,port);
        this.fileReader = new FileReaderHelper(file);
    }


    /**
     * Sends the file line by line to the receiver.
     *
     * Each line is sent in small UDP chunks to fit the buffer size.
     * A newline marker is sent after each line, and an "EOF" marker
     * is sent once the entire file is done.
     */
    public void sendFile()
    {
        System.out.println("Sending file contents to the receiver.");
        while(fileReader.hasLine()) {
            String line = fileReader.readLine();
            udpSender.sendLine(line);
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Interrupted while sleeping: " + e.getMessage());
            }
        }
        udpSender.sendEOF();
        System.out.println("File sent successfully.");

        // Close resources once we're done
        udpSender.socketClose();
        fileReader.scannerClose();
    }

    public static void main(String[] args) {
        // Validate that the required arguments are provided
        if (args.length < 3) {
            System.out.println("Usage: java FileSender <file-path> <host> <port>");
            System.exit(1);
        }

        try {
            FileSender sender = new FileSender(args[0],args[1],Integer.parseInt(args[2]));
            sender.sendFile();
        }catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }

    }
}
