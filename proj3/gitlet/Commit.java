package gitlet;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/** a class for the commit object.
 * @author dilain saparamadu
 */
public class Commit implements Serializable {
    /** the constructor for the commit object.
     *
     * @param log the log message
     * @param parent the parent commit id
     */
    public Commit(String log, String parent) {
        _parent1 = parent;
        _logMessage = log;
        commitData = new HashMap<>();
    }

    /** the normal commit method.
     *
     * @param parent the parent commit id
     * @param log the log message
     */
    public void commit(String parent, String log) {
        _parent1 = parent;
        _logMessage = log;
        String pattern = "E MMM dd HH:mm:ss yyyy";
        SimpleDateFormat simpDateformat = new SimpleDateFormat(pattern);
        _commitTime = simpDateformat.format(new Date());
        sha = Utils.sha1(_parent1, _logMessage);
    }

    /** the initial commit method.
     *
     * @param log the log message
     */
    public void initialCommit(String log) {
        _logMessage = log;
        _commitTime = initialTime();
        sha = Utils.sha1(_logMessage, _commitTime);

    }

    /** the intial time.
     *
     * @return a string
     */
    public String initialTime() {
        String epochTime = "Wed Dec 31 16:00:00 1969";
        return epochTime;
    }

    /** get the time.
     *
     * @return a string
     */
    public String getTime() {
        return _commitTime;
    }

    /** return the first parent. */
    public String getparent1() {
        return _parent1;
    }
    /** get the log message.
     * @return the log message*/
    public String getlogMessage() {
        return _logMessage;
    }
    /** get the sha of this commit.
     * @return the sha as a string*/
    public String getsha() {
        return sha;
    }
    /** puts key and value into hash map.
     * @param a key
     * @param b the blob */
    public void commitdataPut(String a, Blob b) {
        commitData.put(a, b);
    }
    /** gets item from hash map.
     * @param g the key
     * @return the blob*/
    public Blob commitdataGet(String g) {
        return commitData.get(g);
    }
    /** return the entire hash map key set. */
    public Set<String> commitdataKeySet() {
        return commitData.keySet();
    }
    /** returns true if hash map contains key.
     * @param a the key
     * @return if it contains the key*/
    public boolean commidataContains(String a) {
        return commitData.containsKey(a);
    }
    /** replaces the value of the key in the hash map.
     * @param a the string
     * @param b the blob*/
    public void commitdataReplace(String a, Blob b) {
        commitData.replace(a, b);
    }
    /** remove item from hash map.
     * @param a input key*/
    public void commitdataRemove(String a) {
        commitData.remove(a);
    }

    /** the first parent. */
    private String _parent1;

    /** the log message. */
    private String _logMessage;

    /** the commit time. */
    private String _commitTime;

    /** a hash map of this commit's data. */
    private HashMap<String, Blob> commitData;

    /** the sha ID of this commit. */
    private String sha;

}
