package enigma;


import static enigma.EnigmaException.*;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author Dilain Saparamadu
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _name = name;
        _permutation = perm;
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        super.set(_setting + 1);
    }

    @Override
    boolean atNotch() {
        char[] notchray = _notches.toCharArray();
        for (char c : notchray) {
            if (alphabet().toInt(c) == permutation().wrap(setting())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Name of the rotor.
     */
    private String _name;

    /**
     * The permutation of the rotor.
     */
    private Permutation _permutation;

    /**
     * The positions where the notcehs are.
     */
    private String _notches;

    /**
     * The setting of this rotor.
     */
    private int _setting;

}
