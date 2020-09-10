package src;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int player, int x, int y) {
        super(player, x, y);
        initializeImage("bishop");
        name = "BISHOP";
        value = 3.1;
    }
    boolean pseudoLegalMove(int newX, int newY) {
        if (Board.playerAt(newX, newY) == player) return false;
        
        int xDistance = Math.abs(newX - x);
        int yDistance = Math.abs(newY - y);
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
            if (y-x+i >= 0 && y-x+i <= 7 && legalMove(i, y-x+i))
                output.add(output.size(), new int[]{i, y-x+i});
            if (y+x-i >= 0 && y+x-i <= 7 && legalMove(i, y+x-i))
                output.add(output.size(), new int[]{i, y+x-i});
        }
        return output;
    }
}