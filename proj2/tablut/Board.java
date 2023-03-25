package tablut;


import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;
import java.util.Formatter;
import static tablut.Piece.*;
import static tablut.Square.*;
import static tablut.Utils.*;
import static tablut.Move.mv;
import java.util.ArrayList;


/**
 * The state of a Tablut Game.
 *
 * @author Dilain Saparamadu
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 9;

    /**
     * The throne (or castle) square and its four surrounding squares..
     */
    static final Square THRONE = sq(4, 4),
            NTHRONE = sq(4, 5),
            STHRONE = sq(4, 3),
            WTHRONE = sq(3, 4),
            ETHRONE = sq(5, 4);

    /**
     * Initial positions of attackers.
     */
    static final Square[] INITIAL_ATTACKERS = {
            sq(0, 3), sq(0, 4), sq(0, 5), sq(1, 4),
            sq(8, 3), sq(8, 4), sq(8, 5), sq(7, 4),
            sq(3, 0), sq(4, 0), sq(5, 0), sq(4, 1),
            sq(3, 8), sq(4, 8), sq(5, 8), sq(4, 7)
    };

    /**
     * Initial positions of defenders of the king.
     */
    static final Square[] INITIAL_DEFENDERS = {
        NTHRONE, ETHRONE, STHRONE, WTHRONE,
        sq(4, 6), sq(4, 2),
        sq(2, 4), sq(6, 4)
    };

    /**
     * Initializes a game board with SIZE squares on a side in the
     * initial position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {
        if (model == this) {
            return;
        }
        init();
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _recentMovesPiece = new Stack<>();
        _recentMovesSqr = new Stack<>();
        _winner = null;
        wPieces = INITIAL_DEFENDERS.length;
        bPieces = INITIAL_ATTACKERS.length;
        previousMoves = new HashSet<>();
        for (Square i : INITIAL_ATTACKERS) {
            put(BLACK, i);
        }
        for (Square i : INITIAL_DEFENDERS) {
            put(WHITE, i);
        }
        put(KING, THRONE);
        for (int y = 0; y < 9; y += 1) {
            if (y == 0 || y == 8) {
                put(EMPTY, sq(0, y));
                put(EMPTY, sq(1, y));
                put(EMPTY, sq(2, y));
                put(EMPTY, sq(6, y));
                put(EMPTY, sq(7, y));
                put(EMPTY, sq(8, y));
            } else if (y == 1 || y == 7
                    || y == 2 || y == 6) {
                put(EMPTY, sq(0, y));
                put(EMPTY, sq(1, y));
                put(EMPTY, sq(2, y));
                put(EMPTY, sq(3, y));
                put(EMPTY, sq(5, y));
                put(EMPTY, sq(6, y));
                put(EMPTY, sq(7, y));
                put(EMPTY, sq(8, y));
            } else if (y == 3 || y == 5) {
                put(EMPTY, sq(1, y));
                put(EMPTY, sq(2, y));
                put(EMPTY, sq(3, y));
                put(EMPTY, sq(5, y));
                put(EMPTY, sq(6, y));
                put(EMPTY, sq(7, y));
            } else if (y == 4) {
                continue;
            }
        }
        _turn = BLACK;
        previousMoves.add(encodedBoard());
    }


    /**
     * Set the move limit to LIM.  It is an error if 2*LIM <= moveCount().
     *
     * @param n move's limit
     */
    void setMoveLimit(int n) {
        _moveLimit = n;
        if (_moveCount >= 2 * _moveLimit) {
            throw error("The cap on moves has been exceeded");
        }
    }

    /**
     * Return a Piece representing whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the winner in the current position, or null if there is no winner
     * yet.
     */
    Piece winner() {
        return _winner;
    }

    /**
     * Returns true iff this is a win due to a repeated position.
     */
    boolean repeatedPosition() {
        return _repeated;
    }

    /**
     * Record current position and set winner() next mover if the current
     * position is a repeat.
     */
    private void checkRepeated() {
        String encode = encodedBoard();
        if (previousMoves.contains(encode)) {
            _repeated = true;
            _winner = turn();
        } else {
            previousMoves.add(encode);
        }
    }


    /**
     * Return the number of moves since the initial position that have not been
     * undone.
     */
    int moveCount() {
        return _moveCount;
    }

    /**
     * Return location of the king.
     */
    Square kingPosition() {
        for (int x = 0; x < NUM_SQUARES - 1; x += 1) {
            Piece temp = this.get(sq(x));
            if (temp == KING) {
                return sq(x);
            }
        }
        throw error("The King was never found");
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return _board[col][row];
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _board[s.col()][s.row()] = p;
    }

    /**
     * Set square S to P and record for undoing.
     */
    final void revPut(Piece p, Square s) {
        put(p, s);
        _recentMovesPiece.push(p);
        _recentMovesSqr.push(s);

    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, sq(col - 'a', row - '1'));
    }

    /**
     * Return true iff FROM - TO is an unblocked rook move on the current
     * board.  For this to be true, FROM-TO must be a rook move and the
     * squares along it, other than FROM, must be empty.
     */
    boolean isUnblockedMove(Square from, Square to) {
        if (!get(from).equals(KING) && to == THRONE) {
            return false;
        } else if (from.col() == to.col()) {
            if (to.row() > from.row()) {
                int coll = from.col();
                int rowF = from.row() + 1;
                int rowT = to.row();
                for (; rowF <= rowT; rowF += 1) {
                    if (!this.get(sq(coll, rowF)).equals(EMPTY)) {
                        return false;
                    }
                    return true;
                }
            } else {
                int coll = from.col();
                int rowF = from.row() - 1;
                int rowT = to.row();
                for (; rowF >= rowT; rowF -= 1) {
                    if (!this.get(sq(coll, rowF)).equals(EMPTY)) {
                        return false;
                    }
                    return true;
                }
            }
        } else if (from.row() == to.row()) {
            if (to.col() > from.col()) {
                int roww = from.row();
                int colF = from.col() + 1;
                int colT = to.col();
                for (; colF <= colT; colF += 1) {
                    if (!this.get(sq(colF, roww)).equals(EMPTY)) {
                        return false;
                    }
                    return true;
                }
            } else {
                int roww = from.row();
                int colF = from.col() - 1;
                int colT = to.col();
                for (; colF >= colT; colF -= 1) {
                    if (!this.get(sq(colF, roww)).equals(EMPTY)) {
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {
        return get(from).side() == _turn;
    }

    /**
     * Return true iff FROM-TO is a valid move.
     */
    boolean isLegal(Square from, Square to) {
        if (isLegal(from)) {
            if (isUnblockedMove(from, to)) {
                return true;
            }
            return false;
        } else if (this.get(from).equals(KING) && _turn.equals(WHITE)) {
            if (isUnblockedMove(from, to)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to());
    }

    /**
     * Move FROM-TO, assuming this is a legal move.
     */
    void makeMove(Square from, Square to) {
        Square s2;
        assert isLegal(from, to);
        Piece movingPiece = this.get(from);
        this.revPut(EMPTY, from);
        this.revPut(movingPiece, to);
        if ((to.col() + 2 < SIZE)) {
            capture(to, sq(to.col() + 2, to.row()));
        }
        if ((to.col() - 2 >= 0)) {
            capture(to, sq(to.col() - 2, to.row()));
        }
        if ((to.row() + 2 < SIZE)) {
            capture(to, sq(to.col(), to.row() + 2));
        }
        if ((to.row() - 2 >= 0)) {
            capture(to, sq(to.col(), to.row() - 2));
        }
        _turn = _turn.opponent();
        checkRepeated();
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        makeMove(move.from(), move.to());
    }

    /**
     * Capture the piece between SQ0 and SQ2, assuming a piece just moved to
     * SQ0 and the necessary conditions are satisfied.
     */
    private void capture(Square sq0, Square sq2) {
        Square sqrBetween = sq0.between(sq2);
        Piece peace = get(sqrBetween);
        if (!peace.side().equals(get(sq0).side())) {
            if (sqrBetween == THRONE
                    && get(sq0.diag1(sqrBetween)).equals(BLACK)
                    && get(sq0.diag2(sqrBetween)).equals(BLACK)
                    && this.get(sq2).equals(BLACK)
                    && this.get(sq0).equals(BLACK)
                    && get(THRONE) == KING) {
                this.revPut(EMPTY, THRONE);
                _winner = BLACK;
            }
            if (this.get(sq0).equals(BLACK) && this.get(sq2).equals(KING)
                    && get(THRONE) == KING) {
                if (get(sqrBetween.diag1(THRONE)).equals(BLACK)
                        && get(sqrBetween.diag2(THRONE)).equals(BLACK)
                        && this.get(sqrBetween).equals(WHITE)) {
                    Square otherSquare
                            = sqrBetween.rookMove(sqrBetween.direction(THRONE),
                            2);
                    if (this.get(otherSquare).equals(BLACK)) {
                        _recentMovesSqr.push(sqrBetween);
                    }
                }
            }
            if (peace == KING && sqrBetween.adjacent(THRONE)
                    && get(sq0).equals(BLACK)) {
                if (get(sq0.diag1(sqrBetween)).equals(BLACK)
                        || sq0.diag1(sqrBetween).equals(THRONE)) {
                    if (get(sq0.diag2(sqrBetween)).equals(BLACK)
                            || sq0.diag2(sqrBetween).equals(THRONE)) {
                        if (get(sq2).equals(BLACK) || sq2.equals(THRONE)) {
                            _recentMovesPiece.push(KING);
                            this.revPut(EMPTY, sqrBetween);
                            _winner = BLACK;
                        }
                    }
                }
            }
            if (this.get(sq2).side().equals(this.get(sq0).side())
                    || (get(sq2) == EMPTY && sq2 == THRONE)) {
                if (peace == KING) {
                    if (sqrBetween == THRONE || sqrBetween == NTHRONE
                            || sqrBetween == STHRONE || sqrBetween == ETHRONE
                            || sqrBetween == WTHRONE) {
                        return;
                    } else {
                        this.revPut(EMPTY, sqrBetween);
                        _winner = BLACK;
                    }
                } else {
                    _recentMovesPiece.push(peace);
                    _recentMovesSqr.push(sqrBetween);
                    this.revPut(EMPTY, sqrBetween);
                }
            }
        }
    }

    /**
     * Undo one move.  Has no effect on the initial board.
     */
    void undo() {
        if (_moveCount > 0) {
            undoPosition();
            if (_recentMovesPiece != null && _recentMovesSqr != null) {
                if (_recentMovesPiece.peek().equals(EMPTY)) {
                    _recentMovesPiece.pop();
                    _recentMovesSqr.pop();
                    this.revPut(_recentMovesPiece.pop(), _recentMovesSqr.pop());
                    undo();

                } else {
                    Piece tempPiece = _recentMovesPiece.pop();
                    this.revPut(_recentMovesPiece.pop(), _recentMovesSqr.pop());
                    this.revPut(tempPiece, _recentMovesSqr.pop());
                }
            }
        }
    }

    /**
     * Remove record of current position in the set of positions encountered,
     * unless it is a repeated position or we are at the first move.
     */
    private void undoPosition() {

        _repeated = false;
    }

    /**
     * Clear the undo stack and board-position counts. Does not modify the
     * current position or win status.
     */
    void clearUndo() {

    }

    /**
     * Return a new mutable list of all legal moves on the current board for
     * SIDE (ignoring whose turn it is at the moment).
     */
    List<Move> legalMoves(Piece side) {
        HashSet<Square> psLocation = pieceLocations(side);
        movesList = new ArrayList<>();
        for (int i = 0; i < NUM_SQUARES; i += 1) {
            if (psLocation.contains(sq(i))) {
                int coll = sq(i).col();
                int roww = sq(i).row();
                if ((coll + 1) < 9) {
                    while (isUnblockedMove(sq(i), sq(coll + 1, roww))) {
                        movesList.add(mv(sq(i), sq(coll + 1, roww)));
                        if (coll + 1 < 9) {
                            coll += 1;
                        } else {
                            break;
                        }
                    }
                }

                if ((coll - 1) > 0) {
                    while (isUnblockedMove(sq(i), sq(coll - 1, roww))) {
                        movesList.add(mv(sq(i), sq(coll - 1, roww)));
                        if (coll - 1 > 9) {
                            coll -= 1;
                        } else {
                            break;
                        }
                    }
                }
                if ((roww + 1) < 9) {
                    while (isUnblockedMove(sq(i), sq(coll, roww + 1))) {
                        movesList.add(mv(sq(i), sq(coll, roww + 1)));
                        if (roww + 1 < 9) {
                            roww += 1;
                        } else {
                            break;
                        }
                    }
                }
                if ((roww - 1) > 0) {
                    while (isUnblockedMove(sq(i), sq(coll, roww - 1))) {
                        movesList.add(mv(sq(i), sq(coll, roww - 1)));
                        if (roww - 1 > 0) {
                            roww -= 1;
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return movesList;
    }

    /**
     * Return true iff SIDE has a legal move.
     */
    boolean hasMove(Piece side) {
        legalMoves(side);
        if (movesList.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /**
     * Return a text representation of this Board.  If COORDINATES, then row
     * and column designations are included along the left and bottom sides.
     */
    String toString(boolean coordinates) {
        Formatter out = new Formatter();
        for (int r = SIZE - 1; r >= 0; r -= 1) {
            if (coordinates) {
                out.format("%2d", r + 1);
            } else {
                out.format("  ");
            }
            for (int c = 0; c < SIZE; c += 1) {
                out.format(" %s", get(c, r));
            }
            out.format("%n");
        }
        if (coordinates) {
            out.format("  ");
            for (char c = 'a'; c <= 'i'; c += 1) {
                out.format(" %c", c);
            }
            out.format("%n");
        }
        return out.toString();
    }

    /**
     * Return the locations of all pieces on SIDE.
     */
    private HashSet<Square> pieceLocations(Piece side) {
        _pieceLocations = new HashSet<>();
        assert side != EMPTY;
        for (int x = 0; x < NUM_SQUARES - 1; x += 1) {
            Piece temp = this.get(sq(x));
            if (temp.equals(side)) {
                _pieceLocations.add(sq(x));
            }
        }
        return _pieceLocations;
    }

    /**
     * Return the contents of _board in the order of SQUARE_LIST as a sequence
     * of characters: the toString values of the current turn and Pieces.
     */
    String encodedBoard() {
        char[] result = new char[Square.SQUARE_LIST.size() + 1];
        result[0] = turn().toString().charAt(0);
        for (Square sq : SQUARE_LIST) {
            result[sq.index() + 1] = get(sq).toString().charAt(0);
        }
        return new String(result);
    }

    /** white pieces.
     * @return number of whites */
    public int whitePieces() {
        return wPieces;
    }

    /** black pieces.
     * @return number of blacks */
    public int blackPieces() {
        return bPieces;
    }


    /**
     * Piece whose turn it is (WHITE or BLACK).
     */
    private Piece _turn;
    /**
     * Cached value of winner on this board, or null if it has not been
     * computed.
     */
    private Piece _winner;
    /**
     * Number of (still undone) moves since initial position.
     */
    private int _moveCount;
    /**
     * True when current board is a repeated position (ending the game).
     */
    private boolean _repeated;

    /**
     * Creating a board for the game.
     */
    private Piece[][] _board = new Piece[9][9];

    /**
     * Hash Map of all the moves done.
     */
    private HashMap<Integer, HashSet[]> _allMoves;

    /**
     * Cap on moves allowed.
     */
    private int _moveLimit;

    /**
     * Set of all colored moves.
     */
    private HashSet<String> previousMoves;


    /**
     * Set of all locations of pieces.
     */
    private HashSet<Square> _pieceLocations;

    /**
     * index used to count moves.
     */
    private int indxMoveCount;

    /**
     * List of all the movest made.
     */
    private List<Move> movesList;

    /**
     * Stack of moves stored as squares in order.
     */
    private Stack<Square> _recentMovesSqr;

    /**
     * Stack of moves as pieces in order.
     */
    private Stack<Piece> _recentMovesPiece;

    /** white pieces. */
    private int wPieces;

    /** black pieces. */
    private int bPieces;

}
