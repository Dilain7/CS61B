package enigma;



import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Dilain Saparamadu
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        missingCycles();
        checkParen(_cycles);
        members = new String[_count];
        addingChars();
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(char cycle) {
        _cycles += "(" + cycle + ")";
    }

    /**
     * Check that cycles are in correct format, this includes
     * making sure the parenthesis are in correct order and
     * the elements in the cycles are present in the alphabet. Add
     * the elements in cycle to the dictionary mapping characters.
     * @param cycle to check parenthesis.
     */
    void checkParen(String cycle) {
        cycle.toUpperCase();
        int i = 0;
        if (cycle.isEmpty()) {
            missingCycles();
        } else {
            while (i < cycle.length()) {
                if (_cycles.charAt(i) == '(') {
                    int indx = i + 1;
                    while (_cycles.charAt(indx) != ')') {
                        if (!_alphabet.contains(_cycles.charAt(indx))) {
                            throw error("incorrect cycle format");
                        }
                        indx += 1;
                    }
                    _count += 1;
                    i += 1;
                } else {
                    i += 1;
                }
            }
        }
    }

    /** else if (_cycles.charAt(indx + 1) != ')') {
     elemS.put(_cycles.charAt(indx), _cycles.charAt(indx + 1));
     indx += 1;
     } else {
     System.out.println(elemS);
     elemS.put(_cycles.charAt(indx), _cycles.charAt(i + 1));
     indx += 1;
     }
     }
     i = indx; */

    /**
     * Check if all elements of alphabet are present in all the cycles.
     * Add cycles of the elements that are missing.
     */
    void missingCycles() {
        if (_cycles.length() == 0) {
            for (int x = 0; x < _alphabet.size(); x += 1) {
                addCycle(_alphabet.toChar(x));
            }
        } else {
            int x;
            int y = 0;
            while (y < _alphabet.size()) {
                x = 0;
                while (x < _cycles.length()) {
                    if (_cycles.charAt(x) == _alphabet.toChar(y)) {
                        y++;
                        x = _cycles.length();
                    } else if (_cycles.charAt(x) != _alphabet.toChar(y)
                            && (x + 1) == _cycles.length()) {
                        addCycle(_alphabet.toChar(y));
                    } else {
                        x++;
                    }
                }
                y++;
            }
        }
    }

    /**
     * Adding the elements of cycles to the member array.
     */
    void addingChars() {
        int i = 0;
        int k;
        int j = 0;
        while (i < _cycles.length()) {
            if (_cycles.charAt(i) == '(') {
                k = i + 1;
                while (_cycles.charAt(k) != ')') {
                    k += 1;
                }
                members[j] = _cycles.substring(i, k + 1);
                i = k;
                j += 1;
            } else {
                i += 1;
            }
        }
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();

    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {
        return _alphabet.toInt(permute(this._alphabet.toChar(wrap(p))));
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        return _alphabet.toInt(invert(this._alphabet.toChar(wrap(c))));
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        if (p == ' ') {
            return p;
        }
        int alphaNum = _alphabet.toInt(p);
        int i = 0;
        int moduloo = wrap(alphaNum);
        char outChar = p;
        char inpChar = _alphabet.toChar(moduloo);
        while (i < members.length) {
            for (int x = 0; x < members[i].length(); x += 1) {
                if (members[i].charAt(x) == inpChar) {
                    if (members[i].charAt(x + 1) == ')') {
                        outChar = members[i].charAt(1);
                    } else {
                        outChar = members[i].charAt(x + 1);
                    }
                }
            }
            i += 1;
        }
        return outChar;
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    char invert(char c) {
        if (c == ' ') {
            return c;
        }
        int alphaNum = _alphabet.toInt(c);
        int i = 0;
        int moduloo = wrap(alphaNum);
        char outChar = c;
        char inpChar = _alphabet.toChar(moduloo);
        while (i < members.length) {
            int last = members[i].lastIndexOf(')');
            for (int x = last; x != members[i].indexOf('('); x -= 1) {
                if (members[i].charAt(x) == inpChar) {
                    if (members[i].charAt(x - 1) == '(') {
                        outChar = members[i].charAt(last - 1);
                    } else {
                        outChar = members[i].charAt(x - 1);
                    }
                }
            }
            i += 1;
        }
        return outChar;
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (int x = 0; x < _cycles.length(); x += 1) {
            if (_cycles.charAt(x) == '(') {
                if (_cycles.charAt(x + 2) == ')') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;

    /**
     * Initilizing the cycles of this permutation.
     */
    private String _cycles;

    /**
     * count of the number of individual cycles.
     */
    private int _count;

    /**
     * A string array of all the memember cycles individually.
     */
    private String[] members;

   /* void clean(String cycle) {
        if(cycle.contains(" ")){
            cycle = cycle.replaceAll(" ","");
            String[] Kobe = cycle.split("\\(\\)");
        }
    }*/

    /**
     * Main method to test this permutations file.
     * @param string represents the string array
     */
    public static void main(String[] string) {
        Permutation perma = new Permutation("(ADFGHS) (KZNBL)", new Alphabet());
        System.out.println(perma.permute(' '));
    }
}
