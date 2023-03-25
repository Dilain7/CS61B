package gitlet;

import java.io.Serializable;

/** Class for the branch item.
 * @author Dilain Saparamadu */
public class Branch implements Serializable {

    /** Constructor for the for the branch.
     *
     * @param name name of the branch
     * @param head head of the branch
     */
    public Branch(String name, Commit head) {
        _branchName = name;
        _recentCommit = head;
        _commitSha = head.getsha();

    }
    /** Return the name of this branch. */
    public String getbranchname() {
        return _branchName;
    }
    /** return recent commit. */
    public Commit recentcommit() {
        return _recentCommit;
    }
    /** return the sha of this commit. */
    public String getSha() {
        return _commitSha;
    }
    /** the branch name. */
    private String _branchName;

    /** the most recent commit. */
    private Commit _recentCommit;

    /** the commit sha ID. */
    private String _commitSha;
}
