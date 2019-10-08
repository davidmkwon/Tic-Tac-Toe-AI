package DK;

public class Board {
    
    private Piece[][] board;

    public Board() {
        board = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = new Piece();
            }
        }
    }

    public Board(char[][] board) {
        this.board = new Piece[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = new Piece(board[i][j]);
            }
        }
    }

    public void setPiece(int row, int column, char mark) {
        board[row][column].setMark(mark);
    }

    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    /**
     * FIX THIS METHOD --> make it show indices for the user to use
     */
    public String toString() {
        int side = 3;
        String str = "";
        for (Piece[] row : board) {
            str += side + "   ";
            side--;
            for (Piece elem : row) {
                str += (elem.getMark() + " ");
            }
            str += "\n";
        }
        str += "\n    1 2 3";
        return str;
    }

    public boolean sameType(int x1, int y1, int x2, int y2) {
        return this.getPiece(x1, y1).getMark() == this.getPiece(x2, y2).getMark();
    }

    public boolean sameType(int x1, int y1, int x2, int y2, int x3, int y3) {
        return sameType(x1, y1, x2, y2) && sameType(x2, y2, x3, y3);
    }

    public boolean sameType(int x1, int y1, int x2, int y2, int x3, int y3, char c) {
        char comparisonChar = getPiece(x1, y1).getMark();
        return c == comparisonChar && sameType(x1, y1, x2, y2, x3, y3); // this short-circuits
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!this.getPiece(i, j).hasMark()) return false;
            }
        }
        return true;
    }

}