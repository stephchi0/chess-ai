package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
@SuppressWarnings("serial")

class Game extends JPanel implements MouseListener, MouseMotionListener {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);        
        Board.show(g, this);
        
        if (Board.numberOfPieces < 7) AI.depth = 7;
        else if (Board.numberOfPieces < 16) AI.depth = 6;
        if (Board.turn == AI.player) SwingUtilities.invokeLater(AIMove);
    }
    Runnable AIMove = new Runnable() {
        public void run() {
            Board.eval = AI.minimax(Board.position, Board.whitePieces, Board.blackPieces, AI.depth, Board.turn, -99999, 99999);
            Board.selectedPiece = AI.pieceToMove;
            Board.movePiece(AI.coordinateToMoveTo[0], AI.coordinateToMoveTo[1]);
            repaint();
        }
    };

    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (e.getX() > i * 50 && e.getX() < i * 50 + 50 && e.getY() > j * 50 && e.getY() < j * 50 + 50) {
                    if (Board.selectedPiece != null && Board.selectedPiece.x == i && Board.selectedPiece.y == j)//deselect
                        Board.selectedPiece = null;
                    else if (Board.selectedPiece != null && Board.pieceAt(i, j) != null && Board.pieceAt(i, j).player != Board.selectedPiece.player) {//move piece if one is selected
                        Board.movePiece(i, j);
                        Board.dragged = false;
                    }
                    else {//select the piece clicked
                        Board.selectPiece(i, j);
                        Board.dragged = (Board.selectedPiece != null);
                    }
                }
            }
        }
        if (Board.dragged) {
            Board.dragX = e.getX();
            Board.dragY = e.getY();
        }
        repaint();
    }
    public void mouseReleased(MouseEvent e) {
        Board.dragged = false;
        if (Board.selectedPiece != null && e.getX() <= 400 && e.getY() <= 400 &&//if you release mouse on top of its original square, it uses double click control
        !(e.getX() >= Board.selectedPiece.x*50 && e.getX() <= Board.selectedPiece.x*50+50 && e.getY() >= Board.selectedPiece.y*50 && e.getY() <= Board.selectedPiece.y*50+50)) {
            Board.movePiece(e.getX()/50, e.getY()/50);
        }
        repaint();
    }
    public void mouseExited(MouseEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}

	public void mouseDragged(MouseEvent e) {
        if (Board.dragged) {
            Board.dragX = e.getX();
            Board.dragY = e.getY();
            repaint();
        }
    }
	public void mouseMoved(MouseEvent e){}
}