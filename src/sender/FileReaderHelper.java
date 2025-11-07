package sender;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * FileReaderHelper
 * ----------------
 * Reads a file contents line by line.
 * opening a file, reading lines, checking for more lines, and closing the scanner safely.
 */
public class FileReaderHelper {

    // Reference to the file being read.
    private final File file;
    // Scanner used for reading the file contents.
    private Scanner reader;

    /**
     * Constructs a FileReaderHelper to read from the specified file path.
     *
     * @param file the path to the file to be read
     */
    public FileReaderHelper(String file) {
        // Initialize the file object
        this.file = new File(file) ;
        try {
            // Attempt to create a Scanner for the given file
            this.reader = new Scanner(this.file);
        } catch (FileNotFoundException e) {
            // Handle missing file error
            System.out.println("File not found: " + e.getMessage());
            scannerClose();
        }
    }

    /**
     * Reads and returns the next line from the file.
     *
     * @return the next line as a String.
     */
    public String readLine() {
        return reader.nextLine();
    }

    /**
     * Checks whether there is another line available in the file.
     *
     * @return true if there is another line to read; false otherwise
     */
    public boolean hasLine() {
        return reader.hasNextLine();
    }

    /**
     * Closes the  Scanner object
     */
    public void scannerClose() {
        if(reader != null)
        {
            reader.close();
        }
    }
}
