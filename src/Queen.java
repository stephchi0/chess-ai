package src;
import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(int player, int x, int y) {
        super(player, x, y);
        initializeImage("queen");
        name = "QUEEN";
        value = 9;
    }
    public Queen(int player, int x, int y, boolean initializeImage) {//use this for ai looking at theoretically promoting a pawn to avoid initializing image a lot
        super(player, x, y);
        name = "QUEEN";
        value = 9;
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
        if (xDistance == yDistance) {
            for (int i = 1; i < xDistance; i++) {
                if (Board.pieceAt(x + i*(newX-x)/xDistance, y + i*(newY-y)/yDistance) != null)
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
        for (int i = 0; i < 8; i++) {
            if (y-x+i >= 0 && y-x+i <= 7 && legalMove(i, y-x+i))
                output.add(output.size(), new int[]{i, y-x+i});
            if (y+x-i >= 0 && y+x-i <= 7 && legalMove(i, y+x-i))
                output.add(output.size(), new int[]{i, y+x-i});
        }
        return output;
    }
}