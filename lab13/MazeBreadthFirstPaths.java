import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits visible fields:
    protected int[] distTo;
    protected int[] edgeTo;
    protected boolean[] marked;
    */

    /** A breadth-first search of paths in M from (SOURCEX, SOURCEY) to
     *  (TARGETX, TARGETY). */
    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY,
                                 int targetX, int targetY) {
        super(m);
        maze = m;
        _start = maze.xyTo1D(sourceX, sourceY);
        _target = maze.xyTo1D(targetX, targetY);
        distTo[_start] = 0;
        edgeTo[_start] = _start;
        neighbours = new PriorityQueue<>();
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        neighbours.add(_start);
        while (!neighbours.isEmpty()) {
            int nxt = neighbours.poll();
            marked[nxt] = true;
            announce();
            if (nxt == _target) {
                return;
            }
            for (int x : maze.adj(nxt)) {
                if (!marked[x]) {
                neighbours.add(x);
                edgeTo[x] = nxt;
                distTo[x] = distTo[nxt] + 1;
                announce();

                }
            }
        }
        // Use queue to keep track of neighbors?
        // Go to all three neighbors, then go to the neighbors of each of those
        // Keep going until you find the target
        // and marked, as well as call announce()
    }


    @Override
    public void solve() {
        bfs();
    }

    private int _target;

    private int _start;

    private Maze maze;

    Queue<Integer> neighbours;
}

