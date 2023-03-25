package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Dilain saparamadu
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alpha = chars;


    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alpha.length();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        int dexo = 0;
        while (dexo < _alpha.length()) {
            if (_alpha.charAt(dexo) == ch) {
                return true;
            }
            dexo += 1;
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= 0 && index < size()) {
            return _alpha.charAt(index);
        }
        throw new EnigmaException("invalid index given");
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int dexa = 0;
        while (dexa < _alpha.length()) {
            if (_alpha.charAt(dexa) == ch) {
                return dexa;
            }
            dexa += 1;
        }
        throw new EnigmaException("character given not in alaphabet");
    }
    /** creating an instance variable for the string chars passed into the
     * constructor. */
    private String _alpha;

}
