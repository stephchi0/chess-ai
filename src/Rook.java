package src;
import java.util.ArrayList;

public class Rook extends Piece {
    boolean castleLegal = true;
    public Rook(int player, int x, int y) {
        super(player, x, y);
        initializeImage("rook");
        name = "ROOK";
        value = 5;
    }
    boolean pseudoLegalMove(int newX, int newY) {
        if (Board.playerAt(newX, newY) == player) return false;

        int xDistance = Math.abs(newX - x);
        int yDistance = Math.abs(newY - y);
        int distance = Math.max(xDistance, yDistance);
        if (xDistance == 0 || yDistance == 0) {
            for (int i = 1; i < distance; i++) {
                if (Board.pieceAt(x + i*(newX-x)/distance, y + i*(newY-y)/distance) != null)
                    return false;
            }
            return true;
        }
        return false;
    }
    ArrayList<int[]> getLegalMoves() {
        ArrayList<int[]> output = new ArrayList<int[]>();
        for (int i = 0; i < 8; i++) {
            if (legalMove(i, y))
                output.add(output.size(), new int[]{i, y});
            if (legalMove(x, i))
                output.add(output.size(), new int[]{x, i});
        }
        return output;
    }
}