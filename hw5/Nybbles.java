/** Represents an array of integers each in the range -8..7.
 *  Such integers may be represented in 4 bits (called nybbles).
 *  @author
 */
public class Nybbles {

    /** Maximum positive value of a Nybble. */
    public static final int MAX_VALUE = 7;

    /** Return an array of size N. */
    public Nybbles(int N) {
        // DON'T CHANGE THIS.
        _data = new int[(N + 7) / 8];
        _n = N;
    }

    /** Return the size of THIS. */
    public int size() {
        return _n;
    }

    /** Return the Kth integer in THIS array, numbering from 0.
     *  Assumes 0 <= K < N. */
    public int get(int k) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else {
            int _index = k / 8;
            int _subIndex = k % 8;
            int _dexElem = _data[_index] >>> _subIndex;
            _dexElem &= 0b1111;

            if (_dexElem >= 8) {
                return _dexElem - 16;
            } else {
                return _dexElem;
            }
        }
    }

    /** Set the Kth integer in THIS array to VAL.  Assumes
     *  0 <= K < N and -8 <= VAL < 8. */
    public void set(int k, int val) {
        if (k < 0 || k >= _n) {
            throw new IndexOutOfBoundsException();
        } else if (val < (-MAX_VALUE - 1) || val > MAX_VALUE) {
            throw new IllegalArgumentException();
        } else {
            _data[k/8] = (_data[k/8]) &= ~(15 << (k%8)*4);
            int _dexElem = val;
            if (_dexElem < 0) {
                _dexElem += 16;
            } else {
                _dexElem &= 15;
                _dexElem <<= ((k % 8)) * 4;
                _data[k / 8] |= _dexElem;
            }
        }
    }

    // DON'T CHANGE OR ADD TO THESE.
    /** Size of current array (in nybbles). */
    private int _n;
    /** The array data, packed 8 nybbles to an int. */
    private int[] _data;
}
