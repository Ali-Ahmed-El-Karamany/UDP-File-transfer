package sender;

import java.io.IOException;
import java.net.*;

/**
 * The UDPSender class is responsible for sending data in a fila to a UDP receiver.
 *
 * It splits long strings into smaller chunks that fit within a predefined buffer size
 * and sends them sequentially as UDP packets. It also provides methods to send newline
 * and end-of-file markers to help the receiver identify the data boundaries.
 *
 */
public class UDPSender {
    final int BUFFER_SIZE = 128;                     // Byte buffer size for UDP packet
    final byte[] NEW_LINE = "\n".getBytes();        // New Line Indicator bytes
    final byte[] END_OF_FILE = "EOF".getBytes();    // End of file indicator bytes

    private InetAddress address;                    // Destination IP address
    private int port;                               // Destination port number
    private DatagramSocket socket = null;           // UDP Socket for packet sending

    /**
     * Constructs a UDPSender that sends packets to a specific host and port.
     *
     * @param hostName   The destination hostname or IP address
     * @param portNumber The destination port number
     */
    public UDPSender(String hostName, int portNumber) {
        try {
            // Resolve hostname to IP address
            this.address = InetAddress.getByName(hostName);
            this.port = portNumber;
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + e.getMessage());
        }
    }

    /**
     * Sends a single line of text over UDP, splitting it into multiple packets
     * if it exceeds the buffer size.
     *
     * @param line The line of text to send
     */
    public void sendLine(String line) {
        try {
            // Initialize UDP socket for sending data
            socket = new DatagramSocket();

            // Define a byte buffer with BUFFER_SIZE
            byte[] buffer = new byte[BUFFER_SIZE];

            // Convert the line contents into bytes
            byte[] lineBytes = line.getBytes();

            // Send the line in chunks (each up to BUFFER_SIZE bytes)
            int offset = 0;
            while (offset < lineBytes.length) {
                int length = 0;

                // Determine how many bytes to copy for this chunk
                if ((lineBytes.length - offset) > BUFFER_SIZE)
                    length = BUFFER_SIZE;
                else
                    length = lineBytes.length - offset;

                // Copy the chunk into the buffer to send
                System.arraycopy(lineBytes, offset, buffer, 0, length);

                // Create a UDP packet to send the data
                DatagramPacket packet = new DatagramPacket(
                        buffer,    //  Chunk of the line to send
                        length,         // length of the data
                        address,     // Receiver IP address
                        port);    // Receiver port number
                // Send the packet through the socket to the receiver
                socket.send(packet);

                // Move the offset forward by the number of bytes sent
                offset += length;
            }

            // Send new line separator to indicate the end of this line
            sendNewLine();
        } catch (SocketException e) {
            System.out.println("Socket error failed to initialize udp socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("I/O error failed to send the line: " + e.getMessage());
        }
    }

    /**
     * Sends a newline indicator to the receiver to mark the end of a line.
     */
    public void sendNewLine() {
        try {
            socket.send(new DatagramPacket(NEW_LINE, NEW_LINE.length,address,port));
        } catch (IOException e) {
            System.out.println("I/O error: failed to send new line separator" + e.getMessage());
        }
    }

    /**
     * Sends an "End of File" indicator to notify the receiver that
     * all data has been transmitted.
     */
    public void sendEOF() {
        try {
            socket.send(new DatagramPacket(END_OF_FILE, END_OF_FILE.length,address,port));
        } catch (IOException e) {
            System.out.println("I/O error: failed to send end of file indicator" + e.getMessage());
        }
    }

    /**
     * Closes the UDP socket and releases network resources.
     */
    public void socketClose() {
        if(socket != null)
            socket.close();
    }
}
