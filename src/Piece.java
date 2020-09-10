package src;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public abstract class Piece {
    Image pieceImage;
    int x;
    int y;
    int player;
    boolean captured = false;
    String name;
    double value;
    int numberOfMoves = 0;
    public Piece(int player, int x, int y) {
        this.x = x;
        this.y = y;
        this.player = player;
    }
    void initializeImage(String pieceName) {
        try {
            String fileName = pieceName.concat(".png");
            if (player == 1) fileName = "Images/white".concat(fileName);
            else fileName = "Images/black".concat(fileName);
            
            pieceImage = ImageIO.read(getClass().getResource(fileName));
        }
        catch (Exception e) {
            System.out.println("Image not found");
        }
    }
    void show(Graphics g, ImageObserver imageObserver) {
        if (!captured) {
            g.drawImage(pieceImage,
            x*50, y*50, x*50 + 50, y*50+50,
            0, 0, 60, 60,
            imageObserver);
        }
    }
    void show(int dragX, int dragY, Graphics g, ImageObserver imageObserver) {//for showing the selected piece for drag and drop
        if (!captured) {
            g.drawImage(pieceImage,
            dragX - 30, dragY - 30, dragX + 30, dragY+30,
            0, 0, 60, 60,
            imageObserver);
        }
    }
    abstract boolean pseudoLegalMove(int newX, int newY);//get psuedo-legal moves
    boolean legalMove(int newX, int newY) {
        return pseudoLegalMove(newX, newY) && Board.safeMove(this, newX, newY);
    }
    abstract ArrayList<int[]> getLegalMoves();//get legal and safe moves
    String getSquare() {
        return String.valueOf((char)(x + 64+1)).concat(Integer.toString(9-y));
    }
}