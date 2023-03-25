package enigma;
import java.util.ArrayList;



public class MachineTest {
    private Alphabet alphabet = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    private String[] rotors = {"B", "Beta", "I", "II", "III"};
    private ArrayList<Rotor> r = new ArrayList<Rotor>();
    private Machine mach;


    private Permutation perm02 = new Permutation(" (AMQTPR) (HIX) ", alphabet);
    private Permutation perm03 = new Permutation(" (AELTPHQXRU)(S) ", alphabet);
    private Permutation perm04 = new Permutation(" (FIX) (Q) ", alphabet);
    private Permutation perm05 = new Permutation(" (ABDHPEJT) (N) ", alphabet);
    private Permutation plug01 = new Permutation("", alphabet);


    private Reflector r01 = new Reflector("B", perm02);
    private FixedRotor r02 = new FixedRotor("Beta", perm02);
    private MovingRotor r03 = new MovingRotor("I", perm03, "Q");
    private MovingRotor r04 = new MovingRotor("II", perm04, "E");
    private MovingRotor r05 = new MovingRotor("III", perm05, "V");



}

