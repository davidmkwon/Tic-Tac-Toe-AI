package DK;

import java.util.Random;
import java.util.Scanner;

public class TicTacToeOne {

    private Board board;
    private boolean isX;
    private char currMark;
    private Random rand = new Random();
    private int numWinsX = 0, numWinsO = 0;
    private char playerMark = 'X'; // for the sake of simplicity, the player will always be X
    private Minimax_AI AI = new Minimax_AI(); // for the sake of simplicity, the AI will always be O
    private int difficultyLevel = 1; // 1 = easy, 2 = medium, 3 = impossible

    public TicTacToeOne() {
        this.board = new Board();
        this.isX = rand.nextBoolean();
        this.currMark = (isX) ? 'X' : 'O';
    }

    public void printInstructions() {
        System.out.println("\nWelcome to Tic-Tac-Toe! Here is the board:");
        System.out.println(this);
        System.out.println("The statement at the top indicates who's turn it currently is. Each player will " +
                "be asked to \nenter an X-coordinate and Y-coordinate. These coordinates must be within the range" +
                " 1 - 3. The \nscore tally is located at the top.\n");
        System.out.println("What difficult level would you like to play against (1 : easy, 2 : medium, 3: hard)? ");
        int input;
        while (true) {
            Scanner scan = new Scanner(System.in);
            input = scan.nextInt();
            if (input == 1 || input == 2 || input == 3) break;
            System.out.println("Please enter either 1, 2, or 3");
        }
        this.difficultyLevel = input;
    }

    public void printTie() {
        System.out.println(this);
        System.out.println("Tie game!");
    }

    public void printWinner() {
        System.out.println(this);
        if (currMark == 'O') {
            System.out.println("You lost. Better luck next time!");
        } else {
            System.out.println("Congratulations! You won this game!");
        }
    }

    // Make some statement that says "Play again? [y/n]"
    // ALSO have a tally at the top showing who has how many wins in the game
    public void play() {
        printInstructions();
        while(true) {
            int[] coordinates;
            if (isX) {
                coordinates = ask();
                int xCoord = coordinates[1];
                int yCoord = coordinates[0];
                this.addPiece(3 - xCoord, yCoord - 1);
            } else {
                switch (difficultyLevel) {
                    case 1:
                        coordinates = AI.randomMove(board);
                        break;
                    case 2:
                        coordinates = AI.terminalMove(board);
                        break;
                    case 3:
                        coordinates = AI.bestMove(board);
                        break;
                    default:
                        coordinates = new int[] {-1, -1};
                        break;
                }
                this.addPiece(coordinates[0], coordinates[1]);
            }

            if (didWin(currMark)) {
                if (isX) numWinsX++;
                else numWinsO++;
                printWinner();
                if (!playAgain())  break;
                this.board = new Board();
            } else if (didTie()) {
                printTie();
                if (!playAgain())  break;
                this.board = new Board();
            }

            isX = !isX;
            currMark = (isX) ? 'X' : 'O';
            System.out.println(this);
        }
        System.out.println("Thanks for playing! ");
        // reset all variables so user can play again on the same object rather than creating a new one
        this.numWinsO = 0;
        this.numWinsX = 0;
        this.isX = rand.nextBoolean();
        this.currMark = (isX) ? 'X' : 'O';
        this.board = new Board();
    }

    // This method was removed because of inefficiency and lack of purpose
    public void updateScores() {
        if (didWin(this.currMark)) {
            if (isX) numWinsX++;
            else numWinsO++;
        }
    }

    public boolean playAgain() {
        boolean out = false;
        while (true) {
            try {
                System.out.println("Play again? [y/n]");
                Scanner scan = new Scanner(System.in);
                String answer = scan.next();
                if (answer.equals("y")) {
                    out = true;
                    break;
                }
                else if (answer.equals("n")) {
                    out = false;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Not a valid response. Play again? [y/n]");
            }
        }
        return out;
    }

    // Implement it so that when it asks what move the user wants to do then it
    // will include what character they are.
    public int[] askInput() {

        int[] coords = new int[2];
        while(true) {
            try {
                System.out.print("X coordinate: ");
                Scanner scan = new Scanner(System.in);
                coords[0] = scan.nextInt();
                board.getPiece(coords[0] - 1, 0);
                break;
            } catch (Exception e) {
                System.out.println("Invalid value! Must be within 1-3.");
            }
        }

        while(true) {
            try {
                System.out.print("Y coordinate: ");
                Scanner scan = new Scanner(System.in);
                coords[1] = scan.nextInt();
                board.getPiece(0, coords[1] - 1);
                break;
            } catch (Exception e) {
                System.out.println("Invalid value! Must be within 1-3.");
            }
        }

        return coords;

    }

    public int[] ask() {
        int[] coords = new int[2];
        while (true) {
            coords = askInput();
            if (validCoordinates(3 - coords[1], coords[0] - 1)) break;
            System.out.println("That spot is already taken.");
        }
        return coords;
    }

    public boolean validCoordinates(int x, int y) {
        return this.board.getPiece(x, y).getMark() != 'X' && this.board.getPiece(x, y).getMark() != 'O';
    }

    public void addPiece(int x, int y) {
        board.setPiece(x, y, currMark);
    }

//    private void setPiece(int x, int y, char mark) {
//        board.setPiece(x, y, mark);
//    }


    // A hard-bash check to see if char c won the game
    // Implemented because may actually be more/equally efficient.
    public boolean didWin(char c) {
        boolean win = false;
        if (sameType(0, 0, 0, 1, 0, 2, c) ||
                sameType(0, 0, 1, 0, 2, 0, c) ||
                sameType(0, 1, 1, 1, 2, 1, c) ||
                sameType(0, 2, 1, 2, 2, 2, c) ||
                sameType(0, 0, 1, 1, 2, 2, c) ||
                sameType(0, 2, 1, 1, 2, 0, c) ||
                sameType(2, 0, 2, 1, 2, 2, c) ||
                sameType(1, 0, 1, 1, 1, 2, c)) win = true;
        return win;
    }

    // This method should only be called when the board is filled up
    public boolean didTie() {
        return isFull() && !didWin('X') && !didWin('O');
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!board.getPiece(i, j).hasMark()) return false;
            }
        }
        return true;
    }

    public boolean sameType(int x1, int y1, int x2, int y2) {
        return this.board.getPiece(x1, y1).getMark() == this.board.getPiece(x2, y2).getMark();
    }

    public boolean sameType(int x1, int y1, int x2, int y2, int x3, int y3) {
        return sameType(x1, y1, x2, y2) && sameType(x2, y2, x3, y3);
    }

    public boolean sameType(int x1, int y1, int x2, int y2, int x3, int y3, char c) {
        char comparisonChar = board.getPiece(x1, y1).getMark();
        return c == comparisonChar && sameType(x1, y1, x2, y2, x3, y3); // this short-circuits
    }

    public String toString() {
        String str = "\n  PLAYER: " + this.currMark + "\n";
        str += "   X:" + numWinsX + " O:" + numWinsO + "\n\n";
        str += board.toString();
        str += "\n";
        return str;
    }

}
