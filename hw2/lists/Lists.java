package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {

        if (L == null) {
            return null;

            IntList hood = L;
            IntList tool = null;
            IntList pointer = L;
            for (int y = L.head; y <= L.head; L = L.tail) {
                tool = L.tail;
                y = L.head;
                hood = L;

                if (L.tail != null && y == L.tail.head) {
                    break;
                }

            }

            if (head != null) {
                head.tail = null;
            }

        }         return new IntListList(pointer, naturalRuns(tool));
    }
}
