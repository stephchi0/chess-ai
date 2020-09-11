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
        int inputX = e.getX()/50;
        int inputY = e.getY()/50;
        if (inputX >= 0 && inputX <= 7 && inputY >= 0 && inputY <= 7) {
            if (Board.selectedPiece != null && inputX == Board.selectedPiece.x && inputY == Board.selectedPiece.y)//deselect by clicking on selected piece again
                Board.selectedPiece = null;
            else if (Board.selectedPiece != null && (Board.pieceAt(inputX, inputY) == null || Board.pieceAt(inputX, inputY).player != Board.selectedPiece.player)) {//move piece if one is selected
                Board.movePiece(inputX, inputY);
                Board.dragged = false;
            }
            else {//select the piece clicked
                Board.selectPiece(inputX, inputY);
                Board.dragged = (Board.selectedPiece != null);
            }
        }
        if (Board.dragged) {
            Board.dragX = e.getX();
            Board.dragY = e.getY();
        }
        repaint();
    }
    public void mouseReleased(MouseEvent e) {
        if (Board.dragged) {
            int inputX = e.getX()/50;
            int inputY = e.getY()/50;
            if (Board.selectedPiece != null && inputX < 8 && inputY < 8 &&//if you release mouse on top of its original square, it uses double click control
            !(inputX == Board.selectedPiece.x && inputY == Board.selectedPiece.y)) {
                Board.movePiece(e.getX()/50, e.getY()/50);
            }
            Board.dragged = false;
            repaint();
        }
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