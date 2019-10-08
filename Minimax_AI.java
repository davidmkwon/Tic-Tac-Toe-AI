package DK;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Minimax_AI {

    private char mark; // it is important that this mark is initialized properly

    public Minimax_AI() {
        this.mark = 'O';
    }

    // this method takes the assumption that it will only be called when it is the
    // AI's turn
    public int[] bestMove(Board board) {
        int[] bestMove = new int[2];
        ArrayList<ArrayList<Integer>> openMoves = getOpenMoves(board);
        if (openMoves.get(0).size() == 1) {
            bestMove[0] = openMoves.get(0).get(0);
            bestMove[1] = openMoves.get(1).get(0);
        } else {

            // initializes bestScore to the score of the first open move
            int initialRow = openMoves.get(0).get(0);
            int initialCol = openMoves.get(1).get(0);
            bestMove[0] = initialRow;
            bestMove[1] = initialCol;
            board.setPiece(initialRow, initialCol, this.mark);
            int bestScore = minimax(board, 'X');
            board.setPiece(initialRow, initialCol, ' ');

            for (int i = 1; i < openMoves.get(0).size(); i++) {
                int checkRow = openMoves.get(0).get(i);
                int checkCol = openMoves.get(1).get(i);
                board.setPiece(checkRow, checkCol, this.mark);
                int score = minimax(board, 'X');
                board.setPiece(checkRow, checkCol, ' ');
                if (score > bestScore) {
                    bestMove[0] = checkRow;
                    bestMove[1] = checkCol;
                }
            }
        }
        return bestMove;
    }

    public int minimax(Board board, char currMark) {
        // check terminal moves; these are the base cases
//        if (currMark == 'O' && terminalState(board, 'O')) return 10;
//        if (currMark == 'X' && terminalState(board, 'X')) return -10;
        if (currMark == 'O' && didWin(board, 'O')) return 10;
        if (currMark == 'X' && didWin(board, 'X')) return -10;
        if (didTie(board)) return 0;

        // if no terminal moves go through open moves
        ArrayList<ArrayList<Integer>> openMoves = getOpenMoves(board);
        ArrayList<Integer> minimaxScores = new ArrayList<Integer>();

        for (int i = 0; i < openMoves.get(0).size(); i++) {
            int rowIndex = openMoves.get(0).get(i);
            int colIndex = openMoves.get(1).get(i);
            board.setPiece(rowIndex, colIndex, currMark);
            char newMark = (currMark == 'X') ? 'O' : 'X';
            int score = minimax(board, newMark);
            board.setPiece(rowIndex, colIndex, ' ');
            minimaxScores.add(score);
        }

        if (currMark == 'O') {
            return Minimax_AI.max(minimaxScores);
        } else {
            return Minimax_AI.min(minimaxScores);
        }
    }

    public int[] randomMove(Board board) {
        int[] randMove = new int[2];
        ArrayList<ArrayList<Integer>> openMoves = getOpenMoves(board);
        if (openMoves.get(0).size() == 1) {
            randMove[0] = openMoves.get(0).get(0);
            randMove[1] = openMoves.get(1).get(0);
        } else {
            Random rand = new Random();
            int randIndex = rand.nextInt(openMoves.get(0).size());
            randMove[0] = openMoves.get(0).get(randIndex);
            randMove[1] = openMoves.get(1).get(randIndex);
        }
        return randMove;
    }

    public int[] terminalMove(Board board) {
        int[] terminalMove = new int[2];
        if (terminalState(board, this.mark)) {
            ArrayList<Integer> term = oneTerminalMove(board, this.mark);
            terminalMove[0] = term.get(0);
            terminalMove[1] = term.get(1);
        } else {
            terminalMove = randomMove(board);
        }
        return terminalMove;
    }

    public ArrayList<ArrayList<Integer>> getOpenMoves(Board board) {
        ArrayList<ArrayList<Integer>> openMoves = new ArrayList<ArrayList<Integer>>();
        openMoves.add(new ArrayList<Integer>());
        openMoves.add(new ArrayList<Integer>());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getPiece(i, j).getMark() != 'X' && board.getPiece(i, j).getMark() != 'O') {
                    openMoves.get(0).add(i);
                    openMoves.get(1).add(j);
                }
            }
        }
        return openMoves;
    }

    public ArrayList<ArrayList<Integer>> allTerminalMoves (Board board, char mark) {
        ArrayList<ArrayList<Integer>> terminalMoves = new ArrayList<ArrayList<Integer>>();
        terminalMoves.add(new ArrayList<Integer>());
        terminalMoves.add(new ArrayList<Integer>());
        ArrayList<ArrayList<Integer>> openMoves = getOpenMoves(board);
        for (int i = 0; i < openMoves.get(0).size(); i++) {
            int rowIndex = openMoves.get(0).get(i);
            int colIndex = openMoves.get(1).get(i);
            board.setPiece(rowIndex, colIndex, mark);
            if (Minimax_AI.didWin(board, mark)) {
                terminalMoves.get(0).add(rowIndex);
                terminalMoves.get(1).add(colIndex);
            }
            board.setPiece(rowIndex, colIndex, ' ');
        }
        return terminalMoves;
    }
    public ArrayList<Integer> oneTerminalMove(Board board, char mark) {
        ArrayList<Integer> move = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> terminalMoves = allTerminalMoves(board, mark);

        if (terminalMoves.get(0).size() == 1) {
            move.add(terminalMoves.get(0).get(0));
            move.add(terminalMoves.get(1).get(0));
        } else if (terminalMoves.get(0).size() > 1) {
            Random rand = new Random();
            int index = rand.nextInt(terminalMoves.get(0).size());
            move.add(terminalMoves.get(0).get(index));
            move.add(terminalMoves.get(1).get(index));
        }

        return move;
    }

    public boolean terminalState(Board board, char mark) {
        return (oneTerminalMove(board, mark).size() == 2);
    }
//
//    private boolean validCoord(int x, int y) {
//        return !(x < 0 || y < 0 || x > 2 || y > 2);
//    }

    private static boolean didWin(Board board, char c) {
        boolean win = false;
        if (board.sameType(0, 0, 0, 1, 0, 2, c) ||
                board.sameType(0, 0, 1, 0, 2, 0, c) ||
                board.sameType(0, 1, 1, 1, 2, 1, c) ||
                board.sameType(0, 2, 1, 2, 2, 2, c) ||
                board.sameType(0, 0, 1, 1, 2, 2, c) ||
                board.sameType(0, 2, 1, 1, 2, 0, c) ||
                board.sameType(2, 0, 2, 1, 2, 2, c) ||
                board.sameType(1, 0, 1, 1, 1, 2, c)) win = true;
        return win;
    }

    public static int max(ArrayList<Integer> arr) {
        if (arr.size() == 0) return 0;
        if (arr.size() == 1) return arr.get(0);

        int max = arr.get(0);
        for (Integer each : arr) {
            if ((int) each > max) max = (int) each;
        }
        return max;
    }

    public static int min(ArrayList<Integer> arr) {
        if (arr.size() == 0) return 0;
        if (arr.size() == 1) return arr.get(0);

        int min = arr.get(0);
        for (Integer each : arr) {
            if ((int) each < min) min = (int) each;
        }
        return min;
    }

    private static boolean didTie(Board board) {
        return board.isFull() && !didWin(board, 'X') && !didWin(board, 'O');
    }

}
