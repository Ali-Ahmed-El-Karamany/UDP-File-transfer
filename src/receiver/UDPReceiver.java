package receiver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * The UDPReceiver class handles receiving UDP packets that represent
 * file content sent by a UDP sender. Each packet contains a chunk of data
 * that is written into a local file.
 */
public class UDPReceiver {
    // The port number on which the receiver listens for incoming UDP packets
    private final int PORT_NUMBER;
    // File object representing the destination file where received data is written
    private final File file;
    // FileWriter object used to write incoming data into the output file
    private final FileWriter writer;
    // DatagramSocket used to listen for UDP packets
    private DatagramSocket socket = null;


    /**
     * Constructs a UDP receiver with the listening port and output file path.
     * It also handles file creation and prepares a FileWriter for writing incoming data.
     */
    public UDPReceiver(int portNumber, String filePath) {
        this.PORT_NUMBER = portNumber;
        File myFile = null;
        FileWriter fileWriter = null;

        try {
            // Create a new file at the specified path if it doesn't exist
            myFile = new File(filePath);
            if(myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            }else {
                System.out.println("File already exists.");
            }

            // Initialize FileWriter with the output file path
            try {
                fileWriter = new FileWriter(filePath);
            } catch (IOException e) {
                System.out.println("Error creating file writer: " + e.getMessage());
            }
        }catch (IOException e) {
            System.out.println("An error occurred while creating the file." + e.getMessage());
        }

        // Assign the file and file writer references
        this.file = myFile;
        this.writer = fileWriter;
    }

    /**
     * Starts listening for incoming UDP packets and writes their contents to the file.
     * Each packet is assumed to contain either:
     * - A part of a file line (text data)
     * - A newline indicator ("\n")
     * - Or an end-of-file indicator ("EOF") to terminate the process
     */
    public void receiveData() {
        try {
            // Create a UDP socket bound to the specified port
            socket = new DatagramSocket(PORT_NUMBER);
            // Define a buffer to store incoming UDP packet data
            byte[] buffer = new byte[1024];
            boolean receiving = true;

            System.out.println("Receiver started. Listening on port " + PORT_NUMBER + " ...");

            // Continuously listen for incoming packets until "EOF" is received
            while(receiving) {
                // Create a DatagramPacket to receive data into the buffer
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Waits for the incoming data to be sent
                socket.receive(packet);

                // Extract actual received data from the packet
                String receivedData = new String(packet.getData(), 0, packet.getLength());

                // Check if End of file signal received to stop receiving
                if(receivedData.equals("EOF")) {
                    System.out.println("End of file received. Closing connection.");
                    receiving = false;
                    continue;
                }

                // Check if new file signal received to make a new line
                if(receivedData.equals("\n")) {
                    writer.write(System.lineSeparator());
                }else {
                    // Otherwise, write the received text data
                    writer.write(receivedData);
                }

                // Flush writer to ensure immediate data writing
                writer.flush();
            }
        } catch (SocketException e) {
            System.out.println("Socket error failed to initialize udp socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while receiving data: " + e.getMessage());
        }finally {
            closeResources();
        }
    }


    /**
     * Safely closes the FileWriter and DatagramSocket resources.
     */
    public void closeResources() {
        // Close file writer if it exists
        if(writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println("Error closing file writer: " + e.getMessage());
            }
        }
        // Close socket if it was opened
        if(socket != null) {
            socket.close();
        }
    }
}
