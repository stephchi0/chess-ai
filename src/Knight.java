package src;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(int player, int x, int y) {
        super(player, x, y);
        initializeImage("knight");
        name = "KNIGHT";
        value = 3;
    }
    boolean pseudoLegalMove(int newX, int newY) {
        if (Board.playerAt(newX, newY) == player) return false;

        return (Math.abs(newX - x) == 2 && Math.abs(newY - y) == 1 || Math.abs(newX - x) == 1 && Math.abs(newY - y) == 2);
    }
    ArrayList<int[]> getLegalMoves() {
        ArrayList<int[]> output = new ArrayList<int[]>();

        int[][] relativeCoordinates = {{2, 1}, {1, 2}, {-2, 1}, {-1, 2}, {2, -1}, {1, -2}, {-2, -1}, {-1, -2}};
        for (int[] coordinates : relativeCoordinates) {
            int newX = x + coordinates[0];
            int newY = y + coordinates[1];
            if (newX >= 0 && newX <= 7 && newY >= 0 && newY <= 7 && legalMove(newX, newY))
                output.add(output.size(), new int[]{newX, newY});
        }

        /*if (x+1 <= 7 && y+2 <= 7 && legalMove(x+1, y+2))
            output.add(output.size(), new int[]{x+1, y+2});
        if (x+2 <= 7 && y+1 <= 7 && legalMove(x+2, y+1))
            output.add(output.size(), new int[]{x+2, y+1});

        if (x-1 >= 0 && y+2 <= 7 && legalMove(x-1, y+2))
            output.add(output.size(), new int[]{x-1, y+2});
        if (x-2 >= 0 && y+1 <= 7 && legalMove(x-2, y+1))
            output.add(output.size(), new int[]{x-2, y+1});
        
        if (x+1 <= 7 && y-2 >= 0 && legalMove(x+1, y-2))
            output.add(output.size(), new int[]{x+1, y-2});
        if (x+2 <= 7 && y-1 >= 0 && legalMove(x+2, y-1))
            output.add(output.size(), new int[]{x+2, y-1});

        if (x-1 >= 0 && y-2 >= 0 && legalMove(x-1, y-2))
            output.add(output.size(), new int[]{x-1, y-2});
        if (x-2 >= 0 && y-1 >= 0 && legalMove(x-2, y-1))
            output.add(output.size(), new int[]{x-2, y-1});*/
        return output;
    }
}