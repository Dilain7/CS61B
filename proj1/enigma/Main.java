package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author Dilain Saparamadu
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() {
        thisMachine = readConfig();
        if (!_input.next().equals("*")) {
            throw new EnigmaException("incorrect input of arguments");
        }
        String thisrotor = _input.next();
        for (int i = 0; i < 4; i += 1) {
            String whichline = _input.next();
            _rotors2.add(whichline);
        }
        String setting = _input.next();

        if (_input.hasNext()) {
            String plugboard101 = _input.nextLine();
        }
        String nextOne = _input.nextLine();
        if (nextOne.contains("*")) {
            throw new EnigmaException("Incorrect format of input");
        }
        nextOne = nextOne.toUpperCase();
        nextOne = nextOne.replace(" ", "");
        String finale = thisMachine.convert(nextOne);
        if (finale.length() == 0) {
            System.out.println();
        } else {
            printMessageLine(finale);
        }
    }
       /* if (setting.length() < _allFrotors.size()) {
            throw new EnigmaException("incorrect number of rotors");
        }
        setUp(thisMachine, setting);*/


    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            String alpha = _config.nextLine();
            alpha = alpha.trim();
            if (alpha.contains("(") || alpha.contains("*")) {
                throw new EnigmaException("Configuration of incorrect format");
            }
            _alphabet = new Alphabet(alpha);
            int rotorNum;
            int pawlNum;
            if (_config.hasNextInt()) {
                rotorNum = _config.nextInt();
                if (_config.hasNextInt()) {
                    pawlNum = _config.nextInt();
                } else {
                    throw new EnigmaException("Incorrect number of pawls");
                }
            } else {
                throw new EnigmaException("Incorrect number of rotors");
            }
            Scanner checkLine = new Scanner(_config.nextLine());
            while (_config.hasNext()) {
                String frotor = _config.nextLine().trim();
                String frot2 = "";
                if (checkLine.next().equals("(")) {
                    frot2 = _config.nextLine();
                    frotor = frot2 + frotor;
                }
                _allFrotors.add(readRotor(frotor));
            }
            return new Machine(_alphabet, rotorNum, pawlNum, _allFrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     *
     * @param curr to read the current line.
     */
    private Rotor readRotor(String curr) {
        try {
            curr = curr.trim().toUpperCase();
            Scanner rotorLine = new Scanner(curr);
            String rotorName = rotorLine.next();
            String rotorNotch = rotorLine.next();
            Character typo = rotorNotch.charAt(0);
            String cycles = rotorLine.nextLine();
            Scanner chknextline = new Scanner(curr);
            chknextline.nextLine();
            String cyclsconcat = "";
            cycles = cycles + cyclsconcat;
            Permutation permus = new Permutation(cycles, _alphabet);
            if (typo == 'M') {
                return new MovingRotor(rotorName, permus,
                        rotorNotch.substring(1));
            } else if (typo == 'N') {
                return new FixedRotor(rotorName, permus);
            } else {
                return new Reflector(rotorName, permus);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        String mesage = msg;
        if (msg.length() <= 5) {
            System.out.println(msg);
        } else {
            System.out.println(mesage.substring(0, 5) + " ");
            mesage = msg.substring(5, mesage.length());
        }
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * Source of input messages.
     */
    private Scanner _input;

    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /**
     * New machine we're gonna use.
     */
    private Machine thisMachine;

    /**
     * array of the settings.
     */
    private String[] settingsArray;

    /**
     * array of all the rotors.
     */
    private ArrayList<Rotor> _allFrotors = new ArrayList<Rotor>();

    /**
     * array of reflectors.
     */
    private ArrayList<String> _rotors2 = new ArrayList<String>();

    /**
     * array of moving rotors.
     */
    private ArrayList<String> _movingRotor = new ArrayList<String>();

    /**
     * array of fixed rotors.
     */
    private ArrayList<String> _fixedRotor = new ArrayList<String>();
}
