package gitlet;


import java.io.File;
import java.util.ArrayList;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Dilain Saparamadu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        int operands = args.length - 1;
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String com = args[0];
        cmds = new ArrayList<>();
        lst();
        if (!cmds.contains(com)) {
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
        Commands git = new Commands();
        if (com.equals("init")) {
            git.init();
        } else if (args[0].equals("add") && operands == 1) {
            git.add(args[1]);
        } else if (args[0].equals("commit") && operands == 1) {
            git.commit(args[1]);
        } else if (args[0].equals("log") && operands == 0) {
            git.log();
        } else if (args[0].equals("rm") && operands == 1) {
            git.rm(args[1]);
        } else if (args[0].equals("global-log") && operands == 0) {
            git.globalLog();
        } else if (args[0].equals("find") && operands == 1) {
            git.find(args[1]);
        } else if (args[0].equals("status") && operands == 0) {
            if (!Utils.join(new File("."), ".gitlet").exists()) {
                System.out.println("Not in an initialized Gitlet directory");
                System.exit(0);
            }
            git.status();
        } else if (args[0].equals("checkout")) {
            int argsLength = args.length;
            if (argsLength == 1) {
                System.out.println(("Incorrect operands."));
            } else if (argsLength == 2 && operands == 1)  {
                git.checkoutz(args[1]);
            } else if (argsLength == 3 && operands == 2) {
                git.checkoutx(args[2]);
            } else if (argsLength == 4 && operands == 3) {
                git.checkouty(args[1], args[3]);
            }
        } else if (args[0].equals("branch") && operands == 1) {
            git.branch(args[1]);
        } else if (args[0].equals("reset") && operands == 1) {
            git.reset(args[1]);
        } else if (args[0].equals("rm-branch") && operands == 1) {
            git.removeBranch(args[1]);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
    /** adding all commands to the cmd variable. */
    public static void lst() {
        cmds.add("init");
        cmds.add("add");
        cmds.add("commit");
        cmds.add("log");
        cmds.add("rm");
        cmds.add("global-log");
        cmds.add("find");
        cmds.add("status");
        cmds.add("checkout");
        cmds.add("branch");
        cmds.add("reset");
        cmds.add("rm-branch");
        cmds.add("merge");
    }
    /** list of commands. */
    private static ArrayList<String> cmds;
}
