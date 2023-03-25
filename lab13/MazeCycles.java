import org.antlr.v4.runtime.misc.Pair;

/**
 * @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits protected fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */

    /**
     * Set up to find cycles of M.
     */
    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        start = maze.xyTo1D(1, 1);
        cycle = -1;
    }

    @Override
    public void solve() {
        cycle(start);
        for (int i = 0; i < maze.V(); i += 1) {
            marked[i] = false;
        }
        if (cycle == -1) {
            return;
        }
        for (int i = edgeTo[cycle]; i != cycle; i = edgeTo[i]) {
            marked[i] = true;
        }
        marked[cycle] = true;
        announce();

    }

    public void cycle(int k) {
        marked[k] = true;
        for (int x : maze.adj(k)) {
            if (!visited && marked[x] && edgeTo[k] != x) {
                edgeTo[x] = k;
                cycle = x;
                visited = true;
                return;
            }
            if (!marked[x]) {
                edgeTo[x] = k;
                cycle(x);
                if (visited) {
                    break;
                }
            }
        }
    }

    private Maze maze;

    private int start;

    private int cycle;

    private boolean visited;
}

