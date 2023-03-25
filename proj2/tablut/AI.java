package tablut;


import static java.lang.Math.*;

import static tablut.Piece.*;

/** A Player that automatically generates moves.
 *  @author Dilain Saparamadu
 */
class AI extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A position-score magnitude indicating a forced win in a subsequent
     *  move.  This differs from WINNING_VALUE to avoid putting off wins. */
    private static final int WILL_WIN_VALUE = Integer.MAX_VALUE - 40;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        findMove();
        return _lastFoundMove.toString();
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        _lastFoundMove = null;
        if (myPiece().equals(BLACK)) {
            findMove(b, maxDepth(b), true, -1, -INFTY, +INFTY);
        } else {
            findMove(b, maxDepth(b), true, 1, -INFTY, +INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (sense == 1) {
            if (depth == 0 || board.winner() != null) {
                return staticScore(board);
            }
            int bestSoFar = -INFTY;
            for (Move M: board.legalMoves(WHITE)) {
                board.makeMove(M);
                int response = findMove(board, depth, false, -1, alpha, beta);
                board.undo();
                if (response >= bestSoFar) {
                    bestSoFar = response;
                    alpha = max(alpha, response);
                    if (saveMove) {
                        _lastFoundMove = M;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestSoFar;


        } else if (sense == -1) {
            if (depth == 0 || board.winner() != null) {
                return staticScore(board);
            }
            int bestSoFar = +INFTY;
            for (Move M: board.legalMoves(BLACK)) {
                board.makeMove(M);
                int response = findMove(board, depth, false, 1, alpha, beta);
                board.undo();
                if (response <= bestSoFar) {
                    bestSoFar = response;
                    beta = min(beta, response);
                    if (saveMove) {
                        _lastFoundMove = M;
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestSoFar;
        }
        return 0;
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private static int maxDepth(Board board) {
        return 4;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        int total = 0;
        total += board.whitePieces();
        total -= board.blackPieces();

        Square kingspo = board.kingPosition();
        for (int i = 0; i < 4; i += 1) {
            if (board.get(kingspo.rookMove(i, 1)) == BLACK) {
                total -= total;
            }
        }
        return total;
    }




}
