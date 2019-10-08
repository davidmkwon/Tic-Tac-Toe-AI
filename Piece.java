package DK;

public class Piece {
    
    private char mark;

    public Piece() {
        this.mark = ' ';
    }

    public Piece(char mark) {
        this.mark = mark;
    }

    public char getMark() {
        return this.mark;
    }

    public void setMark(char mark) {
        this.mark = mark;
    }

    public boolean hasMark() { return this.mark == 'X' || this.mark == 'O'; }

}