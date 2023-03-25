package gitlet;

import java.io.File;
import java.io.Serializable;

/** Blob item created.
 * @author Dilain Saparamadu
 */
public class Blob implements Serializable {

    /** Blob constructor.
     * @param name */
    public Blob(String name) {
        blobFile = new File(name);
        _name = name;
        _contents = Utils.readContents(blobFile);
        sha = Utils.sha1(_contents);

    }
    /** return name.*/
    public String getname() {
        return _name;
    }
    /** return sha as a String. */
    public String sha() {
        return sha;
    }
    /** return contents as a byte array. */
    public byte[] getContents() {
        return _contents;
    }
    /** return the file associated with the blob. */
    public File getBlobFile() {
        return blobFile;
    }

    /** Name of the Blob. */
    private String _name;

    /** Contents of the Blob. */
    private byte[] _contents;

    /** Secured Hash Code. */
    private String sha;

    /** the file blob file variable. */
    private File blobFile;



}
