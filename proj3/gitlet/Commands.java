package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.util.Objects;

/**
 * Commands of gitlet.
 *
 * @author Dilain Saparamadu
 */
public class Commands implements Serializable {

    /**
     * Constructor for the commands.
     */
    public Commands() {
        File tempfile = new File("./.gitlet");
        if (tempfile.exists()) {
            String cwd = System.getProperty("user.dir");
            Commands c = Utils.readObject(new File(cwd + "/.gitlet/data"),
                    Commands.class);
            copyObjectData(c);
        }
    }

    /**
     * Initialize the command.
     */
    public void init() {
        File file = new File("./.gitlet");
        if (file.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        } else {
            String cwd = System.getProperty("user.dir");
            gitt = new File(cwd + "/.gitlet");
            gitt.mkdirs();
            data = new File(cwd + "/.gitlet/data");
            try {
                data.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            head = new File(cwd + "/.gitlet/head.txt");
            try {
                head.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage = new File(cwd + "/.gitlet/stage");
            stage.mkdir();
            add = new File(cwd + "/.gitlet/add");
            add.mkdir();
            commits = new File(cwd + "/.gitlet/commits");
            commits.mkdir();
            rmDir = new File(cwd + "/.gitlet/rmfiles");
            rmDir.mkdir();
            branch = new File(cwd + "/.gitlet/branches");
            branch.mkdir();
            _current = new Commit("initial commit", null);
            _current.initialCommit("initial commit");
            _currentBranch = "master";
            marks = new ArrayList<>();
            Utils.writeContents(head, _current.getsha());
            byte[] serializedCommit = Utils.serialize(_current);
            String initialCommitSha = _current.getsha();
            Utils.writeContents(Utils.join(branch, "master"), initialCommitSha);
            allCommits.put(initialCommitSha, _current);
            File file1 = new File(commits, initialCommitSha);
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Utils.writeContents(file1, (Object) serializedCommit);
            Utils.writeContents(data, (Object) Utils.serialize(this));
        }
    }

    /**
     * The add method.
     *
     * @param filepath of the file
     */
    public void add(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            Blob blob = new Blob(filepath);
            blob.getContents();
            byte[] blobContent = Utils.readContents(file);
            String blobSha = Utils.sha1(blobContent);
            String name = file.getName();

            List<String> stageFiles = Utils.plainFilenamesIn(stage);
            if (stageFiles.contains(name)) {
                File updateToStage = new File(stage, name);
                updateToStage.delete();
                stagedFilesMap.remove(filepath);
            }
            String contentsToCopy = Utils.readContentsAsString(file);
            File addToStage = new File(stage, name);
            Utils.writeContents(addToStage, contentsToCopy);
            stagedFilesMap.put(filepath, file);
            try {
                addToStage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String commitFileName : _current.commitdataKeySet()) {
                File currBlobFile = blob.getBlobFile();
                Blob blobInCommit = _current.commitdataGet(commitFileName);
                if (blobInCommit.sha().equals(blobSha)) {
                    File stagedFile = new File(stage, name);
                    stagedFile.delete();
                }
            }
            Utils.writeContents(data, (Object) Utils.serialize(this));
        }
    }

    /**
     * Method to commit files.
     *
     * @param log message.
     */
    public void commit(String log) {
        if (log.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        if (stagedFilesMap.size() == 0 && marks.size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        String headContent = Utils.readContentsAsString(head);
        rmforcommit(_current);
        Commit parentCommit = _current;
        _current = new Commit(log, headContent);
        _current.commit(headContent, log);
        for (String filename : Utils.plainFilenamesIn(stage)) {
            File file = new File(filename);
            String name = file.getName();
            File fileToCommit = new File(stage, name);
            String fileToCommitStr = fileToCommit.toString();
            Blob blobToCommit = new Blob(fileToCommitStr);
            if (parentCommit.commidataContains(filename)) {
                _current.commitdataReplace(filename, blobToCommit);
                String commitSha = _current.getsha();
                File stageToCommit = new File(commits, commitSha);
                try {
                    stageToCommit.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileToCommit.delete();
                stagedFilesMap.remove(filename);
            } else {
                _current.commitdataPut(filename, blobToCommit);
                String commitSha = _current.getsha();
                File stageToCommit = new File(commits, commitSha);
                try {
                    stageToCommit.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fileToCommit.delete();
                stagedFilesMap.remove(filename);
            }
        }
        String currentSha = _current.getsha();
        Utils.writeContents(head, currentSha);
        allCommits.put(currentSha, _current);
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * remove in commit.
     *
     * @param parentCommit the parent commit
     */
    public void rmforcommit(Commit parentCommit) {
        String[] filesInRm = rmDir.list();
        for (String filename
                : Objects.requireNonNull(Utils.plainFilenamesIn(commits))) {
            if (filesInRm != null) {
                for (String rmFileName : filesInRm) {
                    if (filename.equals(rmFileName)) {
                        File fileInRm = new File(rmDir, rmFileName);
                        marks.add(fileInRm.toString());
                        fileInRm.delete();
                    }
                }
            } else {
                Blob item = parentCommit.commitdataGet(filename);
                _current.commitdataPut(filename, item);
            }
        }
    }

    /**
     * gives the log of all commits.
     */
    public void log() {
        String headContents = Utils.readContentsAsString(head);
        Commit commit = allCommits.get(headContents);
        while (commit != null) {
            System.out.println("===");
            System.out.println("commit " + headContents);
            System.out.println("Date: " + commit.getTime() + " -0800");
            System.out.println(commit.getlogMessage());
            System.out.println();

            String parentCommit = commit.getparent1();
            Commit c = allCommits.get(parentCommit);
            commit = c;
        }
    }

    /**
     * the global log method.
     */
    public void globalLog() {
        for (Commit commit : allCommits.values()) {
            System.out.println("===");
            System.out.println("commit " + commit.getsha());
            System.out.println("Date: " + commit.getTime() + " -0800");
            System.out.println(commit.getlogMessage());
            System.out.println();
        }
    }

    /**
     * the remove method.
     *
     * @param filename is the name of the file
     */
    public void rm(String filename) {
        File file = new File(filename);
        boolean found = false;
        if (stagedFilesMap.containsKey(filename)) {
            stagedFilesMap.remove(filename);
            File unstagedFile = new File(stage, file.getName());
            if (unstagedFile.exists()) {
                unstagedFile.delete();
                found = true;
                Utils.writeContents(data, (Object) Utils.serialize(this));
                return;
            }
        }
        int size = marks.size();
        boolean find = false;
        for (String str : _current.commitdataKeySet()) {
            if (str.equals(file.getName())) {
                marks.add(filename);
            }
        }
        if (size != marks.size()) {
            find = true;
        }
        if (!find) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

        Blob blob = _current.commitdataGet(filename);
        if (blob == null) {
            return;
        }
        String cwd = System.getProperty("user.dir");
        File rmvFile = new File(cwd, filename);
        if (rmvFile.exists()) {
            rmvFile.delete();
            File someFile = new File(rmDir, filename);
            try {
                someFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }






    /**
     * the first check out method.
     *
     * @param filename of the checked out file
     */
    public void checkoutx(String filename) {
        String cwd = System.getProperty("user.dir");
        String headContents = Utils.readContentsAsString(head);
        Commit commit = allCommits.get(headContents);
        Blob blobFromHead = commit.commitdataGet(filename);
        if (blobFromHead != null) {
            byte[] blobContents = blobFromHead.getContents();
            File workingDirFile = new File(cwd, filename);
            if (workingDirFile.exists()) {
                Utils.writeContents(workingDirFile, blobContents);
            } else {
                try {
                    workingDirFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Utils.writeContents(workingDirFile, blobContents);
            }
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * the second checkout method.
     *
     * @param commitID of the commit
     * @param filename of the necessary file
     */
    public void checkouty(String commitID, String filename) {
        String cwd = System.getProperty("user.dir");
        if (allCommits.containsKey(commitID)) {
            Commit givenCommit = allCommits.get(commitID);
            Blob blob = givenCommit.commitdataGet(filename);
            if (blob != null) {
                File blobFile = blob.getBlobFile();
                byte[] blobContents = blob.getContents();
                File workingDirFile = new File(cwd, filename);
                if (workingDirFile.exists()) {
                    Utils.writeContents(workingDirFile, (Object) blobContents);
                } else {
                    try {
                        workingDirFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Utils.writeContents(workingDirFile, (Object) blobContents);
                }
            } else {
                System.out.println("File does not exist in that commit.");
                System.exit(0);
            }
        } else {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * the third checkout method.
     *
     * @param name the name of the branch
     */
    public void checkoutz(String name) {
        boolean found = false;
        for (String n : Utils.plainFilenamesIn(branch)) {
            if (n.equals(name)) {
                found = true;
            }
        }
        if (!found) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (name.equals(_currentBranch)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * creates a new branch.
     *
     * @param name of the branch
     */
    public void branch(String name) {
        String headcontent = Utils.readContentsAsString(head);
        if (Utils.plainFilenamesIn(branch).contains(name)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Utils.writeContents(Utils.join(branch, name), headcontent);
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * removes a branch.
     *
     * @param name of the branch
     */
    public void removeBranch(String name) {
        if (!Utils.plainFilenamesIn(branch).contains(name)) {
            System.out.println(" A branch with that name does not exist.");
            System.exit(0);
        }
        if (name.equals(_currentBranch)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        Utils.join(branch, name).delete();
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * The status of the current gitlet directory.
     */
    public void status() {
        System.out.println("=== Branches ===");
        for (String name : Utils.plainFilenamesIn(branch)) {
            if (name.equals(_currentBranch)) {
                name = "*" + name;
            }
            System.out.println(name);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String stagedFiles : stagedFilesMap.keySet()) {
            String file = new File(stagedFiles).getName();
            System.out.println(file);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (int i = 0; i < marks.size(); i++) {
            File file = new File(marks.get(i));
            System.out.println(file.getName());
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * Finding a commit using the log message.
     *
     * @param logMessage of that commit
     */
    public void find(String logMessage) {
        boolean foundMsg = false;
        for (Commit c : allCommits.values()) {
            if (c.getlogMessage().equals(logMessage)) {
                System.out.println(c.getsha());
                foundMsg = true;
            }
        }
        if (!foundMsg) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        Utils.writeContents(data, (Object) Utils.serialize(this));
    }

    /**
     * reset method.
     *
     * @param commitID the sha-id of the given commit
     */
    public void reset(String commitID) {
        boolean found = false;
        for (String id : allCommits.keySet()) {
            if (id.equals(commitID)) {
                found = true;
            }
        }
        if (!found) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    /**
     * copying all data from previous commands object.
     *
     * @param previousCommandObj is the previous command object
     */
    public void copyObjectData(Commands previousCommandObj) {
        _current = previousCommandObj._current;
        allCommits = previousCommandObj.allCommits;
        stagedFilesMap = previousCommandObj.stagedFilesMap;
        gitt = previousCommandObj.gitt;
        stage = previousCommandObj.stage;
        add = previousCommandObj.add;
        head = previousCommandObj.head;
        commits = previousCommandObj.commits;
        data = previousCommandObj.data;
        marks = previousCommandObj.marks;
        rmDir = previousCommandObj.rmDir;
        _currentBranch = previousCommandObj._currentBranch;
        branch = previousCommandObj.branch;

    }

    /**
     * the gitlet directory.
     */
    private File gitt;
    /**
     * the staging directory.
     */
    private File stage;
    /**
     * the add direcotry.
     */
    private File add;
    /**
     * the head file.
     */
    private File head;
    /**
     * the commits directory.
     */
    private File commits;
    /**
     * the data file for persistance.
     */
    private File data;
    /**
     * the remove directory.
     */
    private File rmDir;
    /**
     * the branch file.
     */
    private File branch;

    /**
     * the current commit.
     */
    private Commit _current;

    /**
     * a hash map of all the commit.
     */
    private HashMap<String, Commit> allCommits = new HashMap<>();

    /**
     * a hash map of files in the stagin area.
     */
    private HashMap<String, File> stagedFilesMap = new HashMap<>();

    /**
     * the current branch.
     */
    private String _currentBranch;

    /**
     * marking files that were removed.
     */
    private ArrayList<String> marks;


}
