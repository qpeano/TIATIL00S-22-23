/*This class is an interface for storing lines (strings) in files
 *
 * @qpeano [created: 2023-05-19 | last updated: 2023-05-19]
 */
import java.util.ArrayList;
import java.io.*;

public class FileHandler {

    /* FIELDS */

    private ArrayList<String> lines;
    private File file;
    private boolean isEmpty;

    /* METHODS - constructor */

    /**
     * Constructor
     *
     * @param filePath name (path) of file that will store data
     * @exception IOException if something occurrs in file creation, connection or when reading file
     */
    public FileHandler(String filePath) throws IOException {

        this.file = new File(filePath); // makes connection to file
        this.file.createNewFile(); // creates file if it does not exist
        this.lines = new ArrayList<>();

        this.isEmpty = !(this.hasContent(this.file)); // to check if file has content, negated to suit use of field

        if (!this.isEmpty) { // if file is not empty all data is extracted and written to list of units

            this.extract(this.file, this.lines);
        }
    }

    /* METHODS - internal */

    /**
     * Method is used to check if a file has any content whatsoever, used in conjuntion with this.extract
     *
     * @param file the file that the date will be read from
     * @exception IOException if something goes wrong with reading file
     * @return if the file has content (text) or not
     */
    private boolean hasContent(File file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

             result = true;
        }

        br.close();
        return result;
    }

    /**
     * Method extracts all data from a file and fills the FileHandlers lines with said data
     *
     * @param file the file that the date will be read from
     * @param lines the list that the date will be put in
     * @exception IOException if something goes wrong while reading from file
     */
    private void extract(File file, ArrayList<String> lines) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file)); // reading mechanism for file
        String line; // a line in the file

        while ((line = br.readLine()) != null) { // do the following if the line doesn't hold an empty value

            lines.add(line);
        }

        br.close();
    }

    /**
     * Method prints the lines to file
     *
     * @exception IOException if something goes wrong while writing to file
     */
    private void printLines(File file) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        StringBuilder linesString = new StringBuilder();
        for (String line : this.lines) {

            linesString.append(line);
            linesString.append("\n");
        }

        bw.write(linesString.toString());
        bw.close();
    }

    /* METHODS - UI */

    /**
     * Method is used to add a line of text
     *
     * @param line a new line of text
     * @exception IOException if something goes wrong while writing to file
     */
    public void add(String line) throws IOException {

        this.lines.add(line);
        this.printLines(this.file);
        this.isEmpty = false;
    }

    /**
     * Method removes an instance of a line in file
     *
     * @param line a line to be removed
     * @exception IOException if something goes wrong while writing to file
     */
    public void remove(String line) throws IOException {

        this.lines.remove(line);
        this.printLines(this.file);
        this.isEmpty = (this.lines.size() == 0) ? true : false;
    }

   /**
    * Method removes an instance of a line in file
    *
    * @param lineIndex the index of a line to be removed
    * @exception IOException if something goes wrong while writing to file
    */
    public void remove(int lineIndex) throws IOException {

        this.lines.remove(lineIndex);
        this.printLines(this.file);
        this.isEmpty = (this.lines.size() == 0) ? true : false;
    }

    /**
     * Method clears file and the list of lines
     *
     * @exception IOException if something goes wrong while writing to file
     */
    public void clear() throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(this.file)); // connection to file, writing mechanism
        bw.write(""); // overwrite everything with an empty string
        bw.close(); // closed connection

        this.lines.clear(); // deletes all lines from list
        this.isEmpty = true; // sets status to EMPTY
    }

    /**
     * Method fetches all lines
     *
     */
    public ArrayList<String> getAllLines() {

        return new ArrayList<>(this.lines);
    }
}
