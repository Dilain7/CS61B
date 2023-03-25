import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A set of String values.
 *
 * @author
 */
class ECHashStringSet implements StringSet {

    private static double min_load = 0.2;
    private static double max_load = 5;


    public ECHashStringSet() {
        _size = 0;
        _elems = new LinkedList[(int) (1 / min_load)];

    }

    @Override
    public void put(String s) {
        if (s != null) {
            if (load() > max_load)
                resize();
        }
        int index = getDex(s.hashCode());
        if (_elems[index] == null) {
            _elems[index] = new LinkedList<String>();
        }
        _elems[index].add(s);
        _size += 1;

    }


    @Override
    public boolean contains(String s) {
        if (s != null) {
            int index = getDex(s.hashCode());

            if (_elems[index] == null) {
                return false;
            } else {
                return _elems[index].contains(s);
            }
        } else {
            return false;
        }
    }

    @Override
    public List<String> asList() {
        ArrayList<String> _output = new ArrayList<>();
        for (int x = 0; x < _elems.length; x += 1) {
            if (_elems[x] != null) {
                for (int y = 0; y < _elems[x].size(); y += 1) {
                    _output.add(_elems[x].get(y));
                }
            }

        }
        return _output;
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return null;
    }

    private double load() {
        return ((double) _size) / ((double) _elems.length);
    }

    private int _size;

    public void resize() {
        LinkedList<String>[] prev = _elems;
        _elems = new LinkedList[2 * prev.length];
        _size = 0;

        for (LinkedList<String> check : prev) {
            if (check != null) {
                for (String _in : check) {
                    this.put(_in);
                }

            }
        }

    }

    public LinkedList<String>[] info() {
        return _elems;
    }

    public int getDex(int hashCode) {
        int bfor = hashCode & 1;
        int dex = ((hashCode >>> 1) | bfor) % info().length;
        return dex;
    }

    private LinkedList<String>[] _elems;
}
