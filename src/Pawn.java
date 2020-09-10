package src;
import java.util.ArrayList;

public class Pawn extends Piece {
    boolean enPassantable = false;
    public Pawn(int player, int x, int y) {
        super(player, x, y);
        initializeImage("pawn");
        name = "PAWN";
        value = 1;
    }
    boolean canCaptureEnPassant(int newX, int newY) {
        return (Math.abs(newX-x) == 1 && newY == y - player && Board.pieceAt(newX, y) instanceof Pawn && ((Pawn)Board.pieceAt(newX, y)).enPassantable);
    }
    boolean pseudoLegalMove(int newX, int newY) {
        if (Board.playerAt(newX, newY) == player) return false;
        
        if (newX == x && newY == y - player && Board.pieceAt(newX, newY) == null) return true;//move up 1
        if (newX == x && newY == y - 2*player && (y == 1 || y == 6) && Board.pieceAt(newX, newY) == null && Board.pieceAt(newX, newY+player) == null) return true;//move up 2
        if (Math.abs(newX-x) == 1 && newY == y - player && Board.pieceAt(newX, newY) != null) return true;//capture normally
        return false;
    }
    ArrayList<int[]> getLegalMoves() {
        ArrayList<int[]> output = new ArrayList<int[]>();
        if (y-player >= 0 && y-player <= 7 && legalMove(x, y-player))
            output.add(output.size(), new int[]{x, y-player});
        if (y-2*player >= 0 && y-2*player <= 7 && legalMove(x, y-2*player))
            output.add(output.size(), new int[]{x, y-2*player});
        if (y-player >= 0 && y-player <= 7 && x-1 >= 0 && legalMove(x-1, y-player))
            output.add(output.size(), new int[]{x-1, y-player});
        if (y-player >= 0 && y-player <= 7 && x+1 <= 7 && legalMove(x+1, y-player))
            output.add(output.size(), new int[]{x+1, y-player});
        return output;
    }
}