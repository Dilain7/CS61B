import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 *
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /**
     * Creates a new empty set.
     */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = helper(_root, s);
    }

    Node helper(Node _node, String _string) {
        if (_node.equals(null)) {
            new Node(_string);
        }
        if (_string.compareTo(_node.s) > 0) {
            helper(_node.right, _string);
        } else if (_string.compareTo(_node.s) < 0) {
            helper(_node.left, _string);
        }
        return _node;
    }

    @Override
    public boolean contains(String s) {
        return helpContains(_root, s);
    }

    /* helper method to check if the given
     * string is in the tree
     */
    boolean helpContains(Node nun, String sus) {
        if (_root.equals(null)) {
            return false;
        } else if (sus.compareTo(nun.s) == 0) {
            return true;
        } else if (sus.compareTo(nun.s) > 0) {
            return helpContains(nun.right, sus);
        } else {
            return helpContains(nun.left, sus);
        }
    }

    @Override
    public List<String> asList() {
        ArrayList<String> _output = new ArrayList<>();
        for (String st : this) {
            _output.add(st);
        }
        return _output;
    }


    /**
     * Represents a single Node of the tree.
     */
    private static class Node {
        /**
         * String stored in this Node.
         */
        private String s;
        /**
         * Left child of this Node.
         */
        private Node left;
        /**
         * Right child of this Node.
         */
        private Node right;

        /**
         * Creates a Node containing SP.
         */
        Node(String sp) {
            s = sp;
        }
    }

    /**
     * An iterator over BSTs.
     */
    private static class BSTIterator implements Iterator<String> {
        /**
         * Stack of nodes to be delivered.  The values to be delivered
         * are (a) the label of the top of the stack, then (b)
         * the labels of the right child of the top of the stack inorder,
         * then (c) the nodes in the rest of the stack (i.e., the result
         * of recursively applying this rule to the result of popping
         * the stack.
         */
        private Stack<Node> _toDo = new Stack<>();

        /**
         * A new iterator over the labels in NODE.
         */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Add the relevant subtrees of the tree rooted at NODE.
         */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }
    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return;
    }

    /**
     * Root node of the tree.
     */
    private Node _root;
}
