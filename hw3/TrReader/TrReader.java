import java.io.Reader;
import java.io.IOException;

/** Translating Reader: a stream that is a translation of an
 *  existing reader.
 *  @author Dilain Saparamadu
 */
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */
    public TrReader(Reader str, String from, String to) {

        this.str = str;
        this.from = from;
        this.to = to;



    }
    private Reader str;
    private String from;
    private String to;


    public void close() throws IOException {
        str.close();

        }

    public int read(char[] cbuf, int off, int len) throws IOException {
        int count = 0;
        str.read(cbuf, off, len);
        for (int i = off; i <= off + len; i++) {
            int r = from.indexOf(cbuf[i]);
            if (r != -1) {
                cbuf[i] = to.charAt(r);
                count++;

            }
        }

        return count;
    }

}
