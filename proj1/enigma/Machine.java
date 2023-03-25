package enigma;

import java.util.HashMap;
import java.util.Collection;
import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Dilain saparamadu
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numrotors = numRotors;
        _pawls = pawls;
        _allrotors = allRotors;
        _rowtows = new Rotor[numRotors];

    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numrotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        for (Rotor rot : _allrotors) {
            allrots.put(rot.name(), rot);
            for (int i = 0; i < rotors.length; i += 1) {
                if (allrots.get(rot.name()).equals(rotors[i])) {
                    _rowtows[i] = allrots.get(rot.name());
                }
            }
        }
        if (rotors.length != _rowtows.length) {
            throw new EnigmaException("Incorrect number of rotors");
        }

    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 characters in my alphabet. The first letter refers
     * to the leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        if (setting.length() != _rowtows.length) {
            throw new EnigmaException("Wrong number of settings");
        }
        for (int i = 1; i < _rowtows.length; i += 1) {
            _rowtows[i].set(setting.charAt(i - 1));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int forconv = _plugboard.permute(c);
        for (int i = _rowtows.length - 1; i >= 0; i -= 1) {
            forconv = _rowtows[i].convertForward(forconv);
        }
        int backconv = forconv;
        for (int k = _rowtows.length - 1; k >= 0; k -= 1) {
            backconv = _rowtows[k].convertBackward(backconv);
        }
        backconv = _plugboard.permute(backconv);
        return backconv;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String convo = "";
        for (int i = 0; i < msg.length(); i += 1) {
            convo += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return convo;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Number of rotoros.
     **/
    private int _numrotors;

    /**
     * The number of pawls in the machine.
     **/
    private int _pawls;

    /**
     * New collection of all the rotors availale.
     **/
    private Collection<Rotor> _allrotors;

    /**
     * A rotor array of the rotors.
     **/
    private Rotor[] _rowtows;

    /**
     * The plugboard of the machine.
     **/
    private Permutation _plugboard;

    /**
     * All the rotors available.
     **/
    private HashMap<String, Rotor> allrots = new HashMap<String, Rotor>();
}
