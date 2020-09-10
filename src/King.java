package src;
import java.util.ArrayList;

public class King extends Piece {
    boolean castleLegal = true;
    boolean castled = false;
    public King(int player, int x, int y) {
        super(player, x, y);
        initializeImage("king");
        name = "KING";
        value = 9000;
    }
    int castle(int newX, int newY) {//1 for kingside, -1 for queenside, 0 for neither
        if (castleLegal && newY == y) {
            if ((newX == 7 || newX == 6) && //input for kingside castle
                Board.pieceAt(5, y) == null && Board.pieceAt(6, y) == null && //no pieces in between
                Board.pieceAt(7, y) instanceof Rook && ((Rook)Board.pieceAt(7, y)).castleLegal && //pieces haven't moved
                !Board.inCheck(player) && //not in check
                !Board.squareAttacked(-player, 5, y) && !Board.squareAttacked(-player, 6, y) && !Board.squareAttacked(-player, 7, y)//squares aren't attacked
            ) return 1;//if all of the above are true then kingside castle

            else if ((newX == 0 || newX == 2) && //input for queenside castle
                Board.pieceAt(3, y) == null && Board.pieceAt(2, y) == null && Board.pieceAt(1, y) == null && //no pieces in between
                Board.pieceAt(0, y) instanceof Rook && ((Rook)Board.pieceAt(0, y)).castleLegal && //pieces haven't moved
                !Board.inCheck(player) && //not in check
                !Board.squareAttacked(-player, 3, y) && !Board.squareAttacked(-player, 2, y) && !Board.squareAttacked(-player, 1, y) && !Board.squareAttacked(-player, 0, y)//squares aren't attacked
            ) return -1;//if all of the above are true then queenside castle
        }
        return 0;
    }
    boolean pseudoLegalMove(int newX, int newY) {//does not check for castling
        if (Board.playerAt(newX, newY) == player) return false;

        return (Math.abs(newX - x) <= 1 && Math.abs(newY - y) <= 1);
    }
    ArrayList<int[]> getLegalMoves() {
        ArrayList<int[]> output = new ArrayList<int[]>();
        if (castle(7, y) == 1) output.add(output.size(), new int[]{6, y});//add kingside castle if legal
        if (castle(0, y) == -1) output.add(output.size(), new int[]{2, y});//add queenside castle if legal
        for (int i = Math.max(x-1, 0); i <= Math.min(x+1, 7); i++) {
            for (int j = Math.max(y-1, 0); j <= Math.min(y+1, 7); j++) {
                if (legalMove(i, j))
                    output.add(output.size(), new int[]{i, j});
            }
        }
        return output;
    }
}